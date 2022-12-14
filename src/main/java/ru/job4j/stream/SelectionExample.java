package ru.job4j.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionExample {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("1", "2", "3", "4", "5");
        List<String> rsl = strings
                .stream()
                .skip(2)
                .collect(Collectors.toList());
        System.out.println(rsl);
    }
}