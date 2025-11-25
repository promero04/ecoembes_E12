package com.ecoembes.facade;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public Map<String, String> index() {
        // Landing simple para evitar la página Whitelabel y guiar al usuario.
        return Map.of(
                "status", "ok",
                "message", "API ecoembes_E12 operativa",
                "docs", "/swagger-ui/index.html");
    }
}
