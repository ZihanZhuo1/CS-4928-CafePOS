package com.cafepos.app.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class EventBus {
    private final Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();

    public <T> void on(Class<T> type, Consumer<T> handler) {
        handlers.computeIfAbsent(type, k -> new ArrayList<>()).add(handler);
    }

    @SuppressWarnings("unchecked")
    public <T> void emit(T event) {
        var list = handlers.getOrDefault(event.getClass(), List.of());
        for (var h : list) {
            ((Consumer<T>) h).accept(event);
        }
    }
}