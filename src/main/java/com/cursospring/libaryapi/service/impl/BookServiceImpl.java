package com.cursospring.libaryapi.service.impl;

import com.cursospring.libaryapi.model.entiti.Book;
import com.cursospring.libaryapi.model.repository.BookRepository;
import com.cursospring.libaryapi.exception.BusinessException;
import com.cursospring.libaryapi.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado");
        }
        return repository.save(book);
    }
}
