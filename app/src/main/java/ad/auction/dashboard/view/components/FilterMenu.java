package ad.auction.dashboard.view.components;

import ad.auction.dashboard.model.files.records.Impression;
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


public class FilterMenu extends GridPane {

    public static final Logger logger = LogManager.getLogger(FilterMenu.class.getSimpleName());

    public FilterMenu() {
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
        CheckBox[] genders = new CheckBox[2];
        for (int i = 0; i < Impression.Gender.values().length; i++) {
            String toDisplay = Impression.Gender.values()[i].toString();
            genders[i] = new CheckBox(toDisplay.charAt(0) + toDisplay.substring(1).toLowerCase());
            genders[i].setSelected(true);
        }

        // Income section
        var income = new Text("Income");
        income.getStyleClass().add("filter-sub-heading");
        CheckBox[] incomes = new CheckBox[3];
        for (int i = 0; i < Impression.Income.values().length; i++) {
            String toDisplay = Impression.Income.values()[i].toString();
            incomes[i] = new CheckBox(toDisplay.charAt(0) + toDisplay.substring(1).toLowerCase());
            incomes[i].setSelected(true);
        }

        // Age Group section
        var ageGroup = new Text("Age Group");
        ageGroup.getStyleClass().add("filter-sub-heading");
        CheckBox[] ageGroups = new CheckBox[5];
        for (int i = 0; i < Impression.AgeGroup.values().length; i++) {
            String toDisplay = Impression.AgeGroup.values()[i].toString();

            if (toDisplay.contains("UNDER")) {
                toDisplay = "<25";
            } else if (toDisplay.contains("OVER")) {
                toDisplay = ">54";
            } else {
                toDisplay = toDisplay.substring(8).replace("_", "-");
            }
            ageGroups[i] = new CheckBox(toDisplay);
            ageGroups[i].setSelected(true);
        }

        // Context section
        var context = new Text("Context");
        context.getStyleClass().add("filter-sub-heading");
        CheckBox[] contexts = new CheckBox[6];
        for (int i = 0; i < Impression.Context.values().length; i++) {
            String toDisplay = Impression.Context.values()[i].toString();

            if (toDisplay.equals("SOCIAL_MEDIA")) {
                toDisplay = "Social Media";
                contexts[i] = new CheckBox(toDisplay);
            }
            else {
                contexts[i] = new CheckBox(toDisplay.charAt(0) + toDisplay.substring(1).toLowerCase());
            }

            contexts[i].setSelected(true);
        }

        // Add it all to the grid pane
        add(date, 0, 0);
        addRow(1, dates.get(0).getKey(), dates.get(0).getValue());
        addRow(2, dates.get(1).getKey(), dates.get(1).getValue());
        addRow(3, dates.get(2).getKey(), dates.get(2).getValue());
        addRow(4, dates.get(3).getKey(), dates.get(3).getValue());
        add(gender,0, 5);
        addRow(6, genders[0], genders[1]);
        add(income, 0, 7);
        addRow(8, incomes[0], incomes[1]);
        add(incomes[2], 0, 9);
        add(ageGroup, 0, 10);
        addRow(11, ageGroups[0], ageGroups[1]);
        addRow(12, ageGroups[2], ageGroups[3]);
        add(ageGroups[4], 0, 13);
        add(context, 0, 14);
        addRow(15, contexts[0], contexts[1]);
        addRow(16, contexts[2], contexts[3]);
        addRow(17, contexts[4], contexts[5]);

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
