package br.com.inatel.leo.service;

import br.com.inatel.leo.model.GpsData;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class ImageProcessingService {

    // Constantes para manter o padrão visual (Clean Code)
    private static final int FOOTER_HEIGHT = 140;
    private static final Color BACKGROUND_COLOR = new Color(15, 15, 15); // Preto "Premium"
    private static final Color TEXT_COLOR = Color.WHITE;

    public BufferedImage overlayGpsInfo(BufferedImage satelliteImage, GpsData data) {
        int width = satelliteImage.getWidth();
        int height = satelliteImage.getHeight();

        // 1. Criamos uma nova imagem com espaço extra para o rodapé
        BufferedImage finalImage = new BufferedImage(width, height + FOOTER_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = finalImage.createGraphics();

        // 2. Configurações de Qualidade (O segredo do portfólio)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 3. Desenhar a imagem do satélite no topo
        g2d.drawImage(satelliteImage, 0, 0, null);

        // 4. Desenhar o painel inferior (o rodapé preto)
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, height, width, FOOTER_HEIGHT);

        // 5. Escrever os textos (Simulando o seu print)
        g2d.setColor(TEXT_COLOR);

        // Endereço (Destaque)
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.drawString("Endereço", 25, height + 35);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 15));
        g2d.drawString(data.getEndereco(), 25, height + 55);

        // Coluna 1: Coordenadas
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14)); // Fonte mono parece mais "técnica"
        g2d.drawString("Latitude", 25, height + 90);
        g2d.drawString("Longitude", 25, height + 110);

        g2d.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2d.drawString(String.valueOf(data.getLatitude()), 120, height + 90);
        g2d.drawString(String.valueOf(data.getLongitude()), 120, height + 110);

        // Coluna 2: Data e Altitude
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString("Data", width / 2 + 50, height + 90);
        g2d.drawString("Altitude", width / 2 + 50, height + 110);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2d.drawString(data.getFormattedDate(), width / 2 + 130, height + 90);
        g2d.drawString(data.getAltitude() + " m a.s.l", width / 2 + 130, height + 110);

        g2d.dispose();
        return finalImage;
    }
}