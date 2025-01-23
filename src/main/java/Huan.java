import java.util.Scanner;

public class Huan {
    public static String line = "____________________________________________________________";
    public static String space = "    ";
    private Task[] tasks = new Task[100];
    private static int taskId = 0;

    // Prints the greeting message
    public void printGreeting(){
        String greeting =  space + "Hello! I'm HUAN \n"
                + space + "What can I do for you?";
        System.out.println(space + line);
        System.out.println(greeting);
        System.out.println(space + line);
    }

    // Prints the exit message
    public void printExit(){
        String exit = space + "Bye. Hope to see you again soon!";
        System.out.println(space + line);
        System.out.println(exit);
        System.out.println(space + line);
    }

    // Adds task to the list
    public void addTask(String task) {
        tasks[taskId] = new Task(task);
        taskId++;

        System.out.println(space + line);
        System.out.println(space + "Got it. I've added this task:");
        System.out.println(space + "  " + task);
        System.out.println(space + line);
    }

    // Returns the task ID
    public int getTaskId() {
        return taskId;
    }

    // Prints the list of tasks
    public void getTasks() {
        System.out.println(space + line);
        System.out.println(space + "Here are the tasks in your list:");
        if (taskId == 0) {
            System.out.println(space + "No tasks added yet.");
        } else {
            for (int i = 0; i < taskId; i++) {
                System.out.println(space + (i + 1) + ". " + tasks[i].toString());
            }
        }
        System.out.println(space + line);
    }

    // Marks the task with X
    public void markTask(int id){
        Task t = tasks[id - 1];
        t.markAsDone();
        System.out.println(space + line);
        System.out.println(space + "Nice! I've marked this task as done:");
        System.out.println(space + "  " + t);
        System.out.println(space + line);
    }

    // Unmarks the task
    public void UnmarkTask(int id){
        Task t = tasks[id - 1];
        t.markAsUndone();
        System.out.println(space + line);
        System.out.println(space + "OK, I've marked this task as not done yet:");
        System.out.println(space + "  " + t);
        System.out.println(space + line);
    }

    // Add a Deadline
    public void addDeadline(String description, String by) {
        tasks[taskId] = new Deadline(description, by);
        taskId++;
        System.out.println(space + line);
        System.out.println(space + "Got it. I've added this task:");
        System.out.println(space + "  " + tasks[taskId - 1]);
        System.out.println(space + "Now you have " + taskId + " tasks in the list");
        System.out.println(space + line);
    }

    // Add an Event
    public void addEvent(String description, String from, String to) {
        tasks[taskId] = new Event(description, from, to);
        taskId++;
        System.out.println(space + line);
        System.out.println(space + "Got it. I've added this task:");
        System.out.println(space + "  " + tasks[taskId - 1]);
        System.out.println(space + "Now you have " + taskId + " tasks in the list");
        System.out.println(space + line);
    }

    // Add a Todo
    public void addTodo(String description) {
        tasks[taskId] = new Todo(description);
        taskId++;
        System.out.println(space + line);
        System.out.println(space + "Got it. I've added this task:");
        System.out.println(space + "  " + tasks[taskId - 1]);
        System.out.println(space + "Now you have " + taskId + " tasks in the list");
        System.out.println(space + line);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Huan bot = new Huan();
        bot.printGreeting();

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                bot.printExit();
                break;
            } else if (input.equals("list")) {
                bot.getTasks();
            } else if (input.startsWith("mark")) {
                String[] parts = input.split(" ");
                int id = Integer.parseInt(parts[1]);
                bot.markTask(id);
            } else if (input.startsWith("unmark")) {
                String[] parts = input.split(" ");
                int id = Integer.parseInt(parts[1]);
                bot.UnmarkTask(id);
                //TODO
                // make elseif statements for each of the tasks
            } else if (input.startsWith("todo")) {
                String description = input.substring(5).trim();
                bot.addTodo(description);
            } else if (input.startsWith("deadline")) {
                String[] parts = input.split("/by", 2);
                String description =  parts[0].substring(9).trim();
                String by = parts[1].trim();
                bot.addDeadline(description, by);
            } else if (input.startsWith("event")) {
                String[] fromPart = input.split("/from", 2);
                String description = fromPart[0].substring(6).trim();
                String toPart[] = fromPart[1].split("/to", 2);
                String from = toPart[0].trim();
                String to = toPart[1].trim();
                bot.addEvent(description, from, to);
            } else {
                System.out.println(space + line);
                System.out.println(space + "Invalid input!");
                System.out.println(space + line);
            }
        }

        scanner.close();
    }
}