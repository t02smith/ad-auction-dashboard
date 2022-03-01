package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

public class Click extends FileType {

    private float cost;

    public Click(LocalDateTime dateTime, String ID, float cost) {
        setDateTime(dateTime);
        setID(ID);
        setCost(cost);
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getCost() {
        return cost;
    }
}