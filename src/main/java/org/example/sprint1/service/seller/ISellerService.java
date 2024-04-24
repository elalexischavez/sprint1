package org.example.sprint1.service.seller;

import org.example.sprint1.dto.RequestPostDTO;
import org.example.sprint1.entity.Post;
import org.example.sprint1.entity.Seller;

import java.util.List;

public interface ISellerService {
     public Post addPost(RequestPostDTO postDTO);
     public List<Seller> getSellers();
}
