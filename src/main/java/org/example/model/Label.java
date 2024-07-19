package org.example.model;

import java.util.List;

public class Label implements Command {
    private final String label;

    public Label(String label) {
        this.label = label;
    }

    public static Command generate(String arg0) {
        if (!LABEL_OR_FUNCTION_PATTERN.asMatchPredicate().test(arg0)) {
            throw new IllegalArgumentException("标签命名不合法，请检查");
        }
        return new Label(arg0);
    }


    @Override
    public List<String> toAsm() {
        return List.of(String.format("(%s)", label));
    }

    public String getLabel() {
        return label;
    }
}
