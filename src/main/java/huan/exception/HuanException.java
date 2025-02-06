package huan.exception;

/**
 *  Represents a custom exception for Huan bot
 */
public class HuanException extends Exception {
    /**
     * Constructs a new HuanException with the specified message.
     *
     * @param description The exception message describing the error.
     */
    public HuanException(String description) {
        super(description);
    }

    /**
     * Constructs a new HuanException with no message.
     */
    public HuanException() {
        super();
    }

}
