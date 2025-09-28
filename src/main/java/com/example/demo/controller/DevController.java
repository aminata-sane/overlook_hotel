package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dev")
public class DevController {
    
    @GetMapping("/console")
    public String console(Model model) {
        // Informations de connexion H2
        model.addAttribute("h2ConsoleUrl", "http://localhost:8080/h2-console");
        model.addAttribute("jdbcUrl", "jdbc:h2:file:./data/hoteldb");
        model.addAttribute("username", "sa");
        model.addAttribute("password", "");
        
        return "dev/console";
    }
    
    @GetMapping("/info")
    public String info(Model model) {
        // Informations système pour le développement
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("springVersion", "Spring Boot 2.x");
        model.addAttribute("h2Version", "H2 Database");
        
        return "dev/info";
    }
}
