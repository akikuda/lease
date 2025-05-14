package com.toki.web.app.config.AiConfig;

import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import reactor.core.publisher.Flux;
 
/**
 * 自定义AI异常重试顾问
 *  * @author toki
 *  * @since  2025/5/8 15:21
 *  * @version 1.0
 */
public class AiRetryAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    private final RetryTemplate retryTemplate;
 
    public AiRetryAdvisor() {
        this.retryTemplate = new RetryTemplate();
 
        // 配置重试策略：最多2次，间隔1秒
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(2);
 
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        // 1秒
        backOffPolicy.setBackOffPeriod(1000);
 
        retryTemplate.setRetryPolicy(policy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
    }
 
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        return retryTemplate.execute(context -> advisedResponse);
    }
 
    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        return chain.nextAroundStream(advisedRequest);
    }
 
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
 
    @Override
    public int getOrder() {
        return 0;
    }
}