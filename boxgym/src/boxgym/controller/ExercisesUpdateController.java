package boxgym.controller;

import boxgym.dao.ExerciseDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Exercise;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

public class ExercisesUpdateController implements Initializable {

    AlertHelper ah = new AlertHelper();

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

    private Exercise loadExercise;

    private boolean updated = false;

    public Exercise getLoadExercise() {
        return loadExercise;
    }

    public void setLoadExercise(Exercise loadExercise) {
        this.loadExercise = loadExercise;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpdated(false);
        buttonsProperties();
        loadExerciseTypeComboBox();
        loadExerciseGroupComboBox();
        exercisesInputRestrictions();

        Platform.runLater(() -> {
            initExercise();
        });
    }

    private void loadExerciseTypeComboBox() {
        String[] typesList = {"Aeróbico", "Alongamento", "Aquecimento", "Crossfit", "Estabilidade", "Funcional", "Mobilidade", "Musculação", "Pilates"};
        exerciseTypeComboBox.setPromptText("Selecione");
        exerciseTypeComboBox.setItems(FXCollections.observableArrayList(typesList));
    }

    private void loadExerciseGroupComboBox() {
        String[] groupsList = {"Abdômen", "Antebraço", "Bíceps", "Corpo", "Costas", "Glúteo", "Ombro", "Peito", "Perna", "Tríceps"};
        exerciseGroupComboBox.setPromptText("Selecione");
        exerciseGroupComboBox.setItems(FXCollections.observableArrayList(groupsList));
    }

    private void exercisesInputRestrictions() {
        nameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ()%._-]", 255);
        abbreviationTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ()%._-]", 255);
    }

    private void initExercise() {
        nameTextField.setText(loadExercise.getName());
        abbreviationTextField.setText(loadExercise.getAbbreviation());
        exerciseTypeComboBox.valueProperty().set(loadExercise.getExerciseType());
        exerciseGroupComboBox.valueProperty().set(loadExercise.getExerciseGroup());
        descriptionTextArea.setText(loadExercise.getDescription());
        instructionTextArea.setText(loadExercise.getInstruction());
    }

    @FXML
    private void save(ActionEvent event) {
        ExerciseDao exerciseDao = new ExerciseDao();

        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.emptyTextField(nameTextField.getText(), "Preencha o campo Nome. \n");
        validation.emptyTextField(abbreviationTextField.getText(), "Preencha o campo Abreviação. \n");
        validation.invalidComboBox(exerciseTypeComboBox, "Selecione o campo Tipo. \n");
        validation.invalidComboBox(exerciseGroupComboBox, "Selecione o campo Grupo. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível atualizar o cadastro deste exercício", validation.getMessage());
        } else {
            String selectedExerciseType = exerciseTypeComboBox.getSelectionModel().getSelectedItem();
            String selectedExerciseGroup = exerciseGroupComboBox.getSelectionModel().getSelectedItem();
            Exercise exercise = new Exercise(loadExercise.getExerciseId(), nameTextField.getText(), abbreviationTextField.getText(), selectedExerciseType, selectedExerciseGroup,
                    descriptionTextArea.getText(), instructionTextArea.getText());
            exerciseDao.update(exercise);
            setUpdated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "Exercício atualizado com sucesso", "");
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
