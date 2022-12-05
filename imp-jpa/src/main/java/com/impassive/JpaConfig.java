package com.impassive;

import com.google.common.collect.Lists;
import java.sql.SQLException;
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
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;

/**
 * @author impassive
 */
@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = "com.impassive.repository")
public class JpaConfig {

  private ShardingRuleConfiguration getOrderTableRuleConfiguration(
      String databaseName,
      String tableName
  ) {
    ShardingTableRuleConfiguration strc = new ShardingTableRuleConfiguration(tableName,
        String.format("%s.%s_${[0,1]}", databaseName, tableName));

    StandardShardingStrategyConfiguration tableShardingStrategy = new StandardShardingStrategyConfiguration(
        "atlas_id",
        "my_own"
    );
    strc.setTableShardingStrategy(tableShardingStrategy);

    ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
    shardingRuleConfiguration.setTables(Lists.newArrayList(strc));

    Properties props = new Properties();
    props.setProperty("algorithm-expression", "test_${atlas_id % 2}");

    HashMap<String, AlgorithmConfiguration> shardingAlgorithms = new HashMap<>();

    // 这个是现有的，也可以自定义
    AlgorithmConfiguration value = new AlgorithmConfiguration("INLINE",
        props);
    shardingAlgorithms.put("my_own", value);
    shardingRuleConfiguration.setShardingAlgorithms(shardingAlgorithms);

    return shardingRuleConfiguration;
  }

  @Bean
  public List<RuleConfiguration> ruleConfigurations() {
    ShardingRuleConfiguration tableRule = getOrderTableRuleConfiguration("ds_01", "test");
    return Lists.newArrayList(tableRule);
  }

  @Bean
  public DataSource shardingSphereDataSource(
      DataSource dataSource,
      List<RuleConfiguration> ruleConfigurations
  ) throws SQLException {
    Map<String, DataSource> dataSourceMap = new HashMap<>();
    dataSourceMap.put("ds_01", dataSource);
    Properties props = new Properties();
    StandalonePersistRepositoryConfiguration repository = new StandalonePersistRepositoryConfiguration(
        "JDBC", new Properties());
    ModeConfiguration modeConfiguration = new ModeConfiguration("Standalone",
        repository);
    return ShardingSphereDataSourceFactory.createDataSource(
        "ds_01",
        modeConfiguration,
        dataSourceMap,
        ruleConfigurations,
        props
    );
  }

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl(
        "jdbc:h2:tcp://localhost/Users/impassivey/h2/test;USER=sa;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE");
    dataSource.setUsername("sa");
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
    jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
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

}
