package org.panksdmz.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

public class TaxAndDiscountTopologyProduction {

    public static void main(String[] args) {

        TopologyBuilder builder = TaxAndDiscountTopologyHelper.getTopology();

        Config conf = new Config();
        conf.setDebug(true);

        try {
            StormSubmitter.submitTopology("TaxAndDiscountTopo", conf, builder.createTopology());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
