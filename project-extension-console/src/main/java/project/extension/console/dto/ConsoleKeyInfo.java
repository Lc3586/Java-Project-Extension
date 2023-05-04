package project.extension.console.dto;

/**
 * 控制台按下的键
 *
 * @author LCTR
 * @date 2023-05-04
 */
public enum ConsoleKeyInfo {
    /**
     * 回退键
     */
    Backspace(0),
    /**
     * 向左键头
     */
    LeftArrow(1),
    /**
     * 向右键头
     */
    RightArrow(2),
    /**
     * 删除键
     */
    Delete(3),
    /**
     * 回车键
     */
    Enter(10),
    /**
     * 其他按键
     */
    Other(-1);

    /**
     * @param index 索引
     */
    ConsoleKeyInfo(int index) {
        this.index = index;
    }

    /**
     * 索引
     */
    final int index;

    /**
     * 获取索引
     *
     * @return 索引
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static ConsoleKeyInfo toEnum(int index)
            throws
            IllegalArgumentException {
        for (ConsoleKeyInfo value : ConsoleKeyInfo.values()) {
            if (value.getIndex() == index)
                return value;
        }
        return ConsoleKeyInfo.Other;
    }
}