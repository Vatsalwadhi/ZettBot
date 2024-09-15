package  home;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatBotGUI extends JFrame {
    private int userId; // Variable to store the user ID
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "vats";

    public ChatBotGUI(int userId) {
        this.userId = userId;
        initComponents();
    }

    private void initComponents() {
        setTitle("ChatBot");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create panel and set layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create and configure text area
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create panel for input field and send button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // Create and configure input field
        inputField = new JTextField();
        bottomPanel.add(inputField, BorderLayout.CENTER);

        // Create and configure send button
        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 30));
        bottomPanel.add(sendButton, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUserInput();
            }
        });

        add(panel);
    }

    private void handleUserInput() {
        String userInput = inputField.getText();
        if (!userInput.isEmpty()) {
            // Get the response from Dialogflow
            String dialogflowResponse = DialogflowService.getResponseFromDialogflow(userInput);
            textArea.append("User: " + userInput + "\n");
            textArea.append("Bot: " + dialogflowResponse + "\n");

            // Check if the response indicates a need for specific data
            if (dialogflowResponse.toLowerCase().contains("monthly expenses")) {
                textArea.append("Bot: Sure, let me fetch your monthly expenses.\n");
                String expenses = getMonthlyExpensesFromDatabase();
                textArea.append("Bot: Here are your expenses for this month:\n" + expenses + "\n");
            } else if (dialogflowResponse.toLowerCase().contains("expense")) {
                textArea.append("Bot: Here are the details of your expenses:\n");
                String specificExpenses = getSpecificExpensesFromDatabase();
                textArea.append(specificExpenses + "\n");
            } else if (dialogflowResponse.toLowerCase().contains("transactions on")) {
                String date = extractDateFromInput(userInput);
                if (date != null) {
                    textArea.append("Bot: Let me fetch transactions for the date " + date + ".\n");
                    String transactions = getTransactionsByDateFromDatabase(date);
                    textArea.append("Bot: Here are your transactions for " + date + ":\n" + transactions + "\n");
                } else {
                    textArea.append("Bot: I didn't understand the date format. Please specify a date in the format 'dd MMMM yyyy' or 'yyyy-MM-dd'.\n");
                }
            } else {
                textArea.append("Bot: Sorry, I didn’t understand that. Please ask about 'monthly expenses', 'expense', or 'transactions on [date]'.\n");
            }

            // Clear input field
            inputField.setText("");
        }
    }

    private String extractDateFromInput(String input) {
        String[] formats = {"dd MMMM yyyy", "d MMMM yyyy", "dd/MM/yyyy", "d/M/yyyy", "yyyy-MM-dd"};
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                Date date = sdf.parse(input);
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                return targetFormat.format(date);
            } catch (ParseException e) {
                // Continue trying other formats
            }
        }
        return null; // Return null if no format matches
    }

    private String getMonthlyExpensesFromDatabase() {
        StringBuilder expenses = new StringBuilder();
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Execute SQL query
            String query = "SELECT description, amount FROM transactions WHERE user_id = " + userId + " AND MONTH(transactionDate) = MONTH(CURDATE())";
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set
            if (!resultSet.next()) {
                return "No expenses found for this month.";
            }

            // Reset cursor to start
            resultSet.beforeFirst();
            while (resultSet.next()) {
                expenses.append(String.format("Description: %s, Amount: %.2f\n", 
                        resultSet.getString("description"), resultSet.getDouble("amount")));
            }

            // Clean up resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching expenses: " + e.getMessage();
        }
        return expenses.toString();
    }

    private String getSpecificExpensesFromDatabase() {
        StringBuilder expenses = new StringBuilder();
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Execute SQL query
            String query = "SELECT description, amount FROM transactions WHERE user_id = " + userId;
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set
            if (!resultSet.next()) {
                return "No expenses found.";
            }

            // Reset cursor to start
            resultSet.beforeFirst();
            while (resultSet.next()) {
                expenses.append(String.format("Description: %s, Amount: %.2f\n", 
                        resultSet.getString("description"), resultSet.getDouble("amount")));
            }

            // Clean up resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching expenses: " + e.getMessage();
        }
        return expenses.toString();
    }

    private String getTransactionsByDateFromDatabase(String date) {
        StringBuilder transactions = new StringBuilder();
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Execute SQL query
            String query = "SELECT description, amount FROM transactions WHERE user_id = " + userId + " AND DATE(transactionDate) = '" + date + "'";
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set
            if (!resultSet.next()) {
                return "No transactions found for this date.";
            }

            // Reset cursor to start
            resultSet.beforeFirst();
            while (resultSet.next()) {
                transactions.append(String.format("Description: %s, Amount: %.2f\n", 
                        resultSet.getString("description"), resultSet.getDouble("amount")));
            }

            // Clean up resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching transactions: " + e.getMessage();
        }
        return transactions.toString();
    }

    public static void main(String[] args) {
        // For testing purposes, use a default user ID
        SwingUtilities.invokeLater(() -> new ChatBotGUI(1).setVisible(true));
    }
}
