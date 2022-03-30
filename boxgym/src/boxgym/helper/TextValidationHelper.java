package boxgym.helper;

public class TextValidationHelper {

    private int emptyCounter;
    private String message;

    public TextValidationHelper() {
        this.emptyCounter = 0;
        this.message = "Por favor, preencha o(s) seguinte(s) campo(s) obrigatório(s): \n\n";
    }

    public int getEmptyCounter() {
        return emptyCounter;
    }

    public String getMessage() {
        return message;
    }

    public boolean handleEmptyField(String field, String message) {
        if (field.isEmpty()) {
            this.message += message;
            this.emptyCounter++;
            return true;
        }
        return false;
    }
}
