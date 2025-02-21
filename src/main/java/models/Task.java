package models;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Task {

    public IntegerProperty id;
    public StringProperty title;
    private StringProperty description;
    public StringProperty priority;
    public StringProperty deadline;
    public StringProperty status;



    //Constructor
    public Task(int id, String title, String description, String priority, String deadline, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleStringProperty(priority);
        this.deadline = new SimpleStringProperty(deadline);
        this.status = new SimpleStringProperty(status);

    }

    // Getters
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty categoryProperty() {
        return priority;
    }

    public StringProperty deadlineProperty() {
        return deadline;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Getters and Setters for 'status'
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }




}
