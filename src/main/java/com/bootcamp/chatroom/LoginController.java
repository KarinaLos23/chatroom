//package com.bootcamp.chatroom;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class LoginController {
//    private Map<String, String> tokenToUserMap = new HashMap<>();
//    private Map<String, Long> userActivityMap = new HashMap<>();
//
//    @PostMapping("/login")
//    LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
//        if (isTimedOut(loginRequest.getUsername())) {
//            throw new Exception("User already signed in");
//        }
//        String token = UUID.randomUUID().toString();
//        tokenToUserMap.put(token, loginRequest.getUsername());
//        updateUserActivity(loginRequest.getUsername());
//
//        Message message = new Message();
//        message.setMessage(loginRequest.getUsername() + " has joined the chat");
//        message.setSender(token);
//        newMessage(message);
//
//        LoginResponse response = new LoginResponse(token);
//        return response;
//    }
//
//    @PostMapping("/logout")
//    void logout(@RequestBody LogoutRequest logoutRequest) throws Exception {
//        String token = logoutRequest.getUserToken();
//        String username = tokenToUserMap.get(token);
//
//        Message message = new Message();
//        message.setMessage(username + " left the chat");
//        message.setSender(token);
//        newMessage(message);
//
//        tokenToUserMap.remove(token);
//    }
//
//    private void verifyUserLogin(String userToken) throws Exception {
//        if (!tokenToUserMap.containsKey(userToken)) {
//            throw new Exception("User not logged in");
//        }
//    }
//
//    private boolean isTimedOut(String username) {
//        return System.currentTimeMillis() - userActivityMap.getOrDefault(username, 0L) < 30000;
//    }
//
//    private void updateUserActivity(String user) {
//        userActivityMap.put(user, System.currentTimeMillis());
//    }
//}
