package br.com.inatel.leo.service;

import br.com.inatel.leo.client.GoogleMapsClient;
import br.com.inatel.leo.model.GpsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // Gera o construtor para o Spring injetar os componentes
public class GpsReportService {

    private final GoogleMapsClient mapsClient;
    private final ImageProcessingService imageProcessor;

    public BufferedImage generateFullReport(double lat, double lon) throws Exception {
        // 1. Busca os dados reais em paralelo/sequência
        String enderecoReal = mapsClient.getAddressFromCoords(lat, lon);
        double altitudeReal = mapsClient.getElevationFromCoords(lat, lon);
        BufferedImage mapaSatelite = mapsClient.getSatelliteImage(lat, lon, 17);

        // 2. Monta o objeto com os dados que o Google nos deu
        GpsData data = GpsData.builder()
                .latitude(lat)
                .longitude(lon)
                .altitude(Math.round(altitudeReal)) // Arredondando para ficar bonito
                .endereco(enderecoReal)
                .dataHora(LocalDateTime.now())
                .build();

        // 3. Devolve a imagem "soldada" com o rodapé
        return imageProcessor.overlayGpsInfo(mapaSatelite, data);
    }
}