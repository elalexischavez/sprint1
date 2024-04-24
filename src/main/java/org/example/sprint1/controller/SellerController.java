package org.example.sprint1.controller;

import jakarta.validation.Valid;
import org.example.sprint1.dto.RequestPostDTO;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.service.seller.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class SellerController {

    @Autowired
    ISellerService postService;

    @Validated
    @PostMapping("/products/post")
    public ResponseEntity<Void> addPost(@Valid @RequestBody RequestPostDTO postDTO, BindingResult result){
        if(result.hasErrors()){
            throw new BadRequestException("Bad Request");
        }
        postService.addPost(postDTO);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/products/list")
    public ResponseEntity<List<Seller>> getAllSellers(){
        return new ResponseEntity<>(postService.getSellers(), HttpStatus.OK);
    }

}
