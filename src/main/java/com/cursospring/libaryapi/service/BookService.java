package com.cursospring.libaryapi.service;

import com.cursospring.libaryapi.model.entiti.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);

    void delete(Book book);

    Book update(Book book);
}
