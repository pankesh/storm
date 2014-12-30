package org.panksdmz.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class TaxAndDiscountTopology {

    
    public static void main(String[] args) {
        TopologyBuilder builder = TaxAndDiscountTopologyHelper.getTopology();        

        Config conf = new Config();
        conf.setDebug(true);
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("TaxAndDiscountTopo", conf, builder.createTopology());
        Utils.sleep(10000);
        cluster.killTopology("TaxAndDiscountTopo");
        cluster.shutdown();

    }
}
