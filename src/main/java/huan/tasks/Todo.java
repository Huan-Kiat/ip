package huan.tasks;


/**
 * Represents a Todo task with a description.
 */

public class Todo extends Task {
    /**
     * Constructs a Todo with the description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String fileFormat() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
