package com.anton.rest.controllers;

import com.anton.rest.models.Person;
import com.anton.rest.services.PeopleService;
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
}
