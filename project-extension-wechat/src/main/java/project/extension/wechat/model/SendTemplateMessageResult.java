package project.extension.wechat.model;

import lombok.Data;

/**
 * 发送模板消息后返回的信息
 *
 * @author LCTR
 * @date 2023-03-15
 */
@Data
public class SendTemplateMessageResult {
    /**
     * 消息标识
     */
    private String msgId;
}
