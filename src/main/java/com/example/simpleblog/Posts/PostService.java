package com.example.simpleblog.Posts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class PostService {
    public void postadd(Post post){
        new Post(
                post.getTitle(),
                post.getTopic(),
                post.getDescription(),
                post.getAddedat()
        );
    }
}
