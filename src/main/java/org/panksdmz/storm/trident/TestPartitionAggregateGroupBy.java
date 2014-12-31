package org.panksdmz.storm.trident;

import storm.trident.TridentTopology;
import storm.trident.fluent.GroupedStream;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * This test class shows how the to use the groupBy function on a inptu stream of tuple. The (@link
 * {@link GroupedStream#aggregate(Fields, storm.trident.operation.Aggregator, Fields)} function work on the existing
 * partition (2 in this example).
 * 
 * As a result of
 * 
 * <pre>
 *      Input                   Group By                                         Aggregate
 *      ------                  --------                                         ---------
 *      [x, 2, 3]      Partition 1 [ [x, [[2, 3], [1, 1]] ]                     Aggregate [x, 7]
 *      [x, 1, 1]
 *      [y, 4, 7]  =>  Partition 2 [ [y, [[4, 7]], [z, [ [6, 2] ]] ]   =>       Aggregate [y,11], [z, 8]
 *      [z, 6, 2]
 *      [y, 3, 1]      Partition 3 [ [y, [[3, 1]], [z, [ [3, 4] ]] ]            Aggregate [y, 4], [z, 7]
 *      [z, 3, 4]
 * </pre>
 *
 * Note here that the partiton & aggregation happens only within the grouped stream and not on the whole batch.
 */
public class TestPartitionAggregateGroupBy {

    public static StormTopology buildTopology() {
        @SuppressWarnings("unchecked")
//        @formatter:off
        // Partition size is 2. Hence aggregate will run on that partition to calculate the sum.
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("a", "b", "c"),2,
                new Values("x", 2, 3), 
                new Values("x", 1, 1),                
                new Values("y", 4, 7),
                new Values("z", 6, 2),                
                new Values("y", 3, 1),
                new Values("z", 3, 4));
//        spout.setCycle(false);
        TridentTopology topology = new TridentTopology();

        topology.newStream("spout1", spout)
                .groupBy(new Fields("a"))
                .aggregate(new Fields("b", "c"), new CompleteTupleSum(), new Fields("total"))
                .each(new Fields("a", "total"), new DebugFunction(), new Fields());
//      @formatter:on

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
