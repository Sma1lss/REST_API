package org.example.service;

import org.example.dto.friendRequest.FriendRequestDTO;
import org.example.dto.user.UserRegistrationRequest;
import org.example.dto.user.UserRequestDTO;
import org.example.dto.user.UserResponseDTO;
import org.example.exception.user.RoleNotFoundException;
import org.example.exception.user.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.model.Message;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.validation.UserValidator;

import com.google.api.services.gmail.Gmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserValidator userValidator, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
    }

    /**
     * @param registrationRequest
     * @return DTO of new User
     */
    public UserResponseDTO registerUser(UserRegistrationRequest registrationRequest) {
        userValidator.validateRegistration(registrationRequest);

        User newUser = new User();
        newUser.setUserName(registrationRequest.getUsername());
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));


        User savedUser = userRepository.save(newUser);


        return userMapper.userToUserResponseDTO(savedUser);
    }


    /**
     * @param id
     * @param userRequestDTO
     * @return Dto of updatedUser
     */

    public User getUserByUsername(String username) {
        return userRepository.findUserByUserName(username);
    }

    /**
     * @param userId
     * @param friendRequestDTO
     * @return communicat about friend request
     */
    public String sendFriendRequest(Long userId, FriendRequestDTO friendRequestDTO) {
        User sender = userRepository.findById(friendRequestDTO.getSenderId()).orElseThrow(() -> new UserNotFoundException("Can not found user with id = " + friendRequestDTO.getSenderId()));
        User receiver = userRepository.findById(friendRequestDTO.getReceiverId()).orElseThrow(() -> new UserNotFoundException("Can not found User with id = " + friendRequestDTO.getReceiverId()));

        userValidator.validateFriendRequest(sender, receiver);

        sender.getSentFriendRequests().add(receiver);
        receiver.getReceivedFriendRequests().add(sender);

        userRepository.save(sender);
        userRepository.save(receiver);

        return "Friend request sent to user with username = " + receiver.getUserName();
    }

    /**
     * @param userId
     * @return List of all user friends
     */
    public List<UserResponseDTO> getAllFriends(Long userId) {
        userValidator.validateUserExistsAndHasFriends(userId);

        User user = userRepository.findById(userId).get();  // We can call .get() safely now because we've validated that the user exists.

        List<UserResponseDTO> returnedList = user.getFriends().stream().map(userMapper::userToUserResponseDTO).collect(Collectors.toList());

        return returnedList;
    }

    /**
     * @param userId
     * @param senderId
     */
    public void acceptFriendRequest(Long userId, Long senderId) {
        User user = userValidator.validateUserExists(userId);
        User sender = userValidator.validateUserExists(senderId);

        userValidator.validateFriendRequestReceived(user, sender);

        user.getFriends().add(sender);
        sender.getFriends().add(user);

        user.getReceivedFriendRequests().remove(sender);
        sender.getSentFriendRequests().remove(user);

        userRepository.save(user);
        userRepository.save(sender);
    }

    /**
     * @param userId
     * @param senderId
     */
    public void declineFriendRequest(Long userId, Long senderId) {
        User user = userValidator.validateUserExists(userId);
        User sender = userValidator.validateUserExists(senderId);

        userValidator.validateFriendRequestReceived(user, sender);

        user.getReceivedFriendRequests().remove(sender);
        sender.getSentFriendRequests().remove(user);

        userRepository.save(user);
        userRepository.save(sender);
    }

    /**
     * @param userId
     * @param receiverId
     */
    public void deleteFriendRequest(Long userId, Long receiverId) {
        User sender = userValidator.validateUserExists(userId);
        User receiver = userValidator.validateUserExists(receiverId);

        userValidator.validateFriendRequestSent(sender, receiver);

        sender.getSentFriendRequests().remove(receiver);
        receiver.getReceivedFriendRequests().remove(sender);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    private Message createWelcomeMessage(String toEmail, String userName) throws MessagingException, IOException {
        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        mimeMessage.setFrom(new InternetAddress("fake@gmail.com"));  //fake
        mimeMessage.setSubject("Welcome to  service!");
        mimeMessage.setText("Hi " + userName + ",\n\n" +
                "Welcome to our service! We are glad to have you on board. " +
                "You have successfully created a new account. Please let us know if you have any questions.\n\n" +
                "Best,\n" +
                "The me");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mimeMessage.writeTo(outputStream);
        byte[] bytes = outputStream.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setText(encodedEmail);

        return message;
    }
}
