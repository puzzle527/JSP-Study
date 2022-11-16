<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>웹 소켓 채팅</title>
<script>
	var webSocket
		= new WebSocket("<%= application.getInitParameter("CHAT_ADDR") %>/ChattingServer");
	var chatWindow, chatMessage, chatId;
	
	//채팅창이 열리면 대화창, 메시지 입력창, 대화명 표시란으로 사용할 DOM 객체 저장
	window.onload = function() 
	{
		chatWindow = document.getElementById("chatWindow");
		chatMessage = document.getElementById("chatMessage");
		chatId = document.getElementById("chatId").value;
	}
	//메시지 전송
	function sendMessage()
	{
		chatWindow.innerHTML += "<div class='myMsg'>" + chatMessage.value + "</div>";
		webSocket.send(chatId + '|' + chatMessage.value); //서버로 전송
		chatMessage.value = ""; //메시지 입력창 내용 지우기
		chatWindow.scrollTop = chatWindow.scrollHeight;
	}
	//서버와 연결 종료
	function disconnect() { webSocket.close(); }
	//엔터 키 입력 처리
	function enterKey()
	{
		if(13 == window.event.keyCode)
		{
			sendMessage();	
		}
	}
	//웹소켓 서버에 연결됐을 때 실행
	webSocket.onopen = function(event)
	{
		chatWindow.innerHTML += "웹 소켓 서버에 연결되었습니다.<br/>";
	}
	//웹소켓이 닫혔을 때(서버와의 연결이 끊겼을 때) 실행
	webSocket.onclose = function(event)
	{
		chatWindow.innerHTML += "웹 소켓 서버가 종료되었습니다.<br/>";
	}
	//에러 발생 시 실행
	webSocket.onerror = function(event)
	{
		alert(event);
		console.log(event);
		console.log(webSocket);
		chatWindow.innerHTML += "채팅 중 에러가 발생하였습니다.<br/>";
	}
	//메세지를 받았을 때 실행
	webSocket.onmessage = function(event)
	{
		var message = event.data.split("|"); //대화명과 메시지 분리
		console.log(message);
		var sender = message[0];
		var content = message[1];
		if("" != content)
		{
			//귓속말
			if(content.match("/"))
			{
				//나에게 보낸 메시지만 출력
				if(content.match("/" + chatId))
				{
					var temp = content.replace(("/" + chatId), "[귓속말] : ");
					chatWindow.innerHTML += "<div>" + sender + "" + temp + "</div>";
				}
			}
			else //일반 대화
			{
				chatWindow.innerHTML += "<div>" + sender + " : " + content + "</div>";
			}
		}
		chatWindow.scrollTop = chatWindow.scrollHeight;
	}
</script>
<style>  <!-- 대화창 스타일 지정 -->  
	#chatWindow{border:1px solid black; width:270px; height:310px; overflow:scroll; padding:5px;}
	#chatMessage{width:236px; height:30px;}
	#sendBtn{height:30px; position:relative; top:2px; left:-2px;}
	#closeBtn{margin-bottom:3px; position:relative; top:2px; left:-2px;}
	#chatId{width:158px; height:24px; border:1px solid #AAAAAA; background-color:#EEEEEE;}
	.myMsg{text-align:right;}
</style>
</head>
<body>
	대화명 : <input type="text" id="chatId" value="${ param.chatId }" readonly />
    <button id="closeBtn" onclick="disconnect();">채팅 종료</button>
    <div id="chatWindow"></div>
    <div>
        <input type="text" id="chatMessage" onkeyup="enterKey();">
        <button id="sendBtn" onclick="sendMessage();">전송</button>
    </div>
</body>
</html>