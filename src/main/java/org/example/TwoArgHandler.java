package org.example;

import com.google.common.collect.ImmutableMap;
import org.example.model.*;

import java.util.Map;
import java.util.function.BiFunction;

public class TwoArgHandler implements CommandHandler {
    private static Map<String, BiFunction<String, String, Command>> NAME_TO_GENERATOR = ImmutableMap.of(
            "push", Push::generate,
            "pop", Pop::generate,
            "function", Function::generate,
            "call", Call::generate
    );

    @Override
    public Command handle(String[] splitArr) {
        String name = splitArr[0];
        String arg0 = splitArr[1];
        String arg1 = splitArr[2];
        return NAME_TO_GENERATOR.get(name).apply(arg0, arg1);
    }
}
