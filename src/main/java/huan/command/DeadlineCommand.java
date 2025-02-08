package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.Deadline;
import huan.tasks.TaskList;

import java.time.format.DateTimeParseException;

/**
 * Adds a Deadline task.
 */
public class DeadlineCommand extends Command {
    private final String description;
    private final String by;

    public DeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        if (description.isBlank() || by.isBlank()) {
            throw new HuanException("Invalid deadline format!");
        }
        try {
            String response = tasks.addTask(new Deadline(description, by));
            storage.writeTasks(tasks);
            return response;
        } catch (DateTimeParseException e) {
            throw new HuanException("Invalid date/time format for deadline. Use yyyy-MM-dd HHmm");
        }
    }
}
