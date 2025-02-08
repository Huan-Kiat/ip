package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * Represents an invalid command.
 */
public class InvalidCommand extends Command {
    private final String message;

    public InvalidCommand(String message) {
        this.message = message;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        return message;
    }
}
