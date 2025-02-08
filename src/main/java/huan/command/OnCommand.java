package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * Finds tasks that occur on a particular date.
 */
public class OnCommand extends Command {
    private final String date; // "yyyy-MM-dd"

    public OnCommand(String date) {
        this.date = date;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        return tasks.onDate(date);
    }
}
