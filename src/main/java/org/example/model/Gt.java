package org.example.model;

import java.util.List;

public class Gt implements Command  {

    @Override
    public List<String> toAsm() {
        return List.of(
            "@SP",
            "A=M",
            "A=A-1",
            "D=M",
            "A=A-1",
            "D=M-D",
            "@GT_IF_GT_{GT_ID}",
            "D;JGT",
            "@SP",
            "A=M",
            "A=A-1",
            "A=A-1",
            "M=0",
            "@GT_IF_END_{GT_ID}",
            "0;JMP",
            "(GT_IF_GT_{GT_ID})",
            "@SP",
            "A=M",
            "A=A-1",
            "A=A-1",
            "M=-1",
            "(GT_IF_END_{GT_ID})",
            "@SP",
            "M=M-1"
        );
    }
}
