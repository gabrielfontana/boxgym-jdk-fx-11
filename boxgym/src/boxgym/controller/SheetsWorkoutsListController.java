package boxgym.controller;

import boxgym.dao.SheetWorkoutDao;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.model.SheetWorkout;
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

public class SheetsWorkoutsListController implements Initializable {
    
    private int selectedSheet;

    private SheetWorkout selected;
    
    @FXML
    private TableView<SheetWorkout> workoutListTableView;
    
    @FXML
    private TableColumn<SheetWorkout, String> workoutDescriptionTableColumn;
    
    @FXML
    private TableColumn<SheetWorkout, String> dayOfTheWeekTableColumn;
    
    @FXML
    private Label countLabel;
    
    @FXML
    private Label selectedRowLabel;
    
    @FXML
    private MaterialDesignIconView firstRow;
    
    @FXML
    private MaterialDesignIconView lastRow;

    public int getSelectedSheet() {
        return selectedSheet;
    }

    public void setSelectedSheet(int selectedSheet) {
        this.selectedSheet = selectedSheet;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ButtonHelper.iconButton(firstRow, lastRow);
        Platform.runLater(() -> initMethods());
    }
    
    private void initMethods() {
        initWorkoutsListTableView();
        listeners();
        countLabel.setText(TableViewCount.footerMessage(workoutListTableView.getItems().size(), "treino"));
    }
    
    private ObservableList<SheetWorkout> loadData() {
        SheetWorkoutDao dao = new SheetWorkoutDao();
        return FXCollections.observableArrayList(dao.read(getSelectedSheet()));
    }

    private void initWorkoutsListTableView() {
        workoutDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory("tempWorkoutDescription"));
        dayOfTheWeekTableColumn.setCellValueFactory(new PropertyValueFactory("dayOfTheWeek"));
        workoutListTableView.setItems(loadData());
    }
    
    private void listeners() {
        workoutListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (SheetWorkout) newValue;
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(workoutListTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
            }
        });
    }

    @FXML
    void goToFirstRow(MouseEvent event) {
        workoutListTableView.scrollTo(0);
        workoutListTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (workoutListTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            workoutListTableView.getSelectionModel().selectLast();
            workoutListTableView.scrollTo(workoutListTableView.getItems().size() - 1);
        }
    }
    
}
