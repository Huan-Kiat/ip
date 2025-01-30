package huan.parser;

import huan.exception.HuanException;
import huan.ui.Huan;
import huan.tasks.TaskList;

public class Parser {
    /**
     * Parse the user input to determine the input type.
     *
     * @param input Input string.
     * @return The corresponding InputType enum.
     */
    public static Huan.InputType parseInput(String input) {
        if (input == null || input.isEmpty()) {
            return Huan.InputType.INVALID;
        }
        String command = input.split(" ", 2)[0];
        try {
            return Huan.InputType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Huan.InputType.INVALID;
        }
    }

    /**
     * Parses user input and adds task to the arraylist.
     *
     * @param input of user.
     * @throws HuanException for invalid formats.
     */
    public static void parseDeadline(TaskList tasks, String input) throws HuanException{
        String[] deadlineParts = input.split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].substring(9).trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
            throw new HuanException("Follow format \"deadline (description) /by (yyyy-MM-dd HHmm)\"");
        }
        String deadlineDescription =  deadlineParts[0].substring(9).trim();
        String by = deadlineParts[1].trim();
        tasks.addDeadline(deadlineDescription, by);
    }

    /**
     * Parses user input and adds task to the arraylist.
     *
     * @param input of user.
     * @throws HuanException for invalid formats.
     */
    public static void parseToDo(TaskList tasks, String input) throws HuanException{
        if (input.length() == 4) {
            throw new HuanException("todo description cannot be empty!");
        }
        String todoDescription = input.substring(5).trim();
        tasks.addTodo(todoDescription);
    }

    /**
     * Parses user input and adds task to the arraylist.
     *
     * @param input of user.
     * @throws HuanException for invalid formats.
     */
    public static void parseEvent(TaskList tasks, String input) throws HuanException {
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
        tasks.addEvent(eventDescription, from, to);
    }

}
