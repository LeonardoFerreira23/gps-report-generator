package br.com.inatel.leo.service;

import br.com.inatel.leo.model.GpsData;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;

@Service
public class ImageProcessingService {

    // MÉTODO QUE ESTAVA FALTANDO (O RODAPÉ)
    public BufferedImage overlayGpsInfo(BufferedImage originalImage, GpsData data) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int multiplier = (width > 640) ? 2 : 1;
        // Fixamos o rodapé em 100 pixels (tamanho ideal para 4 linhas de texto)
        int footerHeight = 100 * multiplier;

        BufferedImage combined = new BufferedImage(width, height + footerHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = combined.createGraphics();

        g2d.drawImage(originalImage, 0, 0, null);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, height, width, footerHeight);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);

        int margin = 20 * multiplier;

        // LINHA 1: Título Endereço (Y = 25)
        g2d.setFont(new Font("Arial", Font.BOLD, 14 * multiplier));
        g2d.drawString("Endereço", margin, height + (25 * multiplier));

        // LINHA 2: Texto do Endereço (Y = 45)
        g2d.setFont(new Font("Arial", Font.PLAIN, 11 * multiplier));
        String end = (data.getEndereco() != null) ? data.getEndereco() : "Buscando...";
        g2d.drawString(end, margin, height + (45 * multiplier));

        // LINHA 3: Latitude (Esquerda) e Altitude (Direita) (Y = 70)
        g2d.setFont(new Font("Arial", Font.BOLD, 12 * multiplier));
        g2d.drawString("Latitude:", margin, height + (70 * multiplier));

        int colDireitaX = width / 2 + (50 * multiplier);
        g2d.drawString("Altitude:", colDireitaX, height + (70 * multiplier));

        g2d.setFont(new Font("Arial", Font.PLAIN, 12 * multiplier));
        g2d.drawString(String.valueOf(data.getLatitude()), margin + (80 * multiplier), height + (70 * multiplier));
        String altitudeText = String.format("%.2f m a.s.l", data.getAltitude());
        g2d.drawString(altitudeText, colDireitaX + (70 * multiplier), height + (70 * multiplier));

        // LINHA 4: Longitude (Y = 90)
        g2d.setFont(new Font("Arial", Font.BOLD, 12 * multiplier));
        g2d.drawString("Longitude:", margin, height + (90 * multiplier));

        g2d.setFont(new Font("Arial", Font.PLAIN, 12 * multiplier));
        g2d.drawString(String.valueOf(data.getLongitude()), margin + (80 * multiplier), height + (90 * multiplier));

        g2d.dispose();
        return combined;
    }


    // MÉTODO DO PONTO COM NOME (QUE ADICIONAMOS ANTES)
    public BufferedImage addMarkerLabel(BufferedImage image, String label) {
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();

        int x = (image.getWidth() / 2) - (metrics.stringWidth(label) / 2);
        int y = (image.getHeight() / 2) - 25; // Sobe 25 pixels para não cobrir o marcador

        // Sombra leve
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(label, x + 1, y + 1);

        // Texto Principal
        g2d.setColor(Color.YELLOW);
        g2d.drawString(label, x, y);

        g2d.dispose();
        return image;
    }
}