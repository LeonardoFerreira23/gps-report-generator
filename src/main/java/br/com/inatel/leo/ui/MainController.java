package br.com.inatel.leo.ui;

import br.com.inatel.leo.service.GpsReportService;
import br.com.inatel.leo.service.ImageProcessingService;
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
    private final ImageProcessingService imageProcessingService;
    @FXML private TextField txtLat;
    @FXML private TextField txtLon;
    @FXML private ImageView imgPreview;
    @FXML private TextField txtLocalName; // Novo campo

    @FXML
    public void handleGerarRelatorio() {
        try {
            double lat = CoordinateParser.parse(txtLat.getText());
            double lon = CoordinateParser.parse(txtLon.getText());
            String localName = txtLocalName.getText();

            // 1. Gera o relatório base
            BufferedImage bi = reportService.generateFullReport(lat, lon);

            // 2. Adiciona o nome personalizado no ponto se houver texto
            if (localName != null && !localName.isBlank()) {
                bi = imageProcessingService.addMarkerLabel(bi, localName);
            }

            // 3. Exibe e ajusta o tamanho
            Image fxImage = SwingFXUtils.toFXImage(bi, null);
            imgPreview.setImage(fxImage);

            // Força o redimensionamento dinâmico
            imgPreview.setPreserveRatio(true);
            imgPreview.setSmooth(true);

            // Se o ImageView estiver dentro de um ScrollPane ou AnchorPane,
            // podemos forçar ele a ocupar 80% da largura da janela, por exemplo:
            imgPreview.fitWidthProperty().bind(imgPreview.getScene().widthProperty().multiply(0.7));

        } catch (Exception e) {
            exibirErro("Falha no Processamento", e.getMessage());
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