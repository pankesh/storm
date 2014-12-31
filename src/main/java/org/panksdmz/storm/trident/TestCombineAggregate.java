package org.panksdmz.storm.trident;

import storm.trident.TridentTopology;
import storm.trident.operation.BaseFilter;
import storm.trident.operation.CombinerAggregator;
import storm.trident.operation.builtin.Debug;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.tuple.TridentTuple;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * This test class shows how to use a custom {@link CombinerAggregator} - {@link CompleteTupleSum}
 */
public class TestCombineAggregate {

    // partitionAggregate runs a function on each partition of a batch of tuples. Unlike functions, the tuples emitted
    // by partitionAggregate replace the input tuples given to it.
    public static class MyFilter extends BaseFilter {
        private static final long serialVersionUID = 8154313714714442736L;

        @Override
        public boolean isKeep(TridentTuple tuple) {
            return tuple.getInteger(0) == 1 && tuple.getInteger(1) == 2;
        }
    }

    public static StormTopology buildTopology(LocalDRPC drpc) {
        @SuppressWarnings("unchecked")
//        @formatter:off
        // Partition size is 2. Hence aggregate will run on that partition to calculate the sum.
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("a", "b", "c"), 2, 
                new Values("x", 2, 3), 
                new Values("x", 1, 1),
                new Values("y", 4, 7),
                new Values("z", 6, 2),
                new Values("y", 3, 1),
                new Values("z", 3, 4));
//        spout.setCycle(false);
//        @formatter:on
        TridentTopology topology = new TridentTopology();

        topology.newStream("spout1", spout)
                .partitionAggregate(new Fields("b", "c"), new CompleteTupleSum(), new Fields("total"))
                .each(new Fields("total"), new Debug());

        return topology.build();
    }

    public static void main(String[] args) {

        LocalDRPC drpc = new LocalDRPC();
        StormTopology topology = buildTopology(drpc);
        Config conf = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("tester", conf, topology);
        Utils.sleep(4000);
        cluster.shutdown();
        System.exit(0);
    }
}
