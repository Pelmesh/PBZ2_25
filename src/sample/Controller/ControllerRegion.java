package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class ControllerRegion implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> col2, col3;
    @FXML
    private TableColumn<Data, Integer> col1;
    @FXML
    private TextField id, city, area;
    private ObservableList<Data> DataTable = FXCollections.observableArrayList();

    private void createTable() {
        table.getItems().clear();

        col1.setCellValueFactory(new PropertyValueFactory<Data, Integer>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<Data, String>("city"));
        col3.setCellValueFactory(new PropertyValueFactory<Data, String>("area"));

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM region");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                DataTable.add(new Data(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            table.setItems(DataTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private PreparedStatement preparedStatementSearch() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM region WHERE id_region=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void search() {
        try(PreparedStatement preparedStatement = preparedStatementSearch();
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                city.setText(rs.getString(2));
                area.setText(rs.getString(3));
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
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO region(city,area) VALUES (?,?)");
        preparedStatement.setString(1, city.getText());
        preparedStatement.setString(2, area.getText());
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdate() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE region SET city=?,area=? WHERE id_region=?");
        preparedStatement.setString(1, city.getText());
        preparedStatement.setString(2, area.getText());
        preparedStatement.setInt(3, Integer.parseInt(id.getText()));
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
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        createTable();
        id.clear();
    }

    private PreparedStatement preparedStatementDelete() throws SQLException{
        PreparedStatement   preparedStatement = conn.prepareStatement("DELETE FROM region CASCADE WHERE id_region=?");
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
