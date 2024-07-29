package organization;

import commands.Command;
import db.DBReceiver;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class ScriptManager {
    /**
     * Field that links to a script file
     */
    private File script;
    /**
     * A field that refers to a HashMap with keys - command names and the possibility of calling them
     */
    private Map<String, Command> commands;
    /**
     * A field that refers to an object with implementations of all commands
     */
    private DBReceiver dbReceiver;

    public ScriptManager(File script, Map<String, Command> commands, DBReceiver dbReceiver) {
        this.script = script;
        this.commands = commands;
        this.dbReceiver = dbReceiver;
    }

    /**
     * A method that calls the commands specified in the array received from the method {@link ScriptManager#srciptToTokens()}
     */
    public void executeScript() {
        String[] tokens = srciptToTokens();
        for (int i = 0; i < tokens.length; i++) {
            try {
                Command command = commands.get(tokens[i]);
                if (tokens[i].equalsIgnoreCase("add") || tokens[i].equalsIgnoreCase("update") ||
                        tokens[i].equalsIgnoreCase("insert_at") || tokens[i].equalsIgnoreCase("remove_by_id") ||
                        tokens[i].equalsIgnoreCase("execute_script") || tokens[i].equalsIgnoreCase("remove_lower") ||
                        tokens[i].equalsIgnoreCase("remove_any_by_type") ||
                        tokens[i].equalsIgnoreCase("filter_greater_than_annual_turnover") || tokens[i].equalsIgnoreCase("clear") ||
                        tokens[i].equalsIgnoreCase("register")) {
                    dbReceiver.setCompositeCommand(Arrays.copyOfRange(tokens, i + 1, tokens.length));
                }
                boolean isTokenCommand = commands.containsKey(tokens[i]);
                if (!isTokenCommand) {
                    continue;
                }
                command.execute();
            } catch (NullPointerException e) {
                System.err.println("There is an mistake in the script");
            }
        }
    }

    /**
     * A method that reads a script file and converts it to a char array and return result of the {@link ScriptManager#getStrings(char[])}
     *
     * @return Command array
     */
    private String[] srciptToTokens() {
        char[] inputChar = new char[20480000];
        try (InputStreamReader reader = new FileReader(script)) {
            reader.read(inputChar);
            reader.close();
            return getStrings(inputChar);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return new String[0];
        } catch (IOException e) {
            System.out.println("IO exception");
            return new String[0];
        }
    }

    /**
     * Private method that turns an array of characters into an array of strings with individual commands
     *
     * @param inputChar An array of characters read from a file with a script method{@link ScriptManager#srciptToTokens()}
     * @return Command array
     */
    private static String[] getStrings(char[] inputChar) {
        StringBuilder charBuilder = new StringBuilder();
        for (int i = 0; i < inputChar.length; i++) {
            if (inputChar[i] == '\0') {
                break;
            }
            if (inputChar[i] == '\r') {
                charBuilder.append(" ");
                continue;
            }
            if (inputChar[i] == '\n') {

                continue;
            }
            charBuilder.append(inputChar[i]);
        }
        String result = String.valueOf(charBuilder);
        String[] tokens = result.split(" ");
        return tokens;
    }
}
