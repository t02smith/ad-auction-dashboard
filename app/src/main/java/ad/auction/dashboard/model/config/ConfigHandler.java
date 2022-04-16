package ad.auction.dashboard.model.config;

import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.view.settings.Themes;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Store any program info to persist between launches
 */
public class ConfigHandler extends DefaultHandler {

    private static final Logger logger = LogManager.getLogger(ConfigHandler.class.getSimpleName());

    private StringBuilder element;
    private ConfigTemp current;

    private CampaignTemp currentCampaign;

    private static class ConfigTemp {
        Metrics defaultMetric;
        Themes theme;
        List<Campaign.CampaignData> campaigns;
    }

    private static class CampaignTemp {
        String name;
        String impPath;
        String clkPath;
        String svrPath;
    }

    public record Config(Metrics defaultMetric, Themes theme, List<Campaign.CampaignData> campaigns) {}

    public void parse(String filename) throws IOException {
        logger.info("Parsing config file '{};", filename);
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(filename, this);
            logger.info("Parsed config file '{}'", filename);
        } catch (SAXException | ParserConfigurationException e) {
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

            //SETTINGS
            var settings = doc.createElement("settings");
            configWrapper.appendChild(settings);

            var defaultMetric = doc.createElement("defaultMetric");
            defaultMetric.appendChild(doc.createTextNode(config.defaultMetric().name()));
            settings.appendChild(defaultMetric);

            var theme = doc.createElement("theme");
            theme.appendChild(doc.createTextNode(config.theme().name()));
            settings.appendChild(theme);

            //CAMPAIGNS
            Element campaigns = doc.createElement("campaigns");
            configWrapper.appendChild(campaigns);

            config.campaigns().forEach(c -> {
                Element camp = doc.createElement("campaign");

                var name = doc.createElement("name");
                name.appendChild(doc.createTextNode(c.name()));
                camp.appendChild(name);

                var impPath = doc.createElement("impPath");
                impPath.appendChild(doc.createTextNode(c.impPath()));
                camp.appendChild(impPath);

                var svrPath = doc.createElement("svrPath");
                svrPath.appendChild(doc.createTextNode(c.svrPath()));
                camp.appendChild(svrPath);

                var clkPath = doc.createElement("clkPath");
                clkPath.appendChild(doc.createTextNode(c.clkPath()));
                camp.appendChild(clkPath);

                campaigns.appendChild(camp);

            });


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
            case "campaigns" -> this.current.campaigns = new ArrayList<>();
            case "campaign" -> this.currentCampaign= new CampaignTemp();
            case "name", "impPath", "svrPath", "clkPath", "defaultMetric", "theme" -> element = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "defaultMetric" -> {
                try {this.current.defaultMetric = Metrics.valueOf(element.toString());}
                catch (IllegalArgumentException e) {logger.error("Default metric '{}' not found", element.toString());}
            }
            case "theme" -> {
                try {this.current.theme = Themes.valueOf(element.toString());}
                catch (IllegalArgumentException e) {logger.error("Theme '{}' not found", element.toString());}
            }
            case "name" -> currentCampaign.name = element.toString();
            case "impPath" -> currentCampaign.impPath = element.toString();
            case "svrPath" -> currentCampaign.svrPath = element.toString();
            case "clkPath" -> currentCampaign.clkPath = element.toString();
            case "campaign" -> {
                current.campaigns.add(new Campaign.CampaignData(
                        currentCampaign.name, currentCampaign.clkPath, currentCampaign.impPath, currentCampaign.svrPath, null, null));
                logger.info("Campaign '{}' loaded", currentCampaign.name);
            }
        }
    }

    //GETTERS

    public Config getConfig() {
        return new Config(
                current.defaultMetric,
                current.theme,
                current.campaigns
        );
    }
}
