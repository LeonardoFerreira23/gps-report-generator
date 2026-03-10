package br.com.inatel.leo.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class GpsData {
    private double latitude;
    private double longitude;
    private double altitude;
    private String endereco;
    private LocalDateTime dataHora;

    //Método para formatar data
    public String getFormattedDate() {
        if (dataHora == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");
        return dataHora.format(formatter);
    }
}