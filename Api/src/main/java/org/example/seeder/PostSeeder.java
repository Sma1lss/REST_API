package org.example.seeder;

import org.example.model.Post;
import org.example.model.User;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@Order(6)
@Profile("seed")
public class PostSeeder implements DatabaseSeeder {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostSeeder(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * Post:
     * "1": {
     * "title": "My first post",
     * "text": "I'm testing this awesome app named Meetex!"
     * }
     */

    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) {
            Post post = new Post();
            post.setTitle("My first post");
            post.setText("I'm testing this awesome app named Meetex!");
            post.setSendDate(LocalDateTime.now());

            postRepository.save(post);
        }
    }
}
