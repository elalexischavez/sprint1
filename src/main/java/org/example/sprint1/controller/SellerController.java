package org.example.sprint1.controller;

import org.example.sprint1.dto.RequestPostDTO;
import org.example.sprint1.dto.ResponsePostDTO;
import org.example.sprint1.entity.Post;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.service.seller.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class SellerController {
    @Autowired
    ISellerService postService;

    @PostMapping("/products/post")
    public ResponseEntity<Post> addPost(@RequestBody RequestPostDTO postDTO){
        return new ResponseEntity<>(postService.addPost(postDTO), HttpStatus.OK);
    }
    @GetMapping("/products/list")
    public ResponseEntity<List<Seller>> getAllSellers(){
        return new ResponseEntity<>(postService.getSellers(), HttpStatus.OK);
    }

    @GetMapping("/products/followed/{userId}/list")
    public ResponseEntity<ResponsePostDTO> getPostsFromFollowingWithTwoWeeksOld(@PathVariable int userId){
        return new ResponseEntity<>(postService.getPostsFromFollowingWithTwoWeeksOld(userId), HttpStatus.OK);
    }

}
