package trident.memcached;

import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Debug;
import storm.trident.operation.builtin.Sum;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class TestPartitionAggregate {

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

        topology.newStream("spout1", spout).partitionAggregate(new Fields("b", "c"), new Sum(), new Fields("total"))
                .each(new Fields("total"), new Debug());

        return topology.build();
    }

    public static void main(String[] args) {

        LocalDRPC drpc = new LocalDRPC();
        StormTopology topology = buildTopology(drpc);
        Config conf = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("tester", conf, topology);
        Utils.sleep(2000);
        cluster.shutdown();
        System.exit(0);
    }
}
