package com.champlain.apigatewayservice.presentationlayer;

import com.champlain.apigatewayservice.buisnesslogiclayer.UserService;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserRequestModel;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseModel>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModel> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @PostMapping()
    public ResponseEntity<UserResponseModel> addUser(@RequestBody UserRequestModel userRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestModel));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseModel> updateUser(@RequestBody UserRequestModel userRequestModel, @PathVariable String userId) {
        return ResponseEntity.ok().body(userService.updateUser(userRequestModel, userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
