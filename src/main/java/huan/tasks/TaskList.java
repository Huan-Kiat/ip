package huan.tasks;

import huan.exception.HuanException;
import huan.ui.Ui;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Manages a list of tasks and provides operations to manipulate them.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }


    /**
     * Adds a task to this list and prints a confirmation message.
     *
     * @param task The task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println(Ui.SPACE + Ui.LINE);
        System.out.println(Ui.SPACE + "Got it. I've added this task:");
        System.out.println(Ui.SPACE + "  " + tasks.get(tasks.size() - 1));
        System.out.println(Ui.SPACE + "Now you have " + tasks.size() + " task(s) in the list");
        System.out.println(Ui.SPACE + Ui.LINE);
    }

    /**
     * Deletes the task at the given index.
     *
     * @param id The index of the task to delete.
     * @throws HuanException If the index is invalid.
     */
    public void deleteTask(int id) throws HuanException {
        if (id <= 0 || id > tasks.size()) {
            throw new HuanException("Invalid task number!");
        }
        Task removedTask = tasks.get(id - 1);
        tasks.remove(id - 1);
        Ui.printFormat("Noted. I've removed this task:\n"
                + Ui.SPACE + "  " + removedTask + "\n"
                + Ui.SPACE + "Now you have " + tasks.size() + " task(s) in the list");

    }

    /**
     * Adds a task to this list without printing confirmation message.
     *
     * @param task The task to load.
     */
    public void loadTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the total number of tasks in this list.
     *
     * @return The size of this TaskList.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Retrieves a task by its index.
     *
     * @param id The index of the task.
     * @return The Task at the given index.
     */
    public Task getTask(int id) {
        return tasks.get(id);
    }

    /**
     * Checks if this TaskList is empty.
     *
     * @return True if there are no tasks, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the list of Task objects.
     *
     * @return The ArrayList of Task.
     */
    public ArrayList<Task> getTaskList() {
        return tasks;
    }

    /**
     * Marks a task as done based on ID.
     *
     * @param id ID of task to be marked done.
     * @throws HuanException for invalid IDs.
     */
    public void markTask(int id) throws HuanException {
        if (id <= 0 || id > this.getSize()) {
            throw new HuanException("Invalid task number!");
        }
        Task t = this.getTask(id - 1);
        t.markAsDone();
        System.out.println(Ui.SPACE + Ui.LINE);
        System.out.println(Ui.SPACE + "Nice! I've marked this task as done:");
        System.out.println(Ui.SPACE + "  " + t);
        System.out.println(Ui.SPACE + Ui.LINE);
    }

    /**
     * Unmarks a task as done based on ID.
     *
     * @param id ID of task to be marked uundone.
     * @throws HuanException for invalid IDs.
     */
    public void unmarkTask(int id) throws HuanException {
        if (id <= 0 || id > this.getSize()) {
            throw new HuanException("Invalid task number!");
        }
        Task t = this.getTask(id - 1);
        t.markAsUndone();
        System.out.println(Ui.SPACE + Ui.LINE);
        System.out.println(Ui.SPACE + "OK, I've marked this task as not done yet:");
        System.out.println(Ui.SPACE + "  " + t);
        System.out.println(Ui.SPACE + Ui.LINE);
    }

    /**
     * Adds a Deadline
     *
     * @param description Description of deadline.
     * @param by Deadline date.
     */
    public void addDeadline(String description, String by) {
        this.addTask(new Deadline(description, by));
    }

    /**
     * Adds an Event.
     *
     * @param description Description of event.
     * @param from Start date/time of event.
     * @param to End date/time of event.
     */
    public void addEvent(String description, String from, String to) {
        this.addTask(new Event(description, from, to));
    }

    /**
     * Adds a Todo.
     *
     * @param description Description of todo.
     */
    public void addTodo(String description) {
        this.addTask(new Todo(description));
    }

    /**
     * Checks if there are any tasks that occur on the given date.
     *
     * @param date The date to check for tasks.
     * @throws HuanException when there is an error in input date format.
     */
    public void onDate(String date) throws HuanException {
        try {
            LocalDate target = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println(Ui.SPACE + Ui.LINE);
            System.out.println(Ui.SPACE + "Here are the task(s) on this date:");
            boolean validDate = false;
            for (Task task : this.getTaskList()) {
                if (task instanceof Deadline d) {
                    if (d.by.toLocalDate().equals(target)) {
                        System.out.println(Ui.SPACE + task);
                        validDate = true;
                    }
                } else if (task instanceof Event e) {
                    if (!e.from.toLocalDate().isAfter(target) && !e.to.toLocalDate().isBefore(target)) {
                        System.out.println(Ui.SPACE + task);
                        validDate = true;
                    }
                }
            }

            if (!validDate) {
                System.out.println(Ui.SPACE + "Phew! There are no tasks on this date!");
            }
            System.out.println(Ui.SPACE + Ui.LINE);

        } catch (DateTimeParseException e) {
            throw new HuanException("Invalid date format! (yyyy-MM-dd)");
        }
    }

    /**
     * Checks if there are any tasks with a matching description.
     *
     * @param description The description of the task to find.
     */
    public void findTasks(String description) {
        boolean existsTask = false;
        TaskList newTasks = new TaskList();
        for (Task task : tasks) {
            if (task.description.toLowerCase().contains(description.toLowerCase())) {
                newTasks.loadTask(task);
                existsTask = true;
            }
        }
        if (!existsTask) {
            Ui.printFormat("No such task found!");
        } else {
            System.out.println(Ui.SPACE + Ui.LINE);
            System.out.println(Ui.SPACE + "Here are the matching tasks in your list:");
            for (Task task : newTasks.getTaskList()) {
                System.out.println(Ui.SPACE + task);
            }
            System.out.println(Ui.SPACE + Ui.LINE);
        }

    }

}
