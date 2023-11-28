const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:4000/ws'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log(frame);
    console.log(stompClient);
    stompClient.subscribe(
        '/topic/session/6564861632dab1025c9ace72',
        (response) => { console.log(response) },
        { id: "6564861632dab1025c9ace72" }
    );
    const playerId = $("#name").val();
    stompClient.subscribe(`/user/${playerId}/error`, (response) => {
        console.error(response)
        stompClient.unsubscribe("6564861632dab1025c9ace72")
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
    stompClient.unsubscribe("6564861632dab1025c9ace72")
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function joinRoom() {
    stompClient.publish({
        destination: "/app/session/6564861632dab1025c9ace72",
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