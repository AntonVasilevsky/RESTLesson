package com.anton.rest.controllers;

import com.anton.rest.models.Person;
import com.anton.rest.services.PeopleService;
import com.anton.rest.util.PersonErrorResponse;
import com.anton.rest.util.PersonNotCreatedException;
import com.anton.rest.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")

public class PeopleController {

    private final PeopleService peopleService;

    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }
    @GetMapping
    public List<Person> getPeople() {
        return peopleService.findAll(); // Jackson конвертирует в JSON
    }
    @GetMapping("/{id}")
    public Person getById(@PathVariable("id") int id){
        return peopleService.findOne(id); // Jackson конвертирует в JSON
    }

    @PostMapping


    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person
            , BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError e : errors) {
                errMsg.append(e.getField())
                        .append(" - ").append(e.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errMsg.toString());
        }
        peopleService.create(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person not found"
                , System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage()
                , System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
