package com.cursospring.libaryapi.service;

import com.cursospring.libaryapi.model.entiti.Book;
import com.cursospring.libaryapi.model.repository.BookRepository;
import com.cursospring.libaryapi.exception.BusinessException;
import com.cursospring.libaryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }


    @Test
    @DisplayName("Deve salvar o Livro")
    public void saveBookTest() {
        Book book = createValidBook();
        Mockito.when(repository.save(book)).thenReturn(Book.builder()
                .id(1l)
                .isbn("123")
                .title("As aventuras")
                .author("Fulano")
                .build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");

    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar livro com Isbn já cadastrado")
    public void shouldNotSaveABookWithDuplicateISBN() {
        Book book = createValidBook();

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        Throwable throwable = Assertions.catchThrowable(() -> service.save(book));

        assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado");

        Mockito.verify(repository, Mockito.never()).save(book);
    }


    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }
}
