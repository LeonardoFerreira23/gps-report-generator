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

    private BufferedImage currentFullImage;
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

            // 1. Gera a imagem base com o rodapé
            BufferedImage bi = reportService.generateFullReport(lat, lon);

            // 2. ADICIONE ESSA LINHA: (Vincula a imagem ao botão salvar)
            this.currentFullImage = bi;

            // 3. Adiciona o texto no mapa
            if (localName != null && !localName.isBlank()) {
                bi = imageProcessingService.addMarkerLabel(bi, localName);
            }

            // 4. Mostra na tela
            Image fxImage = SwingFXUtils.toFXImage(bi, null);
            imgPreview.setImage(fxImage);

            // Ajuste de tamanho dinâmico
            imgPreview.setPreserveRatio(true);
            imgPreview.fitWidthProperty().bind(imgPreview.getScene().widthProperty().subtract(350));

        } catch (Exception e) {
            exibirErro("Erro", e.getMessage());
        }
    }

    private void exibirErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void handleSalvarImagem() {
        if (currentFullImage == null) {
            exibirErro("Aviso", "Gere um relatório antes de salvar!");
            return;
        }

        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Salvar Relatório");
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Imagem PNG", "*.png"));
        fileChooser.setInitialFileName("Relatorio_GPS.png");

        java.io.File file = fileChooser.showSaveDialog(txtLat.getScene().getWindow());

        if (file != null) {
            try {
                javax.imageio.ImageIO.write(currentFullImage, "png", file);
            } catch (Exception e) {
                exibirErro("Erro", "Falha ao gravar arquivo: " + e.getMessage());
            }
        }
    }
}