package huan.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a specific due date/time.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructs a Deadline object with the specified description and due date/time.
     *
     * @param description The description of the deadline.
     * @param by The deadline in "yyyy-MM-dd HHmm" format.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDateTime.parse(by, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + by.format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mma")) + ")";
    }

    @Override
    public String fileFormat() {
        return "D | " + (isDone ? "1" : "0")
                + " | " + description + " | "
                + by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
}
