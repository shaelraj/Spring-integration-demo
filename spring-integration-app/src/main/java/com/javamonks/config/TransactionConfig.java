/*
package com.javamonks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.*;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    private final TransactionManager transactionManager;

    public TransactionConfig(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Bean
    public TransactionAttributeSource txAttributeSource() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
*/
/*
        // transfer* : REQUIRED + DEFAULT isolation + rollback-for Exception
        RuleBasedTransactionAttribute transferTx = new RuleBasedTransactionAttribute();
        transferTx.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        transferTx.setIsolationLevelName("ISOLATION_DEFAULT");
        transferTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));

        // get* : read-only
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);*//*


        // * : REQUIRED
        RuleBasedTransactionAttribute defaultTx = new RuleBasedTransactionAttribute();
        defaultTx.setPropagationBehaviorName("PROPAGATION_REQUIRED");

//        source.addTransactionalMethod("transfer*", transferTx);
//        source.addTransactionalMethod("get*", readOnlyTx);
        source.addTransactionalMethod("*", defaultTx);
        return source;
    }


    @Bean
    public TransactionAttributeSourceAdvisor txAdvice(TransactionAttributeSource txAttributeSource) {
        TransactionAttributeSourceAdvisor advisor = new TransactionAttributeSourceAdvisor();
        advisor.setTransactionInterceptor(new TransactionInterceptor(transactionManager, txAttributeSource));
        advisor.setOrder(Ordered.LOWEST_PRECEDENCE);
        return advisor;
    }


   */
/* @Bean
    public BeanNameAutoProxyCreator txProxy() {
        BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
        // apply to service beans (adjust as needed)
        proxyCreator.setBeanNames("*Service");
        proxyCreator.setInterceptorNames("txAdvisor");
        return proxyCreator;
    }*//*

}
*/
