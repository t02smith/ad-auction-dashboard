package ad.auction.dashboard.model.config;

import java.util.List;

public record Guide(int id, String name, String desc, List<Step> steps) {

    record Step (String text, boolean isLink, Integer ref) {}
}
