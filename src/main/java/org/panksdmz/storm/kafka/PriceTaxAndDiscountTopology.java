package org.panksdmz.storm.kafka;

import org.panksdmz.storm.CalculateTaxBolt;
import org.panksdmz.storm.CalculateTotalPriceBolt;
import org.panksdmz.storm.DiscountBolt;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class PriceTaxAndDiscountTopology {

    
    private static final String CALCULATE_TOTAL_PRICE_BOLT = "calculateTotalPriceBolt";
    private static final String CALCULATE_TAX_BOLT = "calculateTaxBolt";
    private static final String DISCOUNT_BOLT = "discountBolt";
    private static final String PRODUCT_PRICE_KAFKA_SPOUT = "productPriceKafkaSpout";

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        
        DiscountBolt discountBolt = new DiscountBolt();
        CalculateTaxBolt calculateTaxBolt = new CalculateTaxBolt();
        CalculateTotalPriceBolt calculateTotalPriceBolt = new CalculateTotalPriceBolt();

        builder.setSpout(PRODUCT_PRICE_KAFKA_SPOUT, ProductPriceKafkaSpoutBuilder.configureKafkaSpout(), 10);
        builder.setBolt(DISCOUNT_BOLT, discountBolt, 2).shuffleGrouping(PRODUCT_PRICE_KAFKA_SPOUT);
        builder.setBolt(CALCULATE_TAX_BOLT, calculateTaxBolt, 2).shuffleGrouping(DISCOUNT_BOLT);
        builder.setBolt(CALCULATE_TOTAL_PRICE_BOLT, calculateTotalPriceBolt, 2).shuffleGrouping(CALCULATE_TAX_BOLT);
        

        Config conf = new Config();
        conf.setDebug(true);
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("PriceTaxAndDiscountTopo", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("PriceTaxAndDiscountTopo");
        cluster.shutdown();

    }
}
