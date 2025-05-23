package projecte;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;



import java.sql.*;

public class CompanyController {

    @FXML private TableView<Company> companyTable;
    @FXML private TableColumn<Company, String> colId, colName, colDescription, colDegreeId;
    @FXML private TextField txtId, txtName, txtDescription, txtDegreeId;
    @FXML private ComboBox<String> degreeFilterComboBox;

    private ObservableList<Company> companyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colDescription.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        colDegreeId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDegreeId()));
        
        degreeFilterComboBox.getItems().add("Tots");
        degreeFilterComboBox.getItems().add("ASIX");
        degreeFilterComboBox.getItems().add("DAM");
        degreeFilterComboBox.getItems().add("DAW");
        degreeFilterComboBox.getSelectionModel().selectFirst();

        degreeFilterComboBox.setOnAction(event -> {
            loadCompanies(); // recarrega aplicant el filtre
        });

        
        
        loadCompanies();
    }

    private void loadCompanies() {
        companyList.clear();
        String selectedDegree = degreeFilterComboBox.getValue();

        String query = "SELECT * FROM Companies";
        if (!"Tots".equals(selectedDegree)) {
            query += " WHERE Degree_id = '" + selectedDegree + "'";
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                companyList.add(new Company(
                        rs.getString("Id"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getString("Degree_id")
                ));
            }

            companyTable.setItems(companyList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addCompany() {
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Companies VALUES (?, ?, ?, '', ?)")) {

            ps.setString(1, txtId.getText());
            ps.setString(2, txtName.getText());
            ps.setString(3, txtDescription.getText());
            ps.setString(4, txtDegreeId.getText());

            ps.executeUpdate();
            loadCompanies(); // Refresh table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}