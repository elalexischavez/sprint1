package org.example.sprint1.repository.follow;

import lombok.Setter;
import org.example.sprint1.entity.Seller;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class FollowRepository implements IFollowRepository {
    @Override
    public boolean userIdToFollow(int userId, int userIdToFollow) {
        return false;
    }

}
