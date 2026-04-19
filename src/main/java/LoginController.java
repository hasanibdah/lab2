import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controller for the login screen.
 * Handles loading users, validating login, and switching scenes.
 */
public class LoginController {

    // UI elements connected from FXML
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    // Stores only valid users loaded from file
    private ArrayList<User> users = new ArrayList<>();

    /**
     * Constructor - loads users when controller is created
     */
    public LoginController() {
        loadUsers();
    }

    /**
     * Reads users from users.txt and filters only valid ones.
     * Invalid users are ignored using the User class validation.
     */
    private void loadUsers() {
        try {
            InputStream is = getClass().getResourceAsStream("/users.txt");
            Scanner sc = new Scanner(is);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split("\\s+");

                // Expecting: username password
                if (parts.length == 2) {
                    try {
                        users.add(new User(parts[0], parts[1]));
                    } catch (Exception e) {
                        // Invalid user → ignored
                    }
                }
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading users");
        }
    }

    /**
     * Handles login button click.
     * Checks if entered credentials match a valid user.
     * If success → switch to welcome screen.
     * If fail → show error message.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        errorLabel.setText("");

        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check credentials against valid users
        for (User u : users) {
            if (u.getUsername().equals(username) &&
                    u.getPassword().equals(password)) {

                try {
                    // Load next screen (welcome.fxml)
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcome.fxml"));
                    Scene scene = new Scene(loader.load());

                    // Get current window and replace scene
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return; // Stop after successful login
            }
        }

        // If no match found
        errorLabel.setText("user or password do not match");
    }
}