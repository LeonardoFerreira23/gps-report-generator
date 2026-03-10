package br.com.inatel.leo;

import br.com.inatel.leo.client.GoogleMapsClient;
import br.com.inatel.leo.model.GpsData;
import br.com.inatel.leo.service.GpsReportService;
import br.com.inatel.leo.service.ImageProcessingService;
import br.com.inatel.leo.ui.JavafxApplication;
import javafx.application.Application;
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
        Application.launch(JavafxApplication.class, args);
    }

}