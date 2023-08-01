package Lab5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FormCrud {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JTextField addressField;
    private JComboBox<String> bloodGroupComboBox;
    private JTextField emailField;
    private JButton submitButton;
    private JButton updateButton;
    private JButton selectButton;
    private JButton deleteButton;
    private JTable dataTable;

    private Connection connection;

    public FormCrud() {
        initializeDatabase();
        createForm();
        createTable();
        populateTable();

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDataToTable();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedRow();
                submitButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectRowForUpdate();
                // Disable the Submit and Delete buttons

                submitButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/bim5th",
                    "root",
                    "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createForm() {
        JFrame frame = new JFrame("Form CRUD");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Form fields

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(new Rectangle(20, 40, 80, 30));
        frame.add(firstNameLabel);
        firstNameField = new JTextField();
        firstNameField.setBounds(new Rectangle(180, 40, 200, 30));
        frame.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(new Rectangle(20, 80, 80, 30));
        frame.add(lastNameLabel);
        lastNameField = new JTextField();
        lastNameField.setBounds(new Rectangle(180, 80, 200, 30));
        frame.add(lastNameField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(new Rectangle(20, 120, 80, 30));
        frame.add(genderLabel);
        maleRadioButton = new JRadioButton("Male");
        maleRadioButton.setBounds(new Rectangle(180, 120, 80, 30));
        femaleRadioButton = new JRadioButton("Female");
        ButtonGroup genderButtonGroup = new ButtonGroup();
        genderButtonGroup.add(maleRadioButton);
        genderButtonGroup.add(femaleRadioButton);
        femaleRadioButton.setBounds(new Rectangle(260, 120, 100, 30));
        frame.add(maleRadioButton);
        frame.add(femaleRadioButton);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(new Rectangle(20, 160, 80, 30));
        frame.add(addressLabel);
        addressField = new JTextField();
        addressField.setBounds(new Rectangle(180, 160, 200, 30));
        frame.add(addressField);

        JLabel bloodGroupLabel = new JLabel("Blood Group:");
        bloodGroupLabel.setBounds(new Rectangle(20, 200, 80, 30));
        frame.add(bloodGroupLabel);
        bloodGroupComboBox = new JComboBox<>();
        bloodGroupComboBox.addItem("Select a Blood Group");
        bloodGroupComboBox.addItem("A+");
        bloodGroupComboBox.addItem("A-");
        bloodGroupComboBox.addItem("B+");
        bloodGroupComboBox.addItem("B-");
        bloodGroupComboBox.addItem("O+");
        bloodGroupComboBox.addItem("O-");
        bloodGroupComboBox.addItem("AB+");
        bloodGroupComboBox.addItem("AB-");
        bloodGroupComboBox.setBounds(180, 200, 200, 30);
        frame.add(bloodGroupComboBox);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(new Rectangle(20, 240, 80, 30));
        frame.add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(new Rectangle(180, 240, 200, 30));
        frame.add(emailField);

        submitButton = new JButton("Submit");
        submitButton.setBounds(new Rectangle(20, 280, 100, 30));
        frame.add(submitButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(new Rectangle(130, 280, 100, 30));
        frame.add(updateButton);

        selectButton = new JButton("Select for Update");
        selectButton.setBounds(new Rectangle(240, 280, 150, 30));
        frame.add(selectButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(new Rectangle(400, 280, 100, 30));
        frame.add(deleteButton);

        // Data table


        dataTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(10,350,510,300);
        tabbedPane.addTab("Table",scrollPane);
        frame.add(tabbedPane);

        scrollPane.setBounds(new Rectangle(20, 330, 550, 300));
        frame.getContentPane().add(tabbedPane);




        frame.setSize(550, 700);
        frame.setVisible(true);
    }

    private void createTable() {
        try {

            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "first_name VARCHAR(255)," +
                    "last_name VARCHAR(255)," +
                    "gender VARCHAR(10)," +
                    "address VARCHAR(255)," +
                    "blood_group VARCHAR(10)," +
                    "email VARCHAR(255)" +
                    ")";
            PreparedStatement statement = connection.prepareStatement(createTableQuery);
            statement.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTable() {
        try {
            String selectQuery = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = statement.executeQuery(selectQuery);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("First Name");
            model.addColumn("Last Name");
            model.addColumn("Gender");
            model.addColumn("Address");
            model.addColumn("Blood Group");
            model.addColumn("Email");


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                String address = resultSet.getString("address");
                String bloodGroup = resultSet.getString("blood_group");
                String email = resultSet.getString("email");

                model.addRow(new Object[]{id, firstName, lastName, gender, address, bloodGroup, email});
            }

            dataTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDataToTable() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String gender = maleRadioButton.isSelected() ? "Male" : femaleRadioButton.isSelected()? "Female" : "";
        String address = addressField.getText();
        String bloodGroup = bloodGroupComboBox.getSelectedItem().toString();
        String email = emailField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() ||gender.isEmpty() || address.isEmpty() ||
                bloodGroup.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields");
        } else {
            try {
                String insertQuery = "INSERT INTO users (first_name, last_name, gender, address, blood_group, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(insertQuery);
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, gender);
                statement.setString(4, address);
                statement.setString(5, bloodGroup);
                statement.setString(6, email);
                statement.executeUpdate();

                clearFormFields();
                populateTable();
                JOptionPane.showMessageDialog(null, "Data Inserted");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSelectedRow() {
        int selectedRowIndex = dataTable.getSelectedRow();
        if (selectedRowIndex != -1) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String gender = maleRadioButton.isSelected() ? "Male" : femaleRadioButton.isSelected()? "Female" : "";
            String address = addressField.getText();
            String bloodGroup = bloodGroupComboBox.getSelectedItem().toString();
            String email = emailField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty()  || address.isEmpty() ||
                    bloodGroup.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields");
            } else {
                int userId = (int) dataTable.getValueAt(selectedRowIndex, 0);
                try {
                    String updateQuery = "UPDATE users SET first_name = ?, last_name = ?, gender = ?, " +
                            "address = ?, blood_group = ?, email = ? WHERE id = ?";
                    PreparedStatement statement = connection.prepareStatement(updateQuery);
                    statement.setString(1, firstName);
                    statement.setString(2, lastName);
                    statement.setString(3, gender);
                    statement.setString(4, address);
                    statement.setString(5, bloodGroup);
                    statement.setString(6, email);
                    statement.setInt(7, userId);
                    statement.executeUpdate();

                    clearFormFields();
                    populateTable();
                    JOptionPane.showMessageDialog(null, "Data Updated");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to update");
        }
    }

    private void selectRowForUpdate() {
        int selectedRowIndex = dataTable.getSelectedRow();
        if (selectedRowIndex != -1) {
            int userId = (int) dataTable.getValueAt(selectedRowIndex, 0);
            try {
                String selectQuery = "SELECT * FROM users WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(selectQuery);
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String gender = resultSet.getString("gender");
                    String address = resultSet.getString("address");
                    String bloodGroup = resultSet.getString("blood_group");
                    String email = resultSet.getString("email");

                    firstNameField.setText(firstName);
                    lastNameField.setText(lastName);
                    if (gender.equals("Male")) {
                        maleRadioButton.setSelected(true);
                    } else {
                        femaleRadioButton.setSelected(true);
                    }
                    addressField.setText(address);
                    bloodGroupComboBox.setSelectedItem(bloodGroup);
                    emailField.setText(email);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to update");
        }
    }

    private void deleteSelectedRow() {
        int selectedRowIndex = dataTable.getSelectedRow();
        if (selectedRowIndex != -1) {
            int userId = (int) dataTable.getValueAt(selectedRowIndex, 0);
            try {
                String deleteQuery = "DELETE FROM users WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(deleteQuery);
                statement.setInt(1, userId);
                statement.executeUpdate();

                clearFormFields();
                populateTable();
                JOptionPane.showMessageDialog(null, "Data Deleted");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to delete");
        }
    }

    private void clearFormFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        maleRadioButton.setSelected(true);
        addressField.setText("");
        bloodGroupComboBox.setSelectedIndex(0);
        emailField.setText("");
    }

    public static void main(String[] args) {
       new FormCrud();
    }
}
