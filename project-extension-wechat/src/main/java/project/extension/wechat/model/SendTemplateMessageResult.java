package project.extension.wechat.model;

/**
 * 发送模板消息后返回的信息
 *
 * @author LCTR
 * @date 2023-03-15
 */
public class SendTemplateMessageResult {
    /**
     * 消息标识
     */
    private String msgId;

    /**
     * 消息标识
     */
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
