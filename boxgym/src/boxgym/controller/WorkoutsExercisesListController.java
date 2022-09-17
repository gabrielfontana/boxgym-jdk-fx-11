package boxgym.controller;

import boxgym.dao.WorkoutExerciseDao;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.model.WorkoutExercise;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class WorkoutsExercisesListController implements Initializable {
    
    private int selectedWorkout;

    private WorkoutExercise selected;
    
    @FXML
    private TableView<WorkoutExercise> exerciseListTableView;
    
    @FXML
    private TableColumn<WorkoutExercise, String> exerciseNameTableColumn;
    
    @FXML
    private TableColumn<WorkoutExercise, Integer> setsTableColumn;
    
    @FXML
    private TableColumn<WorkoutExercise, Integer> repsTableColumn;
    
    @FXML
    private TableColumn<WorkoutExercise, Integer> restTableColumn;
    
    @FXML
    private Label countLabel;
    
    @FXML
    private Label selectedRowLabel;
    
    @FXML
    private MaterialDesignIconView firstRow;
    
    @FXML
    private MaterialDesignIconView lastRow;

    public int getSelectedWorkout() {
        return selectedWorkout;
    }

    public void setSelectedWorkout(int selectedWorkout) {
        this.selectedWorkout = selectedWorkout;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ButtonHelper.iconButton(firstRow, lastRow);
        Platform.runLater(() -> initMethods());
    }
    
    private void initMethods() {
        initExercisesListTableView();
        listeners();
        countLabel.setText(TableViewCount.footerMessage(exerciseListTableView.getItems().size(), "exercício"));
    }

    private ObservableList<WorkoutExercise> loadData() {
        WorkoutExerciseDao dao = new WorkoutExerciseDao();
        return FXCollections.observableArrayList(dao.read(getSelectedWorkout()));
    }
    
    private void initExercisesListTableView() {
        exerciseNameTableColumn.setCellValueFactory(new PropertyValueFactory("tempExerciseName"));
        setsTableColumn.setCellValueFactory(new PropertyValueFactory("sets"));
        repsTableColumn.setCellValueFactory(new PropertyValueFactory("reps"));
        restTableColumn.setCellValueFactory(new PropertyValueFactory("rest"));
        exerciseListTableView.setItems(loadData());
    }

    private void listeners() {
        exerciseListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (WorkoutExercise) newValue;
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(exerciseListTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
            }
        });
    }

    @FXML
    void goToFirstRow(MouseEvent event) {
        exerciseListTableView.scrollTo(0);
        exerciseListTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (exerciseListTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            exerciseListTableView.getSelectionModel().selectLast();
            exerciseListTableView.scrollTo(exerciseListTableView.getItems().size() - 1);
        }
    }
    
}
