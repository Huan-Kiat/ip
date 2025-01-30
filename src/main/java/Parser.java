public class Parser {
    /**
     * Parse the user input to determine the input type.
     *
     * @param input Input string.
     * @return The corresponding InputType enum.
     */
    public Huan.InputType parseInput(String input) {
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
}
