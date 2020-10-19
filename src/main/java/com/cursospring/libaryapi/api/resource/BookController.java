package com.cursospring.libaryapi.api.resource;

import com.cursospring.libaryapi.api.dto.BookDTO;
import com.cursospring.libaryapi.api.exception.ApiErrors;
import com.cursospring.libaryapi.model.entiti.Book;
import com.cursospring.libaryapi.exception.BusinessException;
import com.cursospring.libaryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindResult = e.getBindingResult();
        return new ApiErrors(bindResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ApiErrors handleBusinessException(BusinessException ex) {
        return new ApiErrors(ex);
    }
}
