package org.example.sprint1.service.seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sprint1.dto.RequestPostDTO;
import org.example.sprint1.entity.Post;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.exception.NotFoundException;
import org.example.sprint1.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerServiceImplementation implements ISellerService {
    @Autowired
    SellerRepository sellerRepository;

    @Override
    public Post addPost(RequestPostDTO postDTO) {

//        Revisar si existe el Usuario
        Seller seller = sellerRepository.filterSellerById(postDTO.getUserId());
        if (seller == null) {
            throw new NotFoundException("No existe un Vendedor con ese ID");
        }
//        Crear objeto Post a partir de RequestPostDTO
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Post post = mapper.convertValue(postDTO,Post.class);

//        Revisar que el Id del producto no exista en ningun vendedor

        boolean idExists = sellerRepository.productIdExists(post.getProduct().getProductId());
        if(idExists){
            throw new BadRequestException("El ID del producto ya existe");
        }

//        Agregar post al listado de sellers
        seller.getPosts().add(post);

        return post;
    }

    public List<Seller> getSellers(){
        return sellerRepository.getSellersList();
    }

}
