package com.guruprasad.trainticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guruprasad.trainticket.dto.User;
import com.guruprasad.trainticket.service.UserService;
import com.guruprasad.trainticket.utils.TrainUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;
    private final TrainUtils trainUtils;

    public UserController(UserService userService, TrainUtils trainUtils) {
        this.userService = userService;
        this.trainUtils = trainUtils;
    }

    @GetMapping
    public ResponseEntity<String> getUser(@RequestParam("email") Optional<String> email, @RequestParam("id") Optional<String> id) throws JsonProcessingException {
        User user;

        if (id.isPresent()) {
            user = userService.getById(id.get());
        } else if (email.isPresent()) {
            user = userService.getByEmail(email.get());
        } else {
            return ResponseEntity.badRequest().body("Either id or email should be given in the request parameters");
        }

        return ResponseEntity.ok().body(
            trainUtils.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user)
        );
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam("email") Optional<String> email, @RequestParam("id") Optional<String> id) {
        if (id.isPresent()) {
            userService.deleteById(id.get());
        } else if (email.isPresent()) {
            userService.deleteByEmail(email.get());
        } else {
            ResponseEntity.badRequest().body("Either id or email should be given in the request parameters");
        }

        return ResponseEntity.ok().body(String.valueOf(true));
    }
}
