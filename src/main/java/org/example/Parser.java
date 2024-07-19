package org.example;

import com.google.common.collect.ImmutableMap;
import org.example.model.Command;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Parser {
    private static final Map<Integer, CommandHandler> HANDLER_MAP = ImmutableMap.of(
            1, new NoArgHandler(),
            2, new OneArgHandler(),
            3, new TwoArgHandler()
    );

    private final BufferedReader br;

    public Parser(BufferedReader br) {
        this.br = br;
    }

    List<Command> parse() {
        return br.lines().map(this::parseCommand)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Command parseCommand(String line) {
        int slashIdx = line.indexOf("//");
        String noComment = line.substring(0, slashIdx == -1 ? line.length() : slashIdx);
        if (noComment.isBlank()) {
            return null;
        }
        String stripped = noComment.strip();
        String[] splitArr = stripped.split("\\s+");
        return HANDLER_MAP.get(splitArr.length).handle(splitArr);
    }
}
