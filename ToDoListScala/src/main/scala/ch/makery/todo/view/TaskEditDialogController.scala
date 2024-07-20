package ch.makery.todo.view

import ch.makery.todo.modal.Task
import ch.makery.todo.MainApp
import scalafx.scene.control.{Alert, DatePicker, Label, TableColumn, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import ch.makery.todo.util.DateTimeUtil._
import scalafx.event.ActionEvent

@sfxml
class TaskEditDialogController(

                                private val  titleField : TextField,
                                private val   timeField : TextField,
                                private val     descriptionField : TextField,
                                private val dateField : TextField

                                 ){
  var         dialogStage : Stage  = null
  private var _task     : Task = null
  var         okClicked            = false

  def task = _task
  def task_=(x : Task) {
    _task = x

    titleField.text = _task.title.value
    timeField.text  = _task.time.value.asTimeString
    timeField.setPromptText("hh:mm")
    descriptionField.text    = _task.description.value
    dateField.text  = _task.date.value.asDateString
    dateField.setPromptText("dd.mm.yyyy")
  }

  def handleOk(action :ActionEvent){

    if (isInputValid()) {
      _task.title.value = titleField.text()
      _task.time.value  = timeField.text().parseLocalTime
      _task.description.value    = descriptionField.text()
      _task.date.value       = dateField.text().parseLocalDate;
      _task.status.value = "Pending"

      okClicked = true;
      dialogStage.close()
    }
  }

  def handleCancel(action :ActionEvent) {
    dialogStage.close();
  }
  def nullChecking (x : String) = x == null || x.length == 0

  def isInputValid() : Boolean = {
    var errorMessage = ""

    if (nullChecking(titleField.text.value))
      errorMessage += "No valid title!\n"
    if (nullChecking(timeField.text.value))
      errorMessage += "No valid time!\n"
    else {
      if (!timeField.text.value.isValidTime) {
        errorMessage += "No valid time. Use the format hh:mm (24H Format)!\n"
      }
    }
    if (nullChecking(descriptionField.text.value))
      errorMessage += "No valid description!\n"
    if (nullChecking(dateField.text.value))
      errorMessage += "No valid date!\n"
    else {
      if (!dateField.text.value.isValidDate) {
        errorMessage += "No valid date. Use the format dd.mm.yyyy!\n"
      }
    }

    if (errorMessage.length() == 0) {
      return true;
    } else {
      // Show the error message.
      val alert = new Alert(Alert.AlertType.Error){
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()

      return false;
    }
  }
}
