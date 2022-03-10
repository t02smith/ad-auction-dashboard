package ad.auction.dashboard.model.campaigns;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is used to read data from the campaigns .xml file
 * 
 * @author tcs1g20
 */
public class CampaignHandler extends DefaultHandler {

    private static final Logger logger = LogManager.getLogger(CampaignHandler.class.getSimpleName());
    private ArrayList<Campaign> campaigns;

    private StringBuilder element;
    private CampaignTemp current;

    //A temporary class to store the campaign currently being read
    class CampaignTemp {
        String name;
        String impPath;
        String clkPath;
        String svrPath;
    }

    /**
     * Get the data from the .xml file
     * @param filename
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

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (element == null) element = new StringBuilder();
        else element.append(ch, start, length);
    }
    
    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch(qName) {
            case "campaigns":
                this.campaigns = new ArrayList<>();
                break;
            case "campaign":
                this.current = new CampaignTemp();
                break;
            case "name":
            case "impPath":
            case "svrPath":
            case "clkPath":
                element = new StringBuilder();
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "name":
                current.name = element.toString();
                break;
            case "impPath":
                current.impPath = element.toString();
                break;
            case "svrPath":
                current.svrPath = element.toString();
                break;
            case "clkPath":
                current.clkPath = element.toString();
                break;
            case "campaign":
                campaigns.add(new Campaign(
                    current.name, current.impPath, current.clkPath, current.svrPath));
                logger.info("Campaign '{}' loaded", current.name);
                break;
        }
    }

    public ArrayList<Campaign> getCampaigns() {
        return this.campaigns == null ? new ArrayList<>(): this.campaigns;
    }
}
