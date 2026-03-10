package br.com.inatel.leo.ui;

import br.com.inatel.leo.GpsReportApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("classpath:/main_view.fxml") // O caminho do seu arquivo no Scene Builder
    private Resource fxml;
    private final ApplicationContext applicationContext;

    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(fxml.getURL());
            // O segredo está aqui: o Spring vai gerenciar o Controller do JavaFX!
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            Parent parent = fxmlLoader.load();
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent, 800, 600));
            stage.setTitle("GPS Report Generator - Inatel Edition");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}