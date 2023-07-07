package project.extension.mybatis.edge.aop;

import lombok.Data;

/**
 * 切换数据源时触发的事件的参数
 *
 * @author LCTR
 * @date 2023-07-07
 */
@Data
public class DataSourceChangedEventArgs {
    public DataSourceChangedEventArgs(Long threadId,
                                      String originalDataSource,
                                      String currentDataSource) {
        this.threadId = threadId;
        this.originalDataSource = originalDataSource;
        this.currentDataSource = currentDataSource;
    }

    /**
     * 线程Id
     */
    private Long threadId;

    /**
     * 原来的数据源
     */
    private String originalDataSource;

    /**
     * 现在的数据源
     */
    private String currentDataSource;
}
