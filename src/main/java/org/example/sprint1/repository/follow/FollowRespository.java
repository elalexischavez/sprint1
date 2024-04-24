package org.example.sprint1.repository.follow;

import org.example.sprint1.dto.SellerFollowerDto;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Seller;

import java.util.List;

public class FollowRespository implements IFollowRespository{
    @Override
    public boolean userIdToFollow(int userId, int userIdToFollow) {
        return false;
    }
}
