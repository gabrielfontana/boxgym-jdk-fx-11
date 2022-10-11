package boxgym.controller;

import boxgym.dao.SaleDao;
import boxgym.dao.SaleProductDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Sale;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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

public class SalesController implements Initializable {
    
    AlertHelper alert = new AlertHelper();

    private Sale selected;
    
    @FXML
    private MenuButton filterButton;

    @FXML
    private CheckMenuItem caseSensitiveOp;

    @FXML
    private ToggleGroup filterOptions;

    @FXML
    private RadioMenuItem containsOp;

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
    private TableView<Sale> saleTableView;
    
    @FXML
    private TableColumn<Sale, Integer> saleIdTableColumn;
    
    @FXML
    private TableColumn<Sale, String> fkCustomerTableColumn;
    
    @FXML
    private TableColumn<Sale, LocalDate> saleDateTableColumn;
    
    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;
    
    @FXML
    private Label saleIdLabel;
    
    @FXML
    private Label fkCustomerLabel;
    
    @FXML
    private Label saleDateLabel;

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
        initSaleTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }
    
    @FXML
    void addSale(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/SalesAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);
            
            SalesAddController controller = loader.getController();
            
            StageHelper.createAddOrUpdateStage("Cadastrar venda", root);
            
            if (controller.isSaleCreationFlag() && !controller.isProductsEntryCreationFlag()) {
                SaleDao saleDao = new SaleDao();
                saleDao.deleteLastEntry();
            } else if (controller.isSaleCreationFlag() && controller.isProductsEntryCreationFlag()) {
                refreshTableView();
                saleTableView.getSelectionModel().selectLast();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @FXML
    void deleteSale(ActionEvent event) {
        SaleDao saleDao = new SaleDao();
        SaleProductDao saleProductDao = new SaleProductDao();

        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione uma venda para cancelar", "");
        } else {
            alert.confirmationAlert("Cancelar venda", "Tem certeza que deseja cancelar esta venda? "
                    + "\n\nA venda será cancelada de forma definitiva e não poderá ser recuperada. Após isso, os produtos entrarão no estoque novamente.");
            if (alert.getResult().get() == ButtonType.YES) {
                saleProductDao.delete(selected);
                saleDao.delete(selected);
                refreshTableView();
                resetDetails();
                alert.customAlert(Alert.AlertType.WARNING, "Venda cancelada com sucesso", "");
            }
        }
    }
    
    private void resetDetails() {
        if (selected == null) {
            saleIdLabel.setText("");
            fkCustomerLabel.setText("");
            saleDateLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            saleIdLabel.setText(String.valueOf(selected.getSaleId()));
            fkCustomerLabel.setText(selected.getTempCustomerName());
            saleDateLabel.setText(selected.getSaleDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }
    
    @FXML
    void listProducts(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione uma venda para listar os produtos vendidos", "");
        } else {
            int index = saleTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/SalesProductsList.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                SalesProductsListController controller = loader.getController();
                controller.setSelectedSale(selected.getSaleId());

                StageHelper.createAddOrUpdateStage("Listar Produtos da Venda", root);
                saleTableView.getSelectionModel().select(index);
            } catch (IOException ex) {
                Logger.getLogger(StockEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private ObservableList<Sale> loadData() {
        SaleDao saleDao = new SaleDao();
        return FXCollections.observableArrayList(saleDao.read());
    }

    private void refreshTableView() {
        saleTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(saleTableView.getItems().size(), "resultado"));
    }

    private void initSaleTableView() {
        saleIdTableColumn.setCellValueFactory(new PropertyValueFactory("saleId"));
        fkCustomerTableColumn.setCellValueFactory(new PropertyValueFactory("tempCustomerName"));

        saleDateTableColumn.setCellValueFactory(new PropertyValueFactory("saleDate"));
        TextFieldFormat.saleTableCellDateFormat(saleDateTableColumn);

        refreshTableView();
    }
    
    private boolean caseSensitiveEnabled(Sale sale, String searchText, int optionOrder) {
        String saleId = String.valueOf(sale.getSaleId());
        String tempCustomerName = sale.getTempCustomerName();
        String saleDate = sale.getSaleDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<String> fields = Arrays.asList(saleId, tempCustomerName, saleDate);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Sale sale, String searchText, int optionOrder) {
        String saleId = String.valueOf(sale.getSaleId());
        String tempCustomerName = sale.getTempCustomerName().toLowerCase();
        String saleDate = sale.getSaleDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        List<String> fields = Arrays.asList(saleId, tempCustomerName, saleDate);

        return stringComparasion(fields, searchText, optionOrder);
    }
    
    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText))
                        || (list.get(2).contains(searchText));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText))
                        || (list.get(2).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText))
                        || (list.get(2).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText))
                        || (list.get(2).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }
    
    private void search() {
        FilteredList<Sale> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sale -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(sale, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(sale, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(sale, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(sale, newValue, 4);
                } 
                
                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(sale, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(sale, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(sale, newValue.toLowerCase(), 4);
                
                return caseSensitiveDisabled(sale, newValue.toLowerCase(), 1);
            });
            saleTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(saleTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Sale> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(saleTableView.comparatorProperty());
        saleTableView.setItems(sortedData);
    }
    
    private void listeners() {
        saleTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Sale) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(saleTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
        saleTableView.scrollTo(0);
        saleTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (saleTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            saleTableView.getSelectionModel().selectLast();
            saleTableView.scrollTo(saleTableView.getItems().size() - 1);
        }
    }
    
}
