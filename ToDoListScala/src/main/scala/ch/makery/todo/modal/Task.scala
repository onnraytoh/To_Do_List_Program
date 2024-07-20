package ch.makery.todo.modal

import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

import java.time.{LocalDate, LocalTime}
import ch.makery.todo.util.Database
import ch.makery.todo.util.DateTimeUtil._
import scalikejdbc._

import scala.util.{Failure, Success, Try}

class Task(val idI: Int, val titleS: String, val statusS: String) extends Database {
  def this() = this(0, null, null)

  var id = ObjectProperty[Int](idI)
  var title = new StringProperty(titleS)
  var time = ObjectProperty[LocalTime](LocalTime.of(12, 0))
  //var time = new StringProperty("2400")
  var description = new StringProperty("Optional")
  var date = ObjectProperty[LocalDate](LocalDate.of(2023, 7, 21))
  var status = new StringProperty(statusS)

  def save(): Try[Unit] = {
    if (id.value == 0) {
      Try(DB autoCommit { implicit session =>
        val generatedId = sql"""
insert into task (title, time,
description, date, status) values (${title.value}, ${time.value.asTimeString}, ${description.value},
        ${date.value.asDateString}, ${status.value})
""".updateAndReturnGeneratedKey().apply()

        id.value = generatedId.toInt
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
  update task
  set
title = ${title.value},
time = ${time.value.asTimeString},
description = ${description.value},
date       = ${date.value.asDateString},
status = ${status.value}
        where id = ${id.value}
      """.update.apply()
      })
    }
  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
      delete from task where
        id = ${id.value}
      """.update.apply()
      })
    } else
      throw new Exception("Task not Exists in Database")
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
      select * from task where
title = ${title.value} and status = ${status.value} """.map(rs => rs.string("title")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }
  }
}

object Task extends Database {
  def apply(
             id: Int,
             titleS: String,
             timeS: String,
             desctipionS: String,
             statusS: String,

             dateS: String
           ): Task = {
    new Task(id, titleS, statusS) {
      time.value = timeS.parseLocalTime
      description.value = desctipionS
      date.value = dateS.parseLocalDate
    }
  }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
      create table task (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT
BY 1),
        title varchar(64),
        time varchar(64),
        description varchar(200),
        status varchar(64),
        date varchar(64)
)
      """.execute.apply()
    }
  }

  def getAllTask: List[Task] = {
    DB readOnly { implicit session =>
      sql"select * from task".map(rs => Task(rs.int("id"),rs.string("title"),
        rs.string("time"), rs.string("description"),
        rs.string("status"),rs.string("date"))).list.apply()
    }
  }
}
