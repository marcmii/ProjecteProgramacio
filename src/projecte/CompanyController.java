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

        degreeFilterComboBox.getItems().setAll("Tots", "DAM", "DAW", "ASIX");
        degreeFilterComboBox.getSelectionModel().selectFirst();

        degreeFilterComboBox.setOnAction(event -> loadCompanies());

        companyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(newSelection.getId());
                txtName.setText(newSelection.getName());
                txtDescription.setText(newSelection.getDescription());
                txtDegreeId.setText(newSelection.getDegreeId());
            }
        });

        loadCompanies();
    }



    private void loadCompanies() {
        companyList.clear();
        String selectedDegree = degreeFilterComboBox.getValue();

        String query = "SELECT * FROM Companies";
        boolean hasFilter = !"Tots".equalsIgnoreCase(selectedDegree);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     hasFilter ? query + " WHERE Degree_id = ?" : query)) {

            if (hasFilter) {
                String degreeId = "";
                if ("DAM".equals(selectedDegree)) {
                    degreeId = "001";
                } else if ("DAW".equals(selectedDegree)) {
                    degreeId = "002";
                } else if ("ASIX".equals(selectedDegree)) {
                    degreeId = "003";
                }
                ps.setString(1, degreeId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    companyList.add(new Company(
                            rs.getString("Id"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getString("Degree_id")
                    ));
                }
            }

            companyTable.setItems(companyList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void addCompany() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();
        String degreeId = txtDegreeId.getText().trim();

        // Comprovació de camps buits
        if (id.isEmpty() || name.isEmpty() || description.isEmpty() || degreeId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Validació");
            alert.setHeaderText("Tots els camps són obligatoris.");
            alert.setContentText("Si us plau, omple tots els camps abans d'afegir una empresa.");
            alert.showAndWait();
            return; // No continua si hi ha errors
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Companies VALUES (?, ?, ?, '', ?)")) {

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.setString(4, degreeId);

            ps.executeUpdate();
            loadCompanies(); // Actualitza la taula
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Base de Dades");
            alert.setHeaderText("No s'ha pogut afegir l'empresa.");
            alert.setContentText("Revisa la informació i torna-ho a provar.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void deleteCompany() {
        Company selectedCompany = companyTable.getSelectionModel().getSelectedItem();

        if (selectedCompany == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cap selecció");
            alert.setHeaderText("No s'ha seleccionat cap empresa");
            alert.setContentText("Selecciona una empresa de la taula per eliminar-la.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmació d'eliminació");
        confirmation.setHeaderText("Estàs segur que vols eliminar aquesta empresa?");
        confirmation.setContentText("ID: " + selectedCompany.getId());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM Companies WHERE Id = ?")) {

                    ps.setString(1, selectedCompany.getId());
                    ps.executeUpdate();
                    loadCompanies();

                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de Base de Dades");
                    alert.setHeaderText("No s'ha pogut eliminar l'empresa.");
                    alert.setContentText("Torna-ho a intentar més tard.");
                    alert.showAndWait();
                }
            }
        });
    }
    
    @FXML
    private void updateCompany() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();
        String degreeId = txtDegreeId.getText().trim();

        if (id.isEmpty() || name.isEmpty() || description.isEmpty() || degreeId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Validació");
            alert.setHeaderText("Tots els camps són obligatoris.");
            alert.setContentText("Si us plau, omple tots els camps abans de modificar una empresa.");
            alert.showAndWait();
            return;
        }

        String sql = "UPDATE Companies SET Name = ?, Description = ?, Degree_id = ? WHERE Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, degreeId);
            ps.setString(4, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Modificació exitosa");
                alert.setHeaderText(null);
                alert.setContentText("Empresa modificada correctament.");
                alert.showAndWait();

                loadCompanies();  // recarrega la taula
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Modificació fallida");
                alert.setHeaderText(null);
                alert.setContentText("No s'ha trobat l'empresa per modificar.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Base de Dades");
            alert.setHeaderText("No s'ha pogut modificar l'empresa.");
            alert.setContentText("Revisa la informació i torna-ho a provar.");
            alert.showAndWait();
        }
    }



}