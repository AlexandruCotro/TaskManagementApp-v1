package ui;
import javafx.scene.Cursor;
import models.Task;
import DatabaseConnector.DatabaseConnector;
import dao.TaskDAO;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.application.Platform;
import javafx.stage.Window;




public class Main extends Application {

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {


        // Disable resizing
        primaryStage.setResizable(false);

// Task List Container
        VBox taskListContainer = new VBox(); // Vertical container for tasks
        taskListContainer.setSpacing(25); // Increase spacing between tasks for better readability
        taskListContainer.setPadding(new Insets(20));
        taskListContainer.setStyle("-fx-background-color: #000000;");

// ScrollPane for task list
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(taskListContainer);
        scrollPane.setFitToWidth(false); // Ensure the content stretches to fit the width
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Show vertical scrollbar as needed
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);      // Disable horizontal scrollbar
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"); // Transparent background and no border
        scrollPane.setPrefHeight(500); // Increased fixed height for the scrollable area to fit more tasks
        scrollPane.setMaxHeight(500); // Prevent the scroll pane from exceeding 600px
        scrollPane.setMinHeight(500); // Maintain a minimum height
        scrollPane.setFitToHeight(true);

        // Title Label
        Text title = new Text("Task Management App");
        title.setFill(Color.WHITE);
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 24)); // Use correct font



        // New Task Button
        Button createTaskButton = new Button("New Task");
        createTaskButton.getStyleClass().add("button"); // Apply CSS
        createTaskButton.setOnAction(event -> openNewTaskPage(primaryStage));

        // Category and Priority Buttons
        Button allButton = createCategoryButton("All");
        Button inProgressButton = createCategoryButton("In Progress");
        Button completedButton = createCategoryButton("Completed");

        Button lowPriorityButton = createPriorityButton("Low");
        Button mediumPriorityButton = createPriorityButton("Medium");
        Button highPriorityButton = createPriorityButton("High");

        // Handle Category Button Clicks
        // Handle Category Button Clicks
        // "All" Button: Shows all tasks (both completed and pending)
        allButton.setOnAction(event -> {
            // Show all tasks (no priority filter, "all" status filter)
            populateTaskList(taskListContainer, "all", "all");
            // Highlight "All" and deselect other category buttons
            highlightSelectedButton(allButton, inProgressButton, completedButton);

            // Deselect all priority buttons when a category is selected
            if (lowPriorityButton.getStyle().contains("gray"))
                lowPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (mediumPriorityButton.getStyle().contains("gray"))
                mediumPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (highPriorityButton.getStyle().contains("gray"))
                highPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        });

// "In Progress" Button: Shows tasks that are not completed
        inProgressButton.setOnAction(event -> {
            // Show tasks that are "In Progress" (pending tasks)
            populateTaskList(taskListContainer, "all", "pending");
            // Highlight "In Progress" and deselect other category buttons
            highlightSelectedButton(inProgressButton, allButton, completedButton);

            // Deselect all priority buttons when a category is selected
            if (lowPriorityButton.getStyle().contains("gray"))
                lowPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (mediumPriorityButton.getStyle().contains("gray"))
                mediumPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (highPriorityButton.getStyle().contains("gray"))
                highPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        });

// "Completed" Button: Shows completed tasks
        completedButton.setOnAction(event -> {
            // Show tasks that are "Completed"
            populateTaskList(taskListContainer, "all", "completed");
            // Highlight "Completed" and deselect other category buttons
            highlightSelectedButton(completedButton, allButton, inProgressButton);

            // Deselect all priority buttons when a category is selected
            if (lowPriorityButton.getStyle().contains("gray"))
                lowPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (mediumPriorityButton.getStyle().contains("gray"))
                mediumPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (highPriorityButton.getStyle().contains("gray"))
                highPriorityButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        });

// "Low Priority" Button: Shows low-priority tasks
        lowPriorityButton.setOnAction(event -> {
            // Show "Low" priority tasks and all statuses
            populateTaskList(taskListContainer, "Low", "all");
            // Highlight "Low" and deselect other priority buttons
            highlightSelectedButton(lowPriorityButton, mediumPriorityButton, highPriorityButton);

            // Deselect all category buttons when priority is selected
            if (allButton.getStyle().contains("gray"))
                allButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (inProgressButton.getStyle().contains("gray"))
                inProgressButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (completedButton.getStyle().contains("gray"))
                completedButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        });

// "Medium Priority" Button: Shows medium-priority tasks
        mediumPriorityButton.setOnAction(event -> {
            // Show "Medium" priority tasks and all statuses
            populateTaskList(taskListContainer, "Medium", "all");
            // Highlight "Medium" and deselect other priority buttons
            highlightSelectedButton(mediumPriorityButton, lowPriorityButton, highPriorityButton);

            // Deselect all category buttons when priority is selected
            if (allButton.getStyle().contains("gray"))
                allButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (inProgressButton.getStyle().contains("gray"))
                inProgressButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (completedButton.getStyle().contains("gray"))
                completedButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        });

// "High Priority" Button: Shows high-priority tasks
        highPriorityButton.setOnAction(event -> {
            // Show "High" priority tasks and all statuses
            populateTaskList(taskListContainer, "High", "all");
            // Highlight "High" and deselect other priority buttons
            highlightSelectedButton(highPriorityButton, lowPriorityButton, mediumPriorityButton);

            // Deselect all category buttons when priority is selected
            if (allButton.getStyle().contains("gray"))
                allButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (inProgressButton.getStyle().contains("gray"))
                inProgressButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
            if (completedButton.getStyle().contains("gray"))
                completedButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        });


        // HBox for Title and New Task Button
        HBox topBar = new HBox(830); // Spacing between title and button
        topBar.setPadding(new Insets(20));
        topBar.setStyle("-fx-background-color: #000000;"); // Black background
        topBar.getChildren().addAll(title, createTaskButton);

        // HBox for Category and Priority Buttons
        HBox buttonHBox = new HBox(5);
        buttonHBox.setPadding(new Insets(10, 20, 10, 20));
        buttonHBox.setAlignment(Pos.CENTER_RIGHT);
        buttonHBox.setStyle("-fx-background-color: #000000;");
        buttonHBox.getChildren().addAll(allButton, inProgressButton, completedButton, lowPriorityButton, mediumPriorityButton, highPriorityButton);






        // Populate the task list
        populateTaskList(taskListContainer,"All","All");



        // Add the task list container to the main layout
        HBox mainLayout = new HBox(20);
        mainLayout.getChildren().addAll(taskListContainer);
        mainLayout.getChildren().addAll(scrollPane);

        // VBox for Layout
        VBox root = new VBox(5);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #000000;");
        root.getChildren().addAll(topBar, buttonHBox, mainLayout);
        // Scene setup
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Task Management App");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // Populate the task list with tasks from the database


    private void populateTaskList(VBox taskListContainer, String priorityFilter,String statusFilter) {
        TaskDAO taskDAO = new TaskDAO(); // Create a DAO instance
        List<Task> tasks = new ArrayList<>();

             // Determine which tasks to show based on priority and status filters
        // Determine which tasks to show based on priority and status filters
        if ((priorityFilter == null || priorityFilter.equalsIgnoreCase("all"))
                && (statusFilter == null || statusFilter.equalsIgnoreCase("all"))) {
            tasks = taskDAO.getTasksByStatus("Pending"); // Show only in-progress tasks in "All Tasks" category
        } else if ((priorityFilter == null || priorityFilter.equalsIgnoreCase("all"))
                && statusFilter.equalsIgnoreCase("completed")) {
            tasks = taskDAO.getTasksByStatus("Completed"); // Show only completed tasks in "Completed Tasks" category
        } else if ((priorityFilter == null || priorityFilter.equalsIgnoreCase("all"))
                && statusFilter.equalsIgnoreCase("pending")) {
            tasks = taskDAO.getTasksByStatus("Pending"); // Show only pending tasks
        } else if ((statusFilter == null || statusFilter.equalsIgnoreCase("all"))
                && priorityFilter.equalsIgnoreCase("low")) {
            // Only show low priority tasks that are in progress (not completed)
            tasks = taskDAO.getTasksByPriorityAndStatus("Low", "Pending"); // Filter by low priority and not completed
        } else if ((statusFilter == null || statusFilter.equalsIgnoreCase("all"))
                && priorityFilter.equalsIgnoreCase("medium")) {
            // Only show medium priority tasks that are in progress (not completed)
            tasks = taskDAO.getTasksByPriorityAndStatus("Medium", "Pending"); // Filter by medium priority and not completed
        } else if ((statusFilter == null || statusFilter.equalsIgnoreCase("all"))
                && priorityFilter.equalsIgnoreCase("high")) {
            // Only show high priority tasks that are in progress (not completed)
            tasks = taskDAO.getTasksByPriorityAndStatus("High", "Pending"); // Filter by high priority and not completed
        } else if (priorityFilter != null && statusFilter != null) {
            // For combined filter, ensure completed tasks are excluded from the "All Tasks" category
            tasks = taskDAO.getTasksByPriorityAndStatus(priorityFilter, statusFilter); // Filter by both priority and status
        } else {
            tasks = taskDAO.getTasksByStatus("Pending"); // Default fallback to show in-progress tasks
        }


        // Clear the container to avoid duplicate entries
        taskListContainer.getChildren().clear();

        // List to store selected task IDs for deletion
        List<Integer> selectedTaskIds = new ArrayList<>();

        for (Task task : tasks) {
            HBox taskItem = new HBox(10); // Horizontal layout for each task
            taskItem.setPadding(new Insets(10));
            taskItem.setStyle(
                    "-fx-background-color: #000000;" +
                            "-fx-border-width: 1px;" +
                            "-fx-border-radius: 5px;" +
                            "-fx-background-radius: 5px;"
            );



            // Priority bullet point
            Circle priorityBullet = new Circle(8);
            String priority = task.priority.get();
            switch (priority.toLowerCase()) {
                case "low":
                    priorityBullet.setFill(Color.GREEN);
                    break;
                case "medium":
                    priorityBullet.setFill(Color.YELLOW);
                    break;
                case "high":
                    priorityBullet.setFill(Color.RED);
                    break;
                default:
                    priorityBullet.setFill(Color.GRAY);
                    break;
            }

            // Checkbox for task selection
            CheckBox taskCheckbox = new CheckBox();

            taskCheckbox.setOnAction(event -> {
                if (taskCheckbox.isSelected()) {
                    selectedTaskIds.add(task.id.get());
                } else {
                    selectedTaskIds.remove((Integer) task.id.get());
                }
            });

            // Trashcan icon
            ImageView trashcanIcon = new ImageView(new Image(getClass().getResource("/trash-can.png").toExternalForm()));
            trashcanIcon.setFitWidth(20);
            trashcanIcon.setFitHeight(20);
            trashcanIcon.setCursor(Cursor.HAND);
            trashcanIcon.setOnMouseClicked(event -> {
                // Check if the checkbox is selected before deleting
                if (taskCheckbox.isSelected()) {
                    taskDAO.deleteTask(task.id.get());
                    populateTaskList(taskListContainer, priorityFilter,statusFilter); // Repopulate after deletion
                } else {
                    System.out.println("Please check the checkbox to delete the task.");
                }
            });

            // Task title and deadline
            Text taskTitle = new Text(task.titleProperty().get());
            taskTitle.setFill(Color.WHITE);
            taskTitle.setFont(Font.font(16));

            Text taskDeadlineText = new Text("Created: " + task.deadlineProperty().get());
            taskDeadlineText.setFill(Color.LIGHTGRAY);
            taskDeadlineText.setFont(Font.font(14));

            VBox taskDetails = new VBox(5);
            taskDetails.getChildren().addAll(taskTitle, taskDeadlineText);

            HBox.setMargin(taskDetails, new Insets(-5, 0, 0, 10));
            Button markCompleteButton = new Button("Mark as Complete");
            markCompleteButton.setStyle("-fx-background-color: #285729; -fx-text-fill: #000000;");

// Check the task status and adjust the button's visibility
            if (task.status.get().equalsIgnoreCase("completed")) {
                markCompleteButton.setVisible(false); // Make the button invisible
                markCompleteButton.setManaged(false); // Prevent it from taking up space
            } else {
                markCompleteButton.setOnAction(event -> {
                    // Update status in the Task object
                    task.setStatus("Completed");

                    // Save the updated status to the database
                    taskDAO.updateTaskStatus(task.getId(), "Completed");

                    // Refresh the UI or notify the user
                    System.out.println("Task marked as complete!");

                    // Optionally, refresh the displayed task list
                    populateTaskList(taskListContainer, priorityFilter, statusFilter);
                });
            }

// Create a spacer to push the button to the far right dynamically
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS); // Spacer takes up all available space dynamically

// Create an HBox for task details and button
            HBox taskRow = new HBox(15); // Adjust spacing between elements
            taskRow.setAlignment(Pos.CENTER_LEFT); // Align all items to the left
            taskRow.setPrefWidth(1150); // Set a consistent width for each task row
            taskRow.getChildren().addAll(priorityBullet, trashcanIcon, taskCheckbox, taskDetails, spacer, markCompleteButton);

// Add the task row to the container
            taskListContainer.getChildren().add(taskRow);

        }
    }

    private void highlightSelectedButton(Button selectedButton, Button... otherButtons) {
        // Highlight the selected button
        selectedButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        // Reset the style for other buttons
        for (Button button : otherButtons) {
            button.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
        }
    }

    private Button createCategoryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("category-button");
        button.setOnAction(event -> System.out.println(text + " category selected"));
        return button;
    }

    private Button createPriorityButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("priority-button");
        button.setOnAction(event -> System.out.println(text + " priority selected"));
        return button;
    }

    private void openNewTaskPage(Stage primaryStage) {

        // Add Title Label
        Text title = new Text("Add a New Task");
        title.setFill(Color.WHITE);
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Bold.ttf"), 24)); // Use correct font

        //Adding Description text
        Text description = new Text("Description");
        description.setFill(Color.DARKGRAY);
        description.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"),14));


// Arrow Icon for Back Button
        Label backArrow = new Label("\u2190"); // Unicode for left arrow
        backArrow.setStyle(
                "-fx-font-size: 24px;" +  // Larger font size
                        "-fx-text-fill: white;"   // Default color
        );

// Hover Effect and Cursor Change
        backArrow.setOnMouseEntered(event -> backArrow.setStyle(
                "-fx-font-size: 24px;" +  // Keep the size
                        "-fx-text-fill: #100f0f;" +  // Change color to blue on hover
                        "-fx-cursor: hand;"  // Change cursor to a hand icon
        ));
        backArrow.setOnMouseExited(event -> backArrow.setStyle(
                "-fx-font-size: 24px;" +  // Keep the size
                        "-fx-text-fill: white;"   // Revert to default color
        ));

// On Click Event
        backArrow.setOnMouseClicked(event -> start(primaryStage));




        // Create Task Input Field
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter your task here...");
        taskInputField.setPrefWidth(300); // Explicitly set preferred width
        taskInputField.setStyle(
                "-fx-background-color: transparent;" + // Transparent background
                        "-fx-border-color: white;" +          // White border
                        "-fx-border-width: 1px;" +            // Border thickness
                        "-fx-text-fill: white;" +             // White text color
                        "-fx-font-size: 14px;"                // Font size
        );


        //Priority Label
        Text priority = new Text("Priority");
        priority.setFill(Color.DARKGRAY);
        priority.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"),14));

        //Priority Combobox
        ComboBox <String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll("Low","Medium","High"); //Adding priority levels
        priorityComboBox.setPromptText("Select Priority"); //Placeholder text

        priorityComboBox.setStyle(
                        "-fx-background-color: #707070;" +  // Transparent dropdown// Border thickness
                        "-fx-text-fill: white;" +              // Text color
                        "-fx-font-size: 14px;"  + // Font size
                        "-fx-prompt-text-fill: white;"
        );

        priorityComboBox.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: #707070; -fx-text-fill: white;"); // Default styles
                    }
                }
            };

            // Adding hover effect for the ListCell
            cell.setOnMouseEntered(event -> {
                // Change background color and text color on hover
                cell.setStyle("-fx-background-color: #5a5a5a; -fx-text-fill: #9d9d9d;"); // Yellow text on hover
            });

            cell.setOnMouseExited(event -> {
                // Reset the background color and text color when the mouse exits
                cell.setStyle("-fx-background-color: #707070; -fx-text-fill: white;"); // Default color
            });

            return cell;
        });

        priorityComboBox.setPrefWidth(200); //Setting width


        // Event Listener for ComboBox
        priorityComboBox.setOnAction(event -> {
            String selectedPriority = priorityComboBox.getValue();
            System.out.println("Selected Priority: " + selectedPriority);

            // When refreshing, reapply the selected value to the ComboBox
            priorityComboBox.setValue(selectedPriority);
        });



        // Add Button
        Button addButton = new Button("Add");
        addButton.setPrefWidth(120); // Set the button width
        addButton.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: black;" +
                        "-fx-background-color: #4f5050;" + // Gray button
                        "-fx-border-width: 1px;" +
                        "-fx-cursor: hand;" // Pointer cursor on hover
        );

        addButton.setOnAction(event -> {
            // Retrieve the task description and priority from the UI components
            String taskDescription = taskInputField.getText().trim();
            String priority2 = priorityComboBox.getValue(); // Get selected priority

            // Check if both fields are not empty
            if (!taskDescription.isEmpty() && priority2 != null) {
                TaskDAO taskDAO = new TaskDAO();

                // No need to specify deadline, let the addTask method handle it
                String status = "Pending";

                // Pass the task description, priority, and status to the addTask method
                taskDAO.addTask(taskDescription, taskDescription, priority2, status);

                System.out.println("Task added successfully!");

                // Do NOT clear the combo box selection
            } else {
                System.out.println("Please fill out both the task description and priority.");
            }
        });
        //Clear Button


        //Action event








        // Optionally constrain minimum and maximum width
        taskInputField.setMinWidth(150);
        taskInputField.setMaxWidth(300);

        // Layout for Title and Input Field (Top)
        VBox inputLayout = new VBox(10); // Spacing between items
        inputLayout.setPadding(new Insets(20, 0, 0, 20)); // Padding for the top section
        inputLayout.getChildren().addAll(title, description, taskInputField, priority,priorityComboBox);

        // Layout for Back Button (Bottom-Left)
        // HBox to hold Arrow and Add Button
        HBox bottomBar = new HBox(1030); // Spacing between Arrow and Add Button
        bottomBar.setPadding(new Insets(0, 20, 30, 20)); // Padding for the bottom
        bottomBar.setAlignment(Pos.BOTTOM_LEFT); // Align bottom left
        bottomBar.getChildren().addAll(backArrow, addButton);


        // Main Layout
        VBox newTaskLayout = new VBox();
        newTaskLayout.setPadding(new Insets(20));
        newTaskLayout.setStyle("-fx-background-color: #000000;");
        newTaskLayout.getChildren().addAll(inputLayout, bottomBar);
        // Set VBox alignment to stretch child nodes properly
        VBox.setVgrow(bottomBar, javafx.scene.layout.Priority.ALWAYS);
        // New Scene for New Task Page
        Scene newTaskScene = new Scene(newTaskLayout, 1280, 720);
        newTaskScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(newTaskScene);

    }


}
