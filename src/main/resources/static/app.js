const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:4000/ws'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log(frame);
    console.log(stompClient);
    stompClient.subscribe(
        '/topic/session/65661185372ca0018a3ee0ff',
        (response) => { console.log(response) },
        { id: "65661185372ca0018a3ee0ff" }
    );
    const playerId = $("#name").val();
    stompClient.subscribe(`/user/${playerId}/error`, (response) => {
        console.error(response)
        stompClient.unsubscribe("65661185372ca0018a3ee0ff")
    });
}

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.configure({ connectHeaders: { "playerId": $("#name").val() }})
    stompClient.activate();
}

function disconnect() {
    stompClient.unsubscribe("65661185372ca0018a3ee0ff")
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function joinRoom() {
    stompClient.publish({
        destination: "/app/session/65661185372ca0018a3ee0ff",
        body: JSON.stringify({'action': 'JOIN'})
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#joinRoom" ).click(() => joinRoom());
});