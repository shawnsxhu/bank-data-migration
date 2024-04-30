package com.pilot.srcserver.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic topic1()
    {
        return TopicBuilder.name("migration")
                .partitions(4)
                .replicas(1)
                .build();
    }
}
