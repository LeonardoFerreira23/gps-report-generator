package br.com.inatel.leo;

import br.com.inatel.leo.client.GoogleMapsClient;
import br.com.inatel.leo.model.GpsData;
import br.com.inatel.leo.service.ImageProcessingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;

@SpringBootApplication
public class GpsReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsReportApplication.class, args);
    }

    // Este Bean será executado assim que o projeto iniciar
    @Bean
    public CommandLineRunner testarIntegracao(GoogleMapsClient client, ImageProcessingService processor) {
        return args -> {
            try {
                System.out.println(">>> [LOG] Iniciando teste de integração...");

                // 1. Buscamos a imagem do satélite (Coordenadas de teste)
                // Vamos usar as do seu print: -21.014417, -47.541667
                double lat = -21.014417;
                double lon = -47.541667;

                System.out.println(">>> [LOG] Fazendo requisição ao Google Maps...");
                BufferedImage mapaBase = client.getSatelliteImage(lat, lon, 17);

                // 2. Criamos um objeto GpsData "fake" para preencher o rodapé
                GpsData dadosTeste = GpsData.builder()
                        .latitude(lat)
                        .longitude(lon)
                        .altitude(590)
                        .endereco("São Paulo, Brasil")
                        .dataHora(LocalDateTime.now())
                        .build();

                // 3. Chamamos o seu serviço de processamento para "soldar" as partes
                System.out.println(">>> [LOG] Aplicando layout de GPS...");
                BufferedImage imagemFinal = processor.overlayGpsInfo(mapaBase, dadosTeste);

                // 4. Salvamos o arquivo na raiz do seu projeto
                File arquivoResultado = new File("TESTE_GPS_PORTFOLIO.png");
                ImageIO.write(imagemFinal, "png", arquivoResultado);

                System.out.println(">>> [LOG] SUCESSO! A foto foi gerada em: " + arquivoResultado.getAbsolutePath());

            } catch (Exception e) {
                System.err.println(">>> [ERRO] Ocorreu uma falha no teste: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}