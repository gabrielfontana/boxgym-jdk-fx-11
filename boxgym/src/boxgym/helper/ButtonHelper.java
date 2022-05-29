package boxgym.helper;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
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
    
    public static void buttonCursor(Button... buttons) {
        for (Button b : buttons) {
            b.setCursor(Cursor.HAND);
        }
    }

    public static void buttonCursor(MenuButton menuButton1, Button... buttons) {
        menuButton1.setCursor(Cursor.HAND);
        for (Button b : buttons) {
            b.setCursor(Cursor.HAND);
        }
    }

    public static void defaultButton(Button button) {
        button.setDefaultButton(true);
    }

    public static void imageButton(ImageView image) {
        image.setCursor(Cursor.HAND);
    }
    
    public static void iconButton(MaterialDesignIconView... icons) {
        for (MaterialDesignIconView icon : icons) {
            icon.setCursor(Cursor.HAND);
        }
    }

}
