package boxgym.helper;

import boxgym.model.Product;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;

public class ImageHelper {

    private String defaultImagePath;
    private String imagePath;
    private FileInputStream fis;
    private byte[] imageBytes;

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
    
    public void loadDefaultImage(ImageView productImage){
        defaultImagePath = "boxgym/img/default-no-image.png";
        productImage.setImage(new Image(defaultImagePath));
        convertImageToBytes("src/" + defaultImagePath);
    }

    public void chooser(ImageView productImage) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.jpg", "*.png", "*.jpeg"));
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            imagePath = file.getAbsolutePath();
            productImage.setImage(new Image("file:///" + imagePath));
            convertImageToBytes(imagePath);
        }
    }

    public void convertImageToBytes(String path) {
        try {
            //max_allowed_packet=32M (default -> max_allowed_packet=1M)
            fis = new FileInputStream(new File(path));
            imageBytes = IOUtils.toByteArray(fis);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static BufferedImage convertBytesToImage(Product selected) throws IOException {
        byte[] imgByteArray = selected.getImage();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imgByteArray);
        BufferedImage bufferedImg = ImageIO.read(inputStream);
        return bufferedImg;
    }
}
