package boxgym.controller;

import boxgym.dao.WorkoutDao;
import boxgym.dao.WorkoutExerciseDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.model.Workout;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class WorkoutsController implements Initializable {

    AlertHelper alert = new AlertHelper();

    private Workout selected;

    @FXML
    private MenuButton filterButton;

    @FXML
    private CheckMenuItem caseSensitiveOp;

    @FXML
    private RadioMenuItem containsOp;

    @FXML
    private ToggleGroup filterOptions;

    @FXML
    private RadioMenuItem alphabeticalEqualsToOp;

    @FXML
    private RadioMenuItem startsWithOp;

    @FXML
    private RadioMenuItem endsWithOp;

    @FXML
    private TextField searchBox;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Workout> workoutTableView;

    @FXML
    private TableColumn<Workout, Integer> workoutIdTableColumn;

    @FXML
    private TableColumn<Workout, String> descriptionTableColumn;

    @FXML
    private TableColumn<Workout, String> goalTableColumn;

    @FXML
    private TableColumn<Workout, Integer> sessionsTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label workoutIdLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label goalLabel;

    @FXML
    private Label sessionsLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;

    @FXML
    private Button listButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton, addButton, deleteButton, listButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initWorkoutTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }

    @FXML
    private void addWorkout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/WorkoutsAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            WorkoutsAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Adicionando Treino", root);

            if (controller.isWorkoutCreationFlag() && !controller.isExercisesEntryCreationFlag()) {
                WorkoutDao workoutDao = new WorkoutDao();
                workoutDao.deleteLastEntry();
            } else if (controller.isWorkoutCreationFlag() && controller.isExercisesEntryCreationFlag()) {
                refreshTableView();
                workoutTableView.getSelectionModel().selectLast();
            }
        } catch (IOException ex) {
            Logger.getLogger(WorkoutsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteWorkout(ActionEvent event) {
        WorkoutDao workoutDao = new WorkoutDao();
        WorkoutExerciseDao workoutExerciseDao = new WorkoutExerciseDao();

        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um treino para excluir!", "");
        } else {
            alert.confirmationAlert("Tem certeza que deseja excluir este treino?", "Esta ação é irreversível!");
            if (alert.getResult().get() == ButtonType.YES) {
                workoutExerciseDao.delete(selected);
                workoutDao.delete(selected);
                refreshTableView();
                resetDetails();
                alert.customAlert(Alert.AlertType.WARNING, "O treino foi excluído com sucesso!", "");
            }
        }
    }

    private void resetDetails() {
        if (selected == null) {
            workoutIdLabel.setText("");
            descriptionLabel.setText("");
            goalLabel.setText("");
            sessionsLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            workoutIdLabel.setText(String.valueOf(selected.getWorkoutId()));
            descriptionLabel.setText(selected.getDescription());
            goalLabel.setText(selected.getGoal());
            sessionsLabel.setText(String.valueOf(selected.getSessions()));
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }

    @FXML
    private void listExercises(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um treino para listar os exercícios!", "");
        } else {
            int index = workoutTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/WorkoutsExercisesList.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                WorkoutsExercisesListController controller = loader.getController();
                controller.setSelectedWorkout(selected.getWorkoutId());

                StageHelper.createAddOrUpdateStage("Listando Exercícios do Treino", root);
                workoutTableView.getSelectionModel().select(index);
            } catch (IOException ex) {
                Logger.getLogger(WorkoutsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private ObservableList<Workout> loadData() {
        WorkoutDao workoutDao = new WorkoutDao();
        return FXCollections.observableArrayList(workoutDao.read());
    }

    private void refreshTableView() {
        workoutTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(workoutTableView.getItems().size(), "resultado"));
    }

    private void initWorkoutTableView() {
        workoutIdTableColumn.setCellValueFactory(new PropertyValueFactory("workoutId"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory("description"));
        goalTableColumn.setCellValueFactory(new PropertyValueFactory("goal"));
        sessionsTableColumn.setCellValueFactory(new PropertyValueFactory("sessions"));
        refreshTableView();
    }

    private boolean caseSensitiveEnabled(Workout workout, String searchText, int optionOrder) {
        String workoutId = String.valueOf(workout.getWorkoutId());
        String description = workout.getDescription();
        String goal = workout.getGoal();
        String sessions = String.valueOf(workout.getSessions());

        List<String> fields = Arrays.asList(workoutId, description, goal, sessions);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Workout workout, String searchText, int optionOrder) {
        String workoutId = String.valueOf(workout.getWorkoutId());
        String description = workout.getDescription().toLowerCase();
        String goal = workout.getGoal().toLowerCase();
        String sessions = String.valueOf(workout.getSessions());

        List<String> fields = Arrays.asList(workoutId, description, goal, sessions);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText)) || (list.get(2).contains(searchText))
                        || (list.get(3).contains(searchText));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText)) || (list.get(2).equals(searchText))
                        || (list.get(3).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText)) || (list.get(2).startsWith(searchText))
                        || (list.get(3).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText)) || (list.get(2).endsWith(searchText))
                        || (list.get(3).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }

    private void search() {
        FilteredList<Workout> filteredData = new FilteredList<>(loadData(), w -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(workout -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(workout, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(workout, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(workout, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(workout, newValue, 4);
                }

                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(workout, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(workout, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(workout, newValue.toLowerCase(), 4);

                return caseSensitiveDisabled(workout, newValue.toLowerCase(), 1);
            });
            workoutTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(workoutTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Workout> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(workoutTableView.comparatorProperty());
        workoutTableView.setItems(sortedData);
    }

    private void listeners() {
        workoutTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Workout) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(workoutTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
            }
        });

        caseSensitiveOp.selectedProperty().addListener((observable, oldValue, newValue) -> {
            searchBox.setText("");
            searchBox.requestFocus();
        });

        filterOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            searchBox.setText("");
            searchBox.requestFocus();
        });
    }

    @FXML
    void goToFirstRow(MouseEvent event) {
        workoutTableView.scrollTo(0);
        workoutTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (workoutTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            workoutTableView.getSelectionModel().selectLast();
            workoutTableView.scrollTo(workoutTableView.getItems().size() - 1);
        }
    }
}
