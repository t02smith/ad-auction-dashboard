package ad.auction.dashboard.model.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuideHandler extends DefaultHandler {

    private static final Logger logger = LogManager.getLogger(GuideHandler.class.getSimpleName());
    private static final String GUIDE_LOCATION = GuideHandler.class.getResource("/data/info/guides.xml").toExternalForm();

    private final List<Guide> guides = new ArrayList<>();

    private StringBuilder element;

    //TEMP CLASSES
    private GuideTemp currentGuide;
    private StepTemp currentStep;

    private static class GuideTemp {
        int id;
        String name;
        String desc;
        List<Guide.Step> steps;
    }

    private static class StepTemp {
        String text;
        boolean isLink = false;
        Integer ref;
    }

    public void parse() {
        logger.info("Parsing guides file '{};", GUIDE_LOCATION);
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(GUIDE_LOCATION, this);
            logger.info("Parsed guides file '{}'", GUIDE_LOCATION);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            logger.error("Error reading guides file '{}': {}", GUIDE_LOCATION, e.getMessage());
        }
    }

    // HANDLER METHODS


    @Override
    public void characters(char[] ch, int start, int length) {
        if (element == null) element = new StringBuilder();
        else element.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "guide" -> currentGuide = new GuideTemp();
            case "id", "name", "description","step", "linkRef", "linkText" -> element = new StringBuilder();
            case "steps" -> currentGuide.steps = new ArrayList<>();
            case "link" -> {currentStep = new StepTemp(); element = new StringBuilder();}
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "guide" -> guides.add(new Guide(currentGuide.id, currentGuide.name, currentGuide.desc, currentGuide.steps));
            case "id" -> currentGuide.id = Integer.parseInt(element.toString());
            case "name" -> currentGuide.name = element.toString();
            case "description" -> currentGuide.desc = element.toString();
            case "link" -> currentGuide.steps.add(new Guide.Step(currentStep.text, true, currentStep.ref));
            case "step" -> currentGuide.steps.add(new Guide.Step(element.toString(), false, null));
            case "linkText" -> currentStep.text = element.toString();
            case "linkRef" -> currentStep.ref = Integer.parseInt(element.toString());
        }
    }

    //GETTERS

    public List<Guide> getGuides() {
        return this.guides;
    }
}
