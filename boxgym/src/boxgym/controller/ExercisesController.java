package boxgym.controller;

import boxgym.dao.ExerciseDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ChangeTableRow;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.model.Exercise;
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
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class ExercisesController implements Initializable {

    AlertHelper alert = new AlertHelper();

    private Exercise selected;

    @FXML
    private Button importExercisesButton;

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
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Exercise> exerciseTableView;

    @FXML
    private TableColumn<Exercise, String> nameTableColumn;

    @FXML
    private TableColumn<Exercise, String> exerciseTypeTableColumn;

    @FXML
    private TableColumn<Exercise, String> exerciseGroupTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label exerciseIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label exerciseTypeLabel;

    @FXML
    private Label exerciseGroupLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label instructionLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton, importExercisesButton, addButton, updateButton, deleteButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initExerciseTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
        enableOrDisableImportExercisesButton();
    }

    private void enableOrDisableImportExercisesButton() {
        ExerciseDao exerciseDao = new ExerciseDao();
        if (exerciseDao.checkExistingExercises() != 0) {
            importExercisesButton.setDisable(true);
        } else {
            importExercisesButton.setDisable(false);
        }
    }

    @FXML
    private void importExercises(ActionEvent event) {
        ExerciseDao exerciseDao = new ExerciseDao();
        alert.confirmationAlert("Deseja importar os exercícios predefinidos?", "Temos vários exercícios pré-cadastrados no sistema. Você pode importar todos de uma vez e excluir os que não quiser utilizar.");
        if (alert.getResult().get() == ButtonType.YES) {
            exerciseDao.importExercises();
            refreshTableView();
            resetDetails();
            importExercisesButton.setDisable(true);
            alert.customAlert(Alert.AlertType.WARNING, "Os exercícios foram importados com sucesso", "");
        }
    }

    @FXML
    private void addExercise(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/ExercisesAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            ExercisesAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Cadastrar exercício", root);

            if (controller.isCreated()) {
                refreshTableView();
                exerciseTableView.getSelectionModel().selectLast();
            }
        } catch (IOException ex) {
            Logger.getLogger(ExercisesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void updateExercise(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um exercício para atualizar", "");
        } else {
            int index = exerciseTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/ExercisesUpdate.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                ExercisesUpdateController controller = loader.getController();
                controller.setLoadExercise(selected);

                StageHelper.createAddOrUpdateStage("Atualizar exercício", root);

                if (controller.isUpdated()) {
                    refreshTableView();
                    exerciseTableView.getSelectionModel().select(index);
                }
            } catch (IOException ex) {
                Logger.getLogger(ExercisesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void deleteExercise(ActionEvent event) {
        ExerciseDao exerciseDao = new ExerciseDao();

        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um exercício para excluir", "");
        } else {
            alert.confirmationAlert("Excluir exercício", "Tem certeza que deseja excluir o exercício '" + selected.getName()+ "'? "
                    + "\n\nO exercício será excluído de forma definitiva e não poderá ser recuperado.");
            if (alert.getResult().get() == ButtonType.YES) {
                if (exerciseDao.checkWorkoutDeleteConstraint(selected.getExerciseId())){
                    alert.customAlert(Alert.AlertType.WARNING, "Não foi possível excluir", "Existem treinos relacionados a esse exercício.");
                } else {
                    exerciseDao.delete(selected);
                    refreshTableView();
                    resetDetails();
                    alert.customAlert(Alert.AlertType.WARNING, "Exercício excluído com sucesso", "");
                }
            }
        }
    }

    private void resetDetails() {
        if (selected == null) {
            exerciseIdLabel.setText("");
            nameLabel.setText("");
            exerciseTypeLabel.setText("");
            exerciseGroupLabel.setText("");
            descriptionLabel.setText("");
            instructionLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            exerciseIdLabel.setText(String.valueOf(selected.getExerciseId()));
            nameLabel.setText(selected.getName());
            exerciseTypeLabel.setText(selected.getExerciseType());
            exerciseGroupLabel.setText(selected.getExerciseGroup());
            descriptionLabel.setText(selected.getDescription());
            instructionLabel.setText(selected.getInstruction());
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }

    private ObservableList<Exercise> loadData() {
        ExerciseDao exerciseDao = new ExerciseDao();
        return FXCollections.observableArrayList(exerciseDao.read());
    }

    private void refreshTableView() {
        exerciseTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(exerciseTableView.getItems().size(), "resultado"));
    }

    private void initExerciseTableView() {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        exerciseTypeTableColumn.setCellValueFactory(new PropertyValueFactory("exerciseType"));
        exerciseGroupTableColumn.setCellValueFactory(new PropertyValueFactory("exerciseGroup"));
        refreshTableView();
    }

    private boolean caseSensitiveEnabled(Exercise exercise, String searchText, int optionOrder) {
        String name = exercise.getName();
        String exerciseType = exercise.getExerciseType();
        String exerciseGroup = exercise.getExerciseGroup();

        List<String> fields = Arrays.asList(name, exerciseType, exerciseGroup);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Exercise exercise, String searchText, int optionOrder) {
        String name = exercise.getName().toLowerCase();
        String exerciseType = exercise.getExerciseType().toLowerCase();
        String exerciseGroup = exercise.getExerciseGroup().toLowerCase();

        List<String> fields = Arrays.asList(name, exerciseType, exerciseGroup);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText)) || (list.get(2).contains(searchText));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText)) || (list.get(2).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText)) || (list.get(2).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText)) || (list.get(2).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }

    private void search() {
        FilteredList<Exercise> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(exercise -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(exercise, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(exercise, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(exercise, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(exercise, newValue, 4);
                }

                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(exercise, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(exercise, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(exercise, newValue.toLowerCase(), 4);

                return caseSensitiveDisabled(exercise, newValue.toLowerCase(), 1);
            });
            exerciseTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(exerciseTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Exercise> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(exerciseTableView.comparatorProperty());
        exerciseTableView.setItems(sortedData);
    }

    private void listeners() {
        exerciseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Exercise) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(exerciseTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
    private void goToFirstRow() {
        ChangeTableRow.changeToFirstRow(exerciseTableView);
    }

    @FXML
    private void goToLastRow() {
        ChangeTableRow.changeToLastRow(exerciseTableView);
    }

}
