package simpleplayerfx;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    private MediaPlayer mediaPlayer;
    private String sFilePath;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider slider;

    @FXML
    private Slider seeSlider;

    //Metodo para seleccionar el archivo mediante el explorador de archivos y el tamaño de la ventana del reproductor.
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //Objeto para poder escoger los ficheros.
        FileChooser filechoose = new FileChooser();

        //Limitamos la extension de los ficheros a "mp3".
        //FileChooser.ExtensionFilter filtermp4 = new FileChooser.ExtensionFilter("Selecciona un archivo (*.mp4)", "*.mp4");
        FileChooser.ExtensionFilter filtermp3 = new FileChooser.ExtensionFilter("Selecciona un archivo (*.mp3)", "*.mp3");

        filechoose.getExtensionFilters().add(filtermp3);
        //filechoose.getExtensionFilters().add(filtermp4);

        //Abrimos una ventana para que pueda escoger el fichero de forma grafica.
        File file = filechoose.showOpenDialog(null);

        sFilePath = file.toURI().toString();
        if (sFilePath != null) {
            Media media = new Media(sFilePath);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty hight = mediaView.fitHeightProperty();

            // La propiedad bind nos permitira re-escalar la ventana al tamaño que deseemos.
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            hight.bind(Bindings.selectDouble(mediaView.sceneProperty(), "hight"));

            //Controlamos el valor del control de volumen ya sea augmentando o disminuiendo su valor.
            slider.setValue(mediaPlayer.getVolume() * 100);
            slider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(slider.getValue() / 100);
                }
            });

            //Controlamos la posicion del tiempo de reproduccion.
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    seeSlider.setValue(newValue.toSeconds());
                }
            });

            //Permitimos que pueda ser controlado con el ratón.
            seeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(seeSlider.getValue()));
                }
            });

            mediaPlayer.play();
        }
    }

    //Metodos para los botones y sus correspondientes funciones.
    @FXML
    private void pauseVideo(ActionEvent event) {
        mediaPlayer.pause();
    }

    @FXML
    private void playVideo(ActionEvent event) {
        mediaPlayer.play();
        //Definimos la velocidad en 1(normal).
        mediaPlayer.setRate(1);
    }

    @FXML
    private void stopVideo(ActionEvent event) {
        mediaPlayer.stop();
    }

    @FXML
    private void fastVideo(ActionEvent event) {
        mediaPlayer.setRate(1.5);
    }

    @FXML
    private void fasterVideo(ActionEvent event) {
        mediaPlayer.setRate(3.0);
    }

    @FXML
    private void slowVideo(ActionEvent event) {
        mediaPlayer.setRate(0.75);
    }

    @FXML
    private void slowerVideo(ActionEvent event) {
        mediaPlayer.setRate(0.375);
    }

    @FXML
    private void exitVideo(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
