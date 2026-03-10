package br.com.inatel.leo.ui;

import br.com.inatel.leo.service.GpsReportService;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
@RequiredArgsConstructor
public class MainController {

    private final GpsReportService reportService;

    @FXML private TextField txtLat;
    @FXML private TextField txtLon;
    @FXML private ImageView imgPreview;

    @FXML
    public void handleGerarRelatorio() {
        try {
            // Agora aceita tanto -22.257 quanto 22 15 26 S
            double lat = CoordinateParser.parse(txtLat.getText());
            double lon = CoordinateParser.parse(txtLon.getText());

            BufferedImage bufferedImage = reportService.generateFullReport(lat, lon);
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

            imgPreview.setFitWidth(imgPreview.getParent().getBoundsInLocal().getWidth()); // Ou um valor fixo tipo 600
            imgPreview.setSmooth(true);
            imgPreview.setCache(true);
            imgPreview.setPreserveRatio(true);
            imgPreview.setImage(fxImage);

        } catch (Exception e) {
            exibirErro("Erro", "Formato inválido: " + e.getMessage());
        }
    }

    private void exibirErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}