import java.util.Scanner;

public class Huan {
    public static String line = "____________________________________________________________";
    public static String space = "    ";
    private String[] tasks = new String[100];
    private static int taskId = 0;

    public void printGreeting(){
        String greeting =  space + "Hello! I'm HUAN \n"
                + space + "What can I do for you?";
        System.out.println(space + line);
        System.out.println(greeting);
        System.out.println(space + line);
    }

    public void printExit(){
        String exit = space + "Bye. Hope to see you again soon!";
        System.out.println(space + line);
        System.out.println(exit);
        System.out.println(space + line);
    }

    public void printInput(String input){
        System.out.println(space + line);
        System.out.println(space + input);
        System.out.println(space + line);
    }

    public void addTask(String task) {
        tasks[taskId] = task;
        taskId++;
    }

    public int getTaskId() {
        return taskId;
    }

    public void getTasks() {
        System.out.println(space + line);
        if (taskId == 0) {
            System.out.println(space + "No tasks added yet.");
        } else {
            for (int i = 0; i < taskId; i++) {
                System.out.println(space + (i + 1) + ". " + tasks[i]);
            }
        }
        System.out.println(space + line);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Huan bot = new Huan();
        bot.printGreeting();

        while (true) {
            String input = scanner.nextLine();


            if (input.equals("bye")) {
                bot.printExit();
                break;
            } else if (input.equals("list")) {
                bot.getTasks();
            } else if (!input.isEmpty()) {
                bot.addTask(input);
                bot.printInput("added: " + input);
            }
        }

        scanner.close();
    }
}