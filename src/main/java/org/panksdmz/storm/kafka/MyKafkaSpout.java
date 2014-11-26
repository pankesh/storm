package org.panksdmz.storm.kafka;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;

public class MyKafkaSpout extends KafkaSpout {

    private static final long serialVersionUID = -4396408451850715317L;

    public MyKafkaSpout(SpoutConfig spoutConf) {
        super(spoutConf);
    }

    @Override
    public void activate() {
        System.out.println("In activate()");
        super.activate();
    }

    @Override
    public void ack(Object msgId) {
        System.out.println("In ack()");
        super.ack(msgId);
    }

    @Override
    public void deactivate() {
        System.out.println("In deactivate()");
        super.deactivate();
    }

    @Override
    public void nextTuple() {
        super.nextTuple();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        System.out.println("In open()");
        super.open(conf, context, collector);
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        System.out.println("In getComponentConfiguration()");
        return super.getComponentConfiguration();
    }

}
