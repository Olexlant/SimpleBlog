package com.example.simpleblog.Posts;

import com.example.simpleblog.Users.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "student_sequence"
    )
    private Long id;
    private String title;
    private String topic;
    private String description;
    private LocalDateTime addedat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post(String title, String topic, String description, LocalDateTime addedat) {
        this.title = title;
        this.topic = topic;
        this.description = description;
        this.addedat = addedat;
    }
}
