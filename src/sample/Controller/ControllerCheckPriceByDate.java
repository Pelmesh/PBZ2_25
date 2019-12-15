package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Data;
import sample.Main;
import java.sql.*;


public class ControllerCheckPriceByDate {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> col3,col4,col5;
    @FXML
    private TableColumn<Data, Double> col1, col2;
    @FXML
    private DatePicker date;
    private ObservableList<Data> DataTable = FXCollections.observableArrayList();

    private PreparedStatement preparedStatementSearch() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("select * from price_date p\n" +
                "inner join product pr on p.id_product=pr.id_product\n" +
                "inner join factory f on p.id_factory = f.id_factory WHERE date=?");
        preparedStatement.setDate(1, Date.valueOf((date.getValue())));
        return preparedStatement;
    }

    public void search() {
        table.getItems().clear();
        col1.setCellValueFactory(new PropertyValueFactory<Data, Double>("purchase_price"));
        col2.setCellValueFactory(new PropertyValueFactory<Data, Double>("selling_price"));
        col3.setCellValueFactory(new PropertyValueFactory<Data, String>("name"));
        col4.setCellValueFactory(new PropertyValueFactory<Data, String>("name_product"));
        col5.setCellValueFactory(new PropertyValueFactory<Data, String>("date"));

        try(PreparedStatement preparedStatement = preparedStatementSearch();
            ResultSet rs = preparedStatement.executeQuery();) {
            System.out.println(preparedStatement);
            while (rs.next()) {
                DataTable.add(new Data(rs.getDouble(4), rs.getDouble(5 ), rs.getString(18),rs.getString(11),rs.getDate(6).toLocalDate()));
            }
            table.setItems(DataTable);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
