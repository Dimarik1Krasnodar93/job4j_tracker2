package ru.job4j.lombok;

import lombok.*;

@Data
@EqualsAndHashCode()
@RequiredArgsConstructor
public class Category {
    @Getter
    @EqualsAndHashCode.Include private int id;
    @Getter
    @Setter
    private String name;
}
