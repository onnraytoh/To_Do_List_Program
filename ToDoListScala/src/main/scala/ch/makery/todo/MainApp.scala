package ch.makery.todo
import ch.makery.todo.modal.Task
import ch.makery.todo.util.Database
import ch.makery.todo.view.TaskEditDialogController
import javafx.collections.ObservableList
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.beans.Observable
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp {
  Database.setupDB()
  val toDoList = new ObservableBuffer[Task]

  toDoList ++= Task.getAllTask

  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "To Do List"
    icons += new Image("image/tick.png")
    scene = new Scene {
      root = roots
    }
  }
  def showTaskOverview() = {
    val resource = getClass.getResource("view/TaskOverview.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showTaskEditDialog(task: Task): Boolean = {
    val resource = getClass.getResource("view/TaskEditDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[TaskEditDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      title = "New/Edit Task"
      icons += new Image("image/tick.png")
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.task = task
    dialog.showAndWait()
    control.okClicked
  }

  showTaskOverview()

}