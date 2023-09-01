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

    let currentUserName = "";
    let token = "";

    // 페이지 로드시 사용자 이름을 가져옵니다.
    $(document).ready(function() {
        $.ajax({
            url: "/lecUser/getCurrentUserName",
            type: 'GET',
            headers: {
                'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhIiwiaXNzIjoidG9kbyBib290IGFwcCIsImlhdCI6MTY5MzQ4NDY0NSwiZXhwIjoxNjkzNTcxMDQ1fQ.zS-m2xuO8vjaUgtmhdQq4i8DfM4WvaNGgCQ994mR9Sw'
            },
            success: function(data) {
                currentUserName = data.userName;
            },
            error: function(error) {
                console.error("Error fetching username:", error);
            }
        });

        handleConnectClick();

    });

    var messageContent = document.getElementById('msg').value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            content: messageContent,
            sender: currentUserName // For simplicity, using "anonymous" as sender
        };
        stompClient.send("/app/sendMsg/123", {}, JSON.stringify(chatMessage));
        // lectureId, userId
        // 입력한 메시지를 아래의 #communicate에 추가합니다.
        let communicateDiv = document.getElementById("communicate");
        let messageDiv = document.createElement("div");
        messageDiv.innerText = sender + ": " + messageContent;
        communicateDiv.appendChild(messageDiv);

    }


    // 입력 필드를 비웁니다.
    document.getElementById("msg").value = "";
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
