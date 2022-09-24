package boxgym.controller;

import boxgym.dao.ExerciseDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Exercise;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;
import org.controlsfx.control.PrefixSelectionComboBox;

public class ExercisesAddController implements Initializable {

    AlertHelper ah = new AlertHelper();

    private boolean created = false;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private LimitedTextField nameTextField;

    @FXML
    private LimitedTextField abbreviationTextField;

    @FXML
    private PrefixSelectionComboBox<String> exerciseTypeComboBox;

    @FXML
    private PrefixSelectionComboBox<String> exerciseGroupComboBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextArea instructionTextArea;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCreated(false);
        buttonsProperties();
        loadExerciseTypeComboBox();
        loadExerciseGroupComboBox();
        exercisesInputRestrictions();
    }

    private void loadExerciseTypeComboBox() {
        String[] typesList = {"Aeróbico", "Alongamento", "Aquecimento", "Crossfit", "Estabilidade", "Funcional", "Mobilidade", "Musculação", "Pilates"};
        exerciseTypeComboBox.setPromptText("Selecione");
        exerciseTypeComboBox.setItems(FXCollections.observableArrayList(typesList));
    }

    private void loadExerciseGroupComboBox() {
        String[] groupsList = {"Abdome", "Antebraço", "Bíceps", "Corpo", "Costas", "Glúteo", "Ombro", "Peito", "Perna", "Tríceps"};
        exerciseGroupComboBox.setPromptText("Selecione");
        exerciseGroupComboBox.setItems(FXCollections.observableArrayList(groupsList));
    }

    private void exercisesInputRestrictions() {
        nameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ()%._-]", 255);
        abbreviationTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ()%._-]", 255);
    }

    @FXML
    private void save(ActionEvent event) {
        ExerciseDao exerciseDao = new ExerciseDao();

        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.emptyTextField(nameTextField.getText(), "Nome inválido! \n");
        validation.emptyTextField(abbreviationTextField.getText(), "Abreviação inválida! \n");
        validation.invalidComboBox(exerciseTypeComboBox, "Tipo inválido! \n");
        validation.invalidComboBox(exerciseGroupComboBox, "Grupo inválido! \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste exercício!", validation.getMessage());
        } else {
            String selectedExerciseType = exerciseTypeComboBox.getSelectionModel().getSelectedItem();
            String selectedExerciseGroup = exerciseGroupComboBox.getSelectionModel().getSelectedItem();
            Exercise exercise = new Exercise(nameTextField.getText(), abbreviationTextField.getText(), selectedExerciseType, selectedExerciseGroup,
                    descriptionTextArea.getText(), instructionTextArea.getText());
            exerciseDao.create(exercise);
            setCreated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "O exercício foi cadastrado com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void clear() {
        nameTextField.setText("");
        abbreviationTextField.setText("");
        exerciseTypeComboBox.valueProperty().set(null);
        exerciseGroupComboBox.valueProperty().set(null);
        descriptionTextArea.setText("");
        instructionTextArea.setText("");
    }

    private void buttonsProperties() {
        ButtonHelper.buttonCursor(saveButton, clearButton);
    }

}
