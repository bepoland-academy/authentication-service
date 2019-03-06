package pl.betse.beontime.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping
    public String getResponse(){
        return "Hello secure world!";
    }
}
