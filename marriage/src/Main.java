import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Main {
    private static final Color HUNTER_GREEN = new Color(53, 94, 59);
    private static final Color RASPBERRY = new Color(227, 11, 93);
    private static JFrame frame;

    public static void main(String[] args) {
        showHomePage();
    }

    private static void showHomePage() {
        frame = new JFrame("Marriage Record Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(RASPBERRY);
        frame.add(panel);

        JLabel welcomeLabel = new JLabel("Welcome to Marriage Record Manager");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(welcomeLabel, gbc);

        JButton showAllHusbandsButton = new JButton("Show All Husbands");
        JButton showAllWivesButton = new JButton("Show All Wives");
        JButton insertHusbandWifeButton = new JButton("Insert Husband & Wife");
        JButton deleteHusbandButton = new JButton("Delete Husband");
        JButton deleteWifeButton = new JButton("Delete Wife");
        JButton editHusbandButton = new JButton("Edit Husband");
        JButton editWifeButton = new JButton("Edit Wife");

        setButtonColors(showAllHusbandsButton);
        setButtonColors(showAllWivesButton);
        setButtonColors(insertHusbandWifeButton);
        setButtonColors(deleteHusbandButton);
        setButtonColors(deleteWifeButton);
        setButtonColors(editHusbandButton);
        setButtonColors(editWifeButton);

        panel.add(showAllHusbandsButton, gbc);
        panel.add(showAllWivesButton, gbc);
        panel.add(insertHusbandWifeButton, gbc);
        panel.add(deleteHusbandButton, gbc);
        panel.add(deleteWifeButton, gbc);
        panel.add(editHusbandButton, gbc);
        panel.add(editWifeButton, gbc);

        showAllHusbandsButton.addActionListener(e -> showAllHusbands());
        showAllWivesButton.addActionListener(e -> showAllWives());
        insertHusbandWifeButton.addActionListener(e -> showInsertHusbandWifeDialog());
        deleteHusbandButton.addActionListener(e -> showDeleteHusbandDialog());
        deleteWifeButton.addActionListener(e -> showDeleteWifeDialog());
        editHusbandButton.addActionListener(e -> showEditHusbandDialog());
        editWifeButton.addActionListener(e -> showEditWifeDialog());

        frame.setSize(400, 500);
        frame.setVisible(true);
    }

    private static void setButtonColors(JButton button) {
        button.setBackground(HUNTER_GREEN);
        button.setForeground(Color.WHITE);
    }

    private static void showAllHusbands() {
        String url = "jdbc:mysql://localhost:3306/wedding?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "SELECT * FROM husband_record";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            String[] columnNames = {"Husband Name", "Age", "City", "Witness Name"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String husbandName = resultSet.getString("husband_name");
                int age = resultSet.getInt("age");
                String city = resultSet.getString("city");
                String witnessName = resultSet.getString("witness_name");
                tableModel.addRow(new Object[]{husbandName, age, city, witnessName});
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(RASPBERRY);
            JOptionPane.showMessageDialog(null, scrollPane, "All Husbands", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
        }
    }

    private static void showAllWives() {
        String url = "jdbc:mysql://localhost:3306/wedding?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "SELECT * FROM wife_record";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            String[] columnNames = {"Wife Name", "Age", "City", "Witness Name"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String wifeName = resultSet.getString("wife_name");
                int age = resultSet.getInt("age");
                String city = resultSet.getString("city");
                String witnessName = resultSet.getString("witness_name");
                tableModel.addRow(new Object[]{wifeName, age, city, witnessName});
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(RASPBERRY);
            JOptionPane.showMessageDialog(null, scrollPane, "All Wives", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL statement: " + ex.getMessage());
        }
    }

    private static void showInsertHusbandWifeDialog() {
        JTextField husbandNameField = new JTextField(10);
        JTextField husbandAgeField = new JTextField(10);
        JTextField husbandCityField = new JTextField(10);
        JTextField husbandWitnessNameField = new JTextField(10);

        JTextField wifeNameField = new JTextField(10);
        JTextField wifeAgeField = new JTextField(10);
        JTextField wifeCityField = new JTextField(10);
        JTextField wifeWitnessNameField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.setBackground(RASPBERRY);
        panel.add(new JLabel("Husband Name:"));
        panel.add(husbandNameField);
        panel.add(new JLabel("Husband Age:"));
        panel.add(husbandAgeField);
        panel.add(new JLabel("Husband City:"));
        panel.add(husbandCityField);
        panel.add(new JLabel("Husband Witness Name:"));
        panel.add(husbandWitnessNameField);
        panel.add(new JLabel("Wife Name:"));
        panel.add(wifeNameField);
        panel.add(new JLabel("Wife Age:"));
        panel.add(wifeAgeField);
        panel.add(new JLabel("Wife City:"));
        panel.add(wifeCityField);
        panel.add(new JLabel("Wife Witness Name:"));
        panel.add(wifeWitnessNameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Insert Husband & Wife", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String husbandName = husbandNameField.getText();
            int husbandAge = Integer.parseInt(husbandAgeField.getText());
            String husbandCity = husbandCityField.getText();
            String husbandWitnessName = husbandWitnessNameField.getText();

            String wifeName = wifeNameField.getText();
            int wifeAge = Integer.parseInt(wifeAgeField.getText());
            String wifeCity = wifeCityField.getText();
            String wifeWitnessName = wifeWitnessNameField.getText();

            if (insertHusbandWife(husbandName, husbandAge, husbandCity, husbandWitnessName, wifeName, wifeAge, wifeCity, wifeWitnessName)) {
                JOptionPane.showMessageDialog(null, "Husband and Wife records inserted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Insertion failed.");
            }
        }
    }

    private static boolean insertHusbandWife(String husbandName, int husbandAge, String husbandCity, String husbandWitnessName,
                                             String wifeName, int wifeAge, String wifeCity, String wifeWitnessName) {
        String url = "jdbc:mysql://localhost:3306/wedding?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String insertHusbandSQL = "INSERT INTO husband_record (husband_name, age, city, witness_name) VALUES (?, ?, ?, ?)";
        String insertWifeSQL = "INSERT INTO wife_record (wife_name, age, city, witness_name) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pstmtHusband = connection.prepareStatement(insertHusbandSQL);
             PreparedStatement pstmtWife = connection.prepareStatement(insertWifeSQL)) {

            connection.setAutoCommit(false);

            // Insert Husband
            pstmtHusband.setString(1, husbandName);
            pstmtHusband.setInt(2, husbandAge);
            pstmtHusband.setString(3, husbandCity);
            pstmtHusband.setString(4, husbandWitnessName);
            int affectedRowsHusband = pstmtHusband.executeUpdate();

            if (affectedRowsHusband == 0) {
                connection.rollback();
                return false;
            }

            // Insert Wife
            pstmtWife.setString(1, wifeName);
            pstmtWife.setInt(2, wifeAge);
            pstmtWife.setString(3, wifeCity);
            pstmtWife.setString(4, wifeWitnessName);
            int affectedRowsWife = pstmtWife.executeUpdate();

            if (affectedRowsWife == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inserting records: " + ex.getMessage());
            return false;
        }
    }

    private static void showDeleteHusbandDialog() {
        String husbandName = JOptionPane.showInputDialog("Enter Husband Name to Delete:");
        if (husbandName != null && !husbandName.trim().isEmpty()) {
            if (deleteHusband(husbandName)) {
                JOptionPane.showMessageDialog(null, "Husband record deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Deletion failed.");
            }
        }
    }

    private static boolean deleteHusband(String husbandName) {
        String url = "jdbc:mysql://localhost:3306/wedding?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "DELETE FROM husband_record WHERE husband_name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, husbandName);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting record: " + ex.getMessage());
            return false;
        }
    }

    private static void showDeleteWifeDialog() {
        String wifeName = JOptionPane.showInputDialog("Enter Wife Name to Delete:");
        if (wifeName != null && !wifeName.trim().isEmpty()) {
            if (deleteWife(wifeName)) {
                JOptionPane.showMessageDialog(null, "Wife record deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Deletion failed.");
            }
        }
    }

    private static boolean deleteWife(String wifeName) {
        String url = "jdbc:mysql://localhost:3306/wedding?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "DELETE FROM wife_record WHERE wife_name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, wifeName);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting record: " + ex.getMessage());
            return false;
        }
    }

    private static void showEditHusbandDialog() {
        JTextField husbandNameField = new JTextField(10);
        JTextField newHusbandAgeField = new JTextField(10);
        JTextField newHusbandCityField = new JTextField(10);
        JTextField newHusbandWitnessNameField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.setBackground(RASPBERRY);
        panel.add(new JLabel("Husband Name:"));
        panel.add(husbandNameField);
        panel.add(new JLabel("New Age:"));
        panel.add(newHusbandAgeField);
        panel.add(new JLabel("New City:"));
        panel.add(newHusbandCityField);
        panel.add(new JLabel("New Witness Name:"));
        panel.add(newHusbandWitnessNameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Husband", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String husbandName = husbandNameField.getText();
            int newHusbandAge = Integer.parseInt(newHusbandAgeField.getText());
            String newHusbandCity = newHusbandCityField.getText();
            String newHusbandWitnessName = newHusbandWitnessNameField.getText();

            if (editHusband(husbandName, newHusbandAge, newHusbandCity, newHusbandWitnessName)) {
                JOptionPane.showMessageDialog(null, "Husband record updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Update failed.");
            }
        }
    }

    private static boolean editHusband(String husbandName, int newHusbandAge, String newHusbandCity, String newHusbandWitnessName) {
        String url = "jdbc:mysql://localhost:3306/wedding?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "UPDATE husband_record SET age = ?, city = ?, witness_name = ? WHERE husband_name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, newHusbandAge);
            pstmt.setString(2, newHusbandCity);
            pstmt.setString(3, newHusbandWitnessName);
            pstmt.setString(4, husbandName);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating record: " + ex.getMessage());
            return false;
        }
    }

    private static void showEditWifeDialog() {
        JTextField wifeNameField = new JTextField(10);
        JTextField newWifeAgeField = new JTextField(10);
        JTextField newWifeCityField = new JTextField(10);
        JTextField newWifeWitnessNameField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.setBackground(RASPBERRY);
        panel.add(new JLabel("Wife Name:"));
        panel.add(wifeNameField);
        panel.add(new JLabel("New Age:"));
        panel.add(newWifeAgeField);
        panel.add(new JLabel("New City:"));
        panel.add(newWifeCityField);
        panel.add(new JLabel("New Witness Name:"));
        panel.add(newWifeWitnessNameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Wife", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String wifeName = wifeNameField.getText();
            int newWifeAge = Integer.parseInt(newWifeAgeField.getText());
            String newWifeCity = newWifeCityField.getText();
            String newWifeWitnessName = newWifeWitnessNameField.getText();

            if (editWife(wifeName, newWifeAge, newWifeCity, newWifeWitnessName)) {
                JOptionPane.showMessageDialog(null, "Wife record updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Update failed.");
            }
        }
    }

    private static boolean editWife(String wifeName, int newWifeAge, String newWifeCity, String newWifeWitnessName) {
        String url = "jdbc:mysql://localhost:3306/wedding?useSSL=false";
        String user = "root";
        String dbPassword = "12345";
        String sql = "UPDATE wife_record SET age = ?, city = ?, witness_name = ? WHERE wife_name = ?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, newWifeAge);
            pstmt.setString(2, newWifeCity);
            pstmt.setString(3, newWifeWitnessName);
            pstmt.setString(4, wifeName);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating record: " + ex.getMessage());
            return false;
        }
    }
}