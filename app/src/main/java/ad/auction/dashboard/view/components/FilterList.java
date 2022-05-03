package ad.auction.dashboard.view.components;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.files.records.Impression;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;

import java.util.Arrays;
import java.util.List;

/**
 * List of filter options
 *
 * @author tcs1g20
 */
public class FilterList extends VBox {

    private final Controller controller = App.getInstance().controller();
    private final Runnable reloadMetric;

    private final int daySpan;

    /**
     * Create a new filter list
     * @param reloadMetric Function to call whenever a filter section changes
     * @param daySpan the number of days the data goes over
     */
    public FilterList(Runnable reloadMetric, int daySpan) {
        this.reloadMetric = reloadMetric;
        this.daySpan = daySpan;
        this.getStyleClass().add("bg-secondary");
        build();
    }

    /**
     * Build the filter list
     */
    private void build() {
        this.setSpacing(15);
        this.setPadding(new Insets(10));

        this.getChildren().addAll(
                timeSlider(),
                group(genders(), "Genders"),
                group(income(), "Income"),
                group(ageGroup(), "Age Group"),
                group(context(), "Context")
        );
    }

    /**
     * Create the slider to control which dates to include
     * @return the time slider component
     */
    private RangeSlider timeSlider() {
        var slider = new RangeSlider(0, daySpan, 0, daySpan);
        slider.setBlockIncrement(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);

        return slider;
    }

    /**
     * A group of related filter options
     * @param ls the filter checkboxes
     * @param title the group's title
     * @return group of related filter options w/ title
     */
    private VBox group(List<CheckBox> ls, String title) {
        var grp = new VBox();
        grp.setSpacing(5);

        var ttl = new Label(title);
        ttl.getStyleClass().add("filter-sub-heading");
        grp.getChildren().add(ttl);
        for (int i=0; i <= ls.size()/2 + (2*(ls.size()%2)); i+=2) {
            HBox h = new HBox(ls.get(i));
            h.setSpacing(12);
            if (i < ls.size()-1) h.getChildren().add(ls.get(i+1));
            grp.getChildren().add(h);
        }
        return grp;
    }

    /**
     * Checkboxes for gender filters
     * @return list of gender checkboxes
     */
    private List<CheckBox> genders() {
        return Arrays.stream(Impression.Gender.values()).map(g -> {
            int hash = controller.addUserFilter(u -> u.gender() != g);

            var box = new CheckBox(g.toString().charAt(0) + g.toString().substring(1).toLowerCase());
            box.setCursor(Cursor.HAND);
            box.setSelected(true);
            box.selectedProperty().addListener(e -> {
                controller.toggleFilter(hash);
                this.reloadMetric.run();
            });

            return box;
        }).toList();
    }

    /**
     * Checkboxes for income filters
     * @return list of income checkboxes
     */
    private List<CheckBox> income() {
        return Arrays.stream(Impression.Income.values()).map(i -> {
            int hash = controller.addUserFilter(u -> u.income() != i);

            var box = new CheckBox(i.toString().charAt(0) + i.toString().substring(1).toLowerCase());
            box.setCursor(Cursor.HAND);
            box.setSelected(true);
            box.selectedProperty().addListener(e -> {
                controller.toggleFilter(hash);
                this.reloadMetric.run();
            });

            return box;
        }).toList();
    }

    /**
     * Checkboxes for age group filters
     * @return list of age group checkboxes
     */
    private List<CheckBox> ageGroup() {
        return Arrays.stream(Impression.AgeGroup.values()).map(ag -> {
            int hash = controller.addUserFilter(u -> u.ageGroup() != ag);

            var box = new CheckBox(ag.str);
            box.setCursor(Cursor.HAND);
            box.setSelected(true);
            box.selectedProperty().addListener(e -> {
                controller.toggleFilter(hash);
                this.reloadMetric.run();
            });

            return box;
        }).toList();
    }

    /**
     * Checkboxes for context filters
     * @return list of context checkboxes
     */
    private List<CheckBox> context() {
        return Arrays.stream(Impression.Context.values()).map(c -> {
            int hash = controller.addUserFilter(u -> u.context() != c);

            var box = new CheckBox(c.toString().charAt(0) + c.toString().substring(1).toLowerCase());
            box.setCursor(Cursor.HAND);
            box.setSelected(true);
            box.selectedProperty().addListener(e -> {
                controller.toggleFilter(hash);
                this.reloadMetric.run();
            });

            return box;
        }).toList();
    }
}
