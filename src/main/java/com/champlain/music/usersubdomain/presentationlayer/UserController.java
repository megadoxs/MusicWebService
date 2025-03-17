package com.champlain.music.usersubdomain.presentationlayer;


import com.champlain.music.usersubdomain.businesslogiclayer.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseModel>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponseModel> getUserByUserId(@PathVariable String user_id) {
        return ResponseEntity.ok().body(this.userService.getUserbyUserId(user_id));
    }

    @PostMapping()
    public ResponseEntity<UserResponseModel> addUser(@RequestBody UserRequestModel newUserData) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.addUser(newUserData));
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<UserResponseModel> updateUser(@RequestBody UserRequestModel newUserData, @PathVariable String user_id) {
        return ResponseEntity.ok().body(userService.updateUser(user_id, newUserData));
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<String> deleteUserByUserId(@PathVariable String user_id) {
        userService.deleteUserbyUserId(user_id);
        return ResponseEntity.noContent().build();
    }
}
