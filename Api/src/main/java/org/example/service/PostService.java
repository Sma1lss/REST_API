package org.example.service;

import org.example.dto.post.PostRequestDTO;
import org.example.dto.post.PostResponseDTO;
import org.example.dto.post.PostUpdateRequest;
import org.example.exception.post.PostNotFoundException;
import org.example.exception.user.UserNotFoundException;
import org.example.mapper.PostMapper;
import org.example.model.Post;
import org.example.model.User;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.validation.PostValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostValidator postValidator;
    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostValidator postValidator, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postValidator = postValidator;
        this.postMapper = postMapper;
    }

    /**
     * @param id method delete post
     */
    public void delete(Long id) {
        postRepository.delete(postValidator.deleteValidation(id));
    }


    /**
     * @param postRequestDTO
     * @param userId
     * @return shared post DTO
     */
    public PostResponseDTO sharePost(PostRequestDTO postRequestDTO, Long userId) {
        postValidator.sharePostValidation(postRequestDTO, userId);

        User creator = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Can not found user with id = " + userId));

        Post post = new Post();

        post.setTitle(postRequestDTO.getTitle());
        post.setText(postRequestDTO.getText());
        post.setSendDate(LocalDateTime.now());
        post.setSender(creator);
        post.setAddressee(creator.getFriends());

        Post savedPost = postRepository.save(post);
        return postMapper.postToPostResponseDTO(savedPost);
    }

    /**
     * @param postId
     * @return
     */
    public PostResponseDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + postId));
        return postMapper.postToPostResponseDTO(post);
    }

    /**
     * @param postId
     * @param postUpdateRequest
     * @return updated post
     */
    public PostResponseDTO updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
        postValidator.updatePostValidation(postId, postUpdateRequest);

        Post post = postRepository.findById(postId).get();

        post.setTitle(postUpdateRequest.getTitle());
        post.setText(postUpdateRequest.getText());

        Post savedPost = postRepository.save(post);
        return postMapper.postToPostResponseDTO(savedPost);
    }

    /**
     * @param postId
     * @param userId
     * @return Dto of Post with new like
     */
    public PostResponseDTO likePost(Long postId, Long userId) {
        User user = postValidator.likingPostValidation(postId, userId);

        Post post = postRepository.findById(postId).get();

        post.getLikes().add(user);
        post.setReactions(post.getReactions() + 1);

        Post savedPost = postRepository.save(post);
        return postMapper.postToPostResponseDTO(savedPost);
    }

    /**
     * @param postId
     * @param userId
     * @return Dto of post without like from user with id = userId
     */
    public PostResponseDTO unlikePost(Long postId, Long userId) {
        User user = postValidator.unlikingPostValidation(postId, userId);

        Post post = postRepository.findById(postId).get();

        post.getLikes().remove(user);
        post.setReactions(post.getReactions() - 1);

        Post savedPost = postRepository.save(post);

        return postMapper.postToPostResponseDTO(savedPost);
    }
}
