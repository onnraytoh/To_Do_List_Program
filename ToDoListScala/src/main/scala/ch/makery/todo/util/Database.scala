package ch.makery.todo.util

import scalikejdbc._
import ch.makery.todo.modal.Task

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;"; // initialize JDBC driver & connection pool Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine") // ad-hoc session provider on the REPL
  implicit val session = AutoSession
}

object Database extends Database {
  def setupDB() = {
    if (!hasDBInitialize)
      Task.initializeTable()

    def hasDBInitialize: Boolean = {
      DB getTable "Task" match {
        case Some(x) => true
        case None => false
      }
    }
  }
}
