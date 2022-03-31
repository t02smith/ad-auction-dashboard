package ad.auction.dashboard.view.components;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Impression.Gender;
import ad.auction.dashboard.model.files.records.Impression.Income;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 */
public class FilterMenu extends GridPane {

    private final Controller controller = App.getInstance().controller();

    private final Runnable reloadMetric;

    //Start and end times
    private final LocalDate start;
    private final LocalDate end;

    public FilterMenu(Runnable reloadMetric, LocalDate start, LocalDate end) {
        this.reloadMetric = reloadMetric;
        this.start = start;
        this.end = end;

        this.getStyleClass().add("bg-secondary");
        build();
    }

    /**
     * Build the filter menu
     */
    private void build() {

        // Date section
        var date = new Label("Date");
        date.getStyleClass().add("filter-sub-heading");
        ArrayList<Pair<Label, DatePicker>> dates = buildDates();

        // Gender section
        var gender = new Label("Gender");
        gender.getStyleClass().add("filter-sub-heading");

        List<CheckBox> genders = Arrays.stream(Gender.values()).map(g -> {
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


        // Income section
        var income = new Label("Income");
        income.getStyleClass().add("filter-sub-heading");
        List<CheckBox> incomes = Arrays.stream(Income.values()).map(i -> {
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

        // Age Group section
        var ageGroup = new Label("Age Group");
        ageGroup.getStyleClass().add("filter-sub-heading");
        List<CheckBox> ageGroups = Arrays.stream(Impression.AgeGroup.values()).map(ag -> {
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

        // Context section
        var context = new Label("Context");
        context.getStyleClass().add("filter-sub-heading");
        List<CheckBox> contexts = Arrays.stream(Impression.Context.values()).map(c -> {
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

        // Add it all to the grid pane
        add(date, 0, 0);
        addRow(1, dates.get(0).getKey(), dates.get(0).getValue());
        addRow(2, dates.get(1).getKey(), dates.get(1).getValue());
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
    private ArrayList<Pair<Label, DatePicker>> buildDates() {
        var beforeDate = new DatePicker(end);
        var afterDate = new DatePicker(start);

        beforeDate.setDayCellFactory((lam) -> getDisabledDate());
        beforeDate.getEditor().setDisable(true);
        beforeDate.setOnAction((event) -> {
            this.controller.setDate(false, beforeDate.getValue().atTime(23,59,59,0));
            this.reloadMetric.run();
        });


        afterDate.setDayCellFactory((lam) -> getDisabledDate());
        afterDate.getEditor().setDisable(true);
        afterDate.setOnAction((event) -> {
            this.controller.setDate(true, afterDate.getValue().atTime(0,0,0,0));
            this.reloadMetric.run();
        });

        ArrayList<Pair<Label, DatePicker>> datesStuff = new ArrayList<>();

        datesStuff.add(new Pair<Label, DatePicker>(new Label("Start"), afterDate));
        datesStuff.add(new Pair<Label, DatePicker>(new Label("End"), beforeDate));

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

}
