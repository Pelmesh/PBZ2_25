package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Data;
import sample.Main;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerViewAveragePrice implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, Integer> col3;
    @FXML
    private TableColumn<Data, Double> col1, col2;
    @FXML
    private DatePicker date;
    @FXML
    private ChoiceBox type;
    private ObservableList<Data> DataTable = FXCollections.observableArrayList();

    private PreparedStatement preparedStatementSearch() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("select AVG(purchase_price),AVG(selling_price),r.id_region from price_date p\n" +
                "inner join product pr on p.id_product=pr.id_product\n" +
                "inner join factory f on p.id_factory = f.id_factory\n" +
                "inner join region r on f.id_region = r.id_region\n" +
                "WHERE p.date=? and pr.type=? GROUP BY r.id_region;");
        preparedStatement.setDate(1, Date.valueOf(date.getValue()));
        preparedStatement.setString(2, type.getValue().toString());
        return preparedStatement;
    }

    public void search() {
        table.getItems().clear();
        col1.setCellValueFactory(new PropertyValueFactory<Data, Double>("purchase_price"));
        col2.setCellValueFactory(new PropertyValueFactory<Data, Double>("selling_price"));
        col3.setCellValueFactory(new PropertyValueFactory<Data, Integer>("id"));


        try(PreparedStatement preparedStatement = preparedStatementSearch();
            ResultSet rs = preparedStatement.executeQuery();) {
            System.out.println(preparedStatement);
            while (rs.next()) {
                DataTable.add(new Data(rs.getDouble(1), rs.getDouble(2),rs.getInt(3)));
            }
            table.setItems(DataTable);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String groupArray []={"мясная","молочная"};
        type.getItems().addAll(groupArray);
    }
}
