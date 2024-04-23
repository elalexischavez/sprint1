package org.example.sprint1.repository.follow;

import org.example.sprint1.dto.ExceptionDTO;

public interface IFollowRespository {
    boolean userIdToFollow(int userId, int userIdToFollow);
}
