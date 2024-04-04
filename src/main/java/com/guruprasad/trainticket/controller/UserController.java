package com.guruprasad.trainticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    public ResponseEntity<JsonNode> getUser(@RequestParam("email") Optional<String> email, @RequestParam("id") Optional<String> id) throws JsonProcessingException {
        User user;

        if (id.isPresent()) {
            user = userService.getById(Long.valueOf(id.get()));
        } else if (email.isPresent()) {
            user = userService.getByEmail(email.get());
        } else {
            ObjectNode response = trainUtils.getMapper().createObjectNode();
            response.put("reason", "Either id or email should be given in the request parameters");
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(trainUtils.getMapper().convertValue(user, JsonNode.class));
    }

    @DeleteMapping
    public ResponseEntity<JsonNode> deleteUser(@RequestParam("email") Optional<String> email, @RequestParam("id") Optional<String> id) {
        ObjectNode response = trainUtils.getMapper().createObjectNode();

        if (id.isPresent()) {
            userService.deleteById(Long.valueOf(id.get()));
        } else if (email.isPresent()) {
            userService.deleteByEmail(email.get());
        } else {
            response.put("reason", "Either id or email should be given in the request parameters");
            ResponseEntity.badRequest().body(response);
        }

        response.put("message", "DELETED");
        return ResponseEntity.ok().body(response);
    }
}
