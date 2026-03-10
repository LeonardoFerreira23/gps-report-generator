package br.com.inatel.leo.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateParser {
    public static double parse(String input) {
        input = input.trim().replace(",", ".");

        // Se já for decimal (contém apenas números, ponto e sinal de menos)
        if (input.matches("^-?\\d+(\\.\\d+)?$")) {
            return Double.parseDouble(input);
        }

        // Regex para capturar Graus, Minutos e Segundos (GSM/DMS)
        // Ex: 22 15 26.8 S ou 22° 15' 26" S
        Pattern pattern = Pattern.compile("(\\d+)\\D+(\\d+)\\D+(\\d+(\\.\\d+)?)\\D*([NSEWnsew]?)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            double degrees = Double.parseDouble(matcher.group(1));
            double minutes = Double.parseDouble(matcher.group(2));
            double seconds = Double.parseDouble(matcher.group(3));
            String direction = matcher.group(5).toUpperCase();

            double decimal = degrees + (minutes / 60.0) + (seconds / 3600.0);

            if (direction.equals("S") || direction.equals("W")) {
                decimal *= -1;
            }
            return decimal;
        }

        throw new IllegalArgumentException("Formato de coordenada inválido.");
    }
}