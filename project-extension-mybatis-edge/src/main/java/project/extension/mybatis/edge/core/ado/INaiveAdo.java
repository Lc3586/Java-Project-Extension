package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.session.SqlSession;
import project.extension.standard.exception.ApplicationException;
import project.extension.tuple.Tuple2;

import javax.sql.DataSource;

/**
 * 数据库访问对象
 *
 * @author LCTR
 * @date 2022-12-22
 */
public interface INaiveAdo {
    /**
     * 获取数据源
     *
     * @return 数据源
     */
    DataSource getDataSource();

    /**
     * 获取或创建Sql会话
     *
     * @return Sql会话
     */
    SqlSession getOrCreateSqlSession()
            throws
            ApplicationException;

    /**
     * 获取当前的Sql会话
     *
     * @return Sql会话
     */
    Tuple2<Boolean, SqlSession> currentSqlSession();
}
