package boxgym.controller;

import boxgym.dao.MembershipDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Membership;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.controlsfx.control.PrefixSelectionComboBox;

public class MembershipsController implements Initializable {
    
    AlertHelper alert = new AlertHelper();
    
    private Membership selected;
    
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
    private PrefixSelectionComboBox<?> changeTableDataComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;
    
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Membership> membershipTableView;

    @FXML
    private TableColumn<Membership, String> fkCustomerTableColumn;

    @FXML
    private TableColumn<Membership, LocalDate> dueDateTableColumn;

    @FXML
    private TableColumn<Membership, BigDecimal> priceTableColumn;

    @FXML
    private TableColumn<Membership, String> statusTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label membershipIdLabel;

    @FXML
    private Label fkCustomerLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton, addButton, updateButton, deleteButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initMembershipTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }

    @FXML
    private void addMembership(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/MembershipsAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            MembershipsAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Cadastrar mensalidade(s)", root);

            if (controller.isCreated()) {
                refreshTableView();
                membershipTableView.getSelectionModel().selectLast();
            }
        } catch (IOException ex) {
            Logger.getLogger(MembershipsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void updateMembership(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione uma mensalidade para atualizar", "");
        } else {
            int index = membershipTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/MembershipsUpdate.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                MembershipsUpdateController controller = loader.getController();
                controller.setLoadMembership(selected);

                StageHelper.createAddOrUpdateStage("Atualizar mensalidade", root);

                if (controller.isUpdated()) {
                    refreshTableView();
                    membershipTableView.getSelectionModel().select(index);
                }
            } catch (IOException ex) {
                Logger.getLogger(MembershipsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void deleteMembership(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione uma mensalidade para cancelar", "");
        } else {
            alert.confirmationAlert("Cancelar mensalidade", "Tem certeza que deseja cancelar esta mensalidade? "
                    + "\n\nA mensalidade será cancelada de forma definitiva e não poderá ser recuperada. "
                    + "Além disso, qualquer cobrança em aberto também será cancelada.");
            if (alert.getResult().get() == ButtonType.YES) {
                int fkCustomer = selected.getFkCustomer();
                
                MembershipDao membershipDao = new MembershipDao();
                membershipDao.delete(selected);
                
                MembershipDao membershipDao1 = new MembershipDao();
                if (membershipDao1.checkExistingBillingDescription(fkCustomer)) {
                    MembershipDao membershipDao2 = new MembershipDao();
                    List<String> oldDescriptionList = membershipDao2.getBillingDescriptionToRename(fkCustomer);
                    
                    List<String> newDescriptionList = new ArrayList<>();
                    for (int i = 0; i < oldDescriptionList.size(); i++) {
                        newDescriptionList.add("Mensalidade " + (i + 1) + "/" + oldDescriptionList.size());
                    }
                    
                    MembershipDao membershipDao3 = new MembershipDao();
                    List<Integer> idList = membershipDao3.getBillingIdToRename(fkCustomer);
                    
                    MembershipDao membershipDao4 = new MembershipDao();
                    membershipDao4.updateBillingDescription(idList, newDescriptionList);
                }
                
                refreshTableView();
                resetDetails();
                alert.customAlert(Alert.AlertType.WARNING, "Mensalidade cancelada com sucesso", "");
            }
        }
    }
    
    private void resetDetails() {
        if (selected == null) {
            membershipIdLabel.setText("");
            fkCustomerLabel.setText("");
            dueDateLabel.setText("");
            priceLabel.setText("");
            statusLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }
    
    private void showDetails() {
        if (selected != null) {
            membershipIdLabel.setText(String.valueOf(selected.getMembershipId()));
            fkCustomerLabel.setText(selected.getTempCustomerName());
            dueDateLabel.setText(selected.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            TextFieldFormat.currencyFormat(priceLabel, selected.getPrice());
            statusLabel.setText(String.valueOf(selected.getStatus()));
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }
    
    private ObservableList<Membership> loadData() {
        MembershipDao membershipDao = new MembershipDao();
        return FXCollections.observableArrayList(membershipDao.read());
    }

    private void refreshTableView() {
        membershipTableView.setItems(loadData());
        //search();
        countLabel.setText(TableViewCount.footerMessage(membershipTableView.getItems().size(), "resultado"));
    }

    private void initMembershipTableView() {
        fkCustomerTableColumn.setCellValueFactory(new PropertyValueFactory("tempCustomerName"));
        dueDateTableColumn.setCellValueFactory(new PropertyValueFactory("dueDate"));
        TextFieldFormat.membershipTableCellDateFormat(dueDateTableColumn);
        priceTableColumn.setCellValueFactory(new PropertyValueFactory("price"));
        TextFieldFormat.membershipTableCellCurrencyFormat(priceTableColumn);
        statusTableColumn.setCellValueFactory(new PropertyValueFactory("status"));
        refreshTableView();
    }

    private void listeners() {
        membershipTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Membership) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(membershipTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
        membershipTableView.scrollTo(0);
        membershipTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
         if (membershipTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            membershipTableView.getSelectionModel().selectLast(); 
            membershipTableView.scrollTo(membershipTableView.getItems().size() - 1);
         }
    }

}
