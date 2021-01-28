package com.cursospring.libaryapi.model.repository;

import com.cursospring.libaryapi.model.entiti.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    Optional<Book> findByIsbn(String isbn);

}