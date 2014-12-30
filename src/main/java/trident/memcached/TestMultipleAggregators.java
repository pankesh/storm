package trident.memcached;

import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.Debug;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * This test class shows and example of how to combine 2 different aggregate functions and return them as values on a
 * tuple
 * 
 */
public class TestMultipleAggregators {

    public static StormTopology buildTopology() {
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
        spout.setCycle(false);
//        @formatter:on
        TridentTopology topology = new TridentTopology();

        // @formatter:off
        topology.newStream("spout1", spout)
                .chainedAgg()
                .partitionAggregate(new Fields("b", "c"), new CompleteTupleSum(), new Fields("total"))
                .partitionAggregate(new Fields("b", "c"), new Count(), new Fields("count"))
                .chainEnd()
                .each(new Fields("total", "count"), new Debug());
        // @formatter:on

        return topology.build();
    }

    public static void main(String[] args) {

        StormTopology topology = buildTopology();
        Config conf = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("tester", conf, topology);
        Utils.sleep(2000);
        cluster.shutdown();
        System.exit(0);
    }
}
