package org.example.sprint1.service.follow;

import org.example.sprint1.dto.ExceptionDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface IFollowService {
    ExceptionDTO userIdToFollow(int userId,int userIdToFollow);
}
