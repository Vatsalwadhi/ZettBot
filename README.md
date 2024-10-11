# Zettbot

Zettbot is a versatile application designed to help users manage and track their tasks and expenses efficiently. This application provides a user-friendly interface to manage todos, track expenses, and visualize financial data. It integrates with a MySQL database to store and manage user data.

## Features

- **Todo List Management:** Add, remove, and manage your tasks effectively.
- **Expense Tracking:** Add and categorize expenses, and view monthly summaries.
- **User-specific Data:** Each user has their own data and settings, managed via user IDs.
- **Date-Specific Transactions:** Retrieve transactions for a specific date.
- **Dialogflow Integration:** Use Dialogflow for natural language processing to interact with the bot.

## Installation

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- MySQL Server
- Maven
- Dialogflow account
- Your favorite IDE (e.g., IntelliJ IDEA, Eclipse)

### Clone the Repository

```bash
git clone https://github.com/vatsalwadhi/zettbot.git
cd zettbot
```

### Set Up the Database

1. **Create a Database:**

    ```sql
    CREATE DATABASE app;
    ```

2. **Create Required Tables:**

    - **Users Table:**

      ```sql
      CREATE TABLE users (
          id INT AUTO_INCREMENT PRIMARY KEY,
          username VARCHAR(255) NOT NULL,
          password VARCHAR(255) NOT NULL
      );
      ```

    - **Todos Table:**

      ```sql
      CREATE TABLE todos (
          id INT AUTO_INCREMENT PRIMARY KEY,
          item VARCHAR(255) NOT NULL,
          user_id INT NOT NULL,
          FOREIGN KEY (user_id) REFERENCES users(id)
      );
      ```

    - **Transactions Table:**

      ```sql
      CREATE TABLE transactions (
          id INT AUTO_INCREMENT PRIMARY KEY,
          description VARCHAR(255) NOT NULL,
          amount DECIMAL(10, 2) NOT NULL,
          transactionDate DATE NOT NULL,
          user_id INT NOT NULL,
          FOREIGN KEY (user_id) REFERENCES users(id)
      );
      ```

### Set Up Dialogflow

1. **Create a Dialogflow Agent:**
    - Go to [Dialogflow Console](https://dialogflow.cloud.google.com/).
    - Create a new agent and set up intents and entities to handle user queries about expenses and todos.

2. **Configure API Key:**
    - Obtain your Dialogflow API key and integrate it into your application by updating the `DialogflowService` class.

## Configuration

**Database Connection:** Update the database connection details in the source code if necessary. The connection settings are located in the `ChatBotGUI` class and other related classes.

**Dialogflow Integration:** Ensure your Dialogflow API key is correctly set up in the `DialogflowService` class for proper communication.

## Running the Application

1. **Build the Project:**

    ```bash
    mvn clean install
    ```

2. **Run the Application:**

    ```bash
    mvn exec:java -Dexec.mainClass="home.ChatBotGUI" -Dexec.args="1"
    ```

    Replace `"home.ChatBotGUI"` with the main class you want to run, and `"1"` with the appropriate user ID.

## Usage

- **ChatBotGUI:**
    - Interact with the bot to manage your todos and track expenses.
    - Ask about 'monthly expenses', 'specific expenses', or 'transactions on [date]'.
    - The bot uses Dialogflow to interpret user inputs and provide appropriate responses.

## Troubleshooting

- **Database Connection Issues:** Ensure that your MySQL server is running and the connection details are correct in the source code.
- **Table Not Found Errors:** Verify that the database schema and tables are correctly created as per the SQL scripts provided.
- **Dialogflow Integration Issues:** Make sure your Dialogflow API key is correctly set up and the agent is properly configured.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any issues or inquiries, please contact [vats](mailto:alwadhiv@gmail.com).
