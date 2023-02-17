package project.extension.mybatis.edge.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.model.DbType;

/**
 * 连接池配置
 *
 * @author LCTR
 * @date 2022-12-15
 */
@Component
@ConfigurationProperties("spring.datasource.druid")
public class DruidConfig {
    /**
     * 初始连接数
     * <p>默认值：5</p>
     */
    private int initialSize = 5;

    /**
     * 连接池最小连接数
     * <p>默认值：10</p>
     */
    private int minIdle = 10;

    /**
     * 连接池最大连接数
     * <p>默认值：20</p>
     */
    private int maxActive = 20;

    /**
     * 获取连接等待超时的时间
     * <p>默认值：60000</p>
     */
    private int maxWait = 60000;

    /**
     * 检测需要关闭的空闲连接的时间间隔（单位：毫秒）
     * <p>默认值：60000</p>
     */
    private int timeBetweenEvictionRunsMillis = 60000;

    /**
     * 每个连接在池中的最小生存时间（单位：毫秒）
     * <p>默认值：300000</p>
     */
    private int minEvictableIdleTimeMillis = 300000;

    /**
     * 每个连接在池中的最大生存时间（单位：毫秒）
     * <p>默认值：900000</p>
     */
    private int maxEvictableIdleTimeMillis = 900000;

    /**
     * 用于检测连接是否有效的Sql语句
     * <p>默认值：如果是Oracle/Dameng数据库，则值为"select * from dual"；如果是其他数据库，则值为"SELECT 1"；</p>
     */
    private String validationQuery;

    /**
     * 是否在申请连接的时候进行检测
     * <p>如果启用此配置，当应用向连接池申请连接时，连接池将会判断连接是否处于空闲状态（空闲时间大于timeBetweenEvictionRunsMillis配置的值），如果是，则验证这条连接是否可用（执行validationQuery配置的Sql语句）</p>
     * <p>由于程序只在空闲时进行检查，所以启用此配置几乎不会影响性能</p>
     * <p>启用testWhileIdle配置会使此配置无效</p>
     * <p>默认值：true</p>
     */
    private boolean testWhileIdle = true;

    /**
     * 是否在申请连接的时候进行检测
     * <p>如果启用此配置，则程序会确保每个连接都是有效的，但同时也可能会降低性能</p>
     * <p>由于每次申请连接都会进行检测，所以启用此配置会影响性能</p>
     * <p>启用此配置会使testWhileIdle配置无效</p>
     * <p>默认值：false</p>
     */
    private boolean testOnBorrow = false;

    /**
     * 是否在归还连接的时候进行检测
     * <p>如果启用此配置，则在连接结束使用，并被连接池回收时验证这条连接是否还可用（执行validationQuery配置的Sql语句）</p>
     * <p>由于每次申请连接都会进行检测，所以启用此配置会影响性能</p>
     * <p>默认值：false</p>
     */
    private boolean testOnReturn = false;

    /**
     * 初始连接数
     * <p>默认值：5</p>
     */
    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    /**
     * 连接池最小连接数
     * <p>默认值：10</p>
     */
    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * 连接池最大连接数
     * <p>默认值：20</p>
     */
    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * 获取连接等待超时的时间
     * <p>默认值：60000</p>
     */
    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * 检测需要关闭的空闲连接的时间间隔（单位：毫秒）
     * <p>默认值：60000</p>
     */
    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * 每个连接在池中的最小生存时间（单位：毫秒）
     * <p>默认值：300000</p>
     */
    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * 每个连接在池中的最大生存时间（单位：毫秒）
     * <p>默认值：900000</p>
     */
    public int getMaxEvictableIdleTimeMillis() {
        return maxEvictableIdleTimeMillis;
    }

    public void setMaxEvictableIdleTimeMillis(int maxEvictableIdleTimeMillis) {
        this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
    }

    /**
     * 用于检测连接是否有效的Sql语句
     * <p>默认值：如果是Oracle/Dameng数据库，则值为"select * from dual"；如果是其他数据库，则值为"SELECT 1"；</p>
     */
    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    /**
     * 是否在申请连接的时候进行检测
     * <p>如果启用此配置，当应用向连接池申请连接时，连接池将会判断连接是否处于空闲状态（空闲时间大于timeBetweenEvictionRunsMillis配置的值），如果是，则验证这条连接是否可用（执行validationQuery配置的Sql语句）</p>
     * <p>由于程序只在空闲时进行检查，所以启用此配置几乎不会影响性能</p>
     * <p>启用testWhileIdle配置会使此配置无效</p>
     * <p>默认值：true</p>
     */
    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    /**
     * 是否在申请连接的时候进行检测
     * <p>如果启用此配置，则程序会确保每个连接都是有效的，但同时也可能会降低性能</p>
     * <p>由于每次申请连接都会进行检测，所以启用此配置会影响性能</p>
     * <p>启用此配置会使testWhileIdle配置无效</p>
     * <p>默认值：false</p>
     */
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    /**
     * 是否在归还连接的时候进行检测
     * <p>如果启用此配置，则在连接结束使用，并被连接池回收时验证这条连接是否还可用（执行validationQuery配置的Sql语句）</p>
     * <p>由于每次申请连接都会进行检测，所以启用此配置会影响性能</p>
     * <p>默认值：false</p>
     */
    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    /**
     * 应用配置到数据源
     *
     * @param datasource 数据源
     * @return 数据源
     */
    public DruidDataSource applyConfig(DruidDataSource datasource,
                                       DbType dbType) {
        datasource.setInitialSize(this.getInitialSize());
        datasource.setMaxActive(this.getMaxActive());
        datasource.setMinIdle(this.getMinIdle());
        datasource.setMaxWait(this.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(this.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(this.getMinEvictableIdleTimeMillis());
        datasource.setMaxEvictableIdleTimeMillis(this.getMaxEvictableIdleTimeMillis());
        if (StringUtils.hasText(this.getValidationQuery()))
            datasource.setValidationQuery(this.getValidationQuery());
        else {
            switch (dbType) {
                case JdbcOracle19c:
                case JdbcDameng6:
                case JdbcDameng7:
                case JdbcDameng8:
                    datasource.setValidationQuery("select * from dual");
                    break;
                default:
                    datasource.setValidationQuery("select 1");
                    break;
            }
        }
        datasource.setTestWhileIdle(this.isTestWhileIdle());
        datasource.setTestOnBorrow(this.isTestOnBorrow());
        datasource.setTestOnReturn(this.isTestOnReturn());
        return datasource;
    }
}
