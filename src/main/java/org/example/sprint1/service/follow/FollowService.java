package org.example.sprint1.service.follow;

import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.repository.follow.IFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FollowService implements IFollowService {

    @Autowired
    IFollowRepository followRepository;


    @Override
    public void userIdToFollow(int userId, int userIdToFollow) {

        //se optiene el resultado si existen id
        boolean respose =  followRepository.userIdToFollow(userId, userIdToFollow);

        if (respose) {
            throw new BadRequestException("id no encontrado");
        }
    }
}
