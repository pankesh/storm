package org.panksdmz.storm.kafka;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class PriceScheme implements Scheme {

    private static final long serialVersionUID = -4207881469065995236L;

    private static final Logger LOG = Logger.getLogger(PriceScheme.class);

    private static final String PRICE_TIMESTAMP = "priceTimestamp";
    private static final String PRODUCT_ID = "productId";
    private static final String PRODUCT_PRICE = "productPrice";

    @Override
    public List<Object> deserialize(byte[] inputBytes) {

        List<Object> values = new Values();

        try {
            String inputPrice = new String(inputBytes, "UTF-8");
            String[] parts = inputPrice.split("\\|");

            Timestamp timestamp = Timestamp.valueOf(parts[0]);
            String productId = parts[1];
            String price = parts[2];

            values = new Values(timestamp, productId, price);

        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public Fields getOutputFields() {
        return new Fields(PRICE_TIMESTAMP, PRODUCT_ID, PRODUCT_PRICE);
    }

}
