package com.phani.spring.cloudcontracts;

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
