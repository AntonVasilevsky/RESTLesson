package com.anton.rest.controllers;

import com.anton.rest.dto.PersonDto;
import com.anton.rest.models.Person;
import com.anton.rest.services.PeopleService;
import com.anton.rest.util.PersonErrorResponse;
import com.anton.rest.util.PersonNotCreatedException;
import com.anton.rest.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")

public class PeopleController {

    private final PeopleService peopleService;
    private final ModelMapper modelMapper;


    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }
    @GetMapping
    public List<PersonDto> getPeople() {
        return peopleService.findAll().stream()
                .map(this::convertPersonToDto)
                .collect(Collectors.toList()); // Jackson конвертирует в JSON
    }
    @GetMapping("/{id}")
    public PersonDto getById(@PathVariable("id") int id){
        return convertPersonToDto(peopleService.findOne(id)); // Jackson конвертирует в JSON
    }

    @PostMapping


    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDto personDto
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
        Person person = convertDtoToPerson(personDto);
        System.out.println(person);
        peopleService.save(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {

        PersonErrorResponse response = new PersonErrorResponse(
                "Person not found"
                , System.currentTimeMillis()
        );
        System.out.println(response.getMessage());
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
    public Person convertDtoToPerson(PersonDto personDto){

        return modelMapper.map(personDto, Person.class);
    }
    public PersonDto convertPersonToDto(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }



}
