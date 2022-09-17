package boxgym.controller;

import boxgym.dao.ExerciseDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.model.WorkoutExercise;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;
import org.controlsfx.control.PrefixSelectionComboBox;

public class WorkoutsAddController implements Initializable {

    ExerciseDao exerciseDao = new ExerciseDao();
    LinkedHashMap<Integer, String> exerciseMap = exerciseDao.getExerciseForHashMap();

    List<WorkoutExercise> list = new ArrayList<>();
    ObservableList<WorkoutExercise> obsListItens;

    AlertHelper ah = new AlertHelper();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane workoutArea;

    @FXML
    private LimitedTextField descriptionTextField;

    @FXML
    private PrefixSelectionComboBox<String> goalComboBox;

    @FXML
    private LimitedTextField sessionsTextField;

    @FXML
    private Button addWorkoutButton;

    @FXML
    private AnchorPane exercisesEntryArea;

    @FXML
    private TextField workoutIdTextField;

    @FXML
    private PrefixSelectionComboBox<String> exerciseNameComboBox;

    @FXML
    private LimitedTextField setsTextField;

    @FXML
    private LimitedTextField repsTextField;

    @FXML
    private LimitedTextField restTextField;

    @FXML
    private Button addExerciseEntryButton;

    @FXML
    private TableView<?> exerciseEntryTableView;

    @FXML
    private TableColumn<?, ?> exerciseNameTableColumn;

    @FXML
    private TableColumn<?, ?> setsTableColumn;

    @FXML
    private TableColumn<?, ?> repsTableColumn;

    @FXML
    private TableColumn<?, ?> restTableColumn;

    @FXML
    private TableColumn<?, ?> actionButtonTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    private boolean workoutCreationFlag;
    private boolean exercisesEntryCreationFlag;

    public boolean isWorkoutCreationFlag() {
        return workoutCreationFlag;
    }

    public void setWorkoutCreationFlag(boolean workoutCreationFlag) {
        this.workoutCreationFlag = workoutCreationFlag;
    }

    public boolean isExercisesEntryCreationFlag() {
        return exercisesEntryCreationFlag;
    }

    public void setExercisesEntryCreationFlag(boolean exercisesEntryCreationFlag) {
        this.exercisesEntryCreationFlag = exercisesEntryCreationFlag;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonsProperties();

        setWorkoutCreationFlag(false);
        setExercisesEntryCreationFlag(false);
        inputRestrictions();
        loadExerciseNameComboBox();
        loadGoalComboBox();

        initExerciseEntryTableView();
        countLabel.setText("Exibindo nenhum exercício");
    }

    private void buttonsProperties() {
        ButtonHelper.buttonCursor(addWorkoutButton, addExerciseEntryButton, saveButton, clearButton);
        ButtonHelper.iconButton(firstRow, lastRow);
    }

    private void inputRestrictions() {
        descriptionTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        sessionsTextField.setValidationPattern("[0-9]", 10);
        setsTextField.setValidationPattern("[0-9]", 10);
        repsTextField.setValidationPattern("[0-9]", 10);
        restTextField.setValidationPattern("[0-9]", 10);
    }

    private void loadExerciseNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String e : exerciseMap.values()) {
            obsList.add(e);
        }
        exerciseNameComboBox.setPromptText("Selecione");
        exerciseNameComboBox.setItems(obsList);
    }

    private int getKeyFromExerciseNameComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : exerciseMap.entrySet()) {
            if (exerciseNameComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }
    
    private void loadGoalComboBox() {
        String[] goalsList = {"Condicionamento Físico", "Emagrecimento", "Hipertrofia", "Reabilitação Física"};
        goalComboBox.setPromptText("Selecione");
        goalComboBox.setItems(FXCollections.observableArrayList(goalsList));
    }

    @FXML
    private void addWorkout(ActionEvent event) {
        workoutArea.setDisable(true);
        exercisesEntryArea.setDisable(false);
        firstRow.setDisable(false);
        lastRow.setDisable(false);
        saveButton.setDisable(false);
        clearButton.setDisable(false);
        setWorkoutCreationFlag(true);
    }

    @FXML
    private void addExerciseEntry(ActionEvent event) {
    }

    private void initExerciseEntryTableView() {
    }

    private void initCount() {
        int count = obsListItens.size();
        countLabel.setText(TableViewCount.footerMessage(count, "produto"));
    }

    @FXML
    private void save(ActionEvent event) {
    }

    @FXML
    private void clear(ActionEvent event) {
        exerciseNameComboBox.valueProperty().set(null);
        setsTextField.setText("");
        repsTextField.setText("");
        restTextField.setText("");
    }

    @FXML
    void goToFirstRow(MouseEvent event) {
        exerciseEntryTableView.scrollTo(0);
        exerciseEntryTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (exerciseEntryTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            exerciseEntryTableView.scrollTo(exerciseEntryTableView.getItems().size() - 1);
            exerciseEntryTableView.getSelectionModel().selectLast();
        }
    }
}
