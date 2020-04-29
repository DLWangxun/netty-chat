window.app = {

  /**
   * 和后端的枚举对应
   */
  CONNECT: 1, 	// 第一次(或重连)初始化连接
  CHAT: 2, 		// 聊天消息
  SIGNED: 3, 		// 消息签收
  KEEPALIVE: 4, 	// 客户端保持心跳
  PULL_FRIEND:5,	// 重新拉取好友

  /**
   * 和后端的枚举对应
   */
  CONNECT: 1, 	// 第一次(或重连)初始化连接
  CHAT: 2, 		// 聊天消息
  SIGNED: 3, 		// 消息签收
  KEEPALIVE: 4, 	// 客户端保持心跳
  PULL_FRIEND:5,	// 重新拉取好友
  /**
   * 和后端的 ChatMsg 聊天模型对象保持一致
   * @param {Object} senderId
   * @param {Object} receiverId
   * @param {Object} msg
   * @param {Object} msgId
   */
  ChatMsg: function (senderId, receiverId, msg, msgId) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.msg = msg;
    this.msgId = msgId;
  },

  /**
   * 构建消息 DataContent 模型对象
   * @param {Object} action
   * @param {Object} chatMsg
   * @param {Object} extand
   */
  Chat: function (action, chatMsg, extand) {
    this.action = action;
    this.chatMsg = chatMsg;
    this.extand = extand;
  }
}