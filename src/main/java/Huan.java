import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, INVALID, ON
    }

    /**
     * Activate the bot
     */
    public Huan() {
        loadTasks();
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
                case ON:
                    String[] onParts = input.split(" ", 2);
                    if (onParts.length < 2) {
                        throw new HuanException("Follow format: on yyyy-MM-dd");
                    }
                    onDate(onParts[1].trim());
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
            } catch (DateTimeParseException e){
                System.out.println(space + line);
                System.out.println(space + "Error: Ensure you follow the format (yyyy-mm-dd HHmm)");
                System.out.println(space + line);
            } catch (Exception e) {
                System.out.println(space + line);
                System.out.println(space + "Error: OOPS! Something went wrong!");
                System.out.println(space + line);
            }
        }
    }

    /**
     * Parses user input and adds task to the arraylist.
     *
     * @param input of user.
     * @throws HuanException for invalid formats.
     */
    public void parseDeadline(String input) throws HuanException{
        String[] deadlineParts = input.split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].substring(9).trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
            throw new HuanException("Follow format \"deadline (description) /by (yyyy-MM-dd HHmm)\"");
        }
        String deadlineDescription =  deadlineParts[0].substring(9).trim();
        String by = deadlineParts[1].trim();
        addDeadline(deadlineDescription, by);
    }

    /**
     * Parses user input and adds task to the arraylist.
     *
     * @param input of user.
     * @throws HuanException for invalid formats.
     */
    public void parseToDo(String input) throws HuanException{
        if (input.length() == 4) {
            throw new HuanException("todo description cannot be empty!");
        }
        String todoDescription = input.substring(5).trim();
        addTodo(todoDescription);
    }

    /**
     * Parses user input and adds task to the arraylist.
     *
     * @param input of user.
     * @throws HuanException for invalid formats.
     */
    public void parseEvent(String input) throws HuanException {
        String[] fromParts = input.split("/from", 2);
        if (fromParts.length < 2 || fromParts[0].substring(6).trim().isEmpty()) {
            throw new HuanException("Follow format \"event (description) " +
                    "/from (yyyy-MM-dd HHmm) " +
                    "/to (yyyy-MM-dd HHmm)\"");
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
    public void printGreeting() {
        String greeting =  space + "Hello! I'm HUAN \n"
                + space + "What can I do for you?";
        System.out.println(space + line);
        System.out.println(greeting);
        System.out.println(space + line);
    }

    /**
     * Prints the exit .
     */
    public void printExit() {
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
    public void markTask(int id) throws HuanException {
        if (id <= 0 || id > tasks.size()) {
            throw new HuanException("Invalid task number!");
        }
        Task t = tasks.get(id - 1);
        t.markAsDone();
        System.out.println(space + line);
        System.out.println(space + "Nice! I've marked this task as done:");
        System.out.println(space + "  " + t);
        System.out.println(space + line);
        writeTasks();
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
        writeTasks();
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
        writeTasks();
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
    public void deleteTask(int id) throws HuanException {
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

    /**
     * This method does several things:
     *  Ensure that parent directory exists, create it if it does not exist
     *  Create file if it does not exist
     *  Read each line and parses the input
     */
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
                    tasks.add(new Todo(description));
                    break;
                case "D":
                    if (parts.length != 4) {
                        System.out.println("Incorrect deadline format: " + task);
                        continue;
                    }
                    String by = parts[3].trim();
                    tasks.add(new Deadline(description, by));
                    break;
                case "E":
                    if (parts.length != 4) {
                        System.out.println("Incorrect event format: " + task);
                        continue;
                    }
                    String time = parts[3].trim();
                    String[] timeParts = time.split(" to ", 2);
                    if (timeParts.length != 2) {
                        System.out.println("Incorrect event time format: " + task);
                    }
                    String from = timeParts[0];
                    String to = timeParts[1];
                    tasks.add(new Event(description, from, to));
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

    /**
     * This method is responsible for writing user input into the file whenever:
     *  A task is added
     *  A task is marked
     */
    public void writeTasks() {
        try {
            File file = new File(FILE_PATH);
            FileWriter writer = new FileWriter(file);

            for (Task task : tasks) {
                if (task instanceof Todo t) {
                    // T | 1 | read book
                    writer.write("T | " + (t.isDone ? "1" : "0") + " | " + t.description);
                } else if (task instanceof Deadline d) {
                    // D | 0 | return book | June 6th
                    writer.write("D | " + (d.isDone ? "1" : "0")
                            + " | " + d.description + " | "
                            + d.by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));
                } else if (task instanceof Event e) {
                    // E | 0 | project meeting | Aug 6th 2-4pm
                    writer.write("E | " + (e.isDone ? "1" : "0")
                            + " | " + e.description + " | "
                            + e.from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"))
                            + " to "
                            + e.to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));
                }
                writer.write(System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Check if there are any tasks that occur on the given date.
     *
     * @param date The date to check for tasks.
     * @throws HuanException when there is an error in input date format.
     */
    public void onDate(String date) throws HuanException {
        try {
            LocalDate target = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println(space + line);
            System.out.println(space + "Here are the task(s) on this date:");
            boolean validDate = false;
            for (Task task : tasks) {
                if (task instanceof Deadline d) {
                    if (d.by.toLocalDate().equals(target)) {
                        System.out.println(space + task);
                        validDate = true;
                    }
                } else if (task instanceof Event e) {
                    if(!e.from.toLocalDate().isAfter(target) && !e.to.toLocalDate().isBefore(target)) {
                        System.out.println(space + task);
                        validDate = true;
                    }
                }
            }

            if (!validDate) {
                System.out.println(space + "Phew! There are no tasks on this date!");
            }
            System.out.println(space + line);

        } catch (DateTimeParseException e) {
            throw new HuanException("Invalid date format! (yyyy-MM-dd)");
        }
    }


    public static void main(String[] args) {
        Huan bot = new Huan();
        bot.run();
    }
}