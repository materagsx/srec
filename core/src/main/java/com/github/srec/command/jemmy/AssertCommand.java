package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertCommand extends JemmyEventCommand {
    public AssertCommand() {
        super("assert", createParametersDefinition(LOCATOR, Type.STRING, "text", Type.STRING));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        textField(coerceToString(params.get(LOCATOR), ctx)).assertText(coerceToString(params.get("text"), ctx));
    }
}