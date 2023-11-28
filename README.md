# *6 nimmt!* Web Application Server

## About the project

This is the backend server for my implementation of a card game called "*6 nimmt*" web application.

A short summary of the game taken from <a href="https://en.wikipedia.org/wiki/6_nimmt!">Wikipedia</a>:  <br>
*6 nimmt! / Take 6!* is a card game for 2 - 10 players. It has 104 cards, each bearing a number and one to seven bull's 
heads symbols that represent penalty points. A round of ten turns (might make this configurable in this implementation)
is played where all players place one card of their choice onto the table. The placed cards are arranged on four rows 
according to fixed rules. If placed onto a row that already has five cards then the player receives those five cards, 
which count as penalty points that are totaled up at the end of the round. Rounds are played until a player reaches 66 
points, whereupon the player with the least penalty points wins.

### What has been done so far:

- Create rooms/sessions for players and save the session to database
- Use `websocket` to provide live updates of specific sessions 
  - e.g. send out a message when a new player joins the session
- Create a path that is user specific to send out user specific messages
  - e.g. player unable to join the session because the session is full
- Create REST API to get all sessions, get session by id, create session

### Technology used
- `Java 17` using `Spring Boot 3.2.0` and `Maven`
- `MongoDB` as data persistence layer

## Getting Started

### Prerequisite

You will need to have `mongodb-community` running first. For Mac, run:
```
brew services start mongodb-community
```

### Get the code

```
git clone https://github.com/Saphly/6-nimmt.git
```

### Install dependencies

```
mvn clean dependency:copy-dependencies
```

### Run the server
```
mvn spring-boot:run
```

### Run tests
```
mvn test
```

## API Endpoints

`GET` `/sessions`
Returns all sessions in JSON format. For example,
```
[
    {
        "_id": "65661185372ca0018a3ee0ff",
        "name": "Test3",
        "minPlayers": 2,
        "maxPlayers": 10,
        "players": [
            "markdown"
        ],
        "status": "OPEN"
    }
]
```

`GET` `/sessions/{sessionId}` Returns specific session with valid id. Returns status code `404` if session is not found.
```
[
    {
        "_id": "65661185372ca0018a3ee0ff",
        "name": "Test3",
        "minPlayers": 2,
        "maxPlayers": 10,
        "players": [
            "markdown"
        ],
        "status": "OPEN"
    }
]
```

`POST` `/sessions` Creates a session, must provide a name field.
```
Must provide
{
  "name": "New"
}

Returns
[
    {
        "_id": "65661185372ca0018a3ee0ff",
        "name": "Test3",
        "minPlayers": 2,
        "maxPlayers": 10,
        "players": [],
        "status": "OPEN"
    }
]
```

### Reflection

This has been a great journey and learning experience for me. As I have a `Javascript` background, 
building a backend server in `Java` has proved to be challenging. Nonetheless, I am proud of what I have achieved so far.

While I did try to develop this in a test-driven development (TDD) manner,  I found that it was difficult to do so as I 
lack the familiarity of `Spring Boot` and how things work. It ended up being a trial and error experience instead, especially 
setting up the `websocket`. With that said, I do have some unit tests for the `SessionService` which contains 
the most business logic. 

Plans for the future: 
- Have integration/unit tests for controllers
- Find out how to test websockets
- Unsubscribe from sessions
- Implement game logic (default configs, 2 players)
- Create the frontend for it!