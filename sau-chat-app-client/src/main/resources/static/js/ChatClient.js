var stompClient = null;
var baseAddress = 'http://localhost:8080';

window.onload = function() {
	connect();
	decryptDbMessages();
};

function getCookie(name) {
	const cookieValue = document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)');
	return cookieValue ? cookieValue.pop() : '';
}

async function encryptMessage(message, reciever, sender) {
	var encryptionKey = await pullKey(reciever, sender);
	var encrypted = CryptoJS.AES.encrypt(message, encryptionKey).toString();
	return encrypted;
}

async function decryptMessage(encryptedMessage, reciever, sender) {
	var encryptionKey = await pullKey(reciever, sender);
	var decrypted = CryptoJS.AES.decrypt(encryptedMessage, encryptionKey).toString(CryptoJS.enc.Utf8);
	return decrypted;
}
async function pullKey(receiver, sender) {
	var key = "empty";
	const jwt = getCookie('JWT');
	await $.ajax({
		url: 'http://localhost:9090/key',
		type: 'POST',
		data: { receiver: receiver, sender: sender, jwt: jwt },
		success: function(response) {
			key = response;
		},
		error: function(pullKey, status, error) {
			console.error('pullKey() istek hatası:', error);
		}
	});
	return key;
}



function connect() {
	var socket = new SockJS(baseAddress + '/chat');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		//setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic', function(request) {
			handleReceivedMessage(JSON.parse(request.body));
		});
	});
}

function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

/**
sendMessage operasyonu alıcının adını parametre olarak alıyor ve alıcının adı ile biten id ye 
sahip text box daki mesajı okuyarak json objesini string biçiminde client e gönderiyor
*/
async function sendMessage(receiver) {
	var userName = document.getElementById("userName").getAttribute("userName");
	var text = document.getElementById("text-box-message-" + receiver).value;
	var encryptedText = await encryptMessage(text, receiver, userName);
	stompClient.send("/app/chat", {},
		JSON.stringify({ 'sender': userName, 'receiver': receiver, 'message': encryptedText, 'time': new Date().toLocaleTimeString() }));
}

/**
hideOther Divs operasyonu kullanıcı ekranda hangi kullanıcının ismine tıklar ise o kullanıcıya olan mesajları içeren div i getiriyor
ve diğer divleri gizliyor
 */
function hideOtherDivs(className) {
	console.log(className);
	var elements = document.getElementsByClassName("chatDiv");
	for (var i = 0; i < elements.length; i++) {
		elements[i].style.display = "none";
	}
	var elements = document.getElementsByClassName(className);
	for (var i = 0; i < elements.length; i++) {
		elements[i].style.display = "block";
	}
}
/**
Stompt clientinin subscribe olduğu topic e herhangi bir mesaj geldiğinde çalışan operasyon
Operasyonun amacı gelen requesti inceleyerek ekranda mesajın nerede gözükmesi gerektiğine karar veriyor
 */
async function handleReceivedMessage(request) {
	console.log("UserName: " + document.getElementById("userName").getAttribute("userName") + " Sender: " + request.sender + " Receiver: " + request.receiver + " Boolean: " + (document.getElementById("userName").getAttribute("userName") != request.sender && (document.getElementById("userName").getAttribute("userName") == request.receiver || "main" == request.receiver)))
	if (request.sender == document.getElementById("userName").getAttribute("userName")) {
		request.message = await decryptMessage(request.message, request.receiver, request.sender);
		// <li> öğesi oluşturma
		var li = document.createElement("li");
		li.className = "right clearfix";

		// <span> öğesi oluşturma
		var span = document.createElement("span");
		span.className = "chat-img pull-right";
		var img = document.createElement("img");
		img.src = "https://bootdey.com/img/Content/user_1.jpg";
		img.alt = "User Avatar";
		span.appendChild(img);
		li.appendChild(span);

		// <div> öğesi oluşturma
		var div = document.createElement("div");
		div.className = "chat-body clearfix";

		// <div> içerisindeki <div> öğesi oluşturma
		var divHeader = document.createElement("div");
		divHeader.className = "header";
		var strong = document.createElement("strong");
		strong.className = "primary-font";
		strong.innerText = request.sender;
		var small = document.createElement("small");
		small.className = "pull-right text-muted";
		small.innerHTML = '<i class="fa fa-clock-o"></i> ' + new Date().toLocaleTimeString();
		divHeader.appendChild(strong);
		divHeader.appendChild(small);
		div.appendChild(divHeader);

		// <div> içerisindeki <p> öğesi oluşturma
		var p = document.createElement("p");
		p.innerText = request.message;
		div.appendChild(p);

		li.appendChild(div);

		// Yeni öğeyi sayfaya ekleme
		var chatList = document.getElementById("chat-list-ul-" + request.receiver);
		chatList.appendChild(li);
	}
	else if (document.getElementById("userName").getAttribute("userName") != request.sender && (document.getElementById("userName").getAttribute("userName") == request.receiver || "main" == request.receiver)) {
		request.message = await decryptMessage(request.message, request.receiver, request.sender);
		console.log("message: " + request.message)
		// <li> öğesi oluşturma
		var li = document.createElement("li");
		li.className = "left clearfix";

		// <span> öğesi oluşturma
		var span = document.createElement("span");
		span.className = "chat-img pull-left";
		var img = document.createElement("img");
		img.src = "https://bootdey.com/img/Content/user_3.jpg";
		img.alt = "User Avatar";
		span.appendChild(img);
		li.appendChild(span);

		// <div> öğesi oluşturma
		var div = document.createElement("div");
		div.className = "chat-body clearfix";

		// <div> içerisindeki <div> öğesi oluşturma
		var divHeader = document.createElement("div");
		divHeader.className = "header";
		var strong = document.createElement("strong");
		strong.className = "primary-font";
		strong.innerText = request.sender;
		var small = document.createElement("small");
		small.className = "pull-right text-muted";
		small.innerHTML = '<i class="fa fa-clock-o"></i> ' + new Date().toLocaleTimeString();
		divHeader.appendChild(strong);
		divHeader.appendChild(small);
		div.appendChild(divHeader);

		// <div> içerisindeki <p> öğesi oluşturma
		var p = document.createElement("p");
		p.innerText = request.message;
		div.appendChild(p);

		li.appendChild(div);

		// Yeni öğeyi sayfaya ekleme

		var chatList = document.getElementById("chat-list-ul-" + request.sender);
		if (request.receiver == "main") {
			chatList = document.getElementById("chat-list-ul-" + "main");
		}
		chatList.appendChild(li);
		console.log("ChatList: " + chatList);
	}


}
async function decryptDbMessages() {

	var paragraphs = document.getElementsByClassName('message-text');
	
	for (var i = 0; i < paragraphs.length; i++) {
		var paragraph = paragraphs[i];
		var receiver = paragraph.getAttribute("receiver");
		var sender = paragraph.getAttribute("sender");
		paragraph.innerHTML = await decryptMessage(paragraph.innerHTML, receiver, sender);; 
	}
}
