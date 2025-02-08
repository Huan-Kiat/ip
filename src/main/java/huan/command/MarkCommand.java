package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * Marks a task as done.
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        String response = tasks.markTask(taskNumber);
        storage.writeTasks(tasks);
        return response;
    }
}
