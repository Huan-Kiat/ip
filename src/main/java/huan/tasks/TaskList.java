package huan.tasks;

import huan.exception.HuanException;
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
     * Adds a task to this list and returns a confirmation message.
     *
     * @param task The task to add.
     * @return The confirmation message.
     */
    public String addTask(Task task) {
        String conflictResponse = hasConflict(task);
        tasks.add(task);
        StringBuilder sb = new StringBuilder();
        sb.append("Got it. I've added this task:\n");
        sb.append("  ").append(tasks.get(tasks.size() - 1)).append("\n");
        sb.append("You now have ").append(tasks.size()).append(" task(s) in your list\n");
        if (!conflictResponse.isEmpty()) {
            sb.append("\nWarning: ").append(conflictResponse).append("\n");
        }
        return sb.toString();
    }

    /**
     * Deletes the task at the given index and returns a confirmation message.
     *
     * @param id The index of the task to delete.
     * @return The confirmation message.
     * @throws HuanException If the index is invalid.
     */
    public String deleteTask(int id) throws HuanException {
        if (id <= 0 || id > tasks.size()) {
            throw new HuanException("Invalid task number!");
        }
        Task removedTask = tasks.get(id - 1);
        tasks.remove(id - 1);
        StringBuilder sb = new StringBuilder();
        sb.append("Noted. I've removed this task:\n");
        sb.append("  ").append(removedTask).append("\n");
        sb.append("You now have ").append(tasks.size()).append(" task(s) in your list\n");
        return sb.toString();
    }

    /**
     * Adds a task to this list without printing a confirmation message.
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
     * Marks a task as done based on ID and returns a confirmation message.
     *
     * @param id ID of task to be marked done.
     * @return The confirmation message.
     * @throws HuanException for invalid IDs.
     */
    public String markTask(int id) throws HuanException {
        if (id <= 0 || id > this.getSize()) {
            throw new HuanException("Invalid task number!");
        }
        Task t = this.getTask(id - 1);
        t.markAsDone();
        StringBuilder sb = new StringBuilder();
        sb.append("Nice! I've marked this task as done:\n");
        sb.append("  ").append(t).append("\n");
        return sb.toString();
    }

    /**
     * Unmarks a task as done based on ID and returns a confirmation message.
     *
     * @param id ID of task to be unmarked.
     * @return The confirmation message.
     * @throws HuanException for invalid IDs.
     */
    public String unmarkTask(int id) throws HuanException {
        if (id <= 0 || id > this.getSize()) {
            throw new HuanException("Invalid task number!");
        }
        Task t = this.getTask(id - 1);
        t.markAsUndone();
        StringBuilder sb = new StringBuilder();
        sb.append("OK, I've marked this task as not done yet:\n");
        sb.append("  ").append(t).append("\n");
        return sb.toString();
    }

    /**
     * Checks if there are any tasks that occur on the given date.
     *
     * @param date The date to check for tasks.
     * @return A message listing the tasks on that date.
     * @throws HuanException when there is an error in input date format.
     */
    public String onDate(String date) throws HuanException {
        try {
            LocalDate target = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            StringBuilder sb = new StringBuilder();
            sb.append("Here are the task(s) on this date:\n");
            boolean isValidDate = false;
            for (Task task : this.getTaskList()) {
                if (task instanceof Deadline d) {
                    if (d.by.toLocalDate().equals(target)) {
                        sb.append("  ").append(task).append("\n");
                        isValidDate = true;
                    }
                } else if (task instanceof Event e) {
                    if (!e.from.toLocalDate().isAfter(target) && !e.to.toLocalDate().isBefore(target)) {
                        sb.append("  ").append(task).append("\n");
                        isValidDate = true;
                    }
                }
            }
            if (!isValidDate) {
                sb.append("Phew! There are no tasks on this date!\n");
            }
            return sb.toString();
        } catch (DateTimeParseException e) {
            throw new HuanException("Invalid date format! (yyyy-MM-dd)");
        }
    }

    /**
     * Checks if there are any tasks with a matching description.
     *
     * @param description The description of the task to find.
     * @return A message listing the matching tasks.
     */
    public String findTasks(String description) {
        boolean doesTaskExist = false;
        TaskList newTasks = new TaskList();
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            if (task.description.toLowerCase().contains(description.toLowerCase())) {
                newTasks.loadTask(task);
                doesTaskExist = true;
            }
        }
        if (!doesTaskExist) {
            sb.append("No such task found!");
        } else {
            sb.append("Here are the matching tasks in your list:\n");
            for (Task task : newTasks.getTaskList()) {
                sb.append("  ").append(task).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Checks if a new task conflicts with any existing tasks.
     *
     * @param newTask The new task to be added.
     * @return A warning message if there's a conflict, or an empty string if none.
     */
    public String hasConflict(Task newTask) {
        if (newTask instanceof Todo todo) {
            return "";
        }

        for (Task task : tasks) {
            if (task instanceof Deadline && newTask instanceof Deadline) {
                continue;
            } else if (task instanceof Event existingEvent && newTask instanceof Event newEvent) {
                if (!newEvent.from.isAfter(existingEvent.to) && !newEvent.to.isBefore(existingEvent.from)) {
                    return "This event clashes with another event! :\n  " + existingEvent;
                }
            } else if (task instanceof Event existingEvent && newTask instanceof Deadline newDeadline) {
                if (!newDeadline.by.isBefore(existingEvent.from) && !newDeadline.by.isAfter(existingEvent.to)) {
                    return "This deadline falls within an event period! :\n  " + existingEvent;
                }
            }
        }

        return "";
    }
}