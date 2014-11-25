package org.panksdmz.storm.kafka;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import backtype.storm.spout.SchemeAsMultiScheme;

public final class ProductPriceKafkaSpoutBuilder {

    private static final String ZOOKEEPER_HOST_PORT = "localhost:2181";
    private static final String KAFKA_TOPIC_NAME = "productPriceTopic";
    private static final String PRODUCT_PRICE_ZK_ROOT = "/product_price_spout";

    public static KafkaSpout configureKafkaSpout() {
        KafkaSpout kafkaSpout = new MyKafkaSpout(constructKafkaSpoutConfig());
        return kafkaSpout;
    }

    private static SpoutConfig constructKafkaSpoutConfig() {
        BrokerHosts hosts = new ZkHosts(ZOOKEEPER_HOST_PORT);
        String topic = KAFKA_TOPIC_NAME;
        String zkRoot = PRODUCT_PRICE_ZK_ROOT;
        String consumerGroupId = "productPriceGroup";

        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);
        spoutConfig.scheme = new SchemeAsMultiScheme(new PriceScheme());

        return spoutConfig;

    }
}
