package org.example.sprint1.service.follow;

import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.dto.FollowedSellersDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface IFollowService {
    ExceptionDTO userIdToFollow(int userId,int userIdToFollow);
    FollowedSellersDTO getFollowedSellers(int userId, String order);

}
