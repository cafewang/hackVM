package org.example.model;

import java.util.List;

public class Goto implements Command {
    private final String label;

    public Goto(String label) {
        this.label = label;
    }

    public static Command generate(String arg0) {
        if (!LABEL_OR_FUNCTION_PATTERN.asMatchPredicate().test(arg0)) {
            throw new IllegalArgumentException("标签命名不合法，请检查");
        }
        return new Goto(arg0);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public List<String> toAsm() {
        return List.of("@" + label, "0;JMP");
    }

}
