package com.bootcamp.chatroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatroomApplication.class, args);
	}
// curl -X POST localhost:8080/message -H 'Content-type:application/json' -d '{"message": "Samwise Gamgee", "user": "gardener"}'
// curl -X POST localhost:8080/messages -H 'Content-type:application/json' -d '{"messageId": null, "limit": 0}'
}
