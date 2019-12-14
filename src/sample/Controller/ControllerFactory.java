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

public class ControllerFactory implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> col3, col4, col5, col6, col7, col8;
    @FXML
    private TableColumn<Data, Integer> col1, col2, col9;
    @FXML
    private TextField id, name, code, address, telephone, name_employee;
    @FXML
    private ChoiceBox type, position, id_region;
    private ObservableList<Data> DataTable = FXCollections.observableArrayList();


    public void createTable() {
        String typeArray []={"Мясной","Молочный"};
        String positionArray []={"заместитель","деректор"};

        table.getItems().clear();
        type.getItems().clear();
        position.getItems().clear();
        id_region.getItems().clear();

        type.getItems().addAll(typeArray);
        position.getItems().addAll(positionArray);


        col1.setCellValueFactory(new PropertyValueFactory<Data, Integer>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<Data, Integer>("code"));
        col3.setCellValueFactory(new PropertyValueFactory<Data, String>("type"));
        col4.setCellValueFactory(new PropertyValueFactory<Data, String>("name"));
        col5.setCellValueFactory(new PropertyValueFactory<Data, String>("address"));
        col6.setCellValueFactory(new PropertyValueFactory<Data, String>("telephone"));
        col7.setCellValueFactory(new PropertyValueFactory<Data, String>("name_employee"));
        col8.setCellValueFactory(new PropertyValueFactory<Data, String>("position_employee"));
        col9.setCellValueFactory(new PropertyValueFactory<Data, Integer>("id_region"));

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM factory");
             PreparedStatement preparedStatementRegionId = conn.prepareStatement("SELECT id_region FROM region");
             ResultSet rsRegionId = preparedStatementRegionId.executeQuery();
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                DataTable.add(new Data(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),rs.getInt(9)));
            }
            table.setItems(DataTable);
            while (rsRegionId.next()) {
                  id_region.getItems().addAll(rsRegionId.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private PreparedStatement preparedStatementSearch() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM factory WHERE id_factory=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void search() {
        try(PreparedStatement preparedStatement = preparedStatementSearch();
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                code.setText(rs.getString(2));
                type.setValue(rs.getString(3));
                name.setText(rs.getString(4));
                address.setText(rs.getString(5));
                telephone.setText(rs.getString(6));
                name_employee.setText(rs.getString(7));
                position.setValue(rs.getString(8));
                id_region.setValue(rs.getString(9));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementCount() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(id_region) FROM region WHERE id_region=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSave() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO factory(code,type,name,address,telephone,name_employee,position_employee,id_region) " +
                "VALUES (?,?,?,?,?,?,?,?)");
        preparedStatement.setInt(1, Integer.parseInt(code.getText()));
        preparedStatement.setString(2, type.getValue().toString());
        preparedStatement.setString(3, name.getText());
        preparedStatement.setString(4, address.getText());
        preparedStatement.setString(5, telephone.getText());
        preparedStatement.setString(6, name_employee.getText());
        preparedStatement.setString(7, position.getValue().toString());
        preparedStatement.setInt(8, Integer.parseInt(id_region.getValue().toString()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdate() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE factory SET code=?,type=?,name=?,address=?,telephone=?,name_employee=?,position_employee=?,id_region=? WHERE id_factory=?");
        preparedStatement.setInt(1, Integer.parseInt(code.getText()));
        preparedStatement.setString(2, type.getValue().toString());
        preparedStatement.setString(3, name.getText());
        preparedStatement.setString(4, address.getText());
        preparedStatement.setString(5, telephone.getText());
        preparedStatement.setString(6, name_employee.getText());
        preparedStatement.setString(7, position.getValue().toString());
        preparedStatement.setInt(8, Integer.parseInt(id_region.getValue().toString()));
        preparedStatement.setInt(9, Integer.parseInt(id.getText()));
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
        PreparedStatement   preparedStatement = conn.prepareStatement("DELETE FROM factory CASCADE WHERE id_factory=?");
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
