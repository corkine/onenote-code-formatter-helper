import javafx.beans.Observable
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ListChangeListener.Change
import javafx.collections.{FXCollections, ListChangeListener}
import javafx.util.Callback

object PropertyTest extends App {

  //val list = new ObservableListWrapper(List(new SimpleStringProperty("Hello")).asJava, cb)
  //val list = FXCollections.observableArrayList((param: SimpleStringProperty) => Array(param))
  val list = FXCollections.observableArrayList(
    new Callback[SimpleStringProperty, Array[Observable]] {
      override def call(param: SimpleStringProperty): Array[Observable] = {
        Array(param.asInstanceOf[Observable])
      }
  })

  val ele1 = new SimpleStringProperty("344")

  list.addListener(new ListChangeListener[SimpleStringProperty] {
    override def onChanged(c1: Change[_ <: SimpleStringProperty]): Unit = {
      while (c1.next()) {
        println(s"Updated? ${c1.wasUpdated()}, $c1")
      }
    }
  })
  list.add(ele1)
  ele1.setValue("455")

}
