package org.example.model;

import java.util.List;

public class Eq implements Command {


    @Override
    public List<String> toAsm() {
        return List.of(
                "@SP",
                "A=M",
                "A=A-1",
                "D=M",
                "A=A-1",
                "D=M-D",
                "@EQ_IF_ZERO_{EQ_ID}",
                "D;JEQ",
                "@SP",
                "A=M",
                "A=A-1",
                "A=A-1",
                "M=0",
                "@EQ_IF_END_{EQ_ID}",
                "0;JMP",
                "(EQ_IF_ZERO_{EQ_ID})",
                "@SP",
                "A=M",
                "A=A-1",
                "A=A-1",
                "M=-1",
                "(EQ_IF_END_{EQ_ID})",
                "@SP",
                "M=M-1");
    }
}
