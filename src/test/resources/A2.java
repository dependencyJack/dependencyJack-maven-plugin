package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {

    //private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoController.class);


    @GetMapping("/hello")
    public String index() {
        return "index";
    }

    /**
     * Test Description
     * @return index
     */
    @GetMapping("/")
    public String index2() throws ClassNotFoundException {
        //doesn't detect this refelction
        //java.lang.ClassNotFoundException: javax.activation.CommandMap.class
        Class.forName("javax.activation.CommandMap.class", true, this.getClass().getClassLoader());
        //log.debug("adsfasdf");
        return "index";
    }
}