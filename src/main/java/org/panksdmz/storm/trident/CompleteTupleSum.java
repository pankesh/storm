package org.panksdmz.storm.trident;

import storm.trident.operation.CombinerAggregator;
import storm.trident.tuple.TridentTuple;
import clojure.lang.Numbers;

public class CompleteTupleSum implements CombinerAggregator<Number> {

    private static final long serialVersionUID = -6408613375672617097L;

    @Override
    public Number init(TridentTuple tuple) {
        Integer sum = 0;
        for (Object object : tuple) {
            sum += (Integer) object;
        }
        return sum;
    }

    @Override
    public Number combine(Number val1, Number val2) {
        return Numbers.add(val1, val2);
    }

    @Override
    public Number zero() {
        return 0;
    }

}