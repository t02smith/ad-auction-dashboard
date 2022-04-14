package ad.auction.dashboard.model.config;

import ad.auction.dashboard.model.calculator.Metrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Store any program info to persist between launches
 */
public class ConfigHandler extends DefaultHandler {

    private static final Logger logger = LogManager.getLogger(ConfigHandler.class.getSimpleName());

    private StringBuilder element;
    private ConfigTemp current;

    static class ConfigTemp {
        Metrics defaultMetric;
    }

    public record Config(Metrics defaultMetric) {}

    public void parse(String filename) {
        logger.info("Parsing config file '{};", filename);
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(filename, this);
            logger.info("Parsed config file '{}'", filename);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            logger.error("Error reading config file '{}': {}", filename, e.getMessage());
        }
    }

    public void writeToFile(String filename, Config config) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element configWrapper = doc.createElement("config");
            doc.appendChild(configWrapper);

            var defaultMetric = doc.createElement("defaultMetric");
            defaultMetric.appendChild(doc.createTextNode(config.defaultMetric().name()));
            configWrapper.appendChild(defaultMetric);


            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource domSource = new DOMSource(configWrapper);
            StreamResult streamResult = new StreamResult(new File(filename));

            transformer.transform(domSource, streamResult);
            logger.info("Config file written to {}", filename);

        } catch (ParserConfigurationException | TransformerException e) {
            logger.error("Error writing to {}: {}", filename, e.getMessage());
        }
    }

    // PARSER FUNCTIONS

    @Override
    public void characters(char[] ch, int start, int length) {
        if (element == null) element = new StringBuilder();
        else element.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "config" -> this.current = new ConfigTemp();
            case "defaultMetric" -> element = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "defaultMetric" -> {
                try {this.current.defaultMetric = Metrics.valueOf(element.toString());}
                catch (IllegalArgumentException e) {logger.error("Default metric {} not found", element.toString());}
            }
        }
    }

    //GETTERS

    public Config getConfig() {
        return new Config(
                current.defaultMetric
        );
    }
}
