package com.example.searc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @RequestMapping(value = "/**")
    public String redirect() {
        return "forward:/index.html";
    }
}
