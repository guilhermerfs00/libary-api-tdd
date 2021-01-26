package com.cursospring.libaryapi.api.resource;

import com.cursospring.libaryapi.api.dto.BookDTO;
import com.cursospring.libaryapi.api.dto.LoanDTO;
import com.cursospring.libaryapi.api.dto.LoanFilterDTO;
import com.cursospring.libaryapi.api.dto.ReturnedLoanDTO;
import com.cursospring.libaryapi.model.entiti.Book;
import com.cursospring.libaryapi.model.entiti.Loan;
import com.cursospring.libaryapi.service.BookService;
import com.cursospring.libaryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService service;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO dto) {
        Book book = bookService.getBookByIsBn(dto.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));

        Loan entity = Loan.builder().book(book).customer(dto.getCustomer()).loanDate(LocalDate.now()).build();

        entity = service.save(entity);
        return entity.getId();
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void returnedBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {
        Loan loan = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        loan.setReturned(dto.getReturned());

        service.update(loan);
    }

    @GetMapping
    public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageable) {
        Page<Loan> result = service.find(dto, pageable);

        List<LoanDTO> loans = result.getContent()
                .stream()
                .map(entity -> {
                    BookDTO bookDTO = modelMapper.map(entity.getBook(), BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;

                }).collect(toList());

        return new PageImpl<LoanDTO>(loans, pageable, result.getTotalElements());
    }
}
