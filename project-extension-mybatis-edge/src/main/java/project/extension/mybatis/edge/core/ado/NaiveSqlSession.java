package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.result.DefaultMapResultHandler;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询命令执行对象
 *
 * @author LCTR
 * @date 2023-06-01
 */
public class NaiveSqlSession
        extends DefaultSqlSession {
    public NaiveSqlSession(Configuration configuration,
                           Executor executor,
                           boolean autoCommit) {
        super(configuration,
              executor,
              autoCommit);
    }

    public NaiveSqlSession(Configuration configuration,
                           Executor executor) {
        super(configuration,
              executor);
    }


}
