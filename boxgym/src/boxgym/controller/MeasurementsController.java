package boxgym.controller;

import boxgym.dao.MeasurementDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Measurement;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class MeasurementsController implements Initializable {

    AlertHelper alert = new AlertHelper();

    private Measurement selected;

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
    private TableView<Measurement> measurementTableView;

    @FXML
    private TableColumn<Measurement, Integer> measurementIdTableColumn;

    @FXML
    private TableColumn<Measurement, String> fkCustomerTableColumn;

    @FXML
    private TableColumn<Measurement, LocalDate> measurementDateTableColumn;

    @FXML
    private TableColumn<Measurement, Integer> heightTableColumn;

    @FXML
    private TableColumn<Measurement, Float> weightTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label measurementIdLabel;

    @FXML
    private Label fkCustomerLabel;

    @FXML
    private Label measurementDateLabel;

    @FXML
    private Label heightLabel;

    @FXML
    private Label weightLabel;

    @FXML
    private Label neckLabel;

    @FXML
    private Label shoulderLabel;

    @FXML
    private Label rightArmLabel;

    @FXML
    private Label leftArmLabel;

    @FXML
    private Label rightForearmLabel;

    @FXML
    private Label leftForearmLabel;

    @FXML
    private Label thoraxLabel;

    @FXML
    private Label waistLabel;

    @FXML
    private Label abdomenLabel;

    @FXML
    private Label hipLabel;

    @FXML
    private Label rightThighLabel;

    @FXML
    private Label leftThighLabel;

    @FXML
    private Label rightCalfLabel;

    @FXML
    private Label leftCalfLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton, addButton, updateButton, deleteButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initMeasurementTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }

    @FXML
    private void addMeasurement(ActionEvent event) {

    }

    @FXML
    private void updateMeasurement(ActionEvent event) {

    }

    @FXML
    private void deleteMeasurement(ActionEvent event) {
        MeasurementDao measurementDao = new MeasurementDao();

        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione uma medição para excluir!", "");
        } else {
            alert.confirmationAlert("Tem certeza que deseja excluir esta medição?", "Esta ação é irreversível!");
            if (alert.getResult().get() == ButtonType.YES) {
                measurementDao.delete(selected);
                refreshTableView();
                resetDetails();
                alert.customAlert(Alert.AlertType.WARNING, "A medição foi excluída com sucesso!", "");
            }
        }
    }

    private void resetDetails() {
        if (selected == null) {
            measurementIdLabel.setText("");
            fkCustomerLabel.setText("");
            measurementDateLabel.setText("");
            heightLabel.setText("");
            weightLabel.setText("");
            neckLabel.setText("");
            shoulderLabel.setText("");
            rightArmLabel.setText("");
            leftArmLabel.setText("");
            rightForearmLabel.setText("");
            leftForearmLabel.setText("");
            thoraxLabel.setText("");
            waistLabel.setText("");
            abdomenLabel.setText("");
            hipLabel.setText("");
            rightThighLabel.setText("");
            leftThighLabel.setText("");
            rightCalfLabel.setText("");
            leftCalfLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            measurementIdLabel.setText(String.valueOf(selected.getMeasurementId()));
            fkCustomerLabel.setText(selected.getTempCustomerName());
            measurementDateLabel.setText(selected.getMeasurementDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            heightLabel.setText(String.valueOf(selected.getHeight()));
            weightLabel.setText(String.valueOf(selected.getWeight()));
            neckLabel.setText(String.valueOf(selected.getNeck()));
            shoulderLabel.setText(String.valueOf(selected.getShoulder()));
            rightArmLabel.setText(String.valueOf(selected.getRightArm()));
            leftArmLabel.setText(String.valueOf(selected.getLeftArm()));
            rightForearmLabel.setText(String.valueOf(selected.getRightForearm()));
            leftForearmLabel.setText(String.valueOf(selected.getLeftForearm()));
            thoraxLabel.setText(String.valueOf(selected.getThorax()));
            waistLabel.setText(String.valueOf(selected.getWaist()));
            abdomenLabel.setText(String.valueOf(selected.getAbdomen()));
            hipLabel.setText(String.valueOf(selected.getHip()));
            rightThighLabel.setText(String.valueOf(selected.getRightThigh()));
            leftThighLabel.setText(String.valueOf(selected.getLeftThigh()));
            rightCalfLabel.setText(String.valueOf(selected.getRightCalf()));
            leftCalfLabel.setText(String.valueOf(selected.getLeftCalf()));
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }

    private ObservableList<Measurement> loadData() {
        MeasurementDao measurementDao = new MeasurementDao();
        return FXCollections.observableArrayList(measurementDao.read());
    }

    private void refreshTableView() {
        measurementTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(measurementTableView.getItems().size(), "resultado"));
    }

    private void initMeasurementTableView() {
        measurementIdTableColumn.setCellValueFactory(new PropertyValueFactory("measurementId"));
        fkCustomerTableColumn.setCellValueFactory(new PropertyValueFactory("tempCustomerName"));
        measurementDateTableColumn.setCellValueFactory(new PropertyValueFactory("measurementDate"));
        TextFieldFormat.measurementTableCellDateFormat(measurementDateTableColumn);
        heightTableColumn.setCellValueFactory(new PropertyValueFactory("height"));
        weightTableColumn.setCellValueFactory(new PropertyValueFactory("weight"));
        refreshTableView();
    }

    private boolean caseSensitiveEnabled(Measurement measurement, String searchText, int optionOrder) {
        String measurementId = String.valueOf(measurement.getMeasurementId());
        String tempCustomerName = measurement.getTempCustomerName();
        String measurementDate = measurement.getMeasurementDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String height = String.valueOf(measurement.getHeight());
        String weight = String.valueOf(measurement.getWeight());

        List<String> fields = Arrays.asList(measurementId, tempCustomerName, measurementDate, height, weight);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Measurement measurement, String searchText, int optionOrder) {
        String measurementId = String.valueOf(measurement.getMeasurementId());
        String tempCustomerName = measurement.getTempCustomerName().toLowerCase();
        String measurementDate = measurement.getMeasurementDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String height = String.valueOf(measurement.getHeight());
        String weight = String.valueOf(measurement.getWeight());

        List<String> fields = Arrays.asList(measurementId, tempCustomerName, measurementDate, height, weight);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText))
                        || (list.get(2).contains(searchText) || (list.get(3).contains(searchText))
                        || (list.get(4).contains(searchText)));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText))
                        || (list.get(2).equals(searchText)) || (list.get(3).equals(searchText)) 
                        || (list.get(4).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText))
                        || (list.get(2).startsWith(searchText)) || (list.get(3).startsWith(searchText))
                        || (list.get(4).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText))
                        || (list.get(2).endsWith(searchText)) || (list.get(3).endsWith(searchText))
                        || (list.get(4).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }
    
    private void search() {
        FilteredList<Measurement> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(measurement -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(measurement, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(measurement, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(measurement, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(measurement, newValue, 4);
                } 
                
                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(measurement, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(measurement, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(measurement, newValue.toLowerCase(), 4);
                
                return caseSensitiveDisabled(measurement, newValue.toLowerCase(), 1);
            });
            measurementTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(measurementTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Measurement> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(measurementTableView.comparatorProperty());
        measurementTableView.setItems(sortedData);
    }
    
    private void listeners() {
        measurementTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Measurement) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(measurementTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
        measurementTableView.scrollTo(0);
        measurementTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (measurementTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            measurementTableView.getSelectionModel().selectLast();
            measurementTableView.scrollTo(measurementTableView.getItems().size() - 1);
        }
    }
}
