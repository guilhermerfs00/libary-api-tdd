package com.cursospring.libaryapi.model.repository;

import com.cursospring.libaryapi.model.entiti.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o Isbn cadastrado")
    public void returnTrueWhenIsbnExists() {

        String isbn = "123";

        Book book = Book.builder().isbn("123").author("Fulano").title("As aventuras").build();

        entityManager.persist(book);

        boolean exist = repository.existsByIsbn(isbn);

        Assertions.assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o Isbn cadastrado")
    public void returnFalseWhenIsbnDoesentExists() {

        String isbn = "1001010";

        boolean exist = repository.existsByIsbn(isbn);

        Assertions.assertThat(exist).isFalse();
    }
}
