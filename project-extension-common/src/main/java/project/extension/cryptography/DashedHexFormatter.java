package project.extension.cryptography;

/**
 * 格式化为16进制字符串
 *
 * @author LCTR
 * @date 2022-04-02
 * @source MassTransit.NewIdFormatters
 */
public class DashedHexFormatter {
    private final int alpha;

    private final int length;

    private final char prefix;

    private final char suffix;

    public DashedHexFormatter() {
        this('\0', '\0', false);
    }

    public DashedHexFormatter(char prefix, char suffix, boolean upperCase) {
        if (prefix == '\0' || suffix == '\0') {
            this.prefix = 0;
            this.suffix = 0;
            this.length = 36;
        } else {
            this.prefix = prefix;
            this.suffix = suffix;
            this.length = 38;
        }

        this.alpha = (upperCase ? 65 : 97);
    }

    public String Format(byte[] bytes) {
        char[] array = new char[length];
        int i = 0;
        int num = 0;
        if (prefix != 0) {
            array[num++] = prefix;
        }

        for (; i < 4; i++) {
            int num2 = bytes[i];
            array[num++] = hexToChar(num2 >> 4, alpha);
            array[num++] = hexToChar(num2, alpha);
        }

        array[num++] = '-';
        for (; i < 6; i++) {
            int num3 = bytes[i];
            array[num++] = hexToChar(num3 >> 4, alpha);
            array[num++] = hexToChar(num3, alpha);
        }

        array[num++] = '-';
        for (; i < 8; i++) {
            int num4 = bytes[i];
            array[num++] = hexToChar(num4 >> 4, alpha);
            array[num++] = hexToChar(num4, alpha);
        }

        array[num++] = '-';
        for (; i < 10; i++) {
            int num5 = bytes[i];
            array[num++] = hexToChar(num5 >> 4, alpha);
            array[num++] = hexToChar(num5, alpha);
        }

        array[num++] = '-';
        for (; i < 16; i++) {
            int num6 = bytes[i];
            array[num++] = hexToChar(num6 >> 4, alpha);
            array[num++] = hexToChar(num6, alpha);
        }

        if (suffix != 0) {
            array[num] = suffix;
        }

        return new String(array, 0, length);
    }

    private static char hexToChar(int value, int alpha) {
        value &= 0xF;
        return (char) ((value > 9) ? (value - 10 + alpha) : (value + 48));
    }

    public String format(byte[] bytes) {
        return Format(bytes);
    }
}
