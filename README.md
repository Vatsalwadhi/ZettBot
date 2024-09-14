```markdown
# Zettbot

Zettbot is a versatile application designed to help users manage and track their tasks and expenses efficiently. This application provides a user-friendly interface to manage todos, track expenses, and visualize financial data. It integrates with a MySQL database to store and manage user data.

## Features

- **Todo List Management:** Add, remove, and manage your tasks effectively.
- **Expense Tracking:** Add and categorize expenses, and view monthly summaries.
- **User-specific Data:** Each user has their own data and settings, managed via user IDs.

## Installation

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- MySQL Server
- Maven
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

## Configuration

**Database Connection:** Update the database connection details in the source code if necessary. The connection settings are located in the `Moneyplanner` and `todolist` classes.

## Running the Application

1. **Build the Project:**

    ```bash
    mvn clean install
    ```

2. **Run the Application:**

    ```bash
    mvn exec:java -Dexec.mainClass="home.Moneyplanner" -Dexec.args="1"
    ```

    Replace `"home.Moneyplanner"` with the main class you want to run, and `"1"` with the appropriate user ID.

## Usage

- **Moneyplanner:**
    - Manage and track expenses.
    - View total monthly expenses.
    - Add and remove transactions.

- **Todo List:**
    - Add and remove tasks.
    - Manage task list specific to each user.

## Troubleshooting

- **Database Connection Issues:** Ensure that your MySQL server is running and the connection details are correct in the source code.
- **Table Not Found Errors:** Verify that the database schema and tables are correctly created as per the SQL scripts provided.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any issues or inquiries, please contact [vats](mailto:alwadhiv@gmail.com).
```

