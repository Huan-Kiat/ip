package huan.command;

import huan.exception.HuanException;
import huan.storage.Storage;
import huan.tasks.TaskList;

/**
 * Lists tasks to the user.
 */
public class ListCommand extends Command {

    @Override
    public String execute(TaskList tasks, Storage storage) throws HuanException {
        if (tasks.isEmpty()) {
            return "No tasks added yet.";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.getSize(); i++) {
                sb.append((i + 1) + ". " + tasks.getTask(i) + "\n");
            }
            return sb.toString();
        }
    }
}
