package org.example.model;

import java.util.List;

public class IfGoto implements Command {
    private final String label;

    public IfGoto(String label) {
        this.label = label;
    }

    public static Command generate(String arg0) {
        if (!LABEL_OR_FUNCTION_PATTERN.asMatchPredicate().test(arg0)) {
            throw new IllegalArgumentException("标签命名不合法，请检查");
        }
        return new IfGoto(arg0);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public List<String> toAsm() {
        return List.of("@SP", "AM=M-1", "D=M", "@" + label, "D;JNE");
    }

}
