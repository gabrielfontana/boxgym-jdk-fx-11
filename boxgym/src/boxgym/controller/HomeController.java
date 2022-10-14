package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.model.Customer;
import java.net.URL;
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
    private BarChart<String, Integer> ageRangeBarChart;

    @FXML
    private CategoryAxis ageRangeBarChartXAxis;

    @FXML
    private NumberAxis ageRangeBarChartYAxis;

    @FXML
    private Label ageRangeEmptyWarningLabel;

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
            ageRangeEmptyWarningLabel.setText("Não há registros para gerar o gráfico");
            ageRangeBarChart.setVisible(false);
        } else {
            ageRangeBarChartXAxis.setLabel("Idade");
            ageRangeBarChartYAxis.setLabel("Quantidade");
            
            CustomerDao maleAgeRange = new CustomerDao();
            List<Integer> maleList = maleAgeRange.getMaleAgeRangeForDashboard();
            
            XYChart.Series<String, Integer> series1 = new XYChart.Series();
            series1.setName("Masculina");
            series1.getData().add(new XYChart.Data("Até 20", maleList.get(0)));
            series1.getData().add(new XYChart.Data("21 a 30", maleList.get(1)));
            series1.getData().add(new XYChart.Data("31 a 40", maleList.get(2)));
            series1.getData().add(new XYChart.Data("41 a 50", maleList.get(3)));
            series1.getData().add(new XYChart.Data("Acima de 50", maleList.get(4)));

            CustomerDao femaleAgeRange = new CustomerDao();
            List<Integer> femaleList = femaleAgeRange.getFemaleAgeRangeForDashboard();
            
            XYChart.Series<String, Integer> series2 = new XYChart.Series();
            series2.setName("Feminina");
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
            customersProfileEmptyWarningLabel.setText("Não há registros para gerar o gráfico");
        } else {
            CustomerDao male = new CustomerDao();
            CustomerDao female = new CustomerDao();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Masculino", male.getAmountOfEachSexForDashboard("Masculino")),
                    new PieChart.Data("Feminino", female.getAmountOfEachSexForDashboard("Feminino"))
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
            MultiValuedMap<Integer, String> map = amount.getAmountOfCustomersByCityForDashboard();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            map.entries().forEach(entry -> {
                pieChartData.add(new PieChart.Data(entry.getValue(), entry.getKey()));
            });
            CustomerDao withoutCity = new CustomerDao();
            pieChartData.add(new PieChart.Data("Não informado", withoutCity.getAmountOfCustomersWithoutCityForDashboard()));

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
