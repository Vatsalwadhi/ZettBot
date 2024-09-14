package home;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;
import home.DateLabelFormatter;

public class Moneyplanner extends javax.swing.JFrame {

    private Connection conn;
    private DefaultTableModel tableModel;
    private JDatePickerImpl datePicker;
    private int userId; // Variable to store the user ID

    public Moneyplanner(int userId) {
        this.userId = userId; // Initialize userId
        initComponents();
        connectToDatabase();
        setupDatePicker();
        setupListeners();
        loadData();
        calculateMonthlyExpenses();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/app";
            String user = "root";
            String password = "vats";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setupDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBounds(20, 100, 200, 30); // Ensure it's positioned properly
        jPanel1.add(datePicker);
    }

    private void setupListeners() {
        jButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addData();
            }
        });

        jButtonRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeData();
            }
        });
    }

    private void addData() {
        String description = jTextFieldDescription.getText().trim();
        String amountText = jTextFieldAmount.getText().trim();
        Date selectedDate = (Date) datePicker.getModel().getValue();
        if (!description.isEmpty() && !amountText.isEmpty() && selectedDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(selectedDate);
            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
                return;
            }
            try {
                String query = "INSERT INTO transactions (description, amount, transactionDate, user_id) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, description);
                pst.setDouble(2, amount);
                pst.setString(3, date); // Ensure this matches the column name
                pst.setInt(4, userId);
                pst.executeUpdate();
                loadData();
                jTextFieldDescription.setText("");
                jTextFieldAmount.setText("");
                calculateMonthlyExpenses();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }

    private void removeData() {
        int selectedRow = jTableTransactions.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0); // Assuming ID is in the first column
            try {
                String query = "DELETE FROM transactions WHERE id = ? AND user_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, id);
                pst.setInt(2, userId);
                pst.executeUpdate();
                loadData();
                calculateMonthlyExpenses();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a transaction to remove.");
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            String query = "SELECT * FROM transactions WHERE user_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                String date = rs.getString("transactionDate"); // Ensure this matches the column name
                tableModel.addRow(new Object[]{id, description, amount, date});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void calculateMonthlyExpenses() {
        double totalExpense = 0;
        try {
            String query = "SELECT SUM(amount) AS total FROM transactions WHERE MONTH(transactionDate) = MONTH(CURDATE()) AND user_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalExpense = rs.getDouble("total");
            }
            DecimalFormat df = new DecimalFormat("#,###.00"); // Format for currency
            jLabelExpenses.setText("Total Monthly Expense: ₹" + df.format(totalExpense));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jTextFieldDescription = new javax.swing.JTextField();
        jTextFieldAmount = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTransactions = new javax.swing.JTable();
        label1 = new javax.swing.JLabel();
        jLabelExpenses = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Money Planner");

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(null);

        jTextFieldDescription.setText("Enter Description");
        jPanel1.add(jTextFieldDescription);
        jTextFieldDescription.setBounds(20, 20, 250, 30);

        jTextFieldAmount.setText("Enter Amount");
        jPanel1.add(jTextFieldAmount);
        jTextFieldAmount.setBounds(20, 60, 250, 30);

        jButtonAdd.setText("Add Transaction");
        jButtonAdd.setBounds(280, 20, 180, 40); // Adjusted size
        jPanel1.add(jButtonAdd);

        jButtonRemove.setText("Remove Transaction");
        jButtonRemove.setBounds(280, 60, 180, 40); // Adjusted size
        jPanel1.add(jButtonRemove);

        tableModel = new DefaultTableModel(new String[]{"ID", "Description", "Amount", "Date"}, 0);
        jTableTransactions.setModel(tableModel);
        jScrollPane1.setViewportView(jTableTransactions);
        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(20, 150, 520, 200); // Adjusted position and size

        label1.setFont(new java.awt.Font("Times New Roman", 1, 18));
        label1.setText("Transaction List");
        jPanel1.add(label1);
        label1.setBounds(20, 130, 150, 20);

        jLabelExpenses.setFont(new java.awt.Font("Arial", 1, 18)); // Increased font size
        jLabelExpenses.setBounds(20, 370, 520, 30); // Adjusted position and width
        jPanel1.add(jLabelExpenses);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE) // Adjusted size
        );

        pack();
    }

    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableTransactions;
    private javax.swing.JTextField jTextFieldDescription;
    private javax.swing.JTextField jTextFieldAmount;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel jLabelExpenses;

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // For testing, you might want to hardcode a user ID here
                new Moneyplanner(1).setVisible(true);
            }
        });
    }
}
