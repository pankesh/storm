package org.panksdmz.storm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class CalculateTaxBolt extends BaseRichBolt {

    private static final long serialVersionUID = -3599742614305692693L;
    private OutputCollector collector;

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector; 
    }

    @Override
    public void execute(Tuple input) {
        Double price = input.getDouble(0);
        Double tax = price * 0.06; // calculate 6% tax

        collector.emit(input, new Values(price, tax));
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("price", "tax"));
    }

}
