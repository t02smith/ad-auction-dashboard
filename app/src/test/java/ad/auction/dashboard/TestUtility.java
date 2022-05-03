package ad.auction.dashboard;

import java.net.URISyntaxException;

public final class TestUtility {
    
    public static String getResourceFile(String target) {
        try {
            return TestUtility.class
                .getResource(target)
                .toURI()
                .getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

}
