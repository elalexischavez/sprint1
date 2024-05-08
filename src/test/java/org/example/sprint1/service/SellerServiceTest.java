package org.example.sprint1.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolationException;
import org.example.sprint1.dto.PostDTO;
import org.example.sprint1.dto.ResponsePostDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Post;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.exception.NotFoundException;
import org.example.sprint1.repository.ICustomerRepository;
import org.example.sprint1.repository.ISellerRepository;
import org.example.sprint1.service.seller.SellerServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {
    @Mock
    ISellerRepository sellerRepository;

    @Mock
    ICustomerRepository customerRepository;

    @InjectMocks
    SellerServiceImplementation sellerServiceImplementation;

    ResponsePostDTO responsePostDTOAsc;
    ResponsePostDTO responsePostDTODesc;
    Customer customer;
    Map<Integer, List<Post>> postsMap = new HashMap<>();

    @BeforeEach
    public void setup() {
        int userId = 1;
        List<PostDTO> postDTOS = new ArrayList<>();
        postDTOS.add(PostDTO.builder()
                .sellerId(2)
                .postId(100)
                .date(LocalDate.of(2024, 4, 30))
                .category(1000)
                .price(1000)
                .build()
        );

        postDTOS.add(PostDTO.builder()
                .sellerId(3)
                .postId(101)
                .date(LocalDate.of(2024, 5, 2))
                .category(2000)
                .price(1000)
                .build()
        );

        List<PostDTO> reversedPostDTOS = new ArrayList<>(postDTOS);
        Collections.reverse(reversedPostDTOS);

        responsePostDTOAsc = new ResponsePostDTO(userId, postDTOS);
        responsePostDTODesc = new ResponsePostDTO(userId, reversedPostDTOS);


        customer = Customer.builder()
                .userId(1)
                .userName("Juan")
                .sellers(Arrays.asList(2, 3))
                .build();

        postsMap.put(2, List.of(
                        Post.builder()
                                .postId(100)
                                .date(LocalDate.of(2024, 4, 30))
                                .category(1000)
                                .price(1000)
                                .build()
                )
        );

        postsMap.put(3, List.of(
                        Post.builder()
                                .postId(101)
                                .date(LocalDate.of(2024, 5, 2))
                                .category(2000)
                                .price(1000)
                                .build()
                )
        );
    }

    @Test
    @DisplayName("Verificar que el tipo de ordenamiento por fecha exista")
    public void checkDateOrderExistsTest() {
        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.findPostsByFollowing(customer.getSellers())).thenReturn(postsMap);

        // Assert
        ResponsePostDTO responseDateAsc = sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_asc");
        ResponsePostDTO responseDateDesc = sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_desc");

        Assertions.assertDoesNotThrow(() -> responseDateAsc);
        Assertions.assertDoesNotThrow(() -> responseDateDesc);
    }

    @Test
    @DisplayName("El ordenamiento no existe")
    public void checkOrderExistsTest() {
        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.findPostsByFollowing(customer.getSellers())).thenReturn(postsMap);

        // Assert
        Assertions.assertThrows(BadRequestException.class, () -> sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "asdf"));
    }

    @Test
    @DisplayName("Verificar el correcto ordenamiento ascendente y descendente por fecha")
    public void dateOrderingCorrectTest() {
        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.findPostsByFollowing(customer.getSellers())).thenReturn(postsMap);

        ResponsePostDTO responseDateAsc = sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_asc");
        ResponsePostDTO responseDateDesc = sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_desc");

        // Assert
        Assertions.assertEquals(responsePostDTOAsc, responseDateAsc);
        Assertions.assertEquals(responsePostDTODesc, responseDateDesc);
    }


    @Test
    @DisplayName("El id de usuario no existe")
    public void checkUserIdExistsTest() {
        // Act
        when(customerRepository.findCustomerById(anyInt())).thenReturn(null);

        // Assert
        Assertions.assertThrows(NotFoundException.class, () -> sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_asc"));
    }
    package org.example.sprint1.service.seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.sprint1.dto.PostDTO;
import org.example.sprint1.dto.RequestPostDTO;
import org.example.sprint1.dto.ResponsePostDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Post;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.exception.NotFoundException;
import org.example.sprint1.repository.ICustomerRepository;
import org.example.sprint1.repository.ISellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SellerServiceImplementation implements ISellerService {
    @Autowired
    ISellerRepository sellerRepository;
    @Autowired
    ICustomerRepository customerRepository;

    private final static String ORDER_ASC = "date_asc";
    private final static String ORDER_DESC = "date_desc";

    ObjectMapper mapper = new ObjectMapper();

    public SellerServiceImplementation() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Post addPost(RequestPostDTO postDTO) {

//        Revisar si existe el Usuario
        Seller seller = sellerRepository.getSellerById(postDTO.getUserId());
        if (seller == null) {
            throw new NotFoundException("No existe un Vendedor con ese ID");
        }
//        Crear objeto Post a partir de RequestPostDTO
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
    public ResponsePostDTO getPostsFromFollowingWithTwoWeeksOld(int userId, String order) {
        // Obtiene customer con userId
        Customer customer = customerRepository.findCustomerById(userId);
        if(customer == null){
            throw new NotFoundException("No existe un cliente con ese ID");
        }

        // Obtiene un map<idSeller, PostsDelSeller> de los usuarios que sigue el customer
        Map<Integer, List<Post>> postsByFollowing = sellerRepository.findPostsByFollowing(customer.getSellers());

        // Convierte el map en list de PostDto para poder generar un ResponsePostDTO
        List<PostDTO> listPostDto = mappingPostToPostDto(postsByFollowing);
        List<PostDTO> listOrderedPostDto = orderBy(order, listPostDto);

        return new ResponsePostDTO(userId, listOrderedPostDto);
    }


    private List<PostDTO> mappingPostToPostDto(Map<Integer, List<Post>> posts) {
        List<PostDTO> listPostDto = new ArrayList<>();

        for (Map.Entry<Integer, List<Post>> entry : posts.entrySet()) {
            // Mapea Post -> PostDTO y agrega un idSeller
            listPostDto.addAll(
                    entry.getValue().stream()
                            .map(v -> {
                                PostDTO postDTO = mapper.convertValue(v, PostDTO.class);
                                postDTO.setSellerId(entry.getKey());
                                return postDTO;
                            })
                            .toList()
            );
        }

        return listPostDto;
    }

    private List<PostDTO> orderBy(String order, List<PostDTO> posts) {
        if(order == null)
            return posts;

        switch (order) {
            case ORDER_ASC:
                posts.sort(Comparator.comparing(PostDTO::getDate));
                break;

            case ORDER_DESC:
                posts.sort(Comparator.comparing(PostDTO::getDate).reversed());
                break;

            default:
                throw new BadRequestException("Orden no encontrado");
        }

        return posts;
    }
}

}
