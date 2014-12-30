package trident.memcached;

import storm.trident.TridentTopology;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.builtin.Debug;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.tuple.TridentTuple;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * This test class shows how to use a trident function to filter out tuples
 * 
 * @author pcontr200
 *
 */
public class TestFilter {

    public static class Split extends BaseFunction {
        private static final long serialVersionUID = -3857843829862817804L;

        @Override
        public void execute(TridentTuple tuple, TridentCollector collector) {
            String sentence = tuple.getString(0);
            for (String word : sentence.split(" ")) {
                collector.emit(new Values(word));
            }
        }
    }

    // A function takes in a set of input fields and emits zero or more tuples as output. The fields of the output tuple
    // are appended to the original input tuple in the stream. If a function emits no tuples, the original input tuple
    // is filtered out. Otherwise, the input tuple is duplicated for each output tuple. Suppose you have this function:
    public static class MyFunction extends BaseFunction {
        private static final long serialVersionUID = 7141701694970082439L;

        public void execute(TridentTuple tuple, TridentCollector collector) {
            for (int i = 0; i < tuple.getInteger(0); i++) {
                collector.emit(new Values(i));
            }
        }
    }

    public static StormTopology buildTopology() {
        @SuppressWarnings("unchecked")
//        @formatter:off
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("a", "b", "c"), 3, 
                new Values(3, 3, 4), 
                new Values(4, 2, 6),
                new Values(7, 0, 8));
        spout.setCycle(false);
//        @formatter:on
        TridentTopology topology = new TridentTopology();

        topology.newStream("spout1", spout).each(new Fields("b"), new MyFunction(), new Fields("d"))
                .each(new Fields("a", "b", "c", "d"), new Debug());

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
