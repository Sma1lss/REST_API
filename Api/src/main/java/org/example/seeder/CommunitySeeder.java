package org.example.seeder;

import org.example.model.Community;
import org.example.model.User;
import org.example.repository.CommunityRepository;
import org.example.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Order(3)
@Profile("seed")
public class CommunitySeeder implements DatabaseSeeder {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public CommunitySeeder(CommunityRepository communityRepository, UserRepository userRepository) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
    }

    /**
     * Community:
     * "1": {
     * "name": "Coders",
     * "description": "goats",
     * "category": "it",
     * "imageUrl": "https://example.com/asm.jpg"
     * }
     */

    @Override
    public void run(String... args) throws Exception {
        if (communityRepository.count() == 0) {

            Community community = new Community();
            community.setName("Coders");
            community.setDescription("goats");
            community.setCategory("it");
            community.setImageUrl("https://example.com/asm.jpg");

            communityRepository.save(community);
        }
    }
}
