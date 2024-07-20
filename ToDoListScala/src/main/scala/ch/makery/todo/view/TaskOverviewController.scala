package ch.makery.todo.view

import ch.makery.todo.MainApp
import ch.makery.todo.modal.Task
import scalafx.scene.control.{Alert, Label, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import ch.makery.todo.util.DateTimeUtil._
import scalafx.Includes._
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType

import scala.util.{Failure, Success}

@sfxml
class TaskOverviewController(
  private val taskTable: TableView[Task],
  private val titleColumn : TableColumn[Task, String],
  private val statusColumn : TableColumn[Task, String],
  private val timeColumn : TableColumn[Task, String],
  private val dateColumn : TableColumn[Task, String],
  private val titleLabel: Label,
  private val dateLabel: Label,
  private val timeLabel: Label,
  private val statusLabel: Label,
  private val descriptionLabel: Label
  ) {
  //initialize Table View Display Content Model
  taskTable.items = MainApp.toDoList
  titleColumn.cellValueFactory = {
    _.value.title
  }
  statusColumn.cellValueFactory = {
    _.value.status
  }
  timeColumn.cellValueFactory = {
    _.value.time.asString()
  }
  dateColumn.cellValueFactory = {
    _.value.date.asString()
  }

  private def showTaskDetails(task: Option[Task]) = {
    task match {
      case Some(task) =>
        // Fill the labels with info from the task object.
        titleLabel.text <== task.title
        timeLabel.text = task.time.value.asTimeString
        descriptionLabel.text <== task.description
        statusLabel.text <== task.status
        dateLabel.text = task.date.value.asDateString

      case None =>
        // Task is null, remove all the text.
        titleLabel.text.unbind()
        descriptionLabel.text.unbind()
        statusLabel.text.unbind()
        titleLabel.text = ""
        timeLabel.text = ""
        dateLabel.text = ""
        statusLabel.text = ""
        descriptionLabel.text = ""
    }
  }

  showTaskDetails(None)


  taskTable.selectionModel().selectedItem.onChange(
    (_, _, newValue) => showTaskDetails(Some(newValue))
  )

  def handleDeleteTask(action: ActionEvent) = {
    val selectedIndex = taskTable.selectionModel().selectedIndex.value
    val selectedTask = taskTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) {
      selectedTask.delete() match {
        case Success(x) =>
          MainApp.toDoList.remove(selectedIndex)
        //taskTable.items().remove(selectedIndex);
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning) {
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem filed to save changes"
          }.showAndWait()
      }
    } else {
      // Nothing selected.
      val alert = new Alert(AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Task Selected"
        contentText = "Please select a task in the table."
      }.showAndWait()
    }
  }


  def handleNewTask(action: ActionEvent) = {
    val task = new Task(0, "", "")
    val okClicked = MainApp.showTaskEditDialog(task);
    if (okClicked) {
      task.save() match {
        case Success(x) =>
          MainApp.toDoList += task
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning) {
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem filed to save changes"
          }.showAndWait()
      }
    }
  }


  def handleEditTask(action: ActionEvent) = {
    val selectedTask = taskTable.selectionModel().selectedItem.value
    if (selectedTask != null) {
      val okClicked = MainApp.showTaskEditDialog(selectedTask)

      if (okClicked) {
        selectedTask.save() match {
          case Success(x) =>
            showTaskDetails(Some(selectedTask))
          case Failure(e) =>
            val alert = new Alert(Alert.AlertType.Warning) {
              initOwner(MainApp.stage)
              title = "Failed to Save"
              headerText = "Database Error"
              contentText = "Database problem filed to save changes"
            }.showAndWait()
        }
      }
    } else {
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Task Selected"
        contentText = "Please select a task in the table."
      }.showAndWait()
    }
  }

  def handleCompleteTask(actionEvent: ActionEvent) = {
    val selectedTask = taskTable.selectionModel().selectedItem.value
    if (selectedTask != null) {
      if (selectedTask.status.value == "Completed"){
        val warn = new Alert(Alert.AlertType.Warning) {
          initOwner(MainApp.stage)
          title = "Task Completed"
          headerText = "Task has already been completed."
          contentText = "Please re-edit the task if otherwise."
        }.showAndWait()
      } else {
        selectedTask.status = StringProperty("Completed")
        selectedTask.save() match {
          case Success(x) =>
            showTaskDetails(Some(selectedTask))
            taskTable.refresh()
          case Failure(e) =>
            val alert = new Alert(Alert.AlertType.Warning) {
              initOwner(MainApp.stage)
              title = "Failed to Save"
              headerText = "Database Error"
              contentText = "Database problem filed to save changes"
            }.showAndWait()
        }
      }
    } else {
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Task Selected"
        contentText = "Please select a task in the table."
      }.showAndWait()
    }
  }



}
