package ad.auction.dashboard.view.settings;

public enum Settings {
    THEMES(new AppearanceSettings()),
    CAMPAIGN(new CampaignSettings());

    private final SettingPage page;

    Settings(SettingPage page) {
        this.page = page;
    }

    public SettingPage getPage() {
        return page;
    }

    @Override
    public String toString() {
        return this.page.getDisplayName();
    }
}
