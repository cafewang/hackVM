package org.example;

import com.google.common.collect.ImmutableMap;
import org.example.model.Command;
import org.example.model.Goto;
import org.example.model.IfGoto;
import org.example.model.Label;

import java.util.Map;
import java.util.function.Function;

public class OneArgHandler implements CommandHandler {
    private static final Map<String, Function<String, Command>> NAME_TO_GENERATOR = ImmutableMap.of(
            "label", Label::generate,
            "goto", Goto::generate,
            "if-goto", IfGoto::generate
    );

    @Override
    public Command handle(String[] splitArr) {
        String name = splitArr[0];
        String arg0 = splitArr[1];
        return NAME_TO_GENERATOR.get(name).apply(arg0);
    }
}
