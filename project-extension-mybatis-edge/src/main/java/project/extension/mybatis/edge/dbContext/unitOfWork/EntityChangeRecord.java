package project.extension.mybatis.edge.dbContext.unitOfWork;

import project.extension.action.IAction1;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体变更记录
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class EntityChangeRecord {
    /**
     * 变更记录集合
     */
    private final List<ChangeInfo> recordList = new ArrayList<>();

    /**
     * 获取变更记录集合
     *
     * @return 变更记录集合
     */
    public List<ChangeInfo> getRecordList() {
        return this.recordList;
    }

    /**
     * 实体变更事件
     */
    public IAction1<List<ChangeInfo>> onChange;
}
