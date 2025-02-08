package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.Event;
import huan.tasks.TaskList;

import java.time.format.DateTimeParseException;

/**
 * Adds an Event task.
 */
public class EventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;

    /**
     * @param description The event description
     * @param from The event start time/date
     * @param to The event end time/date
     */
    public EventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        if (description.isBlank() || from.isBlank() || to.isBlank()) {
            throw new HuanException("Invalid event format!");
        }
        try {
            String response = tasks.addTask(new Event(description, from, to));
            storage.writeTasks(tasks);
            return response;
        } catch (DateTimeParseException e) {
            throw new HuanException("Invalid date/time format for event. Use yyyy-MM-dd HHmm");
        }
    }
}
