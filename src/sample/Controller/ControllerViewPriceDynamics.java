package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Data;
import sample.Main;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerViewPriceDynamics implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> col3,col4;
    @FXML
    private TableColumn<Data, Double> col1, col2;
    @FXML
    private DatePicker startDate,endDate;
    @FXML
    private ChoiceBox type;
    private ObservableList<Data> DataTable = FXCollections.observableArrayList();

    private PreparedStatement preparedStatementSearch() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM price_date p\n" +
                "inner join product pr on p.id_product=pr.id_product\n" +
                "where date>=? and date <=? and groups=?");
        preparedStatement.setDate(1, Date.valueOf((startDate.getValue())));
        preparedStatement.setDate(2, Date.valueOf((endDate.getValue())));
        preparedStatement.setString(3, type.getValue().toString());
        return preparedStatement;
    }

    public void search() {
        table.getItems().clear();
        col1.setCellValueFactory(new PropertyValueFactory<Data, Double>("purchase_price"));
        col2.setCellValueFactory(new PropertyValueFactory<Data, Double>("selling_price"));
        col3.setCellValueFactory(new PropertyValueFactory<Data, String>("date"));
        col4.setCellValueFactory(new PropertyValueFactory<Data, String>("name_product"));

        try(PreparedStatement preparedStatement = preparedStatementSearch();
            ResultSet rs = preparedStatement.executeQuery();) {
            System.out.println(preparedStatement);
            while (rs.next()) {
                DataTable.add(new Data(rs.getDouble(4), rs.getDouble(5 ), rs.getDate(6).toLocalDate(),rs.getString(11)));
            }
            table.setItems(DataTable);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String groupArray []={"колбасные","мясные","полуфабрикаты","сыры","молоко","творожные","говядина", "свинина"};
        type.getItems().addAll(groupArray);
    }
}
