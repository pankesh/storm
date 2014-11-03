package org.panksdmz.storm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class DiscountBolt extends BaseRichBolt {
    private static final long serialVersionUID = -6880136253227239457L;
    private OutputCollector collector;

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {

        Integer price = input.getInteger(0);
        Double totalPrice = price*1.0;
        
        if (price.toString().endsWith("9")) {
               totalPrice = price*0.8; //apply 20% discount
        } 
        
        collector.emit(input, new Values(totalPrice));
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("totalPrice"));
    }

}
