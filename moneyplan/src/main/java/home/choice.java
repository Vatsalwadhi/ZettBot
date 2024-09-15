package home;

import javax.swing.*;

public class choice extends JFrame {
    private int userId; // Variable to store the user ID

    public choice(int userId) {
        this.userId = userId; // Initialize userId
        initComponents();
    }

    public choice() {
        initComponents();
    }

    private void initComponents() {
        JPanel jPanel1 = new JPanel();
        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();
        JButton jButton3 = new JButton(); // New button for ChatBot
        JLabel jLabel1 = new JLabel();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jButton1.setBackground(new java.awt.Color(0, 123, 255));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ToDoList");
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // Bold font
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        jButton2.setBackground(new java.awt.Color(0, 123, 255));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Money Planner");
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // Bold font
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        jButton3.setBackground(new java.awt.Color(0, 123, 255));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("ChatBot");
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // Bold font
        jButton3.addActionListener(evt -> jButton3ActionPerformed(evt)); // New action

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SELECT THE APP");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // Larger font

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jButton1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(61, 61, 61))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jLabel1)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(158, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new todolist(userId).setVisible(true);
        dispose();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        new Moneyplanner(userId).setVisible(true); // Assuming Moneyplanner takes userId as a parameter
        dispose();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        new ChatBotGUI(userId).setVisible(true); // Show ChatBot GUI
        dispose();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new choice().setVisible(true));
    }
}
