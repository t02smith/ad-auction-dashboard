package ad.auction.dashboard.model.config;

import java.util.List;

/**
 * A user guide from guides.xml
 * @param id The guide's uid
 * @param name The name of the guide
 * @param desc The description of the guide
 * @param steps A list of steps in the guide
 * @param sections A list of larger textual sections
 */
public record Guide(int id, String name, String desc, List<Step> steps, List<Section> sections) {

    /**
     * A single step as part of the guide
     * @param text The step's content
     * @param isLink Whether it links to another guide
     * @param ref The uid of the linked guide (or null)
     */
    public record Step (String text, boolean isLink, Integer ref) {}

    /**
     * A single section as part of the guide
     * @param name The section's name/title
     * @param text The section's content
     */
    public record Section (String name, String text) {}

    @Override
    public String toString() {
        return name;
    }
}
