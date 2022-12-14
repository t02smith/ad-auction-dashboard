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

/**
 * Handler to parse the guides.xml file
 *
 * @author tcs1g20
 */
public class GuideHandler extends DefaultHandler {

    private static final Logger logger = LogManager.getLogger(GuideHandler.class.getSimpleName());
    private static final String GUIDE_LOCATION = GuideHandler.class.getResource("/data/info/guides.xml").toExternalForm();

    private final List<Guide> guides = new ArrayList<>();

    private StringBuilder element;

    //TEMP CLASSES
    private GuideTemp currentGuide;
    private StepTemp currentStep;
    private SectionTemp currentSection;

    private static class GuideTemp {
        int id;
        String name;
        String desc;
        List<Guide.Step> steps;
        List<Guide.Section> sections;
    }

    private static class StepTemp {
        String text;
        Integer ref;
    }

    private static class SectionTemp {
        String name;
        String text;
    }

    /**
     * Parse guides.xml
     */
    public void parse(String filename) {
        logger.info("Parsing guides file '{};", filename);
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(filename, this);
            logger.info("Parsed guides file '{}'", filename);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            logger.error("Error reading guides file '{}': {}", filename, e.getMessage());
        }
    }

    public void parse() {
        this.parse(GUIDE_LOCATION);
    }

    // HANDLER METHODS


    @Override
    public void characters(char[] ch, int start, int length) {
        if (element == null) element = new StringBuilder();
        else element.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "guide" -> currentGuide = new GuideTemp();
            case "id", "name", "description","step", "linkRef", "linkText", "sectionName", "sectionText" -> element = new StringBuilder();
            case "steps" -> currentGuide.steps = new ArrayList<>();
            case "link" -> {currentStep = new StepTemp(); element = new StringBuilder();}
            case "sections" -> currentGuide.sections = new ArrayList<>();
            case "section" -> currentSection = new SectionTemp();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "guide" -> guides.add(new Guide(currentGuide.id, currentGuide.name, currentGuide.desc, currentGuide.steps, currentGuide.sections));
            case "id" -> currentGuide.id = Integer.parseInt(element.toString());
            case "name" -> currentGuide.name = element.toString();
            case "description" -> currentGuide.desc = element.toString();
            case "link" -> currentGuide.steps.add(new Guide.Step(currentStep.text, true, currentStep.ref));
            case "step" -> currentGuide.steps.add(new Guide.Step(element.toString(), false, null));
            case "linkText" -> currentStep.text = element.toString();
            case "linkRef" -> currentStep.ref = Integer.parseInt(element.toString());
            case "sectionName" -> currentSection.name = element.toString();
            case "sectionText" -> currentSection.text = element.toString();
            case "section" -> currentGuide.sections.add(new Guide.Section(currentSection.name, currentSection.text));
        }
    }

    //GETTERS

    public List<Guide> getGuides() {
        return this.guides;
    }
}
