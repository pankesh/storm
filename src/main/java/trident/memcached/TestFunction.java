package trident.memcached;

import storm.trident.TridentTopology;
import storm.trident.operation.BaseFilter;
import storm.trident.operation.builtin.Debug;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.tuple.TridentTuple;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class TestFunction {

    // Filters take in a tuple as input and decide whether or not to keep that tuple or not./
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
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("a", "b", "c"), 3, 
                new Values(1, 2, 3), 
                new Values(2, 1, 1),
                new Values(2, 3, 4));
        spout.setCycle(false);
//        @formatter:on
        TridentTopology topology = new TridentTopology();

        topology.newStream("spout1", spout).each(new Fields("a", "b"), new MyFilter())
                .each(new Fields("a", "b", "c"), new Debug());

        return topology.build();
    }

    public static void main(String[] args) {

        LocalDRPC drpc = new LocalDRPC();
        StormTopology topology = buildTopology(drpc);
        Config conf = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("tester", conf, topology);
        cluster.shutdown();
        
        System.exit(0);
    }
}
