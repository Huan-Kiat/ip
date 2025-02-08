package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * Exits the chatbot.
 */
public class ExitCommand extends Command {

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        return "Bye. Hope to see you again soon!";
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
