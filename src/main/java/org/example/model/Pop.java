package org.example.model;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class Pop implements Command {
    private static final Map<String, Predicate<Integer>> SEGMENT_VALIDATOR = ImmutableMap.of(
            "constant", (idx) -> 0 <= idx && idx <=32767,
            "argument", (idx) -> true,
            "local", (idx) -> true,
            "pointer", (idx) -> idx == 0 || idx == 1,
            "this", (idx) -> true,
            "that", (idx) -> true,
            "temp", (idx) -> 0 <= idx && idx <= 7,
            "static", (idx) -> true
    );
    private static final Map<String, Function<Integer, List<String>>> SEGMENT_ASSEMBLER = ImmutableMap.of(
            "constant", (idx) -> List.of("@SP", "M=M-1"),
            "argument", (idx) ->  List.of(String.format("@%d", idx), "D=A", "@ARG", "A=M", "D=D+A",
                    "@SP", "A=M", "M=D", "@SP", "A=M-1", "D=M", "@SP", "A=M", "A=M", "M=D", "@SP", "M=M-1"),
            "local", (idx) -> List.of(String.format("@%d", idx), "D=A", "@LCL", "A=M", "D=D+A",
                    "@SP", "A=M", "M=D", "@SP", "A=M-1", "D=M", "@SP", "A=M", "A=M", "M=D", "@SP", "M=M-1"),
            "pointer", (idx) -> List.of("@SP", "AM=M-1", "D=M", (idx == 0 ? "@THIS" : "@THAT"), "M=D"),
            "this", (idx) -> List.of(String.format("@%d", idx), "D=A", "@THIS", "A=M", "D=D+A",
                    "@SP", "A=M", "M=D", "@SP", "A=M-1", "D=M", "@SP", "A=M", "A=M", "M=D", "@SP", "M=M-1"),
            "that", (idx) -> List.of(String.format("@%d", idx), "D=A", "@THAT", "A=M", "D=D+A",
                    "@SP", "A=M", "M=D", "@SP", "A=M-1", "D=M", "@SP", "A=M", "A=M", "M=D", "@SP", "M=M-1"),
            "temp", (idx) -> List.of("@SP", "AM=M-1", "D=M", String.format("@%d", idx + 5), "M=D"),
            "static", (idx) -> List.of("@SP", "AM=M-1", "D=M", String.format("@{FILENAME}.%d", idx), "M=D")
    );

    private final String segment;
    private final int index;

    public Pop(String segment, int index) {
        this.segment = segment;
        this.index = index;
    }

    public static Command generate(String arg0, String arg1) {
        int index = Integer.parseInt(arg1);
        Predicate<Integer> validator = SEGMENT_VALIDATOR.get(arg0);
        if (validator == null || !validator.test(index)) {
            throw new IllegalArgumentException("pop命令参数不合法，请检查");
        }
        return new Pop(arg0, index);
    }

    @Override
    public List<String> toAsm() {
        return SEGMENT_ASSEMBLER.get(segment).apply(index);
    }

    public String getSegment() {
        return segment;
    }
}
