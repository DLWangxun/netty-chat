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
   * 消息签收状态
   */
  UNSIGN:0,//未签收
  SIGN:1,//签收


  /**
   *
   * @constructor
   */

  SENDERIMG : "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4255756259,662429176&fm=26&gp=0.jpg",
  RECEIVERIMG : "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2495809039,1190312461&fm=15&gp=0.jpg",

  /**
   * 和后端的 ChatMsg 聊天模型对象保持一致
   * @param {Object} senderId
   * @param {Object} receiverId
   * @param {Object} msg
   * @param {Object} msgId
   */
  ChatMsg: function (senderId, receiverId, msg, id,signMark) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.msg = msg;
    this.id = id;
    this.signMark = signMark;
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
  },


}