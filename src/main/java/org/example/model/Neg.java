package org.example.model;

import java.util.List;

public class Neg implements Command {
    @Override
    public List<String> toAsm() {
        return List.of("@SP", "A=M", "A=A-1", "M=-M");
    }
}
