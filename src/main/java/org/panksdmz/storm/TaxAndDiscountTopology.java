package org.panksdmz.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class TaxAndDiscountTopology {

    
    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        
        PriceSpout priceSpout = new PriceSpout();
        DiscountBolt discountBolt = new DiscountBolt();
        CalculateTaxBolt calculateTaxBolt = new CalculateTaxBolt();
        CalculateTotalPriceBolt calculateTotalPriceBolt = new CalculateTotalPriceBolt();

        builder.setSpout("priceSpout", priceSpout,10);
        builder.setBolt("discountBolt", discountBolt, 2).shuffleGrouping("priceSpout");
        builder.setBolt("calculateTaxBolt", calculateTaxBolt, 2).shuffleGrouping("discountBolt");
        builder.setBolt("calculateTotalPriceBolt", calculateTotalPriceBolt, 2).shuffleGrouping("calculateTaxBolt");
        

        Config conf = new Config();
        conf.setDebug(true);
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("TaxAndDiscountTopo", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("TaxAndDiscountTopo");
        cluster.shutdown();

    }
}
