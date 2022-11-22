package project.extension.hub;

import java.util.Arrays;
import java.util.Optional;

/**
 * 消息类型
 *
 * @author LCTR
 * @date 2022-10-28
 */
public enum MessageType {
    STRING(1,
           "STRING"),
    BUFFER(2,
           "BUFFER");

    /**
     * @param index 索引
     * @param value 值
     */
    MessageType(int index,
                String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * 索引
     */
    final int index;

    /**
     * 值
     */
    final String value;

    /**
     * 获取索引
     *
     * @return 索引
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 获取字符串
     *
     * @return 值
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * 获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static MessageType toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<MessageType> find = Arrays.stream(MessageType.values())
                                           .filter(x -> x.value.equals(value))
                                           .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到符合%s此值的MessageType枚举中",
                                                             value));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static MessageType toEnum(int index)
            throws
            IllegalArgumentException {
        for (MessageType value : MessageType.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}