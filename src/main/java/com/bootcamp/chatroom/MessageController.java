package com.bootcamp.chatroom;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class MessageController {
    private final MessageRepository repository;

    MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/message")
    Message newMessage(@RequestBody Message message) {
        message.setTimestamp(ZonedDateTime.now());
        return repository.save(message);
    }

    @PostMapping("/messages")
    List<Message> retrieveMessages(@RequestBody MessageRequest messageRequest) {
        int limit = messageRequest.getLimit();
        if (messageRequest.getMessageId() == null) {
            List<Message> all = repository.findAll();
            return all.subList(all.size() - limit, all.size());
        }
        return repository.findByIdGreaterThan(messageRequest.getMessageId());
    }
}
