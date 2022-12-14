package ru.job4j.lombok;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder(builderMethodName = "of")
public class Permission {
    private int id;
    private String name;
    @Singular("accessBy")
    private List<String> rules;
}

