package org.example.model;

import java.util.List;

public class Call implements Command {
    private final int argCount;
    private final String name;

    public Call(String name, int argCount) {
        this.name = name;
        this.argCount = argCount;
    }

    public static Command generate(String arg0, String arg1) {
        if (!LABEL_OR_FUNCTION_PATTERN.asMatchPredicate().test(arg0)) {
            throw new IllegalArgumentException("函数命名不合法，请检查");
        }
        int argCount = Integer.parseInt(arg1);
        if (argCount < 0) {
            throw new IllegalArgumentException("参数数量有误，请检查");
        }
        return new Call(arg0, argCount);
    }

    @Override
    public List<String> toAsm() {
        return List.of(
                String.format("@%s$RETURN_ADDRESS_{RETURN_ADDRESS_ID}", name),
                "D=A",
                "@SP",
                "AM=M+1",
                "A=A-1",
                "M=D // top=return address",
                "@LCL",
                "D=M // D=local base",
                "@SP",
                "AM=M+1",
                "A=A-1",
                "M=D // top=local base",
                "@ARG",
                "D=M // D=arg base",
                "@SP",
                "AM=M+1",
                "A=A-1",
                "M=D // top=arg base",
                "@THIS",
                "D=M // D=this base",
                "@SP",
                "AM=M+1",
                "A=A-1",
                "M=D // top=this base",
                "@THAT",
                "D=M // D=that base",
                "@SP",
                "AM=M+1",
                "A=A-1",
                "M=D // top=that base",
                String.format("@%d", argCount),
                "D=A",
                "@5",
                "D=D+A // D=argCount + 5",
                "@SP",
                "D=M-D // D=stack base - argCount - 5",
                "@ARG",
                "M=D // arg base=stack base - argCount - 5",
                "@SP",
                "D=M",
                "@LCL",
                "M=D // local base=stack base",
                String.format("@%s", name),
                "0;JMP // jump to function entry",
                String.format("(%s$RETURN_ADDRESS_{RETURN_ADDRESS_ID})", name),
                "0"
        );
    }
}
