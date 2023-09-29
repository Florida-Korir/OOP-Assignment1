//importing libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

//design sign up panel

public class LoginGUI extends JFrame implements ActionListener {
    private JPanel signUpPanel;
    private JTextField signUpUsernameField;
    private JPasswordField signUpPasswordField;
    private JButton signUpButton;
    private JButton loginButton;
    private Map<String, String> registeredUsers; // Map to store registered usernames and passwords

    // File to store user data
    private static final String USER_DATA_FILE = "userdata.txt";

    // design login panel
    private JPanel loginPanel;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginSubmitButton;

    private int loginAttempts; // Counter for login attempts

    public LoginGUI() {
        setTitle("Login System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200); // Increased the height of the window
        setLocationRelativeTo(null);
        registeredUsers = new HashMap<>(); // Initialize map for registered usernames and passwords

        // Load user data from file if it exists
        loadUserData();

        signUpPanel = new JPanel();
        signUpPanel.setLayout(new GridLayout(4, 2)); // Added one row for the "Log In" button

        JLabel signUpUsernameLabel = new JLabel("Username:");
        signUpUsernameField = new JTextField();
        JLabel signUpPasswordLabel = new JLabel("Password:");
        signUpPasswordField = new JPasswordField();
        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(this); // Add ActionListener for "Sign Up" button

        signUpPanel.add(signUpUsernameLabel);
        signUpPanel.add(signUpUsernameField);
        signUpPanel.add(signUpPasswordLabel);
        signUpPanel.add(signUpPasswordField);
        signUpPanel.add(signUpButton);
        signUpPanel.add(new JLabel()); // Placeholder for spacing
        signUpPanel.add(new JLabel()); // Placeholder for spacing

        loginButton = new JButton("Already have an account: Log in"); // Changed button label
        loginButton.addActionListener(this); // Add ActionListener for "Log In" button
        signUpPanel.add(loginButton); // Added "Log In" button

        loginAttempts = 3; // Initialize login attempts to 3

        // Initialize login panel (hidden initially)
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));

        JLabel loginUsernameLabel = new JLabel("Username:");
        loginUsernameField = new JTextField();
        JLabel loginPasswordLabel = new JLabel("Password:");
        loginPasswordField = new JPasswordField();
        loginSubmitButton = new JButton("Log In");

        loginSubmitButton.addActionListener(this); // Add ActionListener for "Log In" button

        loginPanel.add(loginUsernameLabel);
        loginPanel.add(loginUsernameField);
        loginPanel.add(loginPasswordLabel);
        loginPanel.add(loginPasswordField);
        loginPanel.add(new JLabel()); // Placeholder for spacing
        loginPanel.add(loginSubmitButton);
        loginPanel.add(new JLabel()); // Placeholder for spacing

        add(signUpPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpButton) {
            String newUsername = signUpUsernameField.getText();
            String newPassword = new String(signUpPasswordField.getPassword());

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            } else if (registeredUsers.containsKey(newUsername)) {
                JOptionPane.showMessageDialog(this, "Username already taken. Please choose a different one.");
            } else {
                registeredUsers.put(newUsername, newPassword); // Store the new user's credentials
                saveUserData(); // Save user data to the file
                JOptionPane.showMessageDialog(this, "Sign-up successful! Now, you can log in.");
                clearSignUpFields();
                showLoginPage(); // Direct user to the login page after signing up
            }
        } else if (e.getSource() == loginButton) {
            // Show the login panel
            showLoginPage();
        } else if (e.getSource() == loginSubmitButton) {
            String enteredUsername = loginUsernameField.getText();
            String enteredPassword = new String(loginPasswordField.getPassword());

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                JOptionPane.showMessageDialog(loginPanel, "Please enter both username and password.");
            } else if (!registeredUsers.containsKey(enteredUsername)) {
                JOptionPane.showMessageDialog(loginPanel, "Unregistered user. Please sign up first.");
            } else if (enteredPassword.equals(registeredUsers.get(enteredUsername))) {
                JOptionPane.showMessageDialog(loginPanel, "Thank you, " + enteredUsername + ", you have successfully logged in!");
                loginPanel.setVisible(false);
                signUpPanel.setVisible(true);
                clearFields(loginUsernameField, loginPasswordField);
                loginAttempts = 3; // Reset login attempts
            } else {
                loginAttempts--;
                if (loginAttempts == 0) {
                    JOptionPane.showMessageDialog(loginPanel, "Login attempts exceeded. Returning to blank login page.");
                    loginPanel.setVisible(false);
                    signUpPanel.setVisible(true);
                    clearFields(loginUsernameField, loginPasswordField);
                    loginAttempts = 3; // Reset login attempts
                } else {
                    JOptionPane.showMessageDialog(loginPanel, "Invalid password. You have " + loginAttempts + " attempts remaining.");
                }
            }
        }
    }
   
    //deisnging of showlogin page
    private void showLoginPage() {
        signUpPanel.setVisible(false);

        // Initialize login panel
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));

        JLabel loginUsernameLabel = new JLabel("Username:");
        loginUsernameField = new JTextField();
        JLabel loginPasswordLabel = new JLabel("Password:");
        loginPasswordField = new JPasswordField();
        loginSubmitButton = new JButton("Log In");

        loginSubmitButton.addActionListener(this); // Add ActionListener for "Log In" button

        JButton backButton = new JButton("Back"); // Add a back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPanel.setVisible(false);
                signUpPanel.setVisible(true);
            }
        });

        loginPanel.add(loginUsernameLabel);
        loginPanel.add(loginUsernameField);
        loginPanel.add(loginPasswordLabel);
        loginPanel.add(loginPasswordField);
        loginPanel.add(new JLabel()); // Placeholder for spacing
        loginPanel.add(loginSubmitButton);
        loginPanel.add(backButton); // Add the back button
        loginPanel.add(new JLabel()); // Placeholder for spacing

        add(loginPanel);
        revalidate();
        repaint();
    }
//clearup fields in sign up panel
    private void clearSignUpFields() {
        signUpUsernameField.setText("");
        signUpPasswordField.setText("");
    }

    private void clearFields(JTextField usernameField, JPasswordField passwordField) {
        usernameField.setText("");
        passwordField.setText("");
    }

    private void saveUserData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_DATA_FILE))) {
            for (Map.Entry<String, String> entry : registeredUsers.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   //read user data
    private void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    registeredUsers.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI();
        });
    }
}
