package org.panksdmz.storm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class PriceSpout extends BaseRichSpout {
    private static final long serialVersionUID = -670260135789486934L;
    private SpoutOutputCollector collector;
    private boolean isDistributed;

    @SuppressWarnings("rawtypes")
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;

    }

    @Override
    public void nextTuple() {
        
        final Integer[] prices = new Integer[] { 99, 55, 92, 39, 200 };

        List<Integer> asList = Arrays.asList(prices);
        
        for (Integer price : asList) {
            Utils.sleep(100);
            collector.emit(new Values(price));            
        }
        

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("price"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        if (!isDistributed) {
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
            return ret;
        } else {
            return null;
        }
    }
}
