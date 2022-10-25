package boxgym.helper;

import javafx.scene.control.TableView;

public class ChangeTableRow {

    public static void changeToFirstRow(TableView table) {
        table.scrollTo(0);
        table.getSelectionModel().selectFirst();
    }

    public static void changeToLastRow(TableView table) {
        if (table.getItems().size() == 1) {
            changeToFirstRow(table);
        } else {
            table.getSelectionModel().selectLast();
            table.scrollTo(table.getItems().size() - 1);
        }
    }
}
