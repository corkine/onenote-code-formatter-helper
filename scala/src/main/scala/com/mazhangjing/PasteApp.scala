package com.mazhangjing

import org.jsoup.Jsoup
import scalafx.Includes.when
import scalafx.animation.ScaleTransition
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.{JFXApp, Platform}
import scalafx.beans.property.{BooleanProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Cursor, Scene}
import scalafx.stage.{Screen, Stage, StageStyle}
import scalafx.util.Duration

object PasteApp extends JFXApp {

  val pasteText = StringProperty("")
  val replaceTabToSpace = BooleanProperty(true)
  val copyToClipboard = BooleanProperty(true)
  val wrapTextArea = BooleanProperty(false)
  val clipBoard: Clipboard = scalafx.scene.input.Clipboard.systemClipboard

  var c_x = 0.0
  var c_y = 0.0

  lazy val menu: MenuBar = new MenuBar {
    useSystemMenuBar = true
    menus = ObservableBuffer(
      new Menu("Action") {
        items = ObservableBuffer(
          new MenuItem("转换 HTML") {
            text <== when(copyToClipboard) choose "转换 HTML 并拷贝到剪贴板" otherwise "转换 HTML"
            onAction = _ => {
              actionButton.fire()
            }
            accelerator = new KeyCodeCombination(KeyCode.E, KeyCombination.MetaDown)
          },
          new MenuItem("清空文本") {
            onAction = _ => pasteText.setValue("")
            accelerator = new KeyCodeCombination(KeyCode.R, KeyCombination.MetaDown)
          },
          new MenuItem("退出") {
            onAction = _ => Platform.exit()
            accelerator = new KeyCodeCombination(KeyCode.Q, KeyCombination.MetaDown)
          }
        )
      },
      new Menu("Option") {
        items = ObservableBuffer(
          new CheckMenuItem("Tab 转空格") {
            selected <==> replaceTabToSpace
            accelerator = new KeyCodeCombination(KeyCode.T, KeyCombination.MetaDown, KeyCombination.ShiftDown)
          },
          new CheckMenuItem("自动拷贝到剪贴板") {
            selected <==> copyToClipboard
            accelerator = new KeyCodeCombination(KeyCode.C, KeyCombination.MetaDown, KeyCombination.ShiftDown)
          },
          new CheckMenuItem("文本框内容自动换行") {
            selected <==> wrapTextArea
            accelerator = new KeyCodeCombination(KeyCode.W, KeyCombination.MetaDown, KeyCombination.ShiftDown)
          },
          new MenuItem("最小化窗口"){
            accelerator = new KeyCodeCombination(KeyCode.M, KeyCombination.MetaDown)
            onAction = _ => {
              stage.hide()
              smallStage.show()
            }
          }
        )
      },
      new Menu("About") {
        items = ObservableBuffer(
          new MenuItem("关于本软件") {
            onAction = _ => aboutDlg.showAndWait()
          },
          new MenuItem("帮助") {
            onAction = _ => helpDlg.showAndWait()
          },
          new SeparatorMenuItem(),
          new MenuItem("提建议...") {
            onAction = _ => hostServices.showDocument("http://www.mazhangjing.com")
          },
          new MenuItem("支持...") {
            onAction = _ => hostServices.showDocument("http://www.mazhangjing.com")
          },
          new MenuItem("官方网站...") {
            onAction = _ => hostServices.showDocument("http://www.mazhangjing.com")
          }
        )
      }
    )
  }

  val software_title = "Code Formatter Helper - 0.1.1"
  val software_pic = "edit.png"

  val actionButton: Button = new Button {
    text <==
      when(copyToClipboard) choose "Paste HTML and Copy to Clipboard" otherwise "Paste HTML"
    onAction = _ => {
      if (clipBoard.hasHtml) {
        val originalHTML = if (replaceTabToSpace.value) {
          clipBoard.getHtml.replace("\t"," ")
        } else clipBoard.getHtml
        pasteText.setValue(convertWithJSoup(reformat(originalHTML)))
        val content = ClipboardContent()
        content.putHtml(pasteText.value)
        clipBoard.setContent(content)
      }
    }
  }

  stage = new PrimaryStage {
    alwaysOnTop = true
    title = "Code Formatter Helper"
    icons.add(new Image(software_pic, 100,100,true,true))
    scene = new Scene(600,500) {
      root = new BorderPane {
        top = menu
        center = new VBox {
          BorderPane.setMargin(this, Insets(0,20,20,20))
          children = Seq(
            new HBox {
              alignment = Pos.CenterLeft
              padding = Insets(20,0,20,0)
              spacing = 10
              children = Seq(
                actionButton,
                new Button("Clear") { onAction = _ => pasteText.setValue("") },
              )
            },
            new TextArea {
              VBox.setVgrow(this, Priority.Always)
              editable = false
              wrapText <==> wrapTextArea
              text <== pasteText
              style = "-fx-background-color: transparent, white, transparent, white;-fx-background-insets: 0;"
            }
          )
        }
      }
    }
  }

  val smallStage: Stage = new Stage { st =>
    initOwner(stage)
    initStyle(StageStyle.Transparent)
    alwaysOnTop = true

    scene = new Scene(50,50) {
      fill = Color.Transparent
      root = new StackPane {
        onMousePressed = e => {
          c_x = e.getX
          c_y = e.getY
        }
        onMouseDragged = e => {
          st.x = e.getScreenX - c_x
          st.y = e.getScreenY - c_y
        }
        onMouseEntered = _ => {
          cursor = Cursor.OpenHand
        }
        onMouseExited = _ => cursor = Cursor.Default
        style = "-fx-background-color: #000000"
        opacity = 0.8
        clip = new Rectangle {
          width = 50
          height = 50
          arcWidth = 30
          arcHeight = 30
        }
        children = new ImageView(new Image(software_pic,30,30,true,true)) {
          onShown = _ => {
            st.x = Screen.primary.visualBounds.getWidth - width.value - 10
            st.y = 30
          }
          onMouseEntered = _ => {
            opacity = 1
            cursor = Cursor.Hand
          }
          onMouseExited = _ => opacity = 0.8
          onMouseClicked = e => {
            if (e.isMetaDown) {
              st.hide()
              stage.show()
            } else {
              new ScaleTransition(Duration.apply(70), this) {
                toX = 0.7
                toY = 0.7
                autoReverse = true
                cycleCount = 2
              }.play()
              actionButton.fire()
            }
          }
        }
      }
    }
  }

  val aboutDlg: Alert = new Alert(AlertType.Information) {
    initOwner(stage)
    title = "关于本软件"
    graphic = new ImageView(new Image(software_pic, 50,50,true,true))
    headerText = software_title
    contentText = "本软件用于粘贴 HTML 格式化高亮代码时遇到的缩进消失、空白字符消失问题。\n\n" +
      "主要用于解决从 IntelJ IDEA 复制代码样本到 OneNote 中的黑色背景和空白字符消失问题，以及从 Web " +
      "拷贝代码样本时，缩进不能对齐的问题。\n\n" +
      "Use Library: Scala, JavaFX, ScalaFX, JSoup.\n\n" +
      "Github: @Corkine"
    buttonTypes = Seq(ButtonType.OK)
  }

  val helpDlg: Alert = new Alert(AlertType.Information) {
    initOwner(stage)
    title = "帮助"
    headerText = software_title
    contentText = "按下 Ctrl/Meta + C 从 IDEA 或者 Web 中复制代码，然后点击按钮 Paste HTML... 或者使用菜单 Paste HTML... " +
      "或者按下快捷键 Ctrl/Meta + E 转换文本，转换后的文本默认复制到剪贴板中，Ctrl/Meta 到各处即可（比如 OneNote）。\n\n" +
      "菜单栏提供了更多的工具，比如对于 Tab 的处理，是否自动拷贝到剪贴板，以及预览是否换行等。\n\n" +
      "程序支持最小化，最小化后只显示一个图标，点击图标可以自动转换并复制到剪贴板，拖拽图标边缘黑色边框可以移动其位置，菜单选择或者按下" +
      "Ctrl/Meta + Q 退出程序，按住 Ctrl/Meta，点击图标可以恢复默认大小。"
    buttonTypes = Seq(ButtonType.OK)
  }

  def reformat(inputHTML:String):String = {
    val array = inputHTML.split("\n")
    println("原始数据：\n\n\n" + array.mkString("\n\n"))
    val max_common_space = array.tail.foldLeft(100)((now_space_in_head, current_line) => {
      val current_line_head_space =
        current_line.takeWhile(char => char.equals(' ')).length
      if (current_line_head_space < now_space_in_head) current_line_head_space
      else now_space_in_head
    })
    println(max_common_space)
    array
      .map(_.replaceFirst(" " * max_common_space,""))
      .mkString("\n")
  }

  def convertWithJSoup(inputHTML:String):String = {
    val document = Jsoup.parse(inputHTML)
    val elements = document.select("pre")
    elements.attr("style","background-color:#FFFFFF;color:#00000;font-family:'YaHei Consolas Hybrid")
    println("格式化数据\n\n\n\n" + document.toString)
    //val pattern = "<br>\\W+".r
    //pattern.replaceAllIn(document.toString, "<br>")
    document.toString
      .replace("<br> ","<br>&nbsp;")
      .replace("<br>  ","<br>&nbsp;&nbsp;")
      .replace("<br>   ","<br>&nbsp;&nbsp;&nbsp;")
      .replace("<br>    ","<br>&nbsp;&nbsp;&nbsp;&nbsp;")
      .replace("<br>     ","<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
      .replace("<br>      ","<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
      .replace("<br>       ","<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
      .replace("<br>        ","<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
      .replace("<br>         ","<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
      .replace("<br>          ","<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
  }

}
