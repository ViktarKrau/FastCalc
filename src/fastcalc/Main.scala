package fastcalc

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.control.TextField
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class Main extends Application {
  override def start(stage : Stage) {
    stage.setTitle("FastCalc")
    val fxmlLoader = new FXMLLoader
    val root : Parent = fxmlLoader.load(getClass.getClassLoader.getResource("fastcalc/main.fxml").openStream)
    val controller : Controller = fxmlLoader.getController[Controller]
    val scene = new Scene(root)
    controller.setModel(new Model(new View(scene)))
    controller.setStage(stage)
    stage.setScene(scene)
    stage.setResizable(false)
    List("real", "imag", "rad", "exp") foreach
      (f => List("1", "2") foreach (n => controller.initializeTextField(scene.lookup("#" + n + f).asInstanceOf[TextField])))
    stage.show()
  }
}

object Main {
  def main(args : Array[String]) {
    Application.launch(classOf[Main], args: _*)
  }
}