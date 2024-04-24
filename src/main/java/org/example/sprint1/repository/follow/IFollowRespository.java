package org.example.sprint1.repository.follow;

import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.entity.Seller;

public interface IFollowRespository {
    boolean userIdToFollow(int userId, int userIdToFollow);
    Seller  countFollowers(int sellerId);

}
