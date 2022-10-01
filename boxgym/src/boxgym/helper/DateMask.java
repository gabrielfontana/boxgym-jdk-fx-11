package boxgym.helper;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import limitedtextfield.LimitedTextField;

public class DateMask {

    public static void dateField(final LimitedTextField textField) {
        maxField(textField, 10);

        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(() -> {
                    if (newValue.intValue() < 11) {
                        String value = textField.getText();
                        value = value.replaceAll("[^0-9]", "");
                        value = value.replaceFirst("(\\d{2})(\\d)", "$1/$2");
                        value = value.replaceFirst("(\\d{2})\\/(\\d{2})(\\d)", "$1/$2/$3");
                        textField.setText(value);
                        positionCaret(textField);
                    }
                });
            }
        });
    }

    private static void positionCaret(final LimitedTextField textField) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textField.positionCaret(textField.getText().length());
            }
        });
    }

    private static void maxField(final LimitedTextField textField, final Integer length) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.length() > length) {
                    textField.setText(oldValue);
                }
            }
        });
    }
}
