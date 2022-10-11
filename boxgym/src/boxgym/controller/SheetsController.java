package boxgym.controller;

import boxgym.dao.SheetDao;
import boxgym.dao.SheetWorkoutDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Sheet;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class SheetsController implements Initializable {

    AlertHelper alert = new AlertHelper();

    private Sheet selected;

    @FXML
    private Button statusChangerButton;

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
    private TableView<Sheet> sheetTableView;

    @FXML
    private TableColumn<Sheet, Integer> sheetIdTableColumn;

    @FXML
    private TableColumn<Sheet, String> fkCustomerTableColumn;

    @FXML
    private TableColumn<Sheet, String> descriptionTableColumn;

    @FXML
    private TableColumn<Sheet, LocalDate> expirationDateTableColumn;

    @FXML
    private TableColumn<Sheet, String> commentsTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label sheetIdLabel;

    @FXML
    private Label fkCustomerLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label expirationDateLabel;

    @FXML
    private Label commentsLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;

    @FXML
    private Button listButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton, statusChangerButton, addButton, deleteButton, listButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initSheetTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
        enableOrDisableStatusChangerButton();
    }

    private void enableOrDisableStatusChangerButton() {
        SheetDao sheetDao = new SheetDao();
        if (sheetDao.checkExpiredSheets() == 0) {
            statusChangerButton.setDisable(true);
        } else {
            statusChangerButton.setDisable(false);
        }
    }

    @FXML
    void statusChanger(ActionEvent event) {
        SheetDao sheetDao = new SheetDao();
        alert.confirmationAlert("Existem fichas de treino vencidas", "Deseja inativá-las?");
        if (alert.getResult().get() == ButtonType.YES) {
            sheetDao.updateExpiredSheetsStatus();
            refreshTableView();
            resetDetails();
            statusChangerButton.setDisable(true);
            alert.customAlert(Alert.AlertType.WARNING, "As fichas de treino foram inativadas com sucesso", "");
        }
    }

    @FXML
    void addSheet(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/SheetsAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            SheetsAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Cadastrar ficha", root);

            if (controller.isSheetCreationFlag() && !controller.isWorkoutsEntryCreationFlag()) {
                SheetDao sheetDao = new SheetDao();
                sheetDao.deleteLastEntry();
            } else if (controller.isSheetCreationFlag() && controller.isWorkoutsEntryCreationFlag()) {
                refreshTableView();
                sheetTableView.getSelectionModel().selectLast();
            }

        } catch (IOException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void deleteSheet(ActionEvent event) {
        SheetDao sheetDao = new SheetDao();
        SheetWorkoutDao sheetWorkoutDao = new SheetWorkoutDao();

        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione uma ficha para excluir", "");
        } else {
            alert.confirmationAlert("Excluir ficha", "Tem certeza que deseja excluir a ficha '" + selected.getDescription()+ "' "
                    + "do(a) aluno(a) '" + selected.getTempCustomerName() + "'? "
                    + "\n\nA ficha será excluída de forma definitiva e não poderá ser recuperada.");
            if (alert.getResult().get() == ButtonType.YES) {
                sheetWorkoutDao.delete(selected);
                sheetDao.delete(selected);
                refreshTableView();
                resetDetails();
                alert.customAlert(Alert.AlertType.WARNING, "Ficha excluída com sucesso", "");
            }
        }
    }

    private void resetDetails() {
        if (selected == null) {
            sheetIdLabel.setText("");
            fkCustomerLabel.setText("");
            descriptionLabel.setText("");
            expirationDateLabel.setText("");
            commentsLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            sheetIdLabel.setText(String.valueOf(selected.getSheetId()));
            fkCustomerLabel.setText(selected.getTempCustomerName());
            descriptionLabel.setText(selected.getDescription());
            expirationDateLabel.setText(selected.getExpirationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            commentsLabel.setText(selected.getComments());
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }

    @FXML
    void listWorkouts(ActionEvent event) {

    }

    private ObservableList<Sheet> loadData() {
        SheetDao sheetDao = new SheetDao();
        return FXCollections.observableArrayList(sheetDao.readOnlyRowsWithActiveStatus());
    }

    private void refreshTableView() {
        sheetTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(sheetTableView.getItems().size(), "resultado"));
    }

    private void initSheetTableView() {
        sheetIdTableColumn.setCellValueFactory(new PropertyValueFactory("sheetId"));
        fkCustomerTableColumn.setCellValueFactory(new PropertyValueFactory("tempCustomerName"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory("description"));
        expirationDateTableColumn.setCellValueFactory(new PropertyValueFactory("expirationDate"));
        TextFieldFormat.sheetTableCellDateFormat(expirationDateTableColumn);
        commentsTableColumn.setCellValueFactory(new PropertyValueFactory("comments"));
        refreshTableView();
    }

    private boolean caseSensitiveEnabled(Sheet sheet, String searchText, int optionOrder) {
        return true;
    }

    private boolean caseSensitiveDisabled(Sheet sheet, String searchText, int optionOrder) {
        return true;
    }

    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        return true;
    }

    private void search() {

    }

    private void listeners() {
        sheetTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Sheet) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(sheetTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
        sheetTableView.scrollTo(0);
        sheetTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (sheetTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            sheetTableView.getSelectionModel().selectLast();
            sheetTableView.scrollTo(sheetTableView.getItems().size() - 1);
        }
    }

}
