package boxgym.controller;

import boxgym.dao.ExerciseDao;
import boxgym.dao.WorkoutDao;
import boxgym.dao.WorkoutExerciseDao;
import boxgym.helper.ActionButtonTableCell;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Workout;
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
import javafx.scene.control.Alert;
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
    private TableView<WorkoutExercise> exerciseEntryTableView;

    @FXML
    private TableColumn<WorkoutExercise, String> exerciseNameTableColumn;

    @FXML
    private TableColumn<WorkoutExercise, Integer> setsTableColumn;

    @FXML
    private TableColumn<WorkoutExercise, Integer> repsTableColumn;

    @FXML
    private TableColumn<WorkoutExercise, Integer> restTableColumn;

    @FXML
    private TableColumn<WorkoutExercise, Button> actionButtonTableColumn;

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
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.emptyTextField(descriptionTextField.getText(), "Descrição inválida! \n");
        validation.invalidComboBox(goalComboBox, "Objetivo inválido! \n");
        validation.emptyTextField(sessionsTextField.getText(), "Sessões inválidas! \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível adicionar esse treino!", validation.getMessage());
        } else {
            workoutArea.setDisable(true);
            exercisesEntryArea.setDisable(false);
            firstRow.setDisable(false);
            lastRow.setDisable(false);
            saveButton.setDisable(false);
            clearButton.setDisable(false);
            Workout workout = new Workout(descriptionTextField.getText(), goalComboBox.getValue(), Integer.valueOf(sessionsTextField.getText()));
            WorkoutDao workoutDao = new WorkoutDao();
            workoutDao.create(workout);
            workoutIdTextField.setText(String.valueOf(workoutDao.getWorkoutId()));
            setWorkoutCreationFlag(true);
        }
    }

    @FXML
    private void addExerciseEntry(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(exerciseNameComboBox, "Exercício inválido! \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível adicionar esse exercício!", validation.getMessage());
        } else {
            WorkoutExercise item = new WorkoutExercise(Integer.valueOf(workoutIdTextField.getText()), getKeyFromExerciseNameComboBox(), 
                    setsRepsRest(setsTextField.getText(), 3), setsRepsRest(repsTextField.getText(), 10), setsRepsRest(restTextField.getText(), 30));
            item.setTempExerciseName(exerciseNameComboBox.getSelectionModel().getSelectedItem());
            list.add(item);
            obsListItens = FXCollections.observableArrayList(list);
            exerciseEntryTableView.setItems(obsListItens);
            exerciseEntryTableView.getSelectionModel().selectLast();
            initCount();
            clear();
        }
    }

    private void initExerciseEntryTableView() {
        exerciseNameTableColumn.setCellValueFactory(new PropertyValueFactory("tempExerciseName"));
        setsTableColumn.setCellValueFactory(new PropertyValueFactory("sets"));
        repsTableColumn.setCellValueFactory(new PropertyValueFactory("reps"));
        restTableColumn.setCellValueFactory(new PropertyValueFactory("rest"));
        actionButtonTableColumn.setCellFactory(ActionButtonTableCell.<WorkoutExercise>forTableColumn("", (WorkoutExercise e) -> {
            list.remove(e);
            obsListItens.remove(e);
            exerciseEntryTableView.getItems().remove(e);
            initCount();
            return e;
        }));
    }

    private void initCount() {
        int count = obsListItens.size();
        countLabel.setText(TableViewCount.footerMessage(count, "exercício"));
    }

    @FXML
    private void save() {
        if (!(list == null || list.isEmpty())) {
            for (WorkoutExercise item : list) {
                WorkoutExerciseDao dao = new WorkoutExerciseDao();
                dao.create(item);
            }
            setExercisesEntryCreationFlag(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "O treino foi criado com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        } else {
            ah.customAlert(Alert.AlertType.INFORMATION, "Lista de exercícios vazia!", "");
        }
    }

    @FXML
    private void clear() {
        exerciseNameComboBox.valueProperty().set(null);
        setsTextField.setText("");
        repsTextField.setText("");
        restTextField.setText("");
    }

    private int setsRepsRest(String textField, int value) {
        int exerciseRelated;
        
        if (textField.isEmpty()) {
            exerciseRelated = value;
        } else {
            exerciseRelated = Integer.valueOf(textField);
        }
        
        return exerciseRelated;
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
            exerciseEntryTableView.getSelectionModel().selectLast();
            exerciseEntryTableView.scrollTo(exerciseEntryTableView.getItems().size() - 1);
        }
    }
}
