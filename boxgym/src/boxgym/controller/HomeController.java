package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.dao.ProductDao;
import boxgym.dao.SaleDao;
import boxgym.dao.StockEntryDao;
import boxgym.dao.SupplierDao;
import boxgym.model.Customer;
import boxgym.model.Product;
import boxgym.model.Sale;
import boxgym.model.StockEntry;
import boxgym.model.Supplier;
import com.google.common.collect.TreeMultimap;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class HomeController implements Initializable {

    @FXML
    private Label totalCustomers;

    @FXML
    private Label customersLast90Days;

    @FXML
    private BarChart<String, Integer> ageRangeBarChart;

    @FXML
    private CategoryAxis ageRangeBarChartXAxis;

    @FXML
    private NumberAxis ageRangeBarChartYAxis;

    @FXML
    private Label ageRangeWarningLabel;

    @FXML
    private PieChart customersProfilePieChart;

    @FXML
    private Label customersProfileWarningLabel;

    @FXML
    private PieChart customersByCityPieChart;

    @FXML
    private Label customersByCityWarningLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customersDashboard();
        suppliersDashboard();
        productsDashboard();
        salesDashboard();
    }

    private void customersDashboard() {
        setTotalCustomers();
        setCustomersLast90Days();
        buildAgeRangeBarChart();
        buildCustomersProfilePieChart();
        buildCustomersByCityPieChart();
    }

    private void setTotalCustomers() {
        CustomerDao customerDao = new CustomerDao();
        List<Customer> customersList = customerDao.read();
        totalCustomers.setText(String.valueOf(customersList.size()));
    }

    private void setCustomersLast90Days() {
        CustomerDao customerDao = new CustomerDao();
        customersLast90Days.setText(String.valueOf(customerDao.getAmountOfCustomersLast90DaysForDashboard()));
    }

    private void buildAgeRangeBarChart() {
        CustomerDao customerDao = new CustomerDao();
        List<Customer> customersList = customerDao.read();

        if (customersList == null || customersList.isEmpty()) {
            ageRangeWarningLabel.setText("Não há registros para gerar o gráfico");
            ageRangeBarChart.setVisible(false);
        } else {
            ageRangeBarChartXAxis.setLabel("Idade");
            ageRangeBarChartYAxis.setLabel("Quantidade");
            
            List<String> subtitleList = Arrays.asList("Até 20", "21 a 30", "31 a 40", "41 a 50", "Acima de 50");
            
            CustomerDao maleAgeRange = new CustomerDao();
            List<Integer> maleList = maleAgeRange.getMaleAgeRangeForDashboard();

            XYChart.Series<String, Integer> series1 = new XYChart.Series();
            series1.setName("Masculino");
            for (int i = 0; i < subtitleList.size(); i++) {
                series1.getData().add(new XYChart.Data(subtitleList.get(i), maleList.get(i)));
            }
            
            CustomerDao femaleAgeRange = new CustomerDao();
            List<Integer> femaleList = femaleAgeRange.getFemaleAgeRangeForDashboard();

            XYChart.Series<String, Integer> series2 = new XYChart.Series();
            series2.setName("Feminino");
            for (int i = 0; i < subtitleList.size(); i++) {
                series2.getData().add(new XYChart.Data(subtitleList.get(i), femaleList.get(i)));
            }

            ageRangeBarChart.getData().addAll(series1, series2);

            for (XYChart.Series<String, Integer> series : ageRangeBarChart.getData()) {
                for (XYChart.Data<String, Integer> item : series.getData()) {
                    if (item.getYValue() == 1) {
                        Tooltip.install(item.getNode(), new Tooltip(item.getXValue() + ": " + item.getYValue() + " pessoa"));
                    } else {
                        Tooltip.install(item.getNode(), new Tooltip(item.getXValue() + ": " + item.getYValue() + " pessoas"));
                    }
                }
            }
        }
    }

    private void buildCustomersProfilePieChart() {
        CustomerDao customerDao = new CustomerDao();
        List<Customer> customersList = customerDao.read();

        if (customersList == null || customersList.isEmpty()) {
            customersProfileWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            CustomerDao male = new CustomerDao();
            CustomerDao female = new CustomerDao();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Masculino", male.getAmountOfEachSexForDashboard("Masculino")),
                    new PieChart.Data("Feminino", female.getAmountOfEachSexForDashboard("Feminino"))
            );

            customersProfilePieChart.setData(pieChartData);
            pieChartCommonMethods(customersProfilePieChart, Side.RIGHT);
            pieChartTooltip(customersProfilePieChart, "cliente", "clientes");
        }
    }

    private void buildCustomersByCityPieChart() {
        CustomerDao customerDao = new CustomerDao();
        List<Customer> customersList = customerDao.read();

        if (customersList == null || customersList.isEmpty()) {
            customersByCityWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            CustomerDao amount = new CustomerDao();
            TreeMultimap<String, Integer> ascMap = amount.getAmountOfCustomersByCityForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            ascMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            });
            CustomerDao withoutCity = new CustomerDao();
            int amountOfCustomersWithoutCity = withoutCity.getAmountOfCustomersWithoutCityForDashboard();
            if (amountOfCustomersWithoutCity != 0) {
                pieChartData.add(new PieChart.Data("Não informado", amountOfCustomersWithoutCity));
            }

            customersByCityPieChart.setData(pieChartData);
            pieChartCommonMethods(customersByCityPieChart, Side.RIGHT);
            pieChartTooltip(customersByCityPieChart, "cliente", "clientes");
        }
    }

    @FXML
    private Label totalSuppliers;

    @FXML
    private Label suppliersLast90Days;

    @FXML
    private PieChart suppliersByFUPieChart;

    @FXML
    private Label suppliersByFUWarningLabel;

    @FXML
    private PieChart mostFrequentSuppliersSEPieChart;

    @FXML
    private Label mostFrequentSuppliersSEWarningLabel;

    private void suppliersDashboard() {
        setTotalSuppliers();
        setSuppliersLast90Days();
        buildSuppliersByFUPieChart();
        buildMostFrequentSuppliersSEPieChart();
    }

    private void setTotalSuppliers() {
        SupplierDao supplierDao = new SupplierDao();
        List<Supplier> suppliersList = supplierDao.read();
        totalSuppliers.setText(String.valueOf(suppliersList.size()));
    }

    private void setSuppliersLast90Days() {
        SupplierDao supplierDao = new SupplierDao();
        suppliersLast90Days.setText(String.valueOf(supplierDao.getAmountOfSuppliersLast90DaysForDashboard()));
    }

    private void buildSuppliersByFUPieChart() {
        SupplierDao supplierDao = new SupplierDao();
        List<Supplier> suppliersList = supplierDao.read();

        if (suppliersList == null || suppliersList.isEmpty()) {
            suppliersByFUWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            SupplierDao amount = new SupplierDao();
            TreeMultimap<String, Integer> ascMap = amount.getAmountOfSuppliersByFUForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            ascMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            });
            SupplierDao withoutFU = new SupplierDao();
            int amountOfSuppliersWithoutFU = withoutFU.getAmountOfSuppliersWithoutFUForDashboard();
            if (amountOfSuppliersWithoutFU != 0) {
                pieChartData.add(new PieChart.Data("Não informado", amountOfSuppliersWithoutFU));
            }

            suppliersByFUPieChart.setData(pieChartData);
            pieChartCommonMethods(suppliersByFUPieChart, Side.BOTTOM);
            pieChartTooltip(suppliersByFUPieChart, "fornecedor", "fornecedores");
        }
    }

    private void buildMostFrequentSuppliersSEPieChart() {
        SupplierDao supplierDao = new SupplierDao();
        List<Supplier> suppliersList = supplierDao.read();
        
        StockEntryDao stockEntryDao = new StockEntryDao();
        List<StockEntry> stockEntriesList = stockEntryDao.read();

        if (suppliersList == null || suppliersList.isEmpty() || stockEntriesList == null || stockEntriesList.isEmpty()) {
            mostFrequentSuppliersSEWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            SupplierDao amount = new SupplierDao();
            TreeMultimap<Integer, String> descMap = amount.getMostFrequentSuppliersSEForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            descMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getValue(), entry.getKey()));
            });

            mostFrequentSuppliersSEPieChart.setData(pieChartData);
            pieChartCommonMethods(mostFrequentSuppliersSEPieChart, Side.BOTTOM);
            pieChartTooltip(mostFrequentSuppliersSEPieChart, "entrada de estoque", "entradas de estoque");
        }
    }

    @FXML
    private Label totalProducts;

    @FXML
    private Label productsLast90Days;

    @FXML
    private Label productsThisMonth;

    @FXML
    private Label totalStockValue;

    @FXML
    private PieChart mostPopularProductsPieChart;

    @FXML
    private Label mostPopularProductsWarningLabel;

    private void productsDashboard() {
        setTotalProducts();
        setProductsLast90Days();
        setProductsThisMonth();
        setTotalStockValue();
        buildMostPopularProductsPieChart();
    }

    private void setTotalProducts() {
        ProductDao productDao = new ProductDao();
        List<Product> productsList = productDao.read();
        totalProducts.setText(String.valueOf(productsList.size()));
    }

    private void setProductsLast90Days() {
        ProductDao productDao = new ProductDao();
        productsLast90Days.setText(String.valueOf(productDao.getAmountOfProductsLast90DaysForDashboard()));
    }

    private void setProductsThisMonth() {
        ProductDao productDao = new ProductDao();
        productsThisMonth.setText(String.valueOf(productDao.getAmountOfProductsThisMonthForDashboard()));
    }

    private void setTotalStockValue() {
        ProductDao productDao = new ProductDao();
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        totalStockValue.setText("R$ " + nf.format(productDao.getTotalStockValueForDashboard()));
    }

    private void buildMostPopularProductsPieChart() {
        ProductDao productDao = new ProductDao();
        List<Product> productsList = productDao.read();
        
        SaleDao saleDao = new SaleDao();
        List<Sale> salesList = saleDao.read();

        if (productsList == null || productsList.isEmpty() || salesList == null || salesList.isEmpty()) {
            mostPopularProductsWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            ProductDao sumAmount = new ProductDao();
            TreeMultimap<Integer, String> descMap = sumAmount.getMostPopularProductsForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            descMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getValue(), entry.getKey()));
            });

            mostPopularProductsPieChart.setData(pieChartData);
            pieChartCommonMethods(mostPopularProductsPieChart, Side.RIGHT);
            pieChartTooltip(mostPopularProductsPieChart, "unidade", "unidades");
        }
    }

    @FXML
    private Label salesLast90Days;

    @FXML
    private Label salesThisMonth;

    @FXML
    private Label avgTicketThisMonth;

    @FXML
    private Label lowestSaleThisMonth;

    @FXML
    private Label biggestSaleThisMonth;

    @FXML
    private LineChart<String, BigDecimal> annualSalesHistoryLineChart;

    @FXML
    private CategoryAxis annualSalesHistoryLineChartXAxis;

    @FXML
    private NumberAxis annualSalesHistoryLineChartYAxis;

    @FXML
    private Label annualSalesHistoryWarningLabel;

    @FXML
    private PieChart mostFrequentCustomersLast90DaysPieChart;

    @FXML
    private Label mostFrequentCustomersLast90DaysWarningLabel;

    private void salesDashboard() {
        setSalesLast90Days();
        setSalesThisMonth();
        setAvgTicketThisMonth();
        setLowestSaleThisMonth();
        setBiggestSaleThisMonth();
        buildAnnualSalesHistoryLineChart();
        buildMostFrequentCustomersLast90DaysPieChart();
    }

    private void setSalesLast90Days() {
        SaleDao saleDao = new SaleDao();
        salesLast90Days.setText(String.valueOf(saleDao.getAmountOfSalesLast90DaysForDashboard()));
    }

    private void setSalesThisMonth() {
        SaleDao saleDao = new SaleDao();
        salesThisMonth.setText(String.valueOf(saleDao.getAmountOfSalesThisMonthForDashboard()));
    }

    private void setAvgTicketThisMonth() {
        SaleDao saleDao = new SaleDao();
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        BigDecimal avgTicket = saleDao.getAvgTicketThisMonthForDashboard();
        if (avgTicket == null) avgTicket = new BigDecimal("0");
        avgTicketThisMonth.setText("R$ " + nf.format(avgTicket));
    }

    private void setLowestSaleThisMonth() {
        SaleDao saleDao = new SaleDao();
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        BigDecimal lowestSale = saleDao.getLowestSaleThisMonthForDashboard();
        if (lowestSale == null) lowestSale = new BigDecimal("0");
        lowestSaleThisMonth.setText("R$ " + nf.format(lowestSale));
    }

    private void setBiggestSaleThisMonth() {
        SaleDao saleDao = new SaleDao();
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        BigDecimal biggestSale = saleDao.getBiggestSaleThisMonthForDashboard();
        if (biggestSale == null) biggestSale = new BigDecimal("0");
        biggestSaleThisMonth.setText("R$ " + nf.format(biggestSale));
    }

    private void buildAnnualSalesHistoryLineChart() {
        SaleDao saleDao = new SaleDao();
        List<Sale> salesList = saleDao.read();

        if (salesList == null || salesList.isEmpty()) {
            annualSalesHistoryWarningLabel.setText("Não há registros para gerar o gráfico");
            annualSalesHistoryLineChart.setVisible(false);
        } else {
            annualSalesHistoryLineChartXAxis.setLabel("Meses");
            annualSalesHistoryLineChartYAxis.setLabel("Valor (R$)");

            SaleDao annualSalesHistory = new SaleDao();
            List<BigDecimal> sumOfSalesByMonthList = annualSalesHistory.getAnnualSalesHistoryForDashboard();
            List<String> monthsList = Arrays.asList("Jan.", "Fev.", "Mar.", "Abr.", "Mai.", "Jun.", "Jul.", "Ago.", "Set.", "Out.", "Nov.", "Dez.");

            XYChart.Series<String, BigDecimal> series = new XYChart.Series();
            for (int i = 0; i < monthsList.size(); i++) {
                series.getData().add(new XYChart.Data(monthsList.get(i), sumOfSalesByMonthList.get(i)));
            }
            annualSalesHistoryLineChart.getData().addAll(series);
            annualSalesHistoryLineChart.setLegendVisible(false);

            NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
            for (XYChart.Data<String, BigDecimal> item : series.getData()) {
                Tooltip.install(item.getNode(), new Tooltip(item.getXValue() + ": R$ " + nf.format(item.getYValue())));
            }
        }
    }

    private void buildMostFrequentCustomersLast90DaysPieChart() {
        SaleDao saleDao = new SaleDao();
        List<Sale> salesList = saleDao.read();

        if (salesList == null || salesList.isEmpty()) {
            mostFrequentCustomersLast90DaysWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            SaleDao sumAmount = new SaleDao();
            TreeMultimap<Integer, String> descMap = sumAmount.getMostFrequentCustomersLast90DaysForDashoboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            descMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getValue(), entry.getKey()));
            });

            mostFrequentCustomersLast90DaysPieChart.setData(pieChartData);
            pieChartCommonMethods(mostFrequentCustomersLast90DaysPieChart, Side.BOTTOM);
            pieChartTooltip(mostFrequentCustomersLast90DaysPieChart, "compra", "compras");
        }
    }

    private void pieChartCommonMethods(PieChart pieChart, Side side) {
        pieChart.setClockwise(true);
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(true);
        pieChart.setLegendSide(side);
    }

    private void pieChartTooltip(PieChart pieChart, String tooltipSingular, String tooltipPlural) {
        pieChart.getData().forEach(data -> {
            int pieValue = (int) (data.getPieValue());
            if (pieValue == 1) {
                Tooltip.install(data.getNode(), new Tooltip(String.valueOf(pieValue) + " " + tooltipSingular));
            } else {
                Tooltip.install(data.getNode(), new Tooltip(String.valueOf(pieValue) + " " + tooltipPlural));
            }
        });
    }
}
