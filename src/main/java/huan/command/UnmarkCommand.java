package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * Unmarks a task as not done.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        String response = tasks.unmarkTask(taskNumber);
        storage.writeTasks(tasks);
        return response;
    }
}
