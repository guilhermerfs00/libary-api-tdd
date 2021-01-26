package com.cursospring.libaryapi.service;

import com.cursospring.libaryapi.api.dto.LoanFilterDTO;
import com.cursospring.libaryapi.exception.BusinessException;
import com.cursospring.libaryapi.model.entiti.Book;
import com.cursospring.libaryapi.model.entiti.Loan;
import com.cursospring.libaryapi.model.repository.LoanRepository;
import com.cursospring.libaryapi.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    private LoanService service;

    @MockBean
    private LoanRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void saveLoanTest() {

        var book = Book.builder().id(1l).build();
        String custumer = "Fulano";

        var savingLoan = Loan.builder()
                .book(book)
                .customer(custumer)
                .loanDate(LocalDate.now())
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
        var savedLoan = Loan.builder().id(1l).loanDate(LocalDate.now()).customer(custumer).book(book).build();
        when(repository.save(savingLoan)).thenReturn(savedLoan);
        var loan = service.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook()).isEqualTo(savedLoan.getBook());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());

    }

    @Test
    @DisplayName("Deve lançar um erro de negocio ao salvar um livro já emprestado")
    public void loanedBookSaveTest() {

        var book = Book.builder().id(1l).build();
        String custumer = "Fulano";

        var savingLoan = Loan.builder()
                .book(book)
                .customer(custumer)
                .loanDate(LocalDate.now())
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(true);

        var exception = catchThrowable(() -> service.save(savingLoan));

        assertThat(exception).isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned");

        verify(repository, never()).save(savingLoan);

    }

    @Test
    @DisplayName("Deve obter as informações de emprestimo de ID")
    public void getLoanDetailsTest() {
        Long id = 1l;
        Loan loan = createLoan();
        loan.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(loan));

        var result = service.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar emprestimo")
    public void updateLoanTest() {
        Long id = 1l;
        Loan loan = createLoan();
        loan.setId(id);
        loan.setReturned(true);
        when(repository.save(loan)).thenReturn(loan);

        Loan updatedLoan = service.update(loan);

        verify(repository).save(loan);
    }

    @Test
    @DisplayName("Deve filtrar emprestimos pelas propriedades")
    public void findLoanTest() {

        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("Fulano").isbn("321").build();

        Loan loan = createLoan();
        loan.setId(1l);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> lista = Arrays.asList(loan);
        Page<Loan> page = new PageImpl<Loan>(lista, pageRequest, lista.size());

        when(repository.findByBookIsbnOrCustomer(anyString(), anyString(), any(Pageable.class))).thenReturn(page);

        Page<Loan> result = service.find(loanFilterDTO, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

    public static Loan createLoan() {
        Book book = Book.builder().id(1l).build();
        String custumer = "Fulano";

        return Loan.builder()
                .book(book)
                .customer(custumer)
                .loanDate(LocalDate.now())
                .build();
    }
}
