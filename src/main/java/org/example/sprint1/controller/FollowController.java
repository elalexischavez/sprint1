package org.example.sprint1.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.example.sprint1.dto.SellerFollowerDto;
import org.example.sprint1.dto.FollowedSellersDTO;
import org.example.sprint1.service.follow.IFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
public class FollowController {

    @Autowired
    IFollowService followService;

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    ResponseEntity<?> userIdToFollow(@PathVariable @Min(value = 1, message = "Debe ser un valor positivo") int userId,
                                     @PathVariable @Min(value = 1, message = "Debe ser un valor positivo") int userIdToFollow) {
        followService.userIdToFollow(userId, userIdToFollow);
        return new ResponseEntity<>("follow exitoso" , HttpStatus.OK);
    }
    @GetMapping("/{userId}/followers/count")
    ResponseEntity<?> countFollowers(@PathVariable int userId) {
        return new ResponseEntity<>(followService.countFollowers(userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    ResponseEntity<?> unfollowSeller(@PathVariable int userId,@PathVariable int userIdToUnfollow) {
        followService.unfollowSeller(userId,userIdToUnfollow);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<SellerFollowerDto> getSellerFollowers(@PathVariable int userId, @RequestParam(required = false) String order) {
        return new ResponseEntity<>(followService.getSellerFollowers(userId, order), HttpStatus.OK);
    }

    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<FollowedSellersDTO> getFollowedSellers(@PathVariable int userId, @RequestParam(required = false) String order){
        return new ResponseEntity<>(followService.getFollowedSellers(userId, order), HttpStatus.OK);
    }
}
