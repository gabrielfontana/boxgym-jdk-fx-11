package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.model.Customer;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.apache.commons.collections4.MultiValuedMap;

public class HomeController implements Initializable {

    // Clientes
    @FXML
    private Label totalCustomers;

    @FXML
    private Label customersLast90Days;

    @FXML
    private BarChart<String, Integer> ageGroupBarChart;

    @FXML
    private CategoryAxis ageGroupBarChartXAxis;

    @FXML
    private NumberAxis ageGroupBarChartYAxis;

    @FXML
    private Label ageGroupEmptyWarningLabel;

    @FXML
    private PieChart customersProfilePieChart;

    @FXML
    private Label customersProfileEmptyWarningLabel;

    @FXML
    private PieChart customersByCityPieChart;

    @FXML
    private Label customersByCityEmptyWarningLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customersDashboard();
    }

    private void customersDashboard() {
        setTotalCustomers();
        setCustomersLast90Days();
        buildAgeGroupBarChart();
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
        customersLast90Days.setText(String.valueOf(customerDao.getAmountOfCustomersLast90Days()));
    }

    private void buildAgeGroupBarChart() {
        CustomerDao customerDao = new CustomerDao();
        List<Customer> customersList = customerDao.read();

        if (customersList == null || customersList.isEmpty()) {
            ageGroupEmptyWarningLabel.setText("Não há registros para gerar o gráfico");
            ageGroupBarChart.setVisible(false);
        } else {
            ageGroupBarChartXAxis.setLabel("Idade");
            ageGroupBarChartYAxis.setLabel("Quantidade");

            XYChart.Series<String, Integer> series1 = new XYChart.Series();
            series1.setName("Homens");
            series1.getData().add(new XYChart.Data("Até 20", 5));
            series1.getData().add(new XYChart.Data("21 a 30", 8));
            series1.getData().add(new XYChart.Data("31 a 40", 40));
            series1.getData().add(new XYChart.Data("41 a 50", 2));
            series1.getData().add(new XYChart.Data("Acima de 50", 1));

            XYChart.Series<String, Integer> series2 = new XYChart.Series();
            series2.setName("Mulheres");
            series2.getData().add(new XYChart.Data("Até 20", 3));
            series2.getData().add(new XYChart.Data("21 a 30", 9));
            series2.getData().add(new XYChart.Data("31 a 40", 1));
            series2.getData().add(new XYChart.Data("41 a 50", 2));
            series2.getData().add(new XYChart.Data("Acima de 50", 0));

            ageGroupBarChart.getData().addAll(series1, series2);

            for (XYChart.Series<String, Integer> series : ageGroupBarChart.getData()) {
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
            customersProfileEmptyWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            CustomerDao male = new CustomerDao();
            CustomerDao female = new CustomerDao();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Mulheres", female.getAmountOfEachSexForDashboard("Feminino")),
                    new PieChart.Data("Homens", male.getAmountOfEachSexForDashboard("Masculino"))
            );
            
            pieChartCommonMethods(customersProfilePieChart);
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
            customersByCityEmptyWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            CustomerDao amount = new CustomerDao();
            MultiValuedMap<Integer, String> map = amount.getAmountOfCustomersByCity();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            map.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getValue(), entry.getKey()));
            });
            CustomerDao withoutCity = new CustomerDao();
            pieChartData.add(new PieChart.Data("Não informado", withoutCity.getAmountOfCustomersWithoutCity()));

            pieChartCommonMethods(customersByCityPieChart);
            customersByCityPieChart.setData(pieChartData);
            
            customersByCityPieChart.getData().forEach(data -> {
                int pieValue = (int) (data.getPieValue());
                Tooltip tooltip = new Tooltip(String.valueOf(pieValue));
                Tooltip.install(data.getNode(), tooltip);
            });
        }
    }

    private void pieChartCommonMethods(PieChart pieChart) {
        pieChart.setClockwise(true);
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(true);
        pieChart.setLegendSide(Side.RIGHT);
    }
}
