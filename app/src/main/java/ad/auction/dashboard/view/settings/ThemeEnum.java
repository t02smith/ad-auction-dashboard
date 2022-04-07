package ad.auction.dashboard.view.settings;

public enum ThemeEnum {
    // themes and css path
    DARK_THEME("/style/darkandblue.css"),
    LIGH_THEME("/style/lightandblue.css");

    private String cssResource;

    ThemeEnum(String cssResource) {
        this.cssResource = cssResource;
    }

    public String getCssResource() {
        return cssResource;
    }
}
