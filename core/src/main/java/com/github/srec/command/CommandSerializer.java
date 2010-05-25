package com.github.srec.command;

import com.github.srec.command.parser.antlr.ScriptParser;
import com.github.srec.rec.EventReaderException;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.github.srec.Utils.quote;

/**
 * Class used to write and read events from a file. It follows the Ruby DSL script format.
 * 
 * @author Victor Tatai
 */
public class CommandSerializer {
    public static ExecutionContext load(File file) {
        ExecutionContext context;
        try {
            context = ExecutionContextFactory.getInstance().create(file, file.getParentFile().getCanonicalPath());
        } catch (IOException e) {
            throw new CommandSerializationException(e);
        }
        ScriptParser p = new ScriptParser();
        p.parse(context, file);
        return context;
    }

    public static void write(File file, List<Command> commands) throws IOException {
        Writer writer = new Writer(file);
        try {
            for (Command event : commands) {
                print(event, writer);
            }
        } finally {
            try { writer.flush(); }
            finally { writer.close(); }
        }
    }

    private static void print(Command command, Writer writer) {
        if (StringUtils.isBlank(command.getName())) return;
        if (command instanceof MethodCallEventCommand) print((MethodCallEventCommand) command, writer);
        else if (command instanceof MethodCallCommand) print((MethodCallCommand) command, writer);
        else if (command instanceof MethodScriptCommand) print((MethodScriptCommand) command, writer);
        else if (command instanceof MethodCommand && ((MethodCommand) command).isNative()) ;// Do nothing
        else throw new EventReaderException("Command cannot be serialized: " + command);
    }

    private static void print(MethodCallEventCommand event, Writer writer) {
        if (StringUtils.isBlank(event.getComponentLocator())) return;
        StringBuilder strb = new StringBuilder(event.getName()).append(" ").append(quote(event.getComponentLocator()));
        for (ValueCommand arg : event.getParameters()) {
            strb.append(", ").append(quote(arg.getName()));
        }
        writer.println(strb.toString());
    }

    private static void print(MethodCallCommand command, Writer writer) {
        StringBuilder strb = new StringBuilder(command.getName());
        for (ValueCommand arg : command.getParameters()) {
            strb.append(arg).append(", ");
        }
        String s = strb.toString();
        if (s.endsWith(", ")) s = s.substring(0, s.length() - 2);
        writer.println(s);
    }

    private static void print(MethodScriptCommand command, Writer writer) {
        StringBuilder strb = new StringBuilder("def ").append(command.getName()).append("(");
        for (MethodCommand.Parameter arg : command.getParameters()) {
            strb.append(arg.getName()).append(", ");
        }
        String s = strb.toString();
        if (s.endsWith(", ")) strb.delete(s.length() - 2, s.length());
        strb.append(")");
        writer.println(strb.toString());
        writer.ident();
        for (Command recorderCommand : command.getCommands()) {
            print(recorderCommand, writer);
        }
        writer.decIdent();
        writer.println("end");
    }

    private static class Writer {
        private PrintWriter writer;
        private int ident;

        public Writer(File file) throws IOException {
            writer = new PrintWriter(new FileWriter(file));
        }

        public void println(String line) {
            writer.println(genIdent() + line);
        }

        private String genIdent() {
            StringBuilder strb = new StringBuilder();
            for (int i = 0; i < ident; i++) {
                strb.append(" ");
            }
            return strb.toString();
        }

        public void ident() {
            ident++;
        }

        public void decIdent() {
            ident--;
        }

        public void flush() {
            writer.flush();
        }

        public void close() {
            writer.close();
        }
    }
}