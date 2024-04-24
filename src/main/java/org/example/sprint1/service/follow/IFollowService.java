package org.example.sprint1.service.follow;

import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.entity.Customer;
import org.springframework.web.bind.annotation.PathVariable;

public interface IFollowService {
    ExceptionDTO userIdToFollow(int userId,int userIdToFollow);
    int countFollowers(int sellerId);

    ExceptionDTO unfollowSeller(int userId, int userIdToUnfollow);
}
