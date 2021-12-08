package com.bootcamp.chatroom;

import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import controller.LoginController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class MessageController {
    private final MessageRepository repository;
    private Map<String, String> tokenToUserMap = new HashMap<>();
    private Map<String, Long> userActivityMap = new HashMap<>();


    MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/message")
    Message newMessage(@RequestBody Message message) throws Exception {
        LoginController controller = new LoginController();
        message.setUser(controller.getUsername());
        verifyUserLogin(message.getUser());

        message.setTimestamp(ZonedDateTime.now());
        message.setUser(tokenToUserMap.get(message.getUser()));
        updateUserActivity(message.getUser());
        return repository.save(message);
    }

    @PostMapping("/messages")
    List<Message> retrieveMessages(@RequestBody MessageRequest messageRequest) throws Exception {
        verifyUserLogin(messageRequest.getUserToken());
        updateUserActivity(tokenToUserMap.get(messageRequest.getUserToken()));
        int limit = messageRequest.getLimit();
        if (messageRequest.getMessageId() == null) {
            List<Message> all = repository.findAll();
            int fromIndex = Math.max(0, all.size() - limit);
            return all.subList(fromIndex, all.size());
        }
        return repository.findByIdGreaterThan(messageRequest.getMessageId());
    }

    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        if (isTimedOut(loginRequest.getUsername())) {
            throw new Exception("User already signed in");
        }
        String token = UUID.randomUUID().toString();
        tokenToUserMap.put(token, loginRequest.getUsername());
        updateUserActivity(loginRequest.getUsername());

        LoginResponse response = new LoginResponse(token);
        return response;
    }

    @PostMapping("/logout")
    void logout(@RequestBody LogoutRequest logoutRequest) {
        tokenToUserMap.remove(logoutRequest.getUserToken());
    }

    private void verifyUserLogin(String userToken) throws Exception {
        if (!tokenToUserMap.containsKey(userToken)) {
            throw new Exception("User not logged in");
        }
    }

    private boolean isTimedOut(String username) {
        return System.currentTimeMillis() - userActivityMap.getOrDefault(username, 0L) < 30000;
    }

    private void updateUserActivity(String user) {
        userActivityMap.put(user, System.currentTimeMillis());
    }

}
