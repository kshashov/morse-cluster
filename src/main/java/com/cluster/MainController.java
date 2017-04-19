package com.cluster;

import com.cluster.gui.TableUtils;
import com.cluster.math.model.Vertex;
import com.cluster.model.Decision;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.controlsfx.control.NotificationPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by envoy on 09.03.2017.
 */
public class MainController implements Initializable {

    @FXML
    TableView decisionsTable;
    @FXML
    TableView summaryCoordsTable;
    @FXML
    Label summaryP;
    @FXML
    Label summaryEnergy;
    @FXML
    Label summarySize;
    @FXML
    NotificationPane notificationPane;

    private List<Decision> createDecisions(int count) {
        double p = 14;
        List<Decision> decisions = new ArrayList<Decision>();
        for (int i = 0; i < count; i++) {
            List<Vertex> coords = new ArrayList<Vertex>();
            for (int j = 0; j < 15; j++) {
                coords.add(new Vertex(Math.random() * 10, Math.random() * 10, Math.random() * 10));
            }
            Decision decision = new Decision(p, Math.random() * 1000, coords);
            decision.setTitle("Decision #" + i);
            decisions.add(decision);
        }

        return decisions;
    }

    public void initialize(URL location, ResourceBundle resources) {
        initDecisions();
        initSummary();

        addDecisions(createDecisions(5));
        addDecisions(createDecisions(5));
        addDecisions(createDecisions(5));

    }

    private void addDecisions(List<Decision> decisions) {
        decisionsTable.getItems().addAll(decisions);
        decisionsTable.refresh();
    }

    private void addDecision(Decision decision) {
        decisionsTable.getItems().add(decision);
        decisionsTable.refresh();
    }

    private void showSummary(Decision decision) {
        summaryEnergy.setText(String.valueOf(decision.getEnergy()));
        summaryP.setText(String.valueOf(decision.getP()));
        summarySize.setText(String.valueOf(decision.getCoords().size()));
        summaryCoordsTable.getItems().clear();
        summaryCoordsTable.getItems().addAll(decision.getCoords());
        summaryCoordsTable.refresh();
    }

    private void initDecisions() {
        TableColumn<Decision, Integer> sizeColumn = new TableColumn<>("Размер");
        sizeColumn.prefWidthProperty().bind(decisionsTable.widthProperty().multiply(0.2));
        sizeColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Decision, Integer> param) ->
                        new ReadOnlyIntegerWrapper(param.getValue().getCoords().size()).asObject()
        );
        TableColumn<Decision, Double> energyColumn = new TableColumn<>("Энергия");
        energyColumn.prefWidthProperty().bind(decisionsTable.widthProperty().multiply(0.6));
        energyColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Decision, Double> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().getEnergy()).asObject()
        );
        TableColumn<Decision, Integer> pColumn = new TableColumn<>("P");
        pColumn.prefWidthProperty().bind(decisionsTable.widthProperty().multiply(0.2));
        pColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Decision, Integer> param) ->
                        new ReadOnlyIntegerWrapper(param.getValue().getP().intValue()).asObject()
        );

        decisionsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                       MainController.this.showSummary((Decision) newValue);
                    }
                }
        );
        decisionsTable.getColumns().setAll(sizeColumn, pColumn, energyColumn);
        decisionsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        decisionsTable.setPlaceholder(new Text("Отсутствуют данные"));
        decisionsTable.getSelectionModel().setCellSelectionEnabled(true);
        decisionsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableUtils.installCopyPasteHandler(decisionsTable);

        //setup up notification pane properties

        notificationPane.setText("New data available for review.");
        //notificationPane.setGraphic(new ImageView(INFO));
        notificationPane.setShowFromTop(true);
        /*notificationPane.getActions().add(new Action("123", new Consumer<javafx.event.ActionEvent>() {
            @Override
            public void accept(javafx.event.ActionEvent actionEvent) {
                System.out.println("123");
                notificationPane.hide();
            }
        }));*/
        Button button = new Button("notification");
        notificationPane.setContent(button);
        button.setOnAction((event) -> {
            notificationPane.setText("pressed!");
            notificationPane.show();
        });

    }

    private void initSummary() {
        TableColumn<Vertex, Double> xColumn = new TableColumn<>("X");
        xColumn.prefWidthProperty().bind(summaryCoordsTable.widthProperty().divide(1));
        xColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Vertex, Double> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().x).asObject()
        );
        TableColumn<Vertex, Double> yColumn = new TableColumn<>("Y");
        yColumn.prefWidthProperty().bind(summaryCoordsTable.widthProperty().divide(1));
        yColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Vertex, Double> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().y).asObject()
        );
        TableColumn<Vertex, Double> zColumn = new TableColumn<>("Z");
        zColumn.prefWidthProperty().bind(summaryCoordsTable.widthProperty().divide(1));
        zColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Vertex, Double> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().z).asObject()
        );
        xColumn.setSortable(false);
        yColumn.setSortable(false);
        zColumn.setSortable(false);
        summaryCoordsTable.getColumns().setAll(xColumn, yColumn, zColumn);
        summaryCoordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        summaryCoordsTable.setPlaceholder(new Text("Отсутствуют данные"));
        // enable multi-selection
        summaryCoordsTable.getSelectionModel().setCellSelectionEnabled(true);
        summaryCoordsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // enable copy/paste
        TableUtils.installCopyPasteHandler(summaryCoordsTable);
    }

}
