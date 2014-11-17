package org.panksdmz.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class TaxAndDiscountTopologyProduction {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        PriceSpout priceSpout = new PriceSpout();
        DiscountBolt discountBolt = new DiscountBolt();
        CalculateTaxBolt calculateTaxBolt = new CalculateTaxBolt();
        CalculateTotalPriceBolt calculateTotalPriceBolt = new CalculateTotalPriceBolt();

        builder.setSpout("priceSpout", priceSpout, 10);
        builder.setBolt("discountBolt", discountBolt, 2).shuffleGrouping("priceSpout");
        builder.setBolt("calculateTaxBolt", calculateTaxBolt, 2).shuffleGrouping("discountBolt");
        builder.setBolt("calculateTotalPriceBolt", calculateTotalPriceBolt, 2).shuffleGrouping("calculateTaxBolt");

        Config conf = new Config();
        conf.setDebug(true);

        try {
            StormSubmitter.submitTopology("TaxAndDiscountTopo", conf, builder.createTopology());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
