package ad.auction.dashboard.records;

import java.time.LocalDateTime;

public abstract class FileType {

    private LocalDateTime dateTime;
    private String ID;

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getID() {
        return ID;
    }
}
