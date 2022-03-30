package boxgym.helper;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;

public class ButtonHelper {
    public static void loginButtons(Button loginButton, Hyperlink registerLabel) {
        loginButton.setDefaultButton(true);
        loginButton.setCursor(Cursor.HAND);
        registerLabel.setCursor(Cursor.HAND);
    }
    
    public static void registerButtons(Button registerButton, MaterialDesignIconView backArrow) {
        registerButton.setDefaultButton(true);
        registerButton.setCursor(Cursor.HAND);
        backArrow.setCursor(Cursor.HAND);
    }
    
    public static void buttons(Button... buttons) {
        for(Button b : buttons) {
            b.setCursor(Cursor.HAND);
        }
    }
    
    public static void addOrUpdateButtons(Button saveButton, Button clearButton) {
        saveButton.setDefaultButton(true);
        saveButton.setCursor(Cursor.HAND);
        clearButton.setCursor(Cursor.HAND);
    }
    
    public static void imageButton(ImageView image) {
        image.setCursor(Cursor.HAND);
    }
}
