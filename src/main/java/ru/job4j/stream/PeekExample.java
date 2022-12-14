package ru.job4j.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PeekExample {
    public static void main(String[] args) {
        List<StringBuilder> names = Arrays.asList(
                new StringBuilder("Mischael"), new StringBuilder("Ivan"), new StringBuilder("Elena"));
        List<StringBuilder> editedNames = names.stream().peek((el) -> el.append(" (Student Job4j)")).sorted().collect(Collectors.toList());
        System.out.println(editedNames);
    }
}