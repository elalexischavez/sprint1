package org.example.sprint1.service.follow;

import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.dto.SellerFollowerDto;

public interface IFollowService {
    void userIdToFollow(int userId,int userIdToFollow);

    int countFollowers(int sellerId);

    ExceptionDTO unfollowSeller(int userId, int userIdToUnfollow);

    SellerFollowerDto getSellerFollowers(int userId, String order);
}
