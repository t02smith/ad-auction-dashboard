package ad.auction.dashboard.view.components;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Impression.Gender;
import ad.auction.dashboard.model.files.records.Impression.Income;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 */
public class FilterMenu extends GridPane {

    public static final Logger logger = LogManager.getLogger(FilterMenu.class.getSimpleName());

    private final Controller controller = App.getInstance().controller();

    private final Runnable reloadMetric;

    public FilterMenu(Runnable reloadMetric) {
        this.reloadMetric = reloadMetric;
        build();
    }

    /**
     * Build the filter menu
     */
    private void build() {

        // Date section
        var date = new Text("Date");
        date.getStyleClass().add("filter-sub-heading");
        ArrayList<Pair<Text, DatePicker>> dates = buildDates();

        // Gender section
        var gender = new Text("Gender");
        gender.getStyleClass().add("filter-sub-heading");

        List<CheckBox> genders = Arrays.asList(Gender.values()).stream().map(g -> {
            int hash = controller.addImpFilter(i -> i.gender() != g);

            var box = new CheckBox(g.toString().charAt(0) + g.toString().substring(1).toLowerCase());
            box.setSelected(true);
            box.selectedProperty().addListener(e -> {
                controller.toggleFilter(hash);
            });

            return box;
        }).toList();


        // Income section
        var income = new Text("Income");
        income.getStyleClass().add("filter-sub-heading");
        List<CheckBox> incomes = Arrays.asList(Income.values()).stream().map(i -> {
            int hash = controller.addImpFilter(imp -> imp.income() != i);

            var box = new CheckBox(i.toString().charAt(0) + i.toString().substring(1).toLowerCase());
            box.setSelected(true);
            box.selectedProperty().addListener(e -> {
                controller.toggleFilter(hash);
                this.reloadMetric.run();
            });

            return box;
        }).toList();

        // Age Group section
        var ageGroup = new Text("Age Group");
        ageGroup.getStyleClass().add("filter-sub-heading");
        List<CheckBox> ageGroups = Arrays.asList(Impression.AgeGroup.values()).stream().map(ag -> {
            int hash = controller.addImpFilter(i -> i.ageGroup() != ag);

            var box = new CheckBox(ag.str);
            box.setSelected(true);
            box.selectedProperty().addListener(e -> {
                controller.toggleFilter(hash);
                this.reloadMetric.run();
            });

            return box;
        }).toList();

        // Context section
        var context = new Text("Context");
        context.getStyleClass().add("filter-sub-heading");
        List<CheckBox> contexts = Arrays.asList(Impression.Context.values()).stream().map(c -> {
            int hash = controller.addImpFilter(i -> i.context() != c);

            var box = new CheckBox(c.toString().charAt(0) + c.toString().substring(1).toLowerCase());
            box.setSelected(true);
            box.selectedProperty().addListener(e -> {
                controller.toggleFilter(hash);
                this.reloadMetric.run();
            });

            return box;
        }).toList();

        // Add it all to the grid pane
        add(date, 0, 0);
        addRow(1, dates.get(0).getKey(), dates.get(0).getValue());
        addRow(2, dates.get(1).getKey(), dates.get(1).getValue());
        addRow(3, dates.get(2).getKey(), dates.get(2).getValue());
        addRow(4, dates.get(3).getKey(), dates.get(3).getValue());
        add(gender,0, 5);
        addRow(6, genders.get(0), genders.get(1));
        add(income, 0, 7);
        addRow(8, incomes.get(0), incomes.get(1));
        add(incomes.get(2), 0, 9);
        add(ageGroup, 0, 10);
        addRow(11, ageGroups.get(0), ageGroups.get(1));
        addRow(12, ageGroups.get(2), ageGroups.get(3));
        add(ageGroups.get(4), 0, 13);
        add(context, 0, 14);
        addRow(15, contexts.get(0), contexts.get(1));
        addRow(16, contexts.get(2), contexts.get(3));
        addRow(17, contexts.get(4), contexts.get(5));

        // Add spacing and margins
        setVgap(5);
        setHgap(5);
        setPadding(new Insets(0, 5, 0, 0));
    }

    /**
     * Builds list of date parts for the UI
     * @return list of date parts for the UI
     */
    private ArrayList<Pair<Text, DatePicker>> buildDates() {
        var beforeDate = new DatePicker();
        var afterDate = new DatePicker();
        var betweenDateLowBound = new DatePicker();
        var betweenDateHighBound = new DatePicker();

        beforeDate.setDayCellFactory((lam) -> getDisabledDate());
        beforeDate.getEditor().setDisable(true);
        beforeDate.setOnAction((event) -> {
            afterDate.getEditor().clear();
            betweenDateLowBound.getEditor().clear();
            betweenDateHighBound.getEditor().clear();
        });

        afterDate.setDayCellFactory((lam) -> getDisabledDate());
        afterDate.getEditor().setDisable(true);
        afterDate.setOnAction((event) -> {
            beforeDate.getEditor().clear();
            betweenDateLowBound.getEditor().clear();
            betweenDateHighBound.getEditor().clear();
        });

        betweenDateLowBound.setDayCellFactory((lam) -> getDisabledDate());
        betweenDateLowBound.getEditor().setDisable(true);
        betweenDateLowBound.setOnAction((event) -> {
            beforeDate.getEditor().clear();
            afterDate.getEditor().clear();
            betweenDateHighBound.setDayCellFactory((lam -> getDisabledDate(LocalDate.now(), betweenDateLowBound.getValue())));
        });

        betweenDateHighBound.setDayCellFactory((lam) -> getDisabledDate());
        betweenDateHighBound.getEditor().setDisable(true);
        betweenDateHighBound.setOnAction((event) -> {
            beforeDate.getEditor().clear();
            afterDate.getEditor().clear();
            betweenDateLowBound.setDayCellFactory((lam -> getDisabledDate(betweenDateHighBound.getValue(), LocalDate.MIN)));
        });

        ArrayList<Pair<Text, DatePicker>> datesStuff = new ArrayList<Pair<Text, DatePicker>>();
        datesStuff.add(new Pair<Text, DatePicker>(new Text("Before"), beforeDate));
        datesStuff.add(new Pair<Text, DatePicker>(new Text("After"), afterDate));
        datesStuff.add(new Pair<Text, DatePicker>(new Text("Between"), betweenDateLowBound));
        datesStuff.add(new Pair<Text, DatePicker>(new Text("and"), betweenDateHighBound));

        return datesStuff;
    }

    /**
     * Returns DateCell that is disabled if it's a future date
     * @return DateCell
     */
    private DateCell getDisabledDate() {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        };
    }

    /**
     * Returns DateCell that is disabled if before or after the given times
     * @param after if date after this date then disable
     * @param before if date before this date then disable
     * @return DateCell
     */
    private DateCell getDisabledDate(LocalDate after, LocalDate before) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 ||
                        date.compareTo(after) > 0 || date.compareTo(before) < 0);
            }
        };
    }
}
