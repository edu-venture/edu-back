var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/lecture/123', function(chatMessage) {
            showMessage(JSON.parse(chatMessage.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");

    // Reset the buttons
    document.getElementById('connect').disabled = false;
    document.getElementById('send').disabled = true;
}


function sendMessage() {
    var messageContent = document.getElementById('msg').value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            content: messageContent,
            sender: "김민제" // For simplicity, using "anonymous" as sender
        };
        stompClient.send("/app/sendMsg/{lectureId}", {}, JSON.stringify(chatMessage));
        // lectureId, userId
    }
}

function showMessage(message) {
    var messageList = document.getElementById('message-list');
    var messageElement = document.createElement('li');
    messageElement.innerHTML = message;
    messageList.appendChild(messageElement);
}

// Connect to the websocket upon page load

function handleConnectClick() {
    connect();

    // Disable the "연결" button and enable the "보내기" button after connection.
    document.getElementById('connect').disabled = true;
    document.getElementById('send').disabled = false;
}
