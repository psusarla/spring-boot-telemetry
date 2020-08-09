package com.phani.spring.cloudcontracts;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BooksController {
    @Autowired
    private MeterRegistry meterRegistry;

    private Counter booksCounter;
    @PostConstruct
    public void initialize() {
        booksCounter = meterRegistry.counter("books_count");
    }

    Map<Integer, Book> bookMap = new HashMap<>();
    int counter = 0;

    @GetMapping("/book/{id}")
    public Book getBook(@PathVariable int id) {
        return bookMap.get(id);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String name) {
        List<Book> bookList = findByName(name);
        Gauge.builder("books.list.size", bookList, List::size).register(meterRegistry);
        return ResponseEntity.ok(bookList);
    }

    private List<Book> findByName(String name) {
        List<Book> bookList = bookMap.values()
                .stream()
                .filter( book-> book.getName().contains(name))
                .collect(Collectors.toList());
        return bookList;
    }

    @PostMapping("/book")
    @Timed("save_book")
    public Book saveBook(@RequestBody Book book) {
        book.setId(++counter);
        bookMap.put(book.getId(), book);
        booksCounter.increment();
        return book;
    }

    @PostConstruct
    public void init() {
        saveBook(Book.builder().name("The Hunger Games").author("Suzanne Collins").build());
        saveBook(Book.builder().name("To Kill a Mockingbird").author("Harper Lee").build());
        saveBook(Book.builder().name("Harry Potter and the Order of the Phoenix").author("J.K. Rowling").build());
    }


}
