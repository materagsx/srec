package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.parser.ParseLocation;

/**
 * Represents a generic command recorded.
 *
 * @author Victor Tatai
 */
public interface Command {
    /**
     * The command name
     *
     * @return The name
     */
    String getName();

    /**
     * Runs this command.
     *
     * @param context The execution context
     * @throws com.github.srec.command.exception.CommandExecutionException The exception thrown in case there is an error
     * @return The flow for the next commands
     */
    CommandFlow run(ExecutionContext context) throws CommandExecutionException;
    /**
     * Gets the script tree node represented by this command. May be null in case this command was not parsed.
     *
     * @return The tree, null if this command was not parsed
     */
    ParseLocation getLocation();

    public static enum CommandFlow {
        NEXT,
        BREAK,
        RETURN,
        EXIT
    }
}
