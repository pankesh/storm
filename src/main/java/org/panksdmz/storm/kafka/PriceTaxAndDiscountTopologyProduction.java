package org.panksdmz.storm.kafka;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

public class PriceTaxAndDiscountTopologyProduction {

    public static void main(String[] args) {
        TopologyBuilder builder = TaxAndDiscountKafkaTopologyHelper.getTopology();

        Config conf = new Config();
        conf.setDebug(true);

        try {
            StormSubmitter.submitTopology("PriceTaxAndDiscountTopo", conf, builder.createTopology());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
