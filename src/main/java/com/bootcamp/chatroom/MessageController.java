package com.bootcamp.chatroom;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
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
        verifyUserLogin(message.getSender());

        message.setTimestamp(ZonedDateTime.now());
        message.setSender(tokenToUserMap.get(message.getSender()));
        updateUserActivity(message.getSender());
        return repository.save(message);
    }

    @PostMapping("/messages")
    List<Message> retrieveMessages(@RequestBody MessageRequest messageRequest) throws Exception {
        verifyUserLogin(messageRequest.getUserToken());
        updateUserActivity(tokenToUserMap.get(messageRequest.getUserToken()));
        int limit = messageRequest.getLimit();
        if (messageRequest.getMessageId() == null) {
            List<Message> all = repository.findByChannelName(messageRequest.getChannelName());
            int fromIndex = Math.max(0, all.size() - limit);
            return all.subList(fromIndex, all.size());
        }
        List<Message> newMessages = repository.findByIdGreaterThan(messageRequest.getMessageId());
        newMessages.removeIf(m -> !StringUtils.equals(m.getChannelName(), messageRequest.getChannelName()));
        return newMessages;
    }

    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        if (isActive(loginRequest.getUsername())) {
            throw new Exception("User already signed in");
        }
        String token = UUID.randomUUID().toString();
        tokenToUserMap.put(token, loginRequest.getUsername());
        updateUserActivity(loginRequest.getUsername());

        Message message = new Message();
        message.setMessage("has joined the chat");
        message.setSender(token);
        message.setChannelName("Main");
        newMessage(message);

        LoginResponse response = new LoginResponse(token);
        return response;
    }

    @PostMapping("/logout")
    void logout(@RequestBody LogoutRequest logoutRequest) throws Exception {
        String token = logoutRequest.getUserToken();
        String username = tokenToUserMap.get(token);

        Message message = new Message();
        message.setMessage("has left the chat");
        message.setSender(token);
        message.setChannelName("Main");
        newMessage(message);

        tokenToUserMap.remove(token);
        userActivityMap.remove(username);
    }

    private void verifyUserLogin(String userToken) throws Exception {
        if (!tokenToUserMap.containsKey(userToken)) {
            throw new Exception("User not logged in");
        }
    }

    private boolean isActive(String username) {
        return System.currentTimeMillis() - userActivityMap.getOrDefault(username, 0L) < 30000;
    }

    private void updateUserActivity(String user) {
        userActivityMap.put(user, System.currentTimeMillis());
    }

}
