//
$(document).ready(function() {
    checkClassSchedule();
});

function checkClassSchedule() {
    $.ajax({
        url: '/attendance/check',
        type: 'GET',
        data: {
            "id": 2,
            "name": "이히자"
        }, // 데이터를 JSON 문자열로 변환
        success: function(data) {
            console.log(data);
            console.log(data.item.name);
            if (data.item.bool === "T") {
                $('#studentName').text("오늘은 " + data.item.name + " 학생의 수업일입니다.");
            } else {
                $('#studentName').text("오늘 " + data.item.name + " 학생은 수업이 없습니다.");
                $('#enterButton').prop('disabled', true);
                $('#exitButton').prop('disabled', true);
            }
        },
        error: function(error) {
            console.log("Error: ", error);
        }
    });
}




function registerEnter() {
    $.ajax({
        url: '/attendance/enter',
        type: 'POST',
        data: JSON.stringify({
            "id": 2,
            "name": "이히자"
        }),
        contentType: 'application/json',
        success: function(data) {
            var date = new Date(data.attStart);
            var formattedStartTime = `${date.getHours()}시 ${date.getMinutes()}분`;
            var datePart = `${date.getMonth() + 1}월 ${date.getDate()}일`
            $('#enterTimeLabel').text("입실 시간: " + formattedStartTime);
            // $('#studentName').text( "오늘은 " + data.user.name + "학생의 수업일입니다.");

            console.log(data.attStart);
            console.log(data.attContent);

            },
        error: function(error) {
            console.log("Error: ", error);
        }
    });

}

function registerExit() {

    $.ajax({
        url: '/attendance/exit',
        type: 'POST',
        data: JSON.stringify({
            "id": 2,
            "name": "이히자"
        }),
        contentType: 'application/json',
        success: function(data) {
            var date = new Date(data.attFinish);
            var formattedFinishTime = `${date.getHours()}시 ${date.getMinutes()}분`;
            var datePart = `${date.getMonth() + 1}월 ${date.getDate()}일`
            $('#exitTimeLabel').text("퇴실 시간: " + formattedFinishTime);
            $('#statusLabel').text(data.user.name + " 학생은 금일 " + data.attContent + "입니다.");
        },
        error: function(error) {
            console.log("Error: ", error);
        }
    });
}