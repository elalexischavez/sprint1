package org.example.sprint1.repository.follow;

import org.example.sprint1.entity.Seller;

public interface IFollowRepository {
    boolean userIdToFollow(int userId, int userIdToFollow);
}
