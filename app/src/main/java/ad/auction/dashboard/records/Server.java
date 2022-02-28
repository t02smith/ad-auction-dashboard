package ad.auction.dashboard.records;

import java.time.LocalDateTime;

public class Server extends FileType {

    private String exitDate;
    private int pagesViewed;
    private boolean conversion;

    public Server(LocalDateTime dateTime, String ID, String exitDate, int pagesViewed, boolean conversion) {
        setDateTime(dateTime);
        setID(ID);
        setConversion(conversion);
        setPagesViewed(pagesViewed);
        setExitDate(exitDate);
    }

    public String getExitDate() {
        return exitDate;
    }

    public void setExitDate(String exitDate) {
        this.exitDate = exitDate;
    }

    public int getPagesViewed() {
        return pagesViewed;
    }

    public void setPagesViewed(int pagesViewed) {
        this.pagesViewed = pagesViewed;
    }

    public boolean isConversion() {
        return conversion;
    }

    public void setConversion(boolean conversion) {
        this.conversion = conversion;
    }
}
