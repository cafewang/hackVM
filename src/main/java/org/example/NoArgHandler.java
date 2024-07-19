package org.example;

import com.google.common.collect.ImmutableMap;
import org.example.model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class NoArgHandler implements CommandHandler {
    private static final Map<String, Class<? extends Command>> TYPE_MAP = ImmutableMap.of(
            "add", Add.class,
            "sub", Sub.class,
            "neg", Neg.class,
            "eq", Eq.class,
            "gt", Gt.class,
            "lt", Lt.class,
            "and", And.class,
            "or", Or.class,
            "not", Not.class,
            "return", Return.class
    );

    @Override
    public Command handle(String[] splitArr) {
        Class<? extends Command> type = TYPE_MAP.get(splitArr[0]);
        if (type == null) {
            throw new IllegalArgumentException("命令无效请检查");
        }
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
