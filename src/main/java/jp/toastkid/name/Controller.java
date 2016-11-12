package jp.toastkid.name;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
 * NameMaker's controller.
 *
 * @author Toast kid
 */
public class Controller implements Initializable {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    /** root pane. */
    @FXML
    private Pane root;

    /** name nationalities. */
    @FXML
    private ComboBox<String> nationalities;

    /** name generating values. */
    @FXML
    private ComboBox<Integer> nameNums;

    /** contains generated names. */
    @FXML
    private TextArea nameOutput;

    /** Name generator. */
    private Generator generator;

    @Override
    public void initialize(final URL arg0, final ResourceBundle arg1) {

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            final long start = System.currentTimeMillis();
            generator = new Generator();
            nationalities.getItems().addAll(generator.getNationalities());
            Platform.runLater(() -> nationalities.getSelectionModel().select(0));
            final long elapsed = System.currentTimeMillis() - start;
            LOGGER.info("{} Ended initialize NameMaker. {}[ms]",
                    Thread.currentThread().getName(), elapsed + "ms");
        });
        executor.shutdown();
    }

    /**
     * Generate names.
     */
    @FXML
    private void generateNames() {
        nameOutput.setText(
                generator.generate(nationalities.getValue(), nameNums.getValue().intValue()));
    }

    /**
     * Get root pane.
     * @return root pane
     */
    protected Pane getRoot() {
        return root;
    }

}
