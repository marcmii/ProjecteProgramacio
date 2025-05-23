package projecte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega l'FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/projecte/company_view.fxml"));
        primaryStage.setTitle("Gestor d'Empreses");
        primaryStage.setScene(new Scene(loader.load(), 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Comprovació de connexió a la base de dades SQLite abans de llançar JavaFX
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connexió a SQLite establerta correctament!");
            }
        } catch (SQLException e) {
            System.err.println("Error en connectar amb la base de dades SQLite:");
            e.printStackTrace();
        }

        // Llança l'aplicació JavaFX
        launch(args);
    }
}
