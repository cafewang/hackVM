package org.example;

import org.apache.commons.lang3.tuple.Pair;
import org.example.model.Call;
import org.example.model.Command;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("请输入文件路径");
        }

        String path = args[0];
        if (path.isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在");
        }
        String destFileName;
        List<File> files;
        if (file.isDirectory()) {
            destFileName = file.getName();
            files = Arrays.stream(Objects.requireNonNull(file.listFiles((dir, name) -> name.endsWith(".vm"))))
                    .collect(Collectors.toList());
        } else {
            files = Collections.singletonList(file);
            destFileName = file.getName().split("\\.")[0];
        }
        List<Pair<String, Command>> fileNameAndCommandList = new ArrayList<>();
        for (File srcFile : files) {
            String fileName = srcFile.getName().split("\\.")[0];
            try (BufferedReader br = new BufferedReader(new FileReader(srcFile))) {
                Parser parser = new Parser(br);
                List<Command> commands = parser.parse();
                List<Command> scopedCommands = Command.handleScope(commands);
                fileNameAndCommandList.addAll(scopedCommands.stream()
                        .map(item -> Pair.of(fileName, item)).collect(Collectors.toList()));
            } catch (IOException e) {
                throw new IllegalArgumentException("文件读写失败");
            }
        }
        fileNameAndCommandList.add(0, Pair.of("", new Call("Sys.init", 0)));
        List<String> instructions = fileNameAndCommandList.stream().map(pair -> Pair.of(pair, pair.getRight().toAsm()))
                .map(tuple -> Command.postProcess(Pair.of(tuple.getLeft().getRight(), tuple.getRight()), tuple.getLeft().getLeft()))
                .map(Pair::getRight).flatMap(Collection::stream).collect(Collectors.toList());
        List<String> bootstrapped = setSP(instructions);
        String lines = String.join("\r\n", bootstrapped);
        File writeTo = new File((file.isDirectory() ? file.getAbsolutePath() : file.getParent()) + "/" + destFileName + ".asm");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(writeTo))) {
            bw.write(lines);
            bw.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException("写入文件失败");
        }
    }

    private static List<String> setSP(List<String> instructions) {
        instructions.addAll(0, List.of("@256", "D=A", "@SP", "M=D"));
        return instructions;
    }
}