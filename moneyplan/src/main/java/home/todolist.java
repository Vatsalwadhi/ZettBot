package home;

import javax.swing.*;
import javax.swing.DefaultListModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class todolist extends javax.swing.JFrame {
    private DefaultListModel<String> listModel;
    private Connection conn;
    private int userId; // Variable to store the user ID

    public todolist(int userId) {
        this.userId = userId; // Initialize userId
        initComponents();
        connectToDatabase();
        loadTodoList();
        setupListeners();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/app"; // Updated to 'app' database
            String user = "root";
            String password = "vats";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadTodoList() {
        listModel.clear();
        try {
            String query = "SELECT item FROM todos WHERE user_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                listModel.addElement(rs.getString("item"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setupListeners() {
        jButtonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTodoItem();
            }
        });

        jButtonDone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedTodo();
            }
        });
    }

    private void addTodoItem() {
        String item = jTextField1.getText().trim();
        if (!item.isEmpty()) {
            try {
                String query = "INSERT INTO todos (item, user_id) VALUES (?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, item);
                pst.setInt(2, userId);
                pst.executeUpdate();
                listModel.addElement(item);
                jTextField1.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteSelectedTodo() {
        int selectedIndex = jList1.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedItem = listModel.getElementAt(selectedIndex);
            try {
                String query = "DELETE FROM todos WHERE item = ? AND user_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, selectedItem);
                pst.setInt(2, userId);
                pst.executeUpdate();
                listModel.remove(selectedIndex);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jButtonDone = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        label1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jTextField1.setBackground(new java.awt.Color(153, 153, 153));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("Add your new todo");
        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14));

        jButtonAdd.setBackground(new java.awt.Color(0, 123, 255));
        jButtonAdd.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAdd.setText("Add");
        jButtonAdd.setFont(new java.awt.Font("Segoe UI", 1, 14));

        jButtonDone.setBackground(new java.awt.Color(0, 123, 255));
        jButtonDone.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDone.setText("Done");
        jButtonDone.setFont(new java.awt.Font("Segoe UI", 1, 14));

        listModel = new DefaultListModel<>();
        jList1.setModel(listModel);
        jList1.setBackground(new java.awt.Color(153, 153, 153));
        jList1.setForeground(new java.awt.Color(255, 255, 255));
        jList1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jScrollPane1.setViewportView(jList1);

        label1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        label1.setForeground(new java.awt.Color(255, 255, 255));
        label1.setText("TODO List");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDone))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(label1)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(label1)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAdd)
                    .addComponent(jButtonDone))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonDone;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel label1;

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // For testing, you might want to hardcode a user ID here
                new todolist(1).setVisible(true);
            }
        });
    }
}
