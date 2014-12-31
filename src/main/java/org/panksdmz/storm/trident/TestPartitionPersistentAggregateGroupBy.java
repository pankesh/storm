package org.panksdmz.storm.trident;

import java.net.InetSocketAddress;
import java.util.Arrays;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.fluent.GroupedStream;
import storm.trident.operation.builtin.FilterNull;
import storm.trident.operation.builtin.MapGet;
import storm.trident.state.StateFactory;
import storm.trident.testing.FixedBatchSpout;
import trident.memcached.MemcachedState;
import trident.memcached.Test;
import trident.memcached.Test.Split;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
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
 *      Input                   Group By                                         Aggregate                      Partition Aggregate
 *      ------                  --------                                         ---------                      -------------------
 *      [x, 2, 3]      Partition 1 [ [x, [[2, 3], [1, 1]] ]                     Aggregate [x, 7]                        [x, 7]
 *      [x, 1, 1]
 *      [y, 4, 7]  =>  Partition 2 [ [y, [[4, 7]], [z, [ [6, 2] ]] ]   =>       Aggregate [y,11], [z, 8]     =>         [y, 11+4]
 *      [z, 6, 2]
 *      [y, 3, 1]      Partition 3 [ [y, [[3, 1]], [z, [ [3, 4] ]] ]            Aggregate [y, 4], [z, 7]                [z, 8+7]
 *      [z, 3, 4]
 * </pre>
 *
 * Note here that the partiton & aggregation happens only within the grouped stream and not on the whole batch.
 * 
 * 
 * <p/><b>Important</b>: This test assumes that you are running a memecached on localhost at port 11211
 */

public class TestPartitionPersistentAggregateGroupBy {

    public static StormTopology buildTopology(LocalDRPC drpc, StateFactory memcached) {
        @SuppressWarnings("unchecked")
//        @formatter:off
        // Partition size is 2. Hence aggregate will run on that partition to calculate the sum.
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("a", "b", "c"),2,
                new Values("x", 2, 3), 
                new Values("x", 1, 1),                
                new Values("y", 4, 7),
                new Values("z", 6, 2),                
                new Values("y", 3, 20),
                new Values("z", 3, 4));
//        spout.setCycle(false);
        TridentTopology topology = new TridentTopology();

        topology.newStream("spout1", spout)
        .groupBy(new Fields("a"))
        .aggregate(new Fields("b", "c"), new CompleteTupleSum(), new Fields("total"))
        .each(new Fields("a", "total"), new DebugFunction(), new Fields());

        
        
        TridentState sum = topology.newStream("spout1", spout)
                                    .groupBy(new Fields("a"))
                                    .persistentAggregate(memcached, new Fields("b", "c"), new CompleteTupleSum(), new Fields("total"))
                                    .parallelismHint(6);
//        @formatter:on

        topology.newDRPCStream("totalSum", drpc).each(new Fields("args"), new Split(), new Fields("letter"))
                .groupBy(new Fields("letter")).stateQuery(sum, new Fields("letter"), new MapGet(), new Fields("total"))
                .each(new Fields("total"), new FilterNull());

        return topology.build();
    }

    public static void main(String[] args) {

        int PORT = 11211;
        StateFactory memcached = MemcachedState.nonTransactional(Arrays
                .asList(new InetSocketAddress("localhost", PORT)));

        LocalDRPC drpc = new LocalDRPC();
        StormTopology topology = buildTopology(drpc, memcached);
        Config conf = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("tester", conf, topology);
        Utils.sleep(4000);
        System.out.println(drpc.execute("totalSum", "x y z"));
        Utils.sleep(4000);
        cluster.shutdown();
        System.exit(0);
    }
}
