package ad.auction.dashboard.view.settings;

public enum Themes {
    // themes and css path
    DARK("dark.css"),
    LIGHT("light.css");

    private final String cssResource;

    Themes(String cssResource) {
        this.cssResource = cssResource;
    }

    public String getCssResource() {
        return "/style/themes/" + cssResource;
    }
}
