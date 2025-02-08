package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * The abstract Command class for all Huan commands.
 */
public abstract class Command {
    /**
     * Executes the command.
     *
     * @param tasks The TaskList of current tasks.
     * @param storage The Storage for writing/loading.
     * @return The response.
     * @throws HuanException If something goes wrong during execution.
     */
    public abstract String execute(TaskList tasks, Storage storage) throws HuanException;

    /**
     * Whether this command should exit.
     *
     * @return True if the chatbot should end, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}