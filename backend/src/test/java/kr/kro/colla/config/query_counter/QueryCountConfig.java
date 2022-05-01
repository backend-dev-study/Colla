package kr.kro.colla.config.query_counter;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class QueryCountConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:testdb")
                .username("sa")
                .password("")
                .build();
    }

    @Bean
    public Counter counter() {
        return new Counter();
    }

    @Bean
    public DataSource queryCountDataSource() {
        return new QueryCountDataSource(dataSource(), counter());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(queryCountDataSource());
        em.setPackagesToScan(
                "kr.kro.colla.task.task.domain",
                "kr.kro.colla.project.project.domain",
                "kr.kro.colla.project.task_status.domain",
                "kr.kro.colla.story.domain",
                "kr.kro.colla.user.user.domain",
                "kr.kro.colla.user.notice.domain",
                "kr.kro.colla.comment.domain",
                "kr.kro.colla.meeting_place.meeting_place.domain",
                "kr.kro.colla.meeting_place.mentioned_post.domain",
                "kr.kro.colla.task.history.domain",
                "kr.kro.colla.task.tag.domain",
                "kr.kro.colla.task.task_tag.domain",
                "kr.kro.colla.user_project.domain",
                "kr.kro.colla.task.task_status_log.domain"
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(jpaProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    private Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class.getName());
        return props;
    }

}
