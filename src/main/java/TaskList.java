import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;
    private Ui ui = new Ui();

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(int id) throws HuanException {
        if (id <= 0 || id > tasks.size()) {
            throw new HuanException("Invalid task number!");
        }
        Task removedTask = tasks.get(id - 1);
        tasks.remove(id - 1);
        ui.printFormat(Ui.SPACE + "Noted. I've removed this task:\n"
                + Ui.SPACE + "  " + removedTask + "\n"
                + Ui.SPACE + "Now you have " + tasks.size() + " task(s) in the list");

    }

    public int getSize() {
        return tasks.size();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public ArrayList<Task> getTaskList() {
        return tasks;
    }
}
