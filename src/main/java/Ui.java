import java.util.Scanner;

public class Ui {
    public static String LINE = "____________________________________________________________";
    public static String SPACE = "    ";


    public void showLine() {
        System.out.println(SPACE + LINE);
    }

    /**
     * Prints the greeting.
     */
    public void printGreeting() {
        String greeting =  SPACE + "Hello! I'm HUAN \n"
                + SPACE + "What can I do for you?";
        printFormat(greeting);
    }

    /**
     * Prints the exit.
     */
    public void printExit() {
        String exit = SPACE + "Bye. Hope to see you again soon!";
        printFormat(exit);
    }

    /**
     * Format the output string.
     * @param message the message to be formatted.
     */
    public void printFormat(String message) {
        System.out.println(SPACE + LINE);
        System.out.println(message);
        System.out.println(SPACE + LINE);
    }

    /**
     * Initialises the scanner.
     * @return returns the input command without whitespace.
     */
    public String readInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }

    /**
     * Prints the list of tasks.
     */
    public void showTaskList() {
        TaskList tasks = new TaskList();
        System.out.println(SPACE + LINE);
        if (tasks.isEmpty()) {
            System.out.println(SPACE + "No tasks added yet.");
        } else {
            System.out.println(SPACE + "Here are the tasks in your list:");
            for (int i = 0; i < tasks.getSize(); i++) {
                System.out.println(SPACE + (i + 1) + ". " + tasks.getTask(i).toString());
            }
        }
        System.out.println(SPACE + LINE);
    }
}
