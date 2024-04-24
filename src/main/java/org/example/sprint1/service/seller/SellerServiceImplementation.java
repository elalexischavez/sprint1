package org.example.sprint1.service.seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sprint1.dto.PostDTO;
import org.example.sprint1.dto.RequestPostDTO;
import org.example.sprint1.dto.ResponsePostDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Post;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.exception.NotFoundException;
import org.example.sprint1.repository.CustomerRepository;
import org.example.sprint1.repository.ICustomerRepository;
import org.example.sprint1.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerServiceImplementation implements ISellerService {
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    ICustomerRepository customerRepository;

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

//        Asignar un Post ID
        int uuid = Math.abs(UUID.randomUUID().hashCode());
        post.setPostId(uuid);
        if (sellerRepository.postIdExist(post.getPostId())){
            throw new BadRequestException("El ID de la publicacion ya existe");
        }

//        Agregar post al listado de sellers
        seller.getPosts().add(post);

        return post;
    }

    public List<Seller> getSellers(){
        return sellerRepository.getSellersList();
    }

    @Override
    public ResponsePostDTO getPostsFromFollowingWithTwoWeeksOld(int userId) {
        // Obtiene el customer con el userId
        Customer customer = customerRepository.findCustomerById(userId);
        // Obtiene una lista de post de los usuarios que sigue el customer
        // con dos semanas de antigüedad
        List<Post> posts = sellerRepository.findPostsByFollowing(customer.getSellers());

        return new ResponsePostDTO(userId, posts);
    }

}
