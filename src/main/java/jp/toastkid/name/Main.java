package jp.toastkid.name;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Name generator.
 *
 * @author Toast kid
 */
public class Main extends Application {

    private static final String TITLE = "Name Generator";

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /** FXML. */
    private static final String FXML_PATH = "scenes/NameMaker.fxml";

    /** stage. */
    private final Stage stage;

    /**
     * Constructor.
     */
    public Main() {
        this.stage = new Stage(StageStyle.DECORATED);

        stage.setScene(readScene());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(TITLE);
        stage.setOnCloseRequest(event -> stage.close());
    }

    /**
     * Read scene.
     * @return scene
     */
    private Scene readScene() {
        try {
            final FXMLLoader loader
                = new FXMLLoader(getClass().getClassLoader().getResource(FXML_PATH));
            loader.load();
            final Controller controller = (Controller) loader.getController();
            return new Scene(controller.getRoot());
        } catch (final IOException e) {
            LOGGER.error("Caught error.", e);
            Platform.exit();
        }
        return null;
    }

    /**
     * show this app with passed stage.
     * @param owner
     */
    public void show(final Stage owner) {
        this.stage.getScene().getStylesheets().addAll(owner.getScene().getStylesheets());
        this.stage.initOwner(owner);
        show();
    }

    /**
     * show this app.
     */
    public void show() {
        if (stage.getOwner() == null) {
            stage.setOnCloseRequest(event -> System.exit(0));
        }
        stage.showAndWait();
    }

    @Override
    public void start(final Stage stage) throws Exception {
        show();
    }

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(final String[] args) {
        Application.launch(Main.class);
    }
}
