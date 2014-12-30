package org.panksdmz.storm;

import backtype.storm.topology.TopologyBuilder;

public abstract class TaxAndDiscountTopologyHelper {

    protected static TopologyBuilder getTopology() {
        TopologyBuilder builder = new TopologyBuilder();

        PriceSpout priceSpout = new PriceSpout();
        DiscountBolt discountBolt = new DiscountBolt();
        CalculateTaxBolt calculateTaxBolt = new CalculateTaxBolt();
        CalculateTotalPriceBolt calculateTotalPriceBolt = new CalculateTotalPriceBolt();

        builder.setSpout("priceSpout", priceSpout, 10);
        builder.setBolt("discountBolt", discountBolt, 2).shuffleGrouping("priceSpout");
        builder.setBolt("calculateTaxBolt", calculateTaxBolt, 2).shuffleGrouping("discountBolt");
        builder.setBolt("calculateTotalPriceBolt", calculateTotalPriceBolt, 2).shuffleGrouping("calculateTaxBolt");

        return builder;
    }
}