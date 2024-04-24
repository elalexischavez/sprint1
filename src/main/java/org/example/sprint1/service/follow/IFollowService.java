package org.example.sprint1.service.follow;

import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.dto.SellerFollowerDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IFollowService {
    ExceptionDTO userIdToFollow(int userId,int userIdToFollow);
    SellerFollowerDto getSellerFollowers(int userId);
}
