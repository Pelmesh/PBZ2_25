package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Data;
import sample.Main;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerPrice implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> col3,col2, col6,col7,col8;
    @FXML
    private TableColumn<Data, Integer> col1;
    @FXML
    private TableColumn<Data, Double> col4, col5;
    @FXML
    private TextField id, purchase_price, selling_price,name_employee;
    @FXML
    private ChoiceBox factory, product,position;
    @FXML
    private DatePicker date;
    private ObservableList<Data> DataTable = FXCollections.observableArrayList();


    public void createTable() {
        String positionArray []={"заместитель","деректор"};
        table.getItems().clear();
        factory.getItems().clear();
        product.getItems().clear();
        position.getItems().clear();

        position.getItems().addAll(positionArray);

        col1.setCellValueFactory(new PropertyValueFactory<Data, Integer>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<Data, String>("name"));
        col3.setCellValueFactory(new PropertyValueFactory<Data, String>("name_product"));
        col4.setCellValueFactory(new PropertyValueFactory<Data, Double>("purchase_price"));
        col5.setCellValueFactory(new PropertyValueFactory<Data, Double>("selling_price"));
        col6.setCellValueFactory(new PropertyValueFactory<Data, String>("date"));
        col7.setCellValueFactory(new PropertyValueFactory<Data, String>("name_employee"));
        col8.setCellValueFactory(new PropertyValueFactory<Data, String>("position_employee"));

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM price_date");
             PreparedStatement preparedStatementFactory = conn.prepareStatement("SELECT id_factory FROM factory");
             PreparedStatement preparedStatementProduct = conn.prepareStatement("SELECT id_product FROM product");
             ResultSet rs = preparedStatement.executeQuery();
             ResultSet rsFactory = preparedStatementFactory.executeQuery();
             ResultSet rsProduct = preparedStatementProduct.executeQuery();) {
            while (rs.next()) {
                DataTable.add(new Data(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
                        rs.getDouble(5), rs.getDate(6).toLocalDate(), rs.getString(7), rs.getString(8)));
            }
            table.setItems(DataTable);
            while (rsFactory.next()) {
                factory.getItems().addAll(rsFactory.getString(1));
            }
            while (rsProduct.next()) {
                product.getItems().addAll(rsProduct.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementSearch() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM price_date WHERE id_price_date=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void search() {
        try(PreparedStatement preparedStatement = preparedStatementSearch();
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                factory.setValue(rs.getString(2));
                product.setValue(rs.getString(3));
                purchase_price.setText(rs.getString(4));
                selling_price.setText(rs.getString(5));
                date.setValue(rs.getDate(6).toLocalDate());
                name_employee.setText(rs.getString(7));
                position.setValue(rs.getString(8));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementCount() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(id_price_date) FROM price_date WHERE id_price_date=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSave() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO price_date(id_factory,id_product,purchase_price,selling_price,date,position_employee," +
                "name_employee) " +
                "VALUES (?,?,?,?,?,?,?)");
        preparedStatement.setInt(1, Integer.parseInt(factory.getValue().toString()));
        preparedStatement.setInt(2,Integer.parseInt(product.getValue().toString()));
        preparedStatement.setDouble(3, Double.parseDouble(purchase_price.getText()));
        preparedStatement.setDouble(4, Double.parseDouble(selling_price.getText()));
        preparedStatement.setDate(5, Date.valueOf(date.getValue()));
        preparedStatement.setString(6, name_employee.getText());
        preparedStatement.setString(7, position.getValue().toString());
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdate() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE price_date SET id_factory=?,id_product=?,purchase_price=?,selling_price=?,date=?,name_employee=?,position_employee=? WHERE id_price_date=?");
        preparedStatement.setInt(1, Integer.parseInt(factory.getValue().toString()));
        preparedStatement.setInt(2,Integer.parseInt(product.getValue().toString()));
        preparedStatement.setDouble(3, Double.parseDouble(purchase_price.getText()));
        preparedStatement.setDouble(4, Double.parseDouble(selling_price.getText()));
        preparedStatement.setDate(5, Date.valueOf(date.getValue()));;
        preparedStatement.setInt(6, Integer.parseInt(id.getText()));
        preparedStatement.setString(7, name_employee.getText());
        preparedStatement.setString(8, position.getValue().toString());
        return preparedStatement;
    }

    public void save(ActionEvent actionEvent) throws SQLException {
        int count = 0;
        if (!id.getText().equals("")) {
            try (PreparedStatement preparedStatement = preparedStatementCount();
                 ResultSet rs = preparedStatement.executeQuery();) {
                while (rs.next()) {
                    count = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1) {
            try (PreparedStatement preparedStatement = preparedStatementUpdate();) {
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (count == 0) {
            try (PreparedStatement preparedStatement = preparedStatementSave();) {
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        createTable();
        id.clear();
    }

    private PreparedStatement preparedStatementDelete() throws SQLException{
        PreparedStatement   preparedStatement = conn.prepareStatement("DELETE FROM price_date CASCADE WHERE id_price_date=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void delete() {
        try(PreparedStatement preparedStatement = preparedStatementDelete();) {
            preparedStatement.executeUpdate();
            createTable();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTable();
    }
}
