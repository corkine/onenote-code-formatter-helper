import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, ButtonType, Label}
import scalafx.scene.layout.VBox
import scalafx.stage.Modality

object Hello extends JFXApp {
  stage = new PrimaryStage { ST =>
    scene = new Scene(400,300) {
      root = new VBox {
        alignment = Pos.Center
        spacing = 30
        children = Seq(
          new Label("Hello, World"),
          new Button {
            text = "OK"
            onAction = _ => {
              val alert = new Alert(AlertType.Information) {
                initOwner(ST)
                initModality(Modality.WindowModal)
                headerText = "Hello"
                contentText = "This is a project from Scala with sbt"
                buttonTypes = Seq(ButtonType.OK)
              }
              alert.showAndWait()
            }
          }
        )
      }
    }
  }

}
