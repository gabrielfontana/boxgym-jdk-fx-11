package boxgym.helper;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import limitedtextfield.LimitedTextField;

public class InputMasks {

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

    public static void floatField(final LimitedTextField textField) {
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(() -> {
                    String value = textField.getText();
                    value = value.replaceAll("[^0-9]", "");
                    value = value.replaceAll("([0-9]{1})([0-9]{1})$", "$1,$2");
                    textField.setText(value);
                    positionCaret(textField);
                });

                textField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (newValue.length() > 5) {
                            textField.setText(oldValue);
                        }
                    }
                });
            }
        });
    }

    public static void monetaryField(final LimitedTextField textField) {
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(() -> {
                    String value = textField.getText();
                    value = value.replaceAll("[^0-9]", "");
                    value = value.replaceAll("([0-9]{1})([0-9]{14})$", "$1.$2");
                    value = value.replaceAll("([0-9]{1})([0-9]{11})$", "$1.$2");
                    value = value.replaceAll("([0-9]{1})([0-9]{8})$", "$1.$2");
                    value = value.replaceAll("([0-9]{1})([0-9]{5})$", "$1.$2");
                    value = value.replaceAll("([0-9]{1})([0-9]{2})$", "$1,$2");
                    textField.setText(value);
                    positionCaret(textField);
                });
                
                textField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if (newValue.length() > 17) {
                            textField.setText(oldValue);
                        }
                    }
                });
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
                if (!fieldChange) {
                    final int length = textField.getText().length();
                    if (length > 0 && length < 3) {
                        textField.setText(textField.getText() + "00");
                    }
                }
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
