package dao;

import DatabaseConnector.DatabaseConnector;
import models.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskDAO {
    private static DatabaseConnector databaseConnector;

    public TaskDAO() {
        this.databaseConnector = new DatabaseConnector();
    }

    // Create or update the table structure
    public static void createTable() {
        String alterTableSQL = "ALTER TABLE tasks RENAME COLUMN priority TO priority";
        String createTableSQL = """
        CREATE TABLE IF NOT EXISTS tasks (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            description TEXT,
            priority TEXT,
            deadline TEXT,
            status TEXT
        );
        """;

        try (Connection conn = new DatabaseConnector().getConnection();
             Statement stmt = conn.createStatement()) {

            // Attempt to alter the table, ignore errors if it doesn't exist
            try {
                stmt.execute(alterTableSQL);
                System.out.println("Column renamed to 'priority'.");
            } catch (SQLException e) {
                System.out.println("No need to rename column: " + e.getMessage());
            }

            // Ensure the table exists
            stmt.execute(createTableSQL);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error setting up table: " + e.getMessage());
        }
    }

    // Fetch all tasks
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String priority = rs.getString("priority");
                String deadline = rs.getString("deadline");
                String status = rs.getString("status");

                tasks.add(new Task(id, title, description, priority, deadline, status));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tasks: " + e.getMessage());
        }

        return tasks;
    }

    // Add a task to the database
// Add a task to the database
    public void addTask(String title, String description, String priority, String status) {
        String sql = "INSERT INTO tasks (title, description, priority, deadline, status) VALUES (?, ?, ?, ?, ?)";

        // Get the current system date and time
        String currentSystemDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // Use the system date as the deadline
        String deadline = currentSystemDate;

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, priority);
            pstmt.setString(4, deadline); // Insert the system date as deadline
            pstmt.setString(5, status);

            pstmt.executeUpdate();
            System.out.println("Task added successfully with deadline: " + deadline);
        } catch (SQLException e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
    }



    // Update a task
    public void updateTask(int id, String newTitle, String newDescription, String newPriority, String newDeadline, String newStatus) {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?, deadline = ?, status = ? WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newTitle);
            pstmt.setString(2, newDescription);
            pstmt.setString(3, newPriority);
            pstmt.setString(4, newDeadline);
            pstmt.setString(5, newStatus);
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
            System.out.println("Task updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }

    // Delete a task
    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        System.out.println("Attempting to delete task with ID: " + id);

        // Get the connection manually
        Connection conn = null;
        try {
            // Establish the connection manually
            conn = DriverManager.getConnection("jdbc:sqlite:tasks.db");  // Replace with your connection URL
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);  // Set the task ID

            int rowsAffected = pstmt.executeUpdate();  // Execute the delete query

            // Log rows affected (task deleted)
            if (rowsAffected > 0) {
                System.out.println("Task with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No task found with ID " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();  // Make sure to close the connection manually
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Fetch tasks by priority
    public List<Task> getTasksByPriority(String priority) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE priority = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, priority);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String deadline = rs.getString("deadline");
                String status = rs.getString("status");

                tasks.add(new Task(id, title, description, priority, deadline, status));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tasks by priority: " + e.getMessage());
        }

        return tasks;
    }

    // Delete all tasks
    public static void deleteAllTasks() {
        String sql = "DELETE FROM tasks";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
            System.out.println("All tasks have been deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting all tasks: " + e.getMessage());
        }
    }

    // Update the status of a task
// Update the status of a task
    public void updateTaskStatus(int id, String newStatus) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        System.out.println("Attempting to update status for task with ID: " + id);

        // Get the connection manually
        Connection conn = null;
        try {
            // Establish the connection manually
            conn = DriverManager.getConnection("jdbc:sqlite:tasks.db"); // Replace with your connection URL
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus); // Set the new status
            pstmt.setInt(2, id);          // Set the task ID

            int rowsAffected = pstmt.executeUpdate(); // Execute the update query

            // Log rows affected (status updated)
            if (rowsAffected > 0) {
                System.out.println("Task with ID " + id + " updated successfully to status: " + newStatus);
            } else {
                System.out.println("No task found with ID " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating task status: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close(); // Make sure to close the connection manually
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Fetch tasks by status (completed or pending)
    public List<Task> getTasksByStatus(String status) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE status = ?"; // Filter by the status column

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status); // Set the status filter ('completed' or 'pending')
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String priority = rs.getString("priority");
                String deadline = rs.getString("deadline");
                String taskStatus = rs.getString("status"); // Status from the database

                tasks.add(new Task(id, title, description, priority, deadline, taskStatus)); // Add the task to the list
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tasks by status: " + e.getMessage());
        }

        return tasks;
    }

    public List<Task> getTasksByPriorityAndStatus(String priority, String status) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE priority = ? AND status = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, priority);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String deadline = rs.getString("deadline");

                tasks.add(new Task(id, title, description, priority, deadline, status));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tasks by priority and status: " + e.getMessage());
        }

        return tasks;
    }













}
