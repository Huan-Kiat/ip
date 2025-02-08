package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;
import huan.tasks.Todo;

/**
 * Adds a Todo task.
 */
public class TodoCommand extends Command {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        if (description.isBlank()) {
            throw new HuanException("Todo description cannot be empty!");
        }
        String response = tasks.addTask(new Todo(description));
        storage.writeTasks(tasks);
        return response;
    }
}
