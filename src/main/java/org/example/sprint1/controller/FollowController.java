package org.example.sprint1.controller;

import org.example.sprint1.dto.SellerFollowerDto;
import org.example.sprint1.service.follow.IFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FollowController {

    @Autowired
    IFollowService followService;

    @PostMapping("/users/{userId}/follow/{userIdToFollow}")
    ResponseEntity<?> userIdToFollow(@PathVariable int userId, @PathVariable int userIdToFollow) {
        return new ResponseEntity<>(followService.userIdToFollow(userId, userIdToFollow), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/followers/list")
    public ResponseEntity<SellerFollowerDto> getSellerFollowers(@PathVariable int userId) {
        return new ResponseEntity<>(followService.getSellerFollowers(userId), HttpStatus.OK);
    }
}
