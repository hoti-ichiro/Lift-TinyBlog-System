package code
package model

import net.liftweb.mapper._

class Post extends LongKeyedMapper[Post] with IdPK {
  def getSingleton = Post
  object title extends MappedString(this, 140)
  object content extends MappedTextarea(this, 1000)
  object postedAt extends MappedDateTime(this)
  object author_id extends MappedLongForeignKey(this,User)
}

object Post extends Post with LongKeyedMetaMapper[Post] with CRUDify[Long, Post]