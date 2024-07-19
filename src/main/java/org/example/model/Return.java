package org.example.model;

import java.util.List;

public class Return implements Command {

    @Override
    public List<String> toAsm() {
        return List.of(
                "@LCL",
                "D=M // D=local base",
                "@5",
                "A=D-A // A=local base - 5(return address base)",
                "D=M // D=return address",
                "@R13",
                "M=D // R13=return address",
                "@SP",
                "AM=M-1",
                "D=M // D=return value",
                "@ARG",
                "A=M",
                "M=D // arg[0]=return value",
                "D=A+1",
                "@SP",
                "M=D // SP base=arg base + 1",
                "@LCL",
                "A=M-1",
                "D=M // D=old THAT",
                "@THAT",
                "M=D // THAT=old THAT",
                "@2",
                "D=A",
                "@LCL",
                "A=M-D",
                "D=M // D=old THIS",
                "@THIS",
                "M=D // THIS=old THIS",

                "@3",
                "D=A",
                "@LCL",
                "A=M-D",
                "D=M // D=old ARG",
                "@ARG",
                "M=D // ARG=old ARG",

                "@4",
                "D=A",
                "@LCL",
                "A=M-D",
                "D=M // D=old LCL",
                "@LCL",
                "M=D // LCL=old LCL",

                "@R13",
                "A=M // A=return address",
                "0;JMP // jump to return address"
        );
    }

}
