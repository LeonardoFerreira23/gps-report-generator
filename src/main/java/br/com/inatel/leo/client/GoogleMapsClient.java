package br.com.inatel.leo.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class GoogleMapsClient {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private final String staticMapBaseUrl = "https://maps.googleapis.com/maps/api/staticmap";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Busca a imagem de satélite com o marcador verde centralizado.
     */
    public BufferedImage getSatelliteImage(double lat, double lon, int zoom) throws IOException {
        String url = UriComponentsBuilder.fromUriString(staticMapBaseUrl)
                .queryParam("center", lat + "," + lon)
                .queryParam("zoom", zoom)
                .queryParam("size", "640x640")
                .queryParam("maptype", "satellite")
                .queryParam("markers", "color:green|" + lat + "," + lon)
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);

        if (imageBytes == null) {
            throw new RuntimeException("Falha no download da imagem de satélite.");
        }

        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    /**
     * Busca o endereço formatado a partir de coordenadas (Geocoding).
     */
    public String getAddressFromCoords(double lat, double lon) {
        String url = UriComponentsBuilder.fromUriString("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("latlng", lat + "," + lon)
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(response);
            return root.path("results").get(0).path("formatted_address").asText("Endereço não encontrado");
        } catch (Exception e) {
            return "Erro ao buscar endereço";
        }
    }

    /**
     * Busca a altitude em metros a partir de coordenadas (Elevation).
     */
    public double getElevationFromCoords(double lat, double lon) {
        String url = UriComponentsBuilder.fromUriString("https://maps.googleapis.com/maps/api/elevation/json")
                .queryParam("locations", lat + "," + lon)
                .queryParam("key", apiKey)
                .build().toUriString();

        try {
            JsonNode root = mapper.readTree(restTemplate.getForObject(url, String.class));
            // O segredo está aqui: results[0].elevation
            JsonNode results = root.path("results");
            if (results.isArray() && !results.isEmpty()) {
                return results.get(0).path("elevation").asDouble(0.0);
            }
        } catch (Exception e) {
            System.err.println("Erro na Altitude: " + e.getMessage());
        }
        return 0.0;
    }
}
