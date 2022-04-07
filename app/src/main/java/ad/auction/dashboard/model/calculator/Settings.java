package ad.auction.dashboard.model.calculator;

import ad.auction.dashboard.view.settings.CampaignSettingPage;
import ad.auction.dashboard.view.settings.SettingPage;
import ad.auction.dashboard.view.settings.ThemeSettingPage;
import javafx.scene.text.Text;

public enum Settings {
    THEMES(new ThemeSettingPage()),
    CAMPAIGN(new CampaignSettingPage());

    private SettingPage page;

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
