import java.util.Scanner;

public class Huan {
    private static String line = "____________________________________________________________";
    private static String space = "    ";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String greeting =  space + "Hello! I'm HUAN \n"
                + space + "What can I do for you?";
        String exit = space + "Bye. Hope to see you again soon!";
        System.out.println(space + line);
        System.out.println(greeting);
        System.out.println(space + line);
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println(space + line);
                System.out.println(exit);
                System.out.println(space + line);
                break;
            }
            System.out.println(space + line);
            System.out.println(space + input);
            System.out.println(space + line);
        }

        scanner.close();
    }
}