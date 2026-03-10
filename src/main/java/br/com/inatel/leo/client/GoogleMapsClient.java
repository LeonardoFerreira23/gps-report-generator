package br.com.inatel.leo.client;

// CERTIFIQUE-SE DE QUE ESTE IMPORT ESTÁ EXATAMENTE ASSIM:
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class GoogleMapsClient {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private final String baseUrl = "https://maps.googleapis.com/maps/api/staticmap";
    private final RestTemplate restTemplate = new RestTemplate();

    public BufferedImage getSatelliteImage(double lat, double lon, int zoom) throws IOException {

        // Trocamos o fromHttpUrl por fromUriString (mais seguro e padrão)
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("center", lat + "," + lon)
                .queryParam("zoom", zoom)
                .queryParam("size", "640x640")
                .queryParam("maptype", "satellite")
                .queryParam("markers", "color:green|" + lat + "," + lon)
                .queryParam("key", apiKey)
                .build() // O build() cria o objeto UriComponents
                .toUriString(); // Converte para a String final

        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);

        if (imageBytes == null) {
            throw new RuntimeException("Falha no download. Verifique se a API Key no application.properties está correta.");
        }

        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }
}