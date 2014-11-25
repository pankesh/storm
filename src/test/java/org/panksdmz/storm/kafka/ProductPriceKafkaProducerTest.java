package org.panksdmz.storm.kafka;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProductPriceKafkaProducerTest {

    @Test
    public void testReadPriceData() throws Exception {

        String[] readPriceData = ProductPriceKafkaProducer.readPriceData("price.xml");
        assertEquals(3, readPriceData.length);
        assertEquals("10|12.99", readPriceData[0]);
    }
}
