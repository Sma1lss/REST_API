package org.example.validation;

import org.example.dto.comment.CommentRequestDTO;
import org.example.exception.comment.CommentNotFoundException;
import org.example.exception.comment.InvalidCommentException;
import org.example.exception.post.PostNotFoundException;
import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.User;
import org.example.repository.CommentRepository;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CommentValidator {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentValidator(UserRepository userRepository, CommentRepository commentRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    /**
     * @param commentRequestDTO
     * @return exception or Post that will have new comment
     */
    public Post createCommentValidation(CommentRequestDTO commentRequestDTO) {
        Post post = postRepository.findById(commentRequestDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Can not found post with id = " + commentRequestDTO.getPostId()));
        if (commentRequestDTO.getText() == null || commentRequestDTO.getText().length() > 150) {
            throw new InvalidCommentException("Text of comment can not be null and can not have more than 150 characters.");
        }

        return post;
    }

    /**
     * @param id
     * @return exception or comment to delete
     */
    public Comment deleteValidation(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Can not found comment with id = " + id));
    }

    /**
     * @param postId
     * @return Post which have comments or exception
     */
    public Post getAllCommentsForPostValidation(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Can not found post with id = " + postId));
        if (post.getComments().isEmpty()) {
            throw new CommentNotFoundException("Can not found comments for post with id = " + postId);
        }
        return post;
    }

    /**
     * @param id
     * @return Comment that User want to update
     */
    public Comment updateCommentValidation(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Can not found comment with id = " + id));
        return comment;
    }
}
