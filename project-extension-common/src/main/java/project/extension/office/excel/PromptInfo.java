package project.extension.office.excel;

/**
 * 提示信息
 *
 * @author LCTR
 * @date 2022-05-09
 */
public class PromptInfo {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 标题
     */
    public PromptInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 内容
     */
    public PromptInfo setContent(String content) {
        this.content = content;
        return this;
    }
}
