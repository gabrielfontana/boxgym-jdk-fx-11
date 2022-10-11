package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.dao.SheetDao;
import boxgym.dao.SheetWorkoutDao;
import boxgym.dao.WorkoutDao;
import boxgym.helper.ActionButtonTableCell;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Sheet;
import boxgym.model.SheetWorkout;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;
import org.controlsfx.control.PrefixSelectionComboBox;

public class SheetsAddController implements Initializable {

    CustomerDao customerDao = new CustomerDao();
    LinkedHashMap<Integer, String> customerMap = customerDao.getCustomerForHashMap();

    WorkoutDao workoutDao = new WorkoutDao();
    LinkedHashMap<Integer, String> workoutMap = workoutDao.getWorkoutForHashMap();

    List<SheetWorkout> list = new ArrayList<>();
    ObservableList<SheetWorkout> obsListItens;

    AlertHelper ah = new AlertHelper();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane sheetArea;

    @FXML
    private PrefixSelectionComboBox<String> customerComboBox;

    @FXML
    private LimitedTextField descriptionTextField;

    @FXML
    private DatePicker expirationDateDatePicker;

    @FXML
    private Button addSheetButton;

    @FXML
    private AnchorPane workoutsEntryArea;

    @FXML
    private TextField sheetIdTextField;

    @FXML
    private PrefixSelectionComboBox<String> workoutGoalComboBox;

    @FXML
    private PrefixSelectionComboBox<String> workoutDescriptionComboBox;

    @FXML
    private PrefixSelectionComboBox<String> dayOfTheWeekComboBox;

    @FXML
    private Button addWorkoutEntryButton;

    @FXML
    private TableView<SheetWorkout> workoutEntryTableView;

    @FXML
    private TableColumn<SheetWorkout, String> workoutDescriptionTableColumn;

    @FXML
    private TableColumn<SheetWorkout, String> dayOfTheWeekTableColumn;

    @FXML
    private TableColumn<SheetWorkout, Button> actionButtonTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private TextArea commentsTextArea;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    private boolean sheetCreationFlag;
    private boolean workoutsEntryCreationFlag;

    public boolean isSheetCreationFlag() {
        return sheetCreationFlag;
    }

    public void setSheetCreationFlag(boolean sheetCreationFlag) {
        this.sheetCreationFlag = sheetCreationFlag;
    }

    public boolean isWorkoutsEntryCreationFlag() {
        return workoutsEntryCreationFlag;
    }

    public void setWorkoutsEntryCreationFlag(boolean workoutsEntryCreationFlag) {
        this.workoutsEntryCreationFlag = workoutsEntryCreationFlag;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonsProperties();

        setSheetCreationFlag(false);
        setWorkoutsEntryCreationFlag(false);
        inputRestrictions();
        loadCustomerNameComboBox();
        expirationDateDatePicker.setEditable(false);

        loadWorkoutGoalComboBox();
        workoutDescriptionComboBoxListener();
        workoutDescriptionComboBox.setPromptText("---");
        defaultSheetDescription();
        loadDayOfTheWeekComboBox();

        initWorkoutEntryTableView();
        countLabel.setText("Exibindo nenhum treino");
    }

    private void buttonsProperties() {
        ButtonHelper.buttonCursor(addSheetButton, addWorkoutEntryButton, saveButton, clearButton);
        ButtonHelper.iconButton(firstRow, lastRow);
    }

    private void inputRestrictions() {
        descriptionTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
    }

    private void loadCustomerNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String s : customerMap.values()) {
            obsList.add(s);
        }
        customerComboBox.setPromptText("Selecione");
        customerComboBox.setItems(obsList);
    }

    private int getKeyFromCustomerComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : customerMap.entrySet()) {
            if (customerComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    @FXML
    private void addSheet(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(customerComboBox, "Selecione o campo Cliente. \n");
        validation.emptyTextField(descriptionTextField.getText(), "Preencha o campo Descrição da Ficha. \n");
        validation.nullDatePicker(expirationDateDatePicker, "Selecione o campo Data de Validade. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível iniciar o cadastro desta ficha", validation.getMessage());
        } else {
            sheetArea.setDisable(true);
            workoutsEntryArea.setDisable(false);
            firstRow.setDisable(false);
            lastRow.setDisable(false);
            saveButton.setDisable(false);
            clearButton.setDisable(false);
            SheetDao sheetDao = new SheetDao();
            Sheet sheet = new Sheet(getKeyFromCustomerComboBox(), descriptionTextField.getText(), expirationDateDatePicker.getValue(), "", "1");
            sheetDao.create(sheet);
            sheetIdTextField.setText(String.valueOf(sheetDao.getSheetId()));
            setSheetCreationFlag(true);
        }
    }

    private void loadWorkoutGoalComboBox() {
        String[] goalsList = {"Condicionamento Físico", "Emagrecimento", "Hipertrofia", "Reabilitação Física"};
        workoutGoalComboBox.setPromptText("Selecione");
        workoutGoalComboBox.setItems(FXCollections.observableArrayList(goalsList));
    }

    private void workoutDescriptionComboBoxListener() {
        workoutGoalComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            switch (workoutGoalComboBox.getSelectionModel().getSelectedItem()) {
                case "Condicionamento Físico":
                    loadWorkoutDescriptionComboBox("Condicionamento Físico");
                    break;
                case "Emagrecimento":
                    loadWorkoutDescriptionComboBox("Emagrecimento");
                    break;
                case "Hipertrofia":
                    loadWorkoutDescriptionComboBox("Hipertrofia");
                    break;
                case "Reabilitação Física":
                    loadWorkoutDescriptionComboBox("Reabilitação Física");
                    break;
            }
        });
    }

    private void loadWorkoutDescriptionComboBox(String workoutGoal) {
        WorkoutDao dao = new WorkoutDao();
        ObservableList<String> obsList = FXCollections.observableList(dao.filterWorkoutsByGoal(workoutGoal));
        workoutDescriptionComboBox.setItems(obsList);
    }

    private int getKeyFromWorkoutDescriptionComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : workoutMap.entrySet()) {
            if (workoutDescriptionComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }
    
    private void defaultSheetDescription() {
        customerComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            SheetDao sheetDao = new SheetDao();
            int amount = sheetDao.countSheets(getKeyFromCustomerComboBox()) + 1;
            descriptionTextField.setText("Ficha " + amount);
        });
    }

    private void loadDayOfTheWeekComboBox() {
        String[] days = {"Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};
        dayOfTheWeekComboBox.setPromptText("Selecione");
        dayOfTheWeekComboBox.setItems(FXCollections.observableArrayList(days));
    }

    @FXML
    private void addWorkoutEntry(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(workoutDescriptionComboBox, "Selecione o campo Descrição do Treino. \n");
        validation.invalidComboBox(dayOfTheWeekComboBox, "Preencha o campo Dia da Semana. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível adicionar o treino à ficha", validation.getMessage());
        } else {
            SheetWorkout item = new SheetWorkout(Integer.valueOf(sheetIdTextField.getText()), getKeyFromWorkoutDescriptionComboBox(), dayOfTheWeekComboBox.getSelectionModel().getSelectedItem());
            item.setTempWorkoutDescription(workoutDescriptionComboBox.getSelectionModel().getSelectedItem());
            list.add(item);
            obsListItens = FXCollections.observableArrayList(list);
            workoutEntryTableView.setItems(obsListItens);
            workoutEntryTableView.getSelectionModel().selectLast();
            initCount();
            clear();
        }
    }

    private void initWorkoutEntryTableView() {
        workoutDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory("tempWorkoutDescription"));
        dayOfTheWeekTableColumn.setCellValueFactory(new PropertyValueFactory("dayOfTheWeek"));
        actionButtonTableColumn.setCellFactory(ActionButtonTableCell.<SheetWorkout>forTableColumn("", (SheetWorkout e) -> {
            list.remove(e);
            obsListItens.remove(e);
            workoutEntryTableView.getItems().remove(e);
            initCount();
            return e;
        }));
    }

    private void initCount() {
        int count = obsListItens.size();
        countLabel.setText(TableViewCount.footerMessage(count, "treino"));
    }

    @FXML
    private void save() {
        if (!(list == null || list.isEmpty())) {
            for (SheetWorkout item : list) {
                SheetWorkoutDao dao = new SheetWorkoutDao();
                dao.create(item);
            }
            SheetDao sheetDao = new SheetDao();
            sheetDao.updateComments(commentsTextArea.getText(), Integer.valueOf(sheetIdTextField.getText()));
            setWorkoutsEntryCreationFlag(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "Ficha cadastrada com sucesso", "");
            anchorPane.getScene().getWindow().hide();
        } else {
            ah.customAlert(Alert.AlertType.INFORMATION, "Lista de treinos vazia", "");
        }
    }

    @FXML
    private void clear() {
        workoutDescriptionComboBox.valueProperty().set(null);
        dayOfTheWeekComboBox.valueProperty().set(null);
    }

    @FXML
    void goToFirstRow(MouseEvent event) {
        workoutEntryTableView.scrollTo(0);
        workoutEntryTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (workoutEntryTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            workoutEntryTableView.getSelectionModel().selectLast();
            workoutEntryTableView.scrollTo(workoutEntryTableView.getItems().size() - 1);
        }
    }

}
