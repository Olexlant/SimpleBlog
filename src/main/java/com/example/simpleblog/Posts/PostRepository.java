package com.example.simpleblog.Posts;

import com.example.simpleblog.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findByUser(User user);
    List<Post> findByTitleContains(String keyword);
    List<Post> findByTopicContains(String keyword);
    List<Post> findByDescriptionContains(String keyword);
}
