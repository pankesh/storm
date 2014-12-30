package org.panksdmz.storm.kafka;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class PriceTaxAndDiscountTopology {

    

    public static void main(String[] args) {
        
        TopologyBuilder builder = TaxAndDiscountKafkaTopologyHelper.getTopology();
        
        Config conf = new Config();
        conf.setDebug(true);
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("PriceTaxAndDiscountTopo", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("PriceTaxAndDiscountTopo");
        cluster.shutdown();

    }
}
