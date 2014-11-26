package org.panksdmz.storm.kafka;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ProductPriceKafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPriceKafkaProducer.class);
    private static final String TOPIC = "productPriceTopic";

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException,
            URISyntaxException {
        if (args.length != 2) {

            System.out.println("Usage: ProductPriceKafkaProducer <broker list> <zookeeper>");
            System.exit(-1);
        }

        LOG.debug("Using broker list:" + args[0] + ", zk conn:" + args[1]);

        Properties props = new Properties();
        props.put("metadata.broker.list", args[0]);
        props.put("zk.connect", args[1]);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        String priceEvent = "";

        String priceDataXml = "price.xml";
        String[] priceData = readPriceData(priceDataXml);

        int j = 0;
        while (j < 500) {
            for (int i = 0; i < priceData.length; i++) {

                priceEvent = new Timestamp(new Date().getTime()) + "|" + priceData[i];
                try {
                    KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, priceEvent);
                    LOG.info("Sending Messge #: " + priceEvent);
                    producer.send(data);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            j++;
        }

        producer.close();
    }

    public static String[] readPriceData(String urlString) throws ParserConfigurationException, SAXException,
            IOException {

        Document doc = null;
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = db.parse(ClassLoader.getSystemResourceAsStream(urlString));
        doc.getDocumentElement().normalize();

        NodeList prices = doc.getElementsByTagName("ProductPrice");

        String[] productIdAndPrice = new String[prices.getLength()];
        for (int i = 0; i < prices.getLength(); i++) {

            Node productNode = prices.item(i);
            if (productNode.getNodeType() == Node.ELEMENT_NODE) {
                Element productElement = (Element) productNode;

                String productId = productElement.getElementsByTagName("ProductId").item(0).getTextContent().toString();
                String price = productElement.getElementsByTagName("Price").item(0).getTextContent().toString();

                productIdAndPrice[i] = productId + "|" + price;
            }

        }
        return productIdAndPrice;
    }
}
