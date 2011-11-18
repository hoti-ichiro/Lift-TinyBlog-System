package code
package snippet

import net.liftweb.http.{S, LiftSession}
import net.liftweb.mapper.BaseMetaMapper
import net.liftweb.util._
import net.liftweb.common._
import org.specs._
import org.specs.matcher._
import org.specs.specification._
import code.model._

class MySnippetSpec extends Specification with Contexts {
  // run any block of code in a Lift session
  val session = new LiftSession("", StringHelpers.randomString(20), Empty)
  def inSession(a: =>Any) = {
    S.initIfUninitted(session) { a }
  }
  // function to create and log-in a user
  def loginUser = inSession {
    val user: User = User.create
    user.firstName("XXX")
    user.lastName("YYYY")
    user.save
    User.logUserIn(user)
  }
  // specify what to do before/after each example
  // specify that each example must run in the context of a session
  new SpecContext {
    beforeExample {
      /* setup db here */
      loginUser
    }
    afterExample { /* teardown db here */}
    aroundExpectations(inSession(_))
  }

  "Postは属性値を設定される" in {
    (new bootstrap.liftweb.Boot).boot
    val p = Post.create
    val today = new java.util.Date()
    val user = User.create
    user.firstName("fff").lastName("lll").save

    p.title("Test title").content("Test content").postedAt(today).save
    // check
    p must not be null
    p.title.is must be_==("Test title")
    p.content.is must be_==("Test content")
    p.postedAt.is must be_== (today)
    user.firstName.is must be_==("fff")
    user.lastName.is must be_==("lll")

    p.author_id(user).save
    // check
    val theUser: User = p.author_id.obj.open_!
    theUser must be_==(user)
    theUser.firstName.is must be_==("fff")
  }
}