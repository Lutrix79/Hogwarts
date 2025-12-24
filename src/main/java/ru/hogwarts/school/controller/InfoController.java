package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.hogwarts.school.service.InfoService;

@RestController
public class InfoController {

    @Autowired
    private InfoService infoService;

    /**
     * Getting server's port
     */
    @GetMapping("/port")
    public ResponseEntity<String> getServerPort () {
        return ResponseEntity.ok(infoService.getServerPort());
    }
}
