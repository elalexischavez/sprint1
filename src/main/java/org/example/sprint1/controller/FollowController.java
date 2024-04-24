package org.example.sprint1.controller;

import org.example.sprint1.dto.SellerFollowerDto;
import org.example.sprint1.service.follow.IFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FollowController {

    @Autowired
    IFollowService followService;

    @PostMapping("/users/{userId}/follow/{userIdToFollow}")
    ResponseEntity<?> userIdToFollow(@PathVariable int userId, @PathVariable int userIdToFollow) {
        followService.userIdToFollow(userId, userIdToFollow);
        return new ResponseEntity<>("follow exitoso" , HttpStatus.OK);
    }
    @GetMapping("/users/{userId}/followers/count")
    ResponseEntity<?> countFollowers(@PathVariable int userId) {
        return new ResponseEntity<>(followService.countFollowers(userId), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/unfollow/{userIdToUnfollow}")
    ResponseEntity<?> unfollowSeller(@PathVariable int userId,@PathVariable int userIdToUnfollow) {
        return new ResponseEntity<>(followService.unfollowSeller(userId,userIdToUnfollow), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/followers/list")
    public ResponseEntity<SellerFollowerDto> getSellerFollowers(@PathVariable int userId, @RequestParam(required = false) String order) {
        return new ResponseEntity<>(followService.getSellerFollowers(userId, order), HttpStatus.OK);
    }
}
