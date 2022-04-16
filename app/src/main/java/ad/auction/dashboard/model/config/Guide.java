package ad.auction.dashboard.model.config;

import java.util.List;

public record Guide(int id, String name, String desc, List<Step> steps, List<Section> sections) {

    public record Step (String text, boolean isLink, Integer ref) {}

    public record Section (String name, String text) {}

    @Override
    public String toString() {
        return name;
    }
}
