package com.anton.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController     // @Controller + @ResponseBody над каждым методом
@RequestMapping("api")
public class FirstRestController {

    @RequestMapping("sayHello")
    public String sayHello() {
        return "Hello world";
    }
}
