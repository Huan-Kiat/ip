import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Huan {
    public static String line = "____________________________________________________________";
    public static String space = "    ";
    private ArrayList<Task> tasks = new ArrayList<>();
    private static final String FILE_PATH = "data/huan.txt";

    /**
     * Enum for available inputs.
     */
    public enum InputType {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, INVALID
    }

    /**
     * run() method to de-clutter main method
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        printGreeting();

        while (true) {
            String input = scanner.nextLine().trim();
            InputType command = parseInput(input);

            try {
                switch (command) {
                case BYE:
                    printExit();
                    scanner.close();
                    return;
                case LIST:
                    getTasks();
                    break;
                case MARK:
                    String[] markParts = input.split(" ", 2);
                    if (markParts.length != 2){
                        throw new HuanException("Include task number to mark!");
                    }
                    int markId = Integer.parseInt(markParts[1]);
                    markTask(markId);
                    break;
                case UNMARK:
                    String[] unmarkParts = input.split(" ", 2);
                    if (unmarkParts.length != 2){
                        throw new HuanException("Include task number to unmark!");
                    }
                    int unmarkId = Integer.parseInt(unmarkParts[1]);
                    UnmarkTask(unmarkId);
                    break;
                case DELETE:
                    String[] deleteParts = input.split(" ", 2);
                    if (deleteParts.length != 2){
                        throw new HuanException("Include task number to delete!");
                    }
                    int deleteId = Integer.parseInt(deleteParts[1]);
                    deleteTask(deleteId);
                    break;
                case TODO:
                    parseToDo(input);
                    break;
                case DEADLINE:
                    parseDeadline(input);
                    break;
                case EVENT:
                    parseEvent(input);
                    break;
                case INVALID:
                    System.out.println(space + line);
                    System.out.println(space + "Invalid input!");
                    System.out.println(space + line);
                    break;
                }
            } catch (HuanException e) {
                System.out.println(space + line);
                System.out.println(space + "Error: " + e.getMessage());
                System.out.println(space + line);
            } catch (NumberFormatException e) {
                System.out.println(space + line);
                System.out.println(space + "Error: Please input an integer for the Task number");
                System.out.println(space + line);
            } catch (Exception e) {
                System.out.println(space + line);
                System.out.println(space + "Error: OOPS! Something went wrong!");
                System.out.println(space + line);
            }
        }
    }

    public void parseDeadline(String input) throws HuanException{
        String[] deadlineParts = input.split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].substring(9).trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
            throw new HuanException("Follow format \"deadline (description) /by (date)\"");
        }
        String deadlineDescription =  deadlineParts[0].substring(9).trim();
        String by = deadlineParts[1].trim();
        addDeadline(deadlineDescription, by);
    }

    public void parseToDo(String input) throws HuanException{
        if (input.length() == 4) {
            throw new HuanException("todo description cannot be empty!");
        }
        String todoDescription = input.substring(5).trim();
        addTodo(todoDescription);
    }

    public void parseEvent(String input) throws HuanException {
        String[] fromParts = input.split("/from", 2);
        if (fromParts.length < 2 || fromParts[0].substring(6).trim().isEmpty()) {
            throw new HuanException("Follow format \"event (description) /from (fromDate) /to (toDate)\"");
        }
        String eventDescription = fromParts[0].substring(6).trim();
        String[] toParts = fromParts[1].split("/to", 2);
        String from = toParts[0].trim();
        String to = toParts[1].trim();
        addEvent(eventDescription, from, to);
    }

    /**
     * Prints the greeting.
     */
    public void printGreeting(){
        String greeting =  space + "Hello! I'm HUAN \n"
                + space + "What can I do for you?";
        System.out.println(space + line);
        System.out.println(greeting);
        System.out.println(space + line);
    }

    /**
     * Prints the exit .
     */
    public void printExit(){
        String exit = space + "Bye. Hope to see you again soon!";
        System.out.println(space + line);
        System.out.println(exit);
        System.out.println(space + line);
    }

    /**
     * Prints the list of tasks.
     */
    public void getTasks() {
        System.out.println(space + line);
        if (tasks.isEmpty()) {
            System.out.println(space + "No tasks added yet.");
        } else {
            System.out.println(space + "Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(space + (i + 1) + ". " + tasks.get(i).toString());
            }
        }
        System.out.println(space + line);
    }

    /**
     * Marks a task as done based on ID.
     *
     * @param id ID of task to be marked done.
     * @throws HuanException for invalid IDs.
     */
    public void markTask(int id) throws HuanException{
        if (id <= 0 || id > tasks.size()) {
            throw new HuanException("Invalid task number!");
        }
        Task t = tasks.get(id - 1);
        t.markAsDone();
        System.out.println(space + line);
        System.out.println(space + "Nice! I've marked this task as done:");
        System.out.println(space + "  " + t);
        System.out.println(space + line);
    }

    /**
     * Unmarks a task as done based on ID.
     *
     * @param id ID of task to be marked uundone.
     * @throws HuanException for invalid IDs.
     */
    public void UnmarkTask(int id) throws HuanException {
        if (id <= 0 || id > tasks.size()) {
            throw new HuanException("Invalid task number!");
        }
        Task t = tasks.get(id - 1);
        t.markAsUndone();
        System.out.println(space + line);
        System.out.println(space + "OK, I've marked this task as not done yet:");
        System.out.println(space + "  " + t);
        System.out.println(space + line);
    }

    /**
     * Adds task to list.
     *
     * @param task Task to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println(space + line);
        System.out.println(space + "Got it. I've added this task:");
        System.out.println(space + "  " + tasks.get(tasks.size() - 1));
        System.out.println(space + "Now you have " + tasks.size() + " task(s) in the list");
        System.out.println(space + line);
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
     * Deletes task from list based on ID.
     *
     * @param id ID of the task to delete.
     * @throws HuanException For invalid IDs.
     */
    public void deleteTask(int id) throws HuanException{
        if (id <= 0 || id > tasks.size()) {
            throw new HuanException("Invalid task number!");
        }
        Task removedTask = tasks.get(id - 1);
        tasks.remove(id - 1);
        System.out.println(space + line);
        System.out.println(space + "Noted. I've removed this task:");
        System.out.println(space + "  " + removedTask);
        System.out.println(space + "Now you have " + tasks.size() + " task(s) in the list");
        System.out.println(space + line);
    }

    /**
     * Parse the user input to determine the input type.
     *
     * @param input Input string.
     * @return The corresponding InputType enum.
     */
    public InputType parseInput(String input) {
        if (input == null || input.isEmpty()) {
            return InputType.INVALID;
        }
        String command = input.split(" ", 2)[0];
        try {
            return InputType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return InputType.INVALID;
        }
    }

    public void loadTasks() {
        try {
            File file = new File(FILE_PATH);
            //Create parent directory if missing
            file.getParentFile().mkdirs();

            //Create file if missing
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String task = scanner.nextLine().trim();
                String[] parts = task.split("\\|");
                //Check if format of task is correct
                if (parts.length < 3) {
                    System.out.println("Incorrect task format: " + task);
                    continue;
                }

                String type = parts[0].trim();
                String done = parts[1].trim();
                String description = parts[2].trim();

                switch (type) {
                case "T":
                    addTodo(description);
                    break;
                case "D":
                    if (parts.length != 4) {
                        System.out.println("Incorrect deadline format: " + task);
                        continue;
                    }
                    String by = parts[3].trim();
                    addDeadline(description, by);
                    break;
                case "E":
                    if (parts.length != 4) {
                        System.out.println("Incorrect event format: " + task);
                        continue;
                    }
                    String time = parts[3].trim();
                    String[] timeParts = time.split("-");
                    if (timeParts.length != 2) {
                        System.out.println("Incorrect event time format: " + task);
                    }
                    String from = timeParts[0];
                    String to = timeParts[1];
                    addEvent(description, from, to);
                    break;
                default:
                    System.out.println("Invalid Task: " + task);
                    continue;
                }

                if (done.equals("1")) {
                    Task newTask = tasks.get(tasks.size() - 1);
                    newTask.markAsDone();
                }

            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Huan bot = new Huan();
        bot.run();
    }
}