package org.example.sprint1.controller;

import org.example.sprint1.service.follow.IFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowController {

    @Autowired
    IFollowService followService;

    @PostMapping("/users/{userId}/follow/{userIdToFollow}")
    ResponseEntity<?> userIdToFollow(@PathVariable int userId, @PathVariable int userIdToFollow) {
        followService.userIdToFollow(userId, userIdToFollow);
        return new ResponseEntity<>("follow excitoso" , HttpStatus.OK);
    }
    @GetMapping("/users/{userId}/followers/count")
    ResponseEntity<?> countFollowers(@PathVariable int userId) {
        return new ResponseEntity<>(followService.countFollowers(userId), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/unfollow/{userIdToUnfollow}")
    ResponseEntity<?> unfollowSeller(@PathVariable int userId,@PathVariable int userIdToUnfollow) {
        return new ResponseEntity<>(followService.unfollowSeller(userId,userIdToUnfollow), HttpStatus.OK);
    }
}
