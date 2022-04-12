package boxgym.helper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class TextValidationHelper {

    private int emptyCounter;
    private String message;

    public TextValidationHelper(String message) {
        this.emptyCounter = 0;
        this.message = message;
    }

    public int getEmptyCounter() {
        return emptyCounter;
    }

    public String getMessage() {
        return message;
    }

    public boolean emptyTextField(String field, String message) {
        if (field.isEmpty()) {
            this.message += message;
            this.emptyCounter++;
            return true;
        }
        return false;
    }
    
    public boolean invalidComboBox(ComboBox comboBox, String message) {
        if (comboBox.getItems().size() <= 0 || comboBox.getSelectionModel().getSelectedItem() == null) {
            this.message += message;
            this.emptyCounter++;
            return true;
        }
        return false;
    }

    public boolean nullDatePicker(DatePicker datePicker, String message) {
        if (datePicker.getValue() == null) {
            this.message += message;
            this.emptyCounter++;
            return true;
        }
        return false;
    }
}
