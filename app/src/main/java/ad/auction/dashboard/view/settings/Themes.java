package ad.auction.dashboard.view.settings;

public enum Themes {
    // themes and css path
    DARK("/style/darkandblue.css"),
    LIGHT("/style/lightandblue.css");

    private final String cssResource;

    Themes(String cssResource) {
        this.cssResource = cssResource;
    }

    public String getCssResource() {
        return cssResource;
    }
}
