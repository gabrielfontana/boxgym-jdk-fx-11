package boxgym.helper;

import boxgym.model.Product;
import boxgym.model.StockEntryProduct;
import java.math.BigDecimal;
import java.text.NumberFormat;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class TextFieldFormat {

    public static void currencyFormat(Label label, BigDecimal value) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        label.setText(currencyFormat.format(value));
    }
    
    public static void currencyFormat(TextField text, BigDecimal value) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        text.setText(currencyFormat.format(value));
    }

    public static void productTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<Product, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }

    public static void stockEntryProductTableCellCurrencyFormat(TableColumn column) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        column.setCellFactory(tc -> new TableCell<StockEntryProduct, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }
}
