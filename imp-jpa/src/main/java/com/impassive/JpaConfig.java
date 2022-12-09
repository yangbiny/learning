package com.impassive;

import com.google.common.collect.Lists;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.mode.repository.standalone.StandalonePersistRepositoryConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.ShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;

/**
 * @author impassive
 */
@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = "com.impassive.repository")
public class JpaConfig {

  @Bean
  public List<TableConfig> tableConfigs() {
    List<TableConfig> tableConfigs = new ArrayList<>();
    tableConfigs.add(new TableConfig(
        "buyDataSource",
        "test_shard",
        "external_id",
        2
    ));

/*    tableConfigs.add(new TableConfig(
        "tagDataSource",
        "test_table_tag",
        "external_id",
        1
    ));*/

    return tableConfigs;
  }


  @Bean
  public List<RuleConfiguration> ruleConfigurations(List<TableConfig> tableConfigs) {
    ShardingRuleConfiguration tableRule = getOrderTableRuleConfiguration(tableConfigs);
    return Lists.newArrayList(tableRule);
  }

  @Bean
  public DataSource shardingSphereDataSource(
      DataSource tagDataSource,
      DataSource buyDataSource,
      List<RuleConfiguration> ruleConfigurations
  ) throws SQLException {
    Map<String, DataSource> dataSourceMap = new HashMap<>();
    dataSourceMap.put("tagDataSource", tagDataSource);
    dataSourceMap.put("buyDataSource", buyDataSource);

    /*Properties props = new Properties();
    props.setProperty("provider", "H2");
    props.setProperty("jdbc_url",
        "jdbc:h2:tcp://localhost/~/tmp/h2/config;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;");*/

    // 什么都不给的 情况下，默认是 h2 的 memory 模式
    // JDBC 只 支持 H2
    StandalonePersistRepositoryConfiguration repository = new StandalonePersistRepositoryConfiguration(
        "JDBC", new Properties());

    ModeConfiguration modeConfiguration = new ModeConfiguration("Standalone", repository);
    return ShardingSphereDataSourceFactory.createDataSource(
        "shardingSphere-datasource",
        modeConfiguration,
        dataSourceMap,
        ruleConfigurations,
        new Properties()
    );
  }

  @Bean
  public DataSource tagDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(
        "jdbc:mysql://10.200.68.3:3306/dev_tag?characterEncoding=utf-8&useUnicode=true&zeroDateTimeBehavior=convertToNull&useCursorFetch=true");
    dataSource.setUsername("adm");
    dataSource.setPassword("oK1@cM2]dB2!");
    return dataSource;
  }

  @Bean
  public DataSource buyDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(
        "jdbc:mysql://10.200.68.3:3306/dev_buy?characterEncoding=utf-8&useUnicode=true&zeroDateTimeBehavior=convertToNull&useCursorFetch=true");
    dataSource.setUsername("adm");
    dataSource.setPassword("oK1@cM2]dB2!");
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource shardingSphereDataSource) {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPackagesToScan("com.impassive.entity");
    bean.setDataSource(shardingSphereDataSource);
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(false);
    jpaVendorAdapter.setShowSql(true);
    Properties jpaProperties = new Properties();
    jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
    //jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    bean.setJpaProperties(jpaProperties);
    bean.setJpaVendorAdapter(jpaVendorAdapter);
    return bean;
  }

  /**
   * 如果是需要自动管理 事务，则只能使用  getObject ,如果 使用 getNativeEntityManagerFactory，需要按照如下方式进行事务管理，不然无法 提交数据
   * <code>
   * EntityManagerFactory nativeEntityManagerFactory = emfBean.getNativeEntityManagerFactory();
   * EntityManager entityManager = nativeEntityManagerFactory.createEntityManager();
   * entityManager.getTransaction().begin(); AtlasTagExtra entity = new AtlasTagExtra();
   * entity.setAtlasId(2L); entity.setCreateAt(System.currentTimeMillis()); entity.setRemarks("x");
   * entity.setUpdateAt(System.currentTimeMillis()); entity.setStatus(3);
   * entityManager.persist(entity); entityManager.getTransaction().commit();
   * </code>
   */
  @Bean
  public TransactionManager transactionManager(
      LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
    JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
    jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
    return jpaTransactionManager;
  }


  /* ----------------------- */

  private ShardingRuleConfiguration getOrderTableRuleConfiguration(
      List<TableConfig> tableConfigs
  ) {
    ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

    // 定义 算法 相关
    // 这个是现有的，也可以自定义
    Properties props = new Properties();
    List<ShardingTableRuleConfiguration> strcList = new ArrayList<>();
    for (TableConfig tableConfig : tableConfigs) {
      props.setProperty(tableConfig.magicTableName(), tableConfig.shardCnt() + "");
      strcList.add(buildTableShard(tableConfig));
    }
    AlgorithmConfiguration value = new AlgorithmConfiguration("impassive", props);
    // 这个 my_own 是自定义的算法的名称，下面使用的时候，需要使用这个名称
    shardingRuleConfiguration.getShardingAlgorithms().put("my_own", value);
    // 定义分表策略相关
    shardingRuleConfiguration.setTables(strcList);

    return shardingRuleConfiguration;
  }

  private ShardingTableRuleConfiguration buildTableShard(TableConfig tableConfig) {
    // table 相关的配置
    String actualDataNode;
    if (tableConfig.shardCnt() > 1) {
      actualDataNode = String.format(
          "%s.%s_${[0,%s]}",
          tableConfig.database(),
          tableConfig.magicTableName(),
          tableConfig.shardCnt() - 1);
    } else {
      actualDataNode = String.format("%s.%s", tableConfig.database(), tableConfig.magicTableName());
    }

    ShardingTableRuleConfiguration strc = new ShardingTableRuleConfiguration(
        tableConfig.magicTableName(),
        actualDataNode
    );

    ShardingStrategyConfiguration tableShardingStrategy = new StandardShardingStrategyConfiguration(
        tableConfig.shardColumn(),
        "my_own"
    );

    strc.setTableShardingStrategy(tableShardingStrategy);

    return strc;
  }

}
