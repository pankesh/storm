package org.panksdmz.storm.trident;

import org.panksdmz.storm.CalculateTaxBolt;
import org.panksdmz.storm.CalculateTotalPriceBolt;
import org.panksdmz.storm.DiscountBolt;
import org.panksdmz.storm.kafka.ProductPriceKafkaSpoutBuilder;

import storm.trident.TridentTopology;
import backtype.storm.topology.TopologyBuilder;

public abstract class TridentTaxAndDiscountKafkaTopologyHelper {

    private static final String CALCULATE_TOTAL_PRICE_BOLT = "calculateTotalPriceBolt";
    private static final String CALCULATE_TAX_BOLT = "calculateTaxBolt";
    private static final String DISCOUNT_BOLT = "discountBolt";
    private static final String PRODUCT_PRICE_KAFKA_SPOUT = "productPriceKafkaSpout";

    protected static TopologyBuilder getTopology() {
//        TridentTopology tridentTopology = new TridentTopology();
//        
//        TopologyBuilder builder = new TopologyBuilder();
//
//        DiscountBolt discountBolt = new DiscountBolt();
//        CalculateTaxBolt calculateTaxBolt = new CalculateTaxBolt();
//        CalculateTotalPriceBolt calculateTotalPriceBolt = new CalculateTotalPriceBolt();
//
//        builder.setSpout(PRODUCT_PRICE_KAFKA_SPOUT, ProductPriceKafkaSpoutBuilder.configureKafkaSpout(), 10);
//        builder.setBolt(DISCOUNT_BOLT, discountBolt, 2).shuffleGrouping(PRODUCT_PRICE_KAFKA_SPOUT);
//        builder.setBolt(CALCULATE_TAX_BOLT, calculateTaxBolt, 2).shuffleGrouping(DISCOUNT_BOLT);
//        builder.setBolt(CALCULATE_TOTAL_PRICE_BOLT, calculateTotalPriceBolt, 2).shuffleGrouping(CALCULATE_TAX_BOLT);
//
//
//        return builder;
        return null;
    }
}