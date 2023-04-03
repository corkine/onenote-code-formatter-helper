import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class PropertyTest2 {
    public static void main(String[] args) {

        SimpleStringProperty ele = new SimpleStringProperty("Hello");

        ObservableList<SimpleStringProperty> list = FXCollections.observableArrayList(param -> new Observable[]{param});

        list.addListener((ListChangeListener<SimpleStringProperty>) c -> {
            while (c.next()) {
                if (c.wasUpdated()) {
                    System.out.println("c = " + c + " is updated.");
                }
            }
        });

        list.add(ele);

        ele.setValue("World");
    }
}
