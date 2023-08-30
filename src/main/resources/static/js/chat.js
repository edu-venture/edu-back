var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', function(chatMessage) {
            showMessage(JSON.parse(chatMessage.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage() {
    var messageContent = document.getElementById('message-input').value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            content: messageContent,
            sender: "anonymous" // For simplicity, using "anonymous" as sender
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
    }
}

function showMessage(message) {
    var messageList = document.getElementById('message-list');
    var messageElement = document.createElement('li');
    messageElement.innerHTML = message;
    messageList.appendChild(messageElement);
}

// Connect to the websocket upon page load
connect();
