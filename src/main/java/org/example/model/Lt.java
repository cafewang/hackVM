package org.example.model;

import java.util.List;

public class Lt implements Command {
    @Override
    public List<String> toAsm() {
        return List.of(
            "@SP",
            "A=M",
            "A=A-1",
            "D=M",
            "A=A-1",
            "D=M-D",
            "@LT_IF_LT_{LT_ID}",
            "D;JLT",
            "@SP",
            "A=M",
            "A=A-1",
            "A=A-1",
            "M=0",
            "@LT_IF_END_{LT_ID}",
            "0;JMP",
            "(LT_IF_LT_{LT_ID})",
            "@SP",
            "A=M",
            "A=A-1",
            "A=A-1",
            "M=-1",
            "(LT_IF_END_{LT_ID})",
            "@SP",
            "M=M-1"
        );
    }
}
