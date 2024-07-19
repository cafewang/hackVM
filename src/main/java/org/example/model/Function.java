package org.example.model;

import java.util.List;

public class Function implements Command {
    public static Command generate(String arg0, String arg1) {
        int localVarCount = Integer.parseInt(arg1);
        if (localVarCount < 0) {
            throw new IllegalArgumentException("本地变量定义有误");
        }
        return new Function(arg0, localVarCount);
    }

    private final int localVarCount;
    private final String name;

    public Function(String name, int localVarCount) {
        this.name = name;
        this.localVarCount = localVarCount;
    }

    public String getName() {
        return name;
    }


    @Override
    public List<String> toAsm() {
        return List.of(String.format("(%s)", name),
                String.format("@%d", localVarCount),
                "D=A",
                String.format("(%s$CHECK_LOCAL_VAR_COUNT)", name),
                String.format("@%s$LOCAL_VAR_COUNT_ZERO", name),
                "D;JEQ",
                "@SP",
                "AM=M+1",
                "A=A-1",
                "M=0",
                "D=D-1",
                String.format("@%s$CHECK_LOCAL_VAR_COUNT", name),
                "0;JMP",
                String.format("(%s$LOCAL_VAR_COUNT_ZERO)", name),
                "0");
    }
}
