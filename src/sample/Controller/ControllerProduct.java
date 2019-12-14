package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Data;
import sample.Main;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerProduct implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String>  col3, col4, col5, col6;
    @FXML
    private TableColumn<Data, Integer> col1,col2;
    @FXML
    private TextField id, name, code;
    @FXML
    private ChoiceBox type, sort, group;
    private ObservableList<Data> DataTable = FXCollections.observableArrayList();


    public void createTable() {
        String typeArray []={"молочная","мясная"};
        String sortArray []={"1 сорт","2 сорт","3 сорт"};
        String groupArray []={"колбасные","мясные","полуфабрикаты","сыры","молоко","творожные"};

        table.getItems().clear();
        type.getItems().clear();
        sort.getItems().clear();
        group.getItems().clear();

        type.getItems().addAll(typeArray);
        sort.getItems().addAll(sortArray);
        group.getItems().addAll(groupArray);


        col1.setCellValueFactory(new PropertyValueFactory<Data, Integer>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<Data, Integer>("code"));
        col3.setCellValueFactory(new PropertyValueFactory<Data, String>("name"));
        col4.setCellValueFactory(new PropertyValueFactory<Data, String>("sort"));
        col5.setCellValueFactory(new PropertyValueFactory<Data, String>("group"));
        col6.setCellValueFactory(new PropertyValueFactory<Data, String>("type"));

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM product");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                DataTable.add(new Data(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6)));
            }
            table.setItems(DataTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private PreparedStatement preparedStatementSearch() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM product WHERE id_product=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void search() {
        try(PreparedStatement preparedStatement = preparedStatementSearch();
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                code.setText(rs.getString(2));
                name.setText(rs.getString(3));
                sort.setValue(rs.getString(4));
                group.setValue(rs.getString(5));
                type.setValue(rs.getString(6));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementCount() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(id_product) FROM product WHERE id_product=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSave() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO product(code,name,sort,groups,type) " +
                "VALUES (?,?,?,?,?)");
        preparedStatement.setInt(1, Integer.parseInt(code.getText()));
        preparedStatement.setString(2, name.getText());
        preparedStatement.setString(3, sort.getValue().toString());
        preparedStatement.setString(4, group.getValue().toString());
        preparedStatement.setString(5, type.getValue().toString());
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdate() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE product SET code=?,name=?,sort=?,groups=?,type=? WHERE id_product=?");
        preparedStatement.setInt(1, Integer.parseInt(code.getText()));
        preparedStatement.setString(2, name.getText());
        preparedStatement.setString(3, sort.getValue().toString());
        preparedStatement.setString(4, group.getValue().toString());
        preparedStatement.setString(5, type.getValue().toString());
        preparedStatement.setInt(6, Integer.parseInt(id.getText()));
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
        PreparedStatement   preparedStatement = conn.prepareStatement("DELETE FROM product CASCADE WHERE id_product=?");
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
