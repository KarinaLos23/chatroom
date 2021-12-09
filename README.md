# chatroom

# commands to query the server

when it's hosted locally:
```
curl -X POST localhost:8080/login -H 'Content-type:application/json' -d '{"username": "karina"}'
curl -X POST localhost:8080/logout -H 'Content-type:application/json' -d '{"userToken": "<tokenRetrievedViaLogin>"}'
curl -X POST localhost:8080/message -H 'Content-type:application/json' -d '{"message": "Samwise Gamgee", "user": "<tokenRetrievedViaLogin>"}'
curl -X POST localhost:8080/messages -H 'Content-type:application/json' -d '{"messageId": null, "limit": 0, "userToken": "<tokenRetrievedViaLogin>"}'
```

on heroku (current):
```
curl -X POST https://java-bootcamp-chatroom.herokuapp.com/message -H 'Content-type:application/json' -d '{"message": "Have a good day!", "user": "karina"}'
curl -X POST https://java-bootcamp-chatroom.herokuapp.com/messages -H 'Content-type:application/json' -d '{"messageId": null, "limit": 1}'
```

on heroku (later):
```
curl -X POST https://java-bootcamp-chatroom.herokuapp.com/login -H 'Content-type:application/json' -d '{"username": "gardener"}'
curl -X POST https://java-bootcamp-chatroom.herokuapp.com/logout -H 'Content-type:application/json' -d '{"userToken": "<tokenRetrievedViaLogin>"}'
curl -X POST https://java-bootcamp-chatroom.herokuapp.com/message -H 'Content-type:application/json' -d '{"message": "Samwise Gamgee", "user": "<tokenRetrievedViaLogin>"}'
curl -X POST https://java-bootcamp-chatroom.herokuapp.com/messages -H 'Content-type:application/json' -d '{"messageId": null, "limit": 0, "userToken": "<tokenRetrievedViaLogin>"}'
```
