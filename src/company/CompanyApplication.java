package company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CompanyApplication extends Application {

	static Connection connection;
	ListView<String> employeeListView;
	ComboBox<ComboItem> roleComboBox;
	ComboBox<ComboItem> departmentComboBox;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        employeeListView = new ListView<>();

        // ComboBox de departaments
        departmentComboBox = new ComboBox<>();
        departmentComboBox.getItems().add(new ComboItem(null, "Tots els departaments"));
        departmentComboBox.getSelectionModel().selectFirst();

        try {
            ResultSet rs = getDepartments();
            while (rs.next()) {
                departmentComboBox.getItems().add(new ComboItem(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            showAlert("No s'han pogut carregar els departaments.");
        }

        departmentComboBox.setOnAction(__ -> {
        	reloadEmployeesList();
        });

        // ComboBox de rols d'empleat
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().add(new ComboItem(null, "Tots els rols"));
        roleComboBox.getSelectionModel().selectFirst();

        try {
            ResultSet rs = getRoles();
            while (rs.next()) {
                roleComboBox.getItems().add(new ComboItem(rs.getInt("id"), rs.getString("title")));
            }
        } catch (SQLException e) {
            showAlert("No s'han pogut carregar els rols.");
        }

        roleComboBox.setOnAction(__ -> {
        	reloadEmployeesList();
        });

        HBox controls = new HBox(10, departmentComboBox, roleComboBox);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(5, 0, 5, 0));

        root.getChildren().addAll(new Label("Empreses;"), employeeListView, controls);

        Scene scene = new Scene(root, 640, 480);
        primaryStage.setTitle("Simple Company Application");
        primaryStage.setScene(scene);
        primaryStage.show();

        reloadEmployeesList();
    }

    private void reloadEmployeesList() {
    	employeeListView.getItems().clear();
        ComboItem selectedDept = departmentComboBox.getValue();
        ComboItem selectedRole = roleComboBox.getValue();

        try {
            ResultSet rs = getEmployees(selectedDept.getId(), selectedRole.getId());
            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                String info = " | "+rs.getString("salary")+" | "+rs.getString("role")+" | " + rs.getString("department");
                employeeListView.getItems().add(fullName + info);
            }
        } catch (SQLException e) {
        	showAlert("No s'ha pogut carregar el llistat d'empleats.");
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Avís");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static ResultSet getEmployees(Integer departmentId, Integer roleId) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(5);

        String query =
            "SELECT e.first_name, e.last_name, e.salary, r.title AS role, d.name AS department " +
            "FROM employees e " +
            "JOIN roles r ON e.role_id = r.id " +
            "JOIN departments d ON e.department_id = d.id";

        if (departmentId != null && roleId != null) {
            query += " WHERE e.department_id = " + departmentId + " AND e.role_id = " + roleId;
        } else if (departmentId != null) {
            query += " WHERE e.department_id = " + departmentId;
        } else if (roleId != null) {
            query += " WHERE e.role_id = " + roleId;
        }

        return statement.executeQuery(query);
    }

    public static ResultSet getDepartments() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(5);
        return statement.executeQuery("SELECT id, name FROM departments");
    }

    public static ResultSet getRoles() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(5);
        return statement.executeQuery("SELECT id, title FROM roles");
    }

	public static void main(String[] args) {

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		} catch (SQLException e) {
			System.out.println("No s'ha pogut establir la connexió amb la base de dades.");
			e.printStackTrace(System.err);
			System.exit(1);
		}

        launch(args);
	}
}
