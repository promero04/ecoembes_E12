package com.plassb.facade;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomePlasSbController {

    @GetMapping
    public Map<String, String> index() {
        return Map.of(
                "status", "ok",
                "message", "API PlasSB operativa",
                "docs", "/swagger-ui/index.html",
                "h2", "/h2-console");
    }
}
