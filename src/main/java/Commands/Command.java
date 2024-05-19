package Commands;

/**
 * Interface that every command must implement.
 */
public interface Command {
    /**
     * Command execution method
     */
    void execute();

    /**
     * Method returning description
     * @return Command description
     */
    String description();
}
