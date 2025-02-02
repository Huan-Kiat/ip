package huan.ui;

import huan.tasks.TaskList;
import java.util.Scanner;

public class Ui {
    private Scanner scanner;
    public static final String LINE = "____________________________________________________________";
    public static final String SPACE = "    ";


    public void showLine() {
        System.out.println(SPACE + LINE);
    }

    /**
     * Prints the greeting.
     */
    public void printGreeting() {
        String greeting =  "Hello! I'm HUAN \n"
                + SPACE + "What can I do for you?";
        printFormat(greeting);
    }

    /**
     * Prints the exit.
     */
    public void printExit() {
        String exit = "Bye. Hope to see you again soon!";
        printFormat(exit);
    }

    /**
     * Format the output string.
     * @param message the message to be formatted.
     */
    public static void printFormat(String message) {
        System.out.println(SPACE + LINE);
        System.out.println(SPACE + message);
        System.out.println(SPACE + LINE);
    }

    /**
     * Initialises the scanner.
     * @return returns the input command without whitespace.
     */
    public String readInput() {
        this.scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }

    public void closeScanner() {
        this.scanner.close();
    }

    /**
     * Prints the list of tasks.
     */
    public void showTaskList(TaskList tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println(SPACE + "No tasks added yet.");
        } else {
            System.out.println(SPACE + "Here are the tasks in your list:");
            for (int i = 0; i < tasks.getSize(); i++) {
                System.out.println(SPACE + (i + 1) + ". " + tasks.getTask(i).toString());
            }
        }
        showLine();
    }

    public void showLoadingError() {
        printFormat("Error: Unable to load tasks from file!");
    }
}
