package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * Deletes a task from the list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        String response = tasks.deleteTask(taskNumber);
        storage.writeTasks(tasks);
        return response;
    }
}
