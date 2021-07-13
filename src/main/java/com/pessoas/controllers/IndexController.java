package com.pessoas.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

@RestController
@RequestMapping(value = {"", "/"})
public class IndexController {

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("redirect:/swagger-ui/index.html");
    }
}
