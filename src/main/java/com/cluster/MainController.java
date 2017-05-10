package com.cluster;

import com.cluster.gui.TableUtils;
import com.cluster.math.ExecutorService;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Vertex;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jzy3d.analysis.AnalysisLauncher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * Created by envoy on 09.03.2017.
 */
public class MainController implements Initializable, EventHandler<WindowEvent> {

    @FXML
    TableView decisionsTable;
    @FXML
    TableView summaryVerticesTable;
    @FXML
    Label summaryP;
    @FXML
    TextField summaryEnergy;
    @FXML
    Label summarySize;
    @FXML
    TextField summaryBits;
    @FXML
    MenuItem processMenuItem;
    @FXML
    MenuItem exitMenuItem;
    @FXML
    MenuItem aboutMenuItem;
    @FXML
    HBox processHBox;
    @FXML
    Button graphBtn;

    private static final String ERROR_IO = "Произошла ошибка чтения/записи";
    private static final String ERROR_WTF = "Произошла непредвиденная ошибка";
    private static final String INFO_FINISH = "Расчеты завершились успешно, потраченное время: ";
    private static final String ERROR_EXIT = "Во время расчетов работу приложения невозможно завершить";
    private static final String ABOUT = "Справочная информация";
    private Conformation conformation;

    public void initialize(URL location, ResourceBundle resources) {
        initConformations();
        initSummary();
        initProcess();

        MainApp.primaryStage.setOnCloseRequest(this);

        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainApp.primaryStage.fireEvent(
                        new WindowEvent(
                                MainApp.primaryStage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );
            }
        });

        aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage dialog = new Stage();
                dialog.getIcons().addAll(MainApp.primaryStage.getIcons());
                dialog.setTitle(ABOUT);
                dialog.initOwner(MainApp.primaryStage);
                WebView webView = new WebView();
                webView.getEngine().load(getClass().getResource("/about.html").toExternalForm());
                VBox mainVbox = new VBox(webView, new Label("(С) Коварце А.Н., Шашов К.В."), new Label("Самарский Университет, Самара, 2017"));
                mainVbox.setAlignment(Pos.CENTER_LEFT);
                mainVbox.setSpacing(5);
                mainVbox.setPadding(new Insets(10));
                dialog.setScene(new Scene(mainVbox, 750, 400));
                dialog.setMinWidth(720);
                dialog.setMinHeight(400);

                dialog.show();
            }
        });
    }

    private void initProcess() {
        processMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onStart();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ExecutorService.init(MainApp.args);

                            StronginTask.ProgressCallBack progressCallBack = initProgressCallBack();

                            ExecutorService.process(progressCallBack, new ExecutorService.OnFinishCallBack() {
                                @Override
                                public void onFinish(List<Conformation> results, long milliseconds) {
                                    addConformations(results);
                                    showFinishMsg(INFO_FINISH + (milliseconds / 1000) + " c.", false);
                                }
                            });

                        } catch (ExecutionException e) {
                            ExecutorService.logError(e);
                            showFinishMsg(ERROR_WTF, true);
                        } catch (InterruptedException e) {
                            ExecutorService.logError(e);
                            showFinishMsg(ERROR_WTF, true);
                        } catch (FileNotFoundException e) {
                            ExecutorService.logError(e);
                            showFinishMsg(ERROR_IO + " при сохранении результатов", true);
                        } catch (IOException e) {
                            ExecutorService.logError(e);
                            showFinishMsg(ERROR_IO, true);
                        }
                    }
                }).start();
            }
        });
    }

    private StronginTask.ProgressCallBack initProgressCallBack() {
        ArrayList<ProgressIndicator> indicators = new ArrayList<>();
        ProgressIndicator finishIndicator = new ProgressIndicator(0);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                processHBox.getChildren().clear();
                for (int i = 0; i < Configuration.get().getTHREADS_COUNT(); i++) {
                    VBox vBox = new VBox();
                    vBox.setAlignment(Pos.CENTER);
                    vBox.setPadding(new Insets(10));
                    indicators.add(new ProgressIndicator(0));
                    vBox.getChildren().addAll(new Label("Поток #" + (i + 1)), indicators.get(i));
                    processHBox.getChildren().add(vBox);
                }

                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                vBox.setPadding(new Insets(10));
                vBox.getChildren().addAll(new Label("Обработка"), finishIndicator);
                processHBox.getChildren().addAll(vBox);
            }
        });

        StronginTask.ProgressCallBack progressCallBack = new StronginTask.ProgressCallBack() {
            @Override
            public void onProgress(int percent) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        indicators.get(getId()).setProgress(percent / 100.0);
                    }
                });
            }

            @Override
            public void onFinish(int percent) {
                finishIndicator.setProgress(percent / 100.0);
            }
        };
        return progressCallBack;
    }

    private void addConformations(Collection<Conformation> conformations) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                decisionsTable.getItems().addAll(conformations);
                decisionsTable.refresh();
            }
        });
    }

    private void showSummary(Conformation conformation) {
        this.conformation = conformation;
        graphBtn.setDisable(false);
        summaryEnergy.setText(String.valueOf(conformation.getEnergy()));
        summaryP.setText(String.valueOf(Configuration.get().getRO()));
        summarySize.setText(String.valueOf(Configuration.get().getN()));
        summaryBits.setText(conformation.getBits().getBites().toString());
        summaryVerticesTable.getItems().clear();
        summaryVerticesTable.getItems().addAll(conformation.getVertices());
        summaryVerticesTable.refresh();
    }

    private void initConformations() {
        TableColumn<Conformation, Double> energyColumn = new TableColumn<>("Энергия");
        energyColumn.prefWidthProperty().bind(decisionsTable.widthProperty().multiply(0.6));
        energyColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Conformation, Double> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().getEnergy()).asObject()
        );

        decisionsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                                                                                  @Override
                                                                                  public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                                                                      if (newValue != null) {
                                                                                          MainController.this.showSummary((Conformation) newValue);
                                                                                      }
                                                                                  }
                                                                              }
        );
        energyColumn.setSortable(false);
        decisionsTable.getColumns().setAll(energyColumn);
        decisionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        decisionsTable.setPlaceholder(new Text("Отсутствуют данные"));
        decisionsTable.getSelectionModel().setCellSelectionEnabled(true);
        decisionsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableUtils.installCopyPasteHandler(decisionsTable);
        decisionsTable.getItems().clear();
    }

    private void onStart() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                processMenuItem.setDisable(true);

                if (decisionsTable.getItems() != null) {
                    decisionsTable.getItems().clear();
                    decisionsTable.refresh();
                }

                processHBox.setVisible(true);
            }
        });

    }

    private void showFinishMsg(String s, boolean isError) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(isError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
                alert.initOwner(MainApp.primaryStage);
                alert.setTitle(isError ? "Ошибка" : "Информация");
                alert.setHeaderText(null);
                alert.setContentText(s);
                alert.show();
                onFinish();
            }
        });
    }

    private void onFinish() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                processMenuItem.setDisable(false);
                processHBox.getChildren().clear();
            }
        });
    }

    private void initSummary() {
        TableColumn<Vertex, Double> xColumn = new TableColumn<>("X");
        xColumn.prefWidthProperty().bind(summaryVerticesTable.widthProperty().divide(1));
        xColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Vertex, Double> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().x).asObject()
        );
        TableColumn<Vertex, Double> yColumn = new TableColumn<>("Y");
        yColumn.prefWidthProperty().bind(summaryVerticesTable.widthProperty().divide(1));
        yColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Vertex, Double> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().y).asObject()
        );
        TableColumn<Vertex, Double> zColumn = new TableColumn<>("Z");
        zColumn.prefWidthProperty().bind(summaryVerticesTable.widthProperty().divide(1));
        zColumn.setCellValueFactory(
                (TableColumn.CellDataFeatures<Vertex, Double> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().z).asObject()
        );
        xColumn.setSortable(false);
        yColumn.setSortable(false);
        zColumn.setSortable(false);
        summaryVerticesTable.getColumns().setAll(xColumn, yColumn, zColumn);
        summaryVerticesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        summaryVerticesTable.setPlaceholder(new Text("Отсутствуют данные"));
        // enable multi-selection
        summaryVerticesTable.getSelectionModel().setCellSelectionEnabled(true);
        summaryVerticesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        summaryVerticesTable.getColumns().addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced()) {
                    summaryVerticesTable.getColumns().clear();
                    summaryVerticesTable.getColumns().addAll(xColumn, yColumn, zColumn);
                }
            }
        });
        summaryBits.setText("--");
        // enable copy/paste
        TableUtils.installCopyPasteHandler(summaryVerticesTable);
        graphBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (conformation != null) {
                    try {
                        AnalysisLauncher.open(new Graph(conformation.getVertices()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        graphBtn.setDisable(true);
    }

    @Override
    public void handle(WindowEvent event) {
        if (processMenuItem.isDisable()) {
            event.consume();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(MainApp.primaryStage);
                    alert.setTitle("Предупреждение");
                    alert.setHeaderText(null);
                    alert.setContentText(ERROR_EXIT);
                    alert.show();
                }
            });

        }
    }
}
