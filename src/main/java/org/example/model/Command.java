package org.example.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Command {
    Pattern LABEL_OR_FUNCTION_PATTERN = Pattern.compile("[a-zA-Z_.:][0-9a-zA-Z_.:]*");
    AtomicInteger GT_ID = new AtomicInteger(0),
            LT_ID = new AtomicInteger(0),
            EQ_ID = new AtomicInteger(0),
            RETURN_ADDRESS_ID = new AtomicInteger(0);

    static List<Command> handleScope(List<Command> commands) {
        String currentFunction = null;
        List<Command> result = new ArrayList<>();
        for (Command command : commands) {
            Command newCommand = command;
            if (command instanceof Function) {
                currentFunction = ((Function) command).getName();
            } else if (command instanceof Goto) {
                String label = ((Goto) command).getLabel();
                newCommand = currentFunction != null ? new Goto(currentFunction + "$" + label) : command;
            } else if (command instanceof IfGoto) {
                String label = ((IfGoto) command).getLabel();
                newCommand = currentFunction != null ? new IfGoto(currentFunction + "$" + label) : command;
            } else if (command instanceof Label) {
                String label = ((Label) command).getLabel();
                newCommand = currentFunction != null ? new Label(currentFunction + "$" + label) : command;
            }

            result.add(newCommand);
        }
        return result;
    }

    List<String> toAsm();

    static Pair<Command, List<String>> postProcess(Pair<Command, List<String>> pair, String fileName) {
        List<String> result = pair.getRight();
        Command command = pair.getLeft();
        if (command instanceof Eq) {
            String eqID = String.valueOf(EQ_ID.getAndIncrement());
            result = pair.getRight().stream().map(str -> str.replace("{EQ_ID}", eqID))
                    .collect(Collectors.toList());
        } else if (command instanceof Gt) {
            String gtID = String.valueOf(GT_ID.getAndIncrement());
            result = pair.getRight().stream().map(str -> str.replace("{GT_ID}", gtID))
                    .collect(Collectors.toList());
        } else if (command instanceof Lt) {
            String ltID = String.valueOf(LT_ID.getAndIncrement());
            result = pair.getRight().stream().map(str -> str.replace("{LT_ID}", ltID))
                    .collect(Collectors.toList());
        } else if (command instanceof Push) {
            Push push = (Push)command;
            if (push.getSegment().equals("static")) {
                result = pair.getRight().stream().map(str -> str.replace("{FILENAME}", fileName))
                        .collect(Collectors.toList());
            }
        } else if (command instanceof Pop) {
            Pop pop = (Pop)command;
            if (pop.getSegment().equals("static")) {
                result = pair.getRight().stream().map(str -> str.replace("{FILENAME}", fileName))
                        .collect(Collectors.toList());
            }
        } else if (command instanceof Call) {
            String returnAddressID = String.valueOf(RETURN_ADDRESS_ID.getAndIncrement());
            result = pair.getRight().stream().map(str -> str.replace("{RETURN_ADDRESS_ID}", returnAddressID))
                    .collect(Collectors.toList());
        }
        return Pair.of(pair.getLeft(), result);
    }
}
