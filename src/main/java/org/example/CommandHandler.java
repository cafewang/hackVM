package org.example;

import org.example.model.Command;

public interface CommandHandler {
    Command handle(String[] splitArr);
}
