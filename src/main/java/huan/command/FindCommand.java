package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * Finds tasks that match a given description.
 */
public class FindCommand extends Command {
    private final String description;

    public FindCommand(String description) {
        this.description = description;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        if (description.isBlank()) {
            throw new HuanException("What do you want to find?");
        }
        return tasks.findTasks(description);
    }
}
