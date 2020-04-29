$(function () {
    var FADE_TIME = 150; // ms
    var COLORS = [
        '#e21400', '#91580f', '#f8a700', '#f78b00',
        '#58dc00', '#287b00', '#a8f07a', '#4ae8c4',
        '#3b88eb', '#3824aa', '#a700ff', '#d300e7'
    ];

    // Initialize variables
    var $window = $(window);
    var $usernameInput = $('.usernameInput'); // 昵称
    var $messages = $('.messages'); // 消息区域
    var $inputMessage = $('.inputMessage'); // 消息框
    var $loginPage = $('.login.page'); // 登录页
    var $chatPage = $('.chat.page'); // 聊天室页


    // 构建聊天业务CHAT
    window.CHAT = {
        socket: null,
        record: null,
        unSignMsgIds: null,
        init: function () {
            if (window.WebSocket) {

                // 如果当前的状态已经连接，那就不需要重复初始化websocket
                if (CHAT.socket != null
                    && CHAT.socket != undefined
                    && CHAT.socket.readyState == WebSocket.OPEN) {
                    return false;
                }

                CHAT.socket = new WebSocket("ws://localhost:8011/ws");
                CHAT.socket.onopen = CHAT.wsopen,
                    CHAT.socket.onclose = CHAT.wsclose,
                    CHAT.socket.onerror = CHAT.wserror,
                    CHAT.socket.onmessage = CHAT.wsmessage;
            } else {
                alert("手机设备过旧，请升级手机设备...");
            }
        },
        chat: function (msg) {
            debugger
            // 如果当前websocket的状态是已打开，则直接发送， 否则重连
            if (CHAT.socket != null
                && CHAT.socket != undefined
                && (CHAT.socket.readyState == WebSocket.OPEN || CHAT.socket.readyState == WebSocket.CONNECTING)
            ) {
                CHAT.socket.send(msg);
            } else {
                // 重连websocket
                CHAT.init();
                setTimeout("CHAT.reChat('" + msg + "')", "10000");
            }
        },
        getRecord: function (senderId, receiverId) {
            //发起请求
            $.ajax({
                url: '/chat/record?senderId=' + senderId + '&recieverId=' + receiverId,
                type: 'get',
                dataType: 'json',
                async: false,
                success: function (data) {
                    var result = eval(data);
                    CHAT.record = result.chatRecord;
                    CHAT.unSignMsgIds = result.unSignMsgIds;
                }
            });
        },
        reChat: function (msg) {
            console.log("消息重新发送...");
            CHAT.socket.send(msg);
        },
        wsopen: function () {
            console.log("websocket连接已建立...");
            connected = true;
            // 构建ChatMsg
            var chatMsg = new app.ChatMsg(username, null, null, null);
            // 构建Chat
            var chat = new app.Chat(app.CONNECT, chatMsg, null);
            // 发送websocket
            CHAT.chat(JSON.stringify(chat));
            if(CHAT.unSignMsgIds){
                recordSigned();
            }
            // 定时发送心跳
            setInterval("CHAT.keepalive()", 10000);
        },
        wsmessage: function (e) {
            // 转换Chat为对象
            debugger
            if (e.data == "PONG") {
                console.log("心跳接收到");
                return
            }
            var chat = JSON.parse(e.data);
            addChatMessage({username: chat.chatMsg.senderId, message: chat.chatMsg.msg}, null, "recieve");
        },
        wsclose: function () {
            connected = false;
            CHAT.socket.close();
            console.log("连接关闭...");
        },
        wserror: function () {
            connected = false;
            console.log("发生错误...");
            CHAT.socket.close();
            setTimeout("CHAT.init()", "10000");
        },
        keepalive: function () {
            // 构建对象
            var chat = new app.Chat(app.KEEPALIVE, null, null);

            // 发送心跳
            CHAT.chat(JSON.stringify(chat));
        }
    };


    // Prompt for setting a username
    var username;
    var connected = false; // 连接状态
    var $currentInput = $usernameInput.focus();


    //签收
    function recordSigned(){
        debugger
        $.ajax("/chat/record", {
            type: 'PATCH',
            accepts: 'application/json',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(CHAT.unSignMsgIds),
            processData: false,
            success: function (data) {
                result = eval(data).result;
                if( result == "SUCCESS"){
                    console.log("消息签收成功");
                }else {
                    console.log("消息签收失败");
                }
            }
        })

    }

    // 设置昵称
    function setUsername() {
        debugger;
        // 收到进入房间的响应 包含房间信息
        username = cleanInput($usernameInput.val().trim());
        log(username + "的聊天窗口");
        if (username) {
            $loginPage.fadeOut();
            $chatPage.show();
            $loginPage.off('click');
            $currentInput = $inputMessage.focus();
            if (CHAT.record == null) {
                debugger
                var receiverId = getFriend(username);
                CHAT.getRecord(username, receiverId);
            }
            $.each(CHAT.record, function (n, value) {
                addChatMessage({
                    username: value.senderId,
                    message: value.msg
                }, null, value.senderId == username ? "send" : "recieve");
            });
            CHAT.init();
        }
    }


    // 输出日志信息
    function log(message, options) {
        var $el = $('<li>').addClass('log').text(message);
        addMessageElement($el, options);
    }

    // 输出聊天信息
    function addChatMessage(data, options, msgType) {
        options = options || {};
        var $usernameDiv = $('<span class="username"/>')
            .text(data.username)
            .css('color', getUsernameColor(data.username));
        var $messageBodyDiv = $('<span class="messageBody">')
            .text(data.message);

        var typingClass = data.typing ? 'typing' : '';
        var $messageDiv;
        if (msgType == "send") {
            $messageDiv = $('<li class="sendMsg"/>')
                .data('username', data.username)
                .addClass(typingClass)
                .append($messageBodyDiv, $usernameDiv);
        } else {
            $messageDiv = $('<li class="recieveMsg"/>')
                .data('username', data.username)
                .addClass(typingClass)
                .append($usernameDiv, $messageBodyDiv);
        }

        addMessageElement($messageDiv, options);
    }

    // DOM 操作

    function addMessageElement(el, options) {
        var $el = $(el);

        if (!options) {
            options = {};
        }
        if (typeof options.fade === 'undefined') {
            options.fade = true;
        }
        if (typeof options.prepend === 'undefined') {
            options.prepend = false;
        }

        if (options.fade) {
            $el.hide().fadeIn(FADE_TIME);
        }
        if (options.prepend) {
            $messages.prepend($el);
        } else {
            $messages.append($el);
        }
        $messages[0].scrollTop = $messages[0].scrollHeight;
    }

    // 清除输入框中注入的信息
    function cleanInput(input) {
        return $('<div/>').text(input).html();
    }

    // 通过 hash 函数给用户名上色
    function getUsernameColor(username) {

        var hash = 7;
        for (var i = 0; i < username.length; i++) {
            hash = username.charCodeAt(i) + (hash << 5) - hash;
        }
        // 计算颜色下标
        var index = Math.abs(hash % COLORS.length);
        return COLORS[index];
    }

    //获取聊天好友
    function getFriend(username) {
        var result;
        $.ajax("/friendId/" + username, {
            dataType: 'json',
            type: 'get',//HTTP请求类型
            timeout: 10000,//超时时间设置为10秒；
            data: {},
            async: false,
            success: function (data) {
                result = eval(data).friendId;
            }
        })
        return result;

    }

    // Keyboard events

    $window.keydown(function (event) {
        // 回车后依旧获取焦点
        if (!(event.ctrlKey || event.metaKey || event.altKey)) {
            $currentInput.focus();
        }
        // 监听回车键
        if (event.which === 13) {
            if (username) {
                sendMessage();
            } else {
                setUsername();
            }
        }
    });

    // 获取焦点
    $loginPage.click(function () {
        $currentInput.focus();
    });


    // 发送消息
    function sendMessage() {
        debugger;
        if (connected) {
            // 构建ChatMsg
            var recieveId = getFriend(username);
            var msg = cleanInput($inputMessage.val());
            var chatMsg = new app.ChatMsg(username, recieveId, msg, null);
            // 构建Chat
            var chat = new app.Chat(app.CHAT, chatMsg, null);
            CHAT.chat(JSON.stringify(chat));
            addChatMessage({username: username, message: chat.chatMsg.msg}, null, "send");
            $inputMessage.val("");
        } else {
            log("与服务器断开连接了，刷新重新连接~");
        }

    }

});