package org.example.sprint1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.sprint1.entity.Post;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponsePostDTO {
    @JsonProperty("user_id")
    private int userId;
    @JsonSerialize(contentAs = Post.class)
    @JsonDeserialize(contentAs = Post.class)
    private List<Post> posts;
}
