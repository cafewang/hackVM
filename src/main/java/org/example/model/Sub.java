package org.example.model;

import java.util.List;

public class Sub implements Command {
    @Override
    public List<String> toAsm() {
        return List.of("@SP", "A=M", "A=A-1", "D=M", "A=A-1", "D=M-D", "M=D", "@SP", "M=M-1");
    }
}
