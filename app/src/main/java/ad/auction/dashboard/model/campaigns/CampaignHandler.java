package ad.auction.dashboard.model.campaigns;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class is used to read data from the campaigns .xml file
 * 
 * @author tcs1g20
 */
public class CampaignHandler extends DefaultHandler {

    private static final Logger logger = LogManager.getLogger(CampaignHandler.class.getSimpleName());
    private ArrayList<FilteredCampaign> campaigns;

    private StringBuilder element;
    private CampaignTemp current;

    // A temporary class to store the campaign currently being read
    static class CampaignTemp {
        String name;
        String impPath;
        String clkPath;
        String svrPath;
    }

    /**
     * Get the data from the .xml file
     * @param filename .xml file location
     */
    public void parse(String filename) {
        logger.info("Parsing campaign file '{}'", filename);
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(filename, this);
            logger.info("Parsed campaign file '{}'", filename);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            logger.error("Error reading campaign file '{}': {}", filename, e.getMessage());
        }
    }

    /**
     * Writes a list of campaigns to a file
     * @param filename location to write to
     * @param campaignLs List of campaigns
     */
    public void writeToFile(String filename, List<CampaignData> campaignLs) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element campaigns = doc.createElement("campaigns");
            doc.appendChild(campaigns);

            campaignLs.forEach(c -> {
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
            DOMSource domSource = new DOMSource(campaigns);
            StreamResult streamResult = new StreamResult(new File(filename));

            transformer.transform(domSource, streamResult);

            logger.info("Campaigns written to {}", filename);

        } catch (ParserConfigurationException | TransformerException e) {
            logger.error("Error writing to {}: {}", filename, e.getMessage());
        }

    }

    // PARSER FUNCTIONS

    @Override
    public void characters(char[] ch, int start, int length) {
        if (element == null)
            element = new StringBuilder();
        else
            element.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) {
        switch (qName) {
            case "campaigns" -> this.campaigns = new ArrayList<>();
            case "campaign" -> this.current = new CampaignTemp();
            case "name", "impPath", "svrPath", "clkPath" -> element = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "name" -> current.name = element.toString();
            case "impPath" -> current.impPath = element.toString();
            case "svrPath" -> current.svrPath = element.toString();
            case "clkPath" -> current.clkPath = element.toString();
            case "campaign" -> {
                campaigns.add(new FilteredCampaign(
                        current.name, current.impPath, current.clkPath, current.svrPath));
                logger.info("Campaign '{}' loaded", current.name);
            }
        }
    }

    public ArrayList<FilteredCampaign> getCampaigns() {
        return this.campaigns == null ? new ArrayList<>() : this.campaigns;
    }
}
