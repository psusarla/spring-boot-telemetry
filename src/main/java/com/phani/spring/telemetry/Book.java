package com.phani.spring.telemetry;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    private int id;
    private String name;
    private String author;
}
