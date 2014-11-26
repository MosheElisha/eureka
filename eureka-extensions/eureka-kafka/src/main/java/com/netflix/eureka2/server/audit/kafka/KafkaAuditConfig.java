/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.eureka2.server.audit.kafka;

import com.netflix.governator.annotations.Configuration;

/**
 * Kafka audit service configuration options.
 *
 * @author Tomasz Bak
 */
public class KafkaAuditConfig {

    public static final String KAFKA_SERVERS_KEY = "eureka.ext.audit.kafka.servers";
    public static final String KAFKA_VIP_KEY = "eureka.ext.audit.kafka.vip";
    public static final String KAFKA_PORT_KEY = "eureka.ext.audit.kafka.port";
    public static final String KAFKA_TOPIC_KEY = "eureka.ext.audit.kafka.topic";
    public static final String KAFKA_RETRY_INTERVAL_KEY = "eureka.ext.audit.kafka.retryInterval";
    public static final String KAFKA_MAX_QUEUE_SIZE_KEY = "eureka.ext.audit.kafka.maxQueueSize";

    public static final int DEFAULT_KAFKA_PORT = 7101;
    public static final long DEFAULT_RETRY_INTERVAL_MS = 30000;
    public static final int DEFAULT_MAX_QUEUE_SIZE = 10000;

    /**
     * Kafka server list can be injected directly via configuration, in the following format:
     * host[:port][;host[:port...]]
     *
     * This property is an alternative to {@link #kafkaVip}, with higher priority if both defined.
     */
    @Configuration(KAFKA_SERVERS_KEY)
    private String kafkaServerList;

    /**
     * VIP address resolved in local Eureka registry. This property must be set, unless a list of
     * Kafka servers is defined directly with {@link #kafkaServerList}.
     */
    @Configuration(KAFKA_VIP_KEY)
    private String kafkaVip;

    /**
     * Kafka server port. Optional parameter with default defined by {@link #kafkaPort}.
     */
    @Configuration(KAFKA_PORT_KEY)
    private int kafkaPort;

    /**
     * Kafka topic for audit messages. If not set, Eureka cluster name is used.
     */
    @Configuration(KAFKA_TOPIC_KEY)
    private String kafkaTopic;

    /**
     * Kafka reconnect retry interval.
     */
    @Configuration(KAFKA_RETRY_INTERVAL_KEY)
    private long retryIntervalMs = DEFAULT_RETRY_INTERVAL_MS;

    /**
     * Kafka reconnect retry interval.
     */
    @Configuration(KAFKA_MAX_QUEUE_SIZE_KEY)
    private int maxQueueSize = DEFAULT_MAX_QUEUE_SIZE;

    // For configuration injection
    public KafkaAuditConfig() {
    }

    public KafkaAuditConfig(String kafkaServerList, String kafkaVip, int kafkaPort, String kafkaTopic,
                            long retryIntervalMs, int maxQueueSize) {
        this.kafkaServerList = kafkaServerList;
        this.kafkaVip = kafkaVip;
        this.kafkaPort = kafkaPort;
        this.kafkaTopic = kafkaTopic;
        this.retryIntervalMs = retryIntervalMs;
        this.maxQueueSize = maxQueueSize;
    }

    public String getKafkaServerList() {
        return kafkaServerList;
    }

    public String getKafkaVip() {
        return kafkaVip;
    }

    public int getKafkaPort() {
        return kafkaPort <= 0 ? DEFAULT_KAFKA_PORT : kafkaPort;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public long getRetryIntervalMs() {
        return retryIntervalMs;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void validateConfiguration() {
        if (kafkaServerList == null) {
            if (kafkaVip == null) {
                throw new IllegalArgumentException("Kafka vip not defined via property " + KAFKA_VIP_KEY);
            }
            if (kafkaPort == 0) {
                throw new IllegalArgumentException("Kafka port not defined via property " + KAFKA_PORT_KEY);
            }
        }
    }
}
