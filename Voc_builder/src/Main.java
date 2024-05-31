import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Main {
    private static JFrame frame;
    private static boolean isAdmin;
    private static String currentUsername;

    private static final Color SAILOR_BLUE = new Color(0, 32, 96);
    private static final Color MINT = new Color(170, 255, 195);

    public static void main(String[] args) {
        UIManager.put("Button.background", SAILOR_BLUE);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Panel.background", MINT);
        UIManager.put("OptionPane.background", MINT);
        UIManager.put("OptionPane.messageForeground", SAILOR_BLUE);
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));

        showInitialDialog();
    }

    private static void showInitialDialog() {
        String[] options = {"Admin", "User"};
        int choice = JOptionPane.showOptionDialog(null,
                "Are you logging in as an Admin or a User?",
                "Login Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        isAdmin = (choice == 0);
        showLoginPage();
    }

    private static void showLoginPage() {
        frame = new JFrame("Login or Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        JLabel welcomeLabel = new JLabel("Welcome to Vocabulary Builder App");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(welcomeLabel, gbc);

        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        loginButton.addActionListener(e -> showLoginDialog());

        registerButton.addActionListener(e -> showRegisterDialog());

        frame.setSize(400, 200);
        frame.setVisible(true);
    }

    private static void showLoginDialog() {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticate(username, password)) {
                currentUsername = username;
                showHomePage();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        }
    }

    private static boolean authenticate(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/voc_builder?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "SELECT * FROM identity WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
            return false;
        }
    }

    private static void showRegisterDialog() {
        JTextField usernameField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JPasswordField confirmPasswordField = new JPasswordField(10);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match.");
                return;
            }

            if (register(username, email, password)) {
                JOptionPane.showMessageDialog(null, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(null, "Registration failed.");
            }
        }
    }

    private static boolean register(String username, String email, String password) {
        String url = "jdbc:mysql://localhost:3306/voc_builder?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "INSERT INTO identity (username, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
            return false;
        }
    }

    private static void showHomePage() {
        frame.dispose();
        frame = new JFrame("Vocabulary Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUsername);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(welcomeLabel, gbc);

        JButton searchButton = new JButton("Search");
        panel.add(searchButton, gbc);

        JButton showAllButton = new JButton("Show All Words");
        panel.add(showAllButton, gbc);

        if (isAdmin) {
            JButton insertButton = new JButton("Insert Word");
            panel.add(insertButton, gbc);

            JButton deleteButton = new JButton("Delete Word");
            panel.add(deleteButton, gbc);

            JButton editButton = new JButton("Edit Word");
            panel.add(editButton, gbc);

            insertButton.addActionListener(e -> showInsertDialog());

            deleteButton.addActionListener(e -> showDeleteDialog());

            editButton.addActionListener(e -> showEditDialog());
        }

        searchButton.addActionListener(e -> showSearchDialog());

        showAllButton.addActionListener(e -> showAllWords());

        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private static void showSearchDialog() {
        String word = JOptionPane.showInputDialog(null, "Enter Word to Search:");
        if (word != null && !word.trim().isEmpty()) {
            searchWord(word);
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid word.");
        }
    }

    private static void searchWord(String word) {
        String url = "jdbc:mysql://localhost:3306/voc_builder?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "SELECT * FROM dictionary WHERE word = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, word);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String description = resultSet.getString("description");
                String synonyms = resultSet.getString("synonyms");
                String antonyms = resultSet.getString("antonyms");
                JOptionPane.showMessageDialog(null, "Word: " + word + "\nDescription: " + description +
                        "\nSynonyms: " + synonyms + "\nAntonyms: " + antonyms);
            } else {
                JOptionPane.showMessageDialog(null, "Word not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
        }
    }

    private static void showAllWords() {
        String url = "jdbc:mysql://localhost:3306/voc_builder?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "SELECT * FROM dictionary";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            String[] columnNames = {"Word", "Description", "Synonyms", "Antonyms"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String word = resultSet.getString("word");
                String description = resultSet.getString("description");
                String synonyms = resultSet.getString("synonyms");
                String antonyms = resultSet.getString("antonyms");
                tableModel.addRow(new Object[]{word, description, synonyms, antonyms});
            }

            JTable table = new JTable(tableModel);
            JOptionPane.showMessageDialog(null, new JScrollPane(table), "All Words", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
        }
    }

    private static void showInsertDialog() {
        JTextField wordField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);
        JTextField synonymsField = new JTextField(10);
        JTextField antonymsField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Word:"));
        panel.add(wordField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Synonyms:"));
        panel.add(synonymsField);
        panel.add(new JLabel("Antonyms:"));
        panel.add(antonymsField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Insert Word", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String word = wordField.getText();
            String description = descriptionField.getText();
            String synonyms = synonymsField.getText();
            String antonyms = antonymsField.getText();

            if (insertWord(word, description, synonyms, antonyms)) {
                JOptionPane.showMessageDialog(null, "Word inserted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Insertion failed.");
            }
        }
    }

    private static boolean insertWord(String word, String description, String synonyms, String antonyms) {
        String url = "jdbc:mysql://localhost:3306/voc_builder?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "INSERT INTO dictionary (word, description, synonyms, antonyms) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, word);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, synonyms);
            preparedStatement.setString(4, antonyms);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
            return false;
        }
    }

    private static void showDeleteDialog() {
        String word = JOptionPane.showInputDialog(null, "Enter Word to Delete:");
        if (word != null && !word.trim().isEmpty()) {
            if (deleteWord(word)) {
                JOptionPane.showMessageDialog(null, "Word deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Deletion failed.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid word.");
        }
    }

    private static boolean deleteWord(String word) {
        String url = "jdbc:mysql://localhost:3306/voc_builder?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "DELETE FROM dictionary WHERE word = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, word);
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
            return false;
        }
    }

    private static void showEditDialog() {
        JTextField wordField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Enter Word to Edit:"));
        panel.add(wordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Word", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String word = wordField.getText();
            if (word != null && !word.trim().isEmpty()) {
                editWord(word);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid word.");
            }
        }
    }

    private static void editWord(String word) {
        JTextField descriptionField = new JTextField(10);
        JTextField synonymsField = new JTextField(10);
        JTextField antonymsField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("New Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("New Synonyms:"));
        panel.add(synonymsField);
        panel.add(new JLabel("New Antonyms:"));
        panel.add(antonymsField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Word", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String description = descriptionField.getText();
            String synonyms = synonymsField.getText();
            String antonyms = antonymsField.getText();

            if (updateWord(word, description, synonyms, antonyms)) {
                JOptionPane.showMessageDialog(null, "Word updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Update failed.");
            }
        }
    }

    private static boolean updateWord(String word, String description, String synonyms, String antonyms) {
        String url = "jdbc:mysql://localhost:3306/voc_builder?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "UPDATE dictionary SET description = ?, synonyms = ?, antonyms = ? WHERE word = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, description);
            preparedStatement.setString(2, synonyms);
            preparedStatement.setString(3, antonyms);
            preparedStatement.setString(4, word);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
            return false;
        }
    }
}
