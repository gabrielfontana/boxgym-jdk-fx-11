package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.dao.ProductDao;
import boxgym.dao.SaleDao;
import boxgym.dao.SupplierDao;
import boxgym.model.Customer;
import boxgym.model.Product;
import boxgym.model.Sale;
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

            CustomerDao maleAgeRange = new CustomerDao();
            List<Integer> maleList = maleAgeRange.getMaleAgeRangeForDashboard();

            XYChart.Series<String, Integer> series1 = new XYChart.Series();
            series1.setName("Masculino");
            series1.getData().add(new XYChart.Data("Até 20", maleList.get(0)));
            series1.getData().add(new XYChart.Data("21 a 30", maleList.get(1)));
            series1.getData().add(new XYChart.Data("31 a 40", maleList.get(2)));
            series1.getData().add(new XYChart.Data("41 a 50", maleList.get(3)));
            series1.getData().add(new XYChart.Data("Acima de 50", maleList.get(4)));

            CustomerDao femaleAgeRange = new CustomerDao();
            List<Integer> femaleList = femaleAgeRange.getFemaleAgeRangeForDashboard();

            XYChart.Series<String, Integer> series2 = new XYChart.Series();
            series2.setName("Feminino");
            series2.getData().add(new XYChart.Data("Até 20", femaleList.get(0)));
            series2.getData().add(new XYChart.Data("21 a 30", femaleList.get(1)));
            series2.getData().add(new XYChart.Data("31 a 40", femaleList.get(2)));
            series2.getData().add(new XYChart.Data("41 a 50", femaleList.get(3)));
            series2.getData().add(new XYChart.Data("Acima de 50", femaleList.get(4)));

            ageRangeBarChart.getData().addAll(series1, series2);

            for (XYChart.Series<String, Integer> series : ageRangeBarChart.getData()) {
                for (XYChart.Data<String, Integer> item : series.getData()) {
                    if (item.getYValue() != 1) {
                        Tooltip.install(item.getNode(), new Tooltip(item.getXValue() + ": " + item.getYValue() + " pessoas"));
                    } else {
                        Tooltip.install(item.getNode(), new Tooltip(item.getXValue() + ": " + item.getYValue() + " pessoa"));
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

            pieChartCommonMethods(customersProfilePieChart, Side.RIGHT);
            customersProfilePieChart.setData(pieChartData);

            customersProfilePieChart.getData().forEach(data -> {
                int pieValue = (int) (data.getPieValue());
                Tooltip tooltip = new Tooltip(String.valueOf(pieValue));
                Tooltip.install(data.getNode(), tooltip);
            });
        }
    }

    private void buildCustomersByCityPieChart() {
        CustomerDao customerDao = new CustomerDao();
        List<Customer> customersList = customerDao.read();

        if (customersList == null || customersList.isEmpty()) {
            customersByCityWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            CustomerDao amount = new CustomerDao();
            TreeMultimap<String, Integer> sortedMap = amount.getAmountOfCustomersByCityForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            sortedMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            });
            CustomerDao withoutCity = new CustomerDao();
            int amountOfCustomersWithoutCity = withoutCity.getAmountOfCustomersWithoutCityForDashboard();
            if (amountOfCustomersWithoutCity != 0) {
                pieChartData.add(new PieChart.Data("Não informado", amountOfCustomersWithoutCity));
            }

            pieChartCommonMethods(customersByCityPieChart, Side.RIGHT);
            customersByCityPieChart.setData(pieChartData);

            customersByCityPieChart.getData().forEach(data -> {
                int pieValue = (int) (data.getPieValue());
                Tooltip tooltip = new Tooltip(String.valueOf(pieValue));
                Tooltip.install(data.getNode(), tooltip);
            });
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
            TreeMultimap<String, Integer> sortedMap = amount.getAmountOfSuppliersByFUForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            sortedMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            });
            SupplierDao withoutFU = new SupplierDao();
            int amountOfSuppliersWithoutFU = withoutFU.getAmountOfSuppliersWithoutFUForDashboard();
            if (amountOfSuppliersWithoutFU != 0) {
                pieChartData.add(new PieChart.Data("Não informado", amountOfSuppliersWithoutFU));
            }

            pieChartCommonMethods(suppliersByFUPieChart, Side.BOTTOM);
            suppliersByFUPieChart.setData(pieChartData);

            suppliersByFUPieChart.getData().forEach(data -> {
                int pieValue = (int) (data.getPieValue());
                Tooltip tooltip = new Tooltip(String.valueOf(pieValue));
                Tooltip.install(data.getNode(), tooltip);
            });
        }
    }

    private void buildMostFrequentSuppliersSEPieChart() {
        SupplierDao supplierDao = new SupplierDao();
        List<Supplier> suppliersList = supplierDao.read();

        if (suppliersList == null || suppliersList.isEmpty()) {
            mostFrequentSuppliersSEWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            SupplierDao amount = new SupplierDao();
            TreeMultimap<Integer, String> sortedMap = amount.getMostFrequentSuppliersSEForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            sortedMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getValue(), entry.getKey()));
            });

            pieChartCommonMethods(mostFrequentSuppliersSEPieChart, Side.BOTTOM);
            mostFrequentSuppliersSEPieChart.setData(pieChartData);

            mostFrequentSuppliersSEPieChart.getData().forEach(data -> {
                int pieValue = (int) (data.getPieValue());
                Tooltip tooltip = new Tooltip(String.valueOf(pieValue));
                Tooltip.install(data.getNode(), tooltip);
            });
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

        if (productsList == null || productsList.isEmpty()) {
            mostPopularProductsWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            ProductDao sumAmount = new ProductDao();
            TreeMultimap<Integer, String> sortedMap = sumAmount.getMostPopularProductsForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            sortedMap.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getValue(), entry.getKey()));
            });

            pieChartCommonMethods(mostPopularProductsPieChart, Side.RIGHT);
            mostPopularProductsPieChart.setData(pieChartData);

            mostPopularProductsPieChart.getData().forEach(data -> {
                int pieValue = (int) (data.getPieValue());
                Tooltip tooltip = new Tooltip(String.valueOf(pieValue));
                Tooltip.install(data.getNode(), tooltip);
            });
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

    private void salesDashboard() {
        setSalesLast90Days();
        setSalesThisMonth();
        setAvgTicketThisMonth();
        setLowestSaleThisMonth();
        setBiggestSaleThisMonth();
        buildAnnualSalesHistoryLineChart();
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
        avgTicketThisMonth.setText("R$ " + nf.format(saleDao.getAvgTicketThisMonthForDashboard()));
    }

    private void setLowestSaleThisMonth() {
        SaleDao saleDao = new SaleDao();
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        lowestSaleThisMonth.setText("R$ " + nf.format(saleDao.getLowestSaleThisMonthForDashboard()));
    }

    private void setBiggestSaleThisMonth() {
        SaleDao saleDao = new SaleDao();
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        biggestSaleThisMonth.setText("R$ " + nf.format(saleDao.getBiggestSaleThisMonthForDashboard()));
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
            for (int i = 0; i < 12; i++) {
                series.getData().add(new XYChart.Data(monthsList.get(i), sumOfSalesByMonthList.get(i)));
            }
            annualSalesHistoryLineChart.getData().addAll(series);
            annualSalesHistoryLineChart.setLegendVisible(false);
            
            NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
            for (XYChart.Data<String, BigDecimal> item : series.getData()) {
                Tooltip.install(item.getNode(), new Tooltip("X: " + item.getXValue() + "\nY: R$ " + nf.format(item.getYValue())));
            }
        }
    }

    private void pieChartCommonMethods(PieChart pieChart, Side side) {
        pieChart.setClockwise(true);
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(true);
        pieChart.setLegendSide(side);
    }
}
