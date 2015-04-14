package org.panksdmz.storm.trident;

import java.util.Map;

import storm.trident.operation.Function;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

public class DebugFunction implements Function {

    private static final long serialVersionUID = -3398675651551048570L;

    @Override
    public void prepare(Map conf, TridentOperationContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        System.out.println(tuple.toString());
    }

}
