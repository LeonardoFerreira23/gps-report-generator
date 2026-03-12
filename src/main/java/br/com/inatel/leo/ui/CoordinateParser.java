package br.com.inatel.leo.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateParser {
    public static double parse(String input) {
        if (input == null || input.isBlank()) throw new IllegalArgumentException("Campo vazio");
        input = input.trim().replace(",", ".");

        // Caso Decimal: -22.257
        if (input.matches("^-?\\d+(\\.\\d+)?$")) {
            return Double.parseDouble(input);
        }

        // Caso GSM: Aceita com ou sem espaços/símbolos
        // Explicação: Procura grupos de números que podem ou não ter sujeira entre eles
        Pattern pattern = Pattern.compile("(\\d+)\\D*(\\d+)\\D*(\\d+(\\.\\d+)?)\\D*([NSEWnsew]?)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            double d = Double.parseDouble(matcher.group(1));
            double m = Double.parseDouble(matcher.group(2));
            double s = Double.parseDouble(matcher.group(3));
            String dir = matcher.group(5).toUpperCase();

            double decimal = d + (m / 60.0) + (s / 3600.0);
            return (dir.equals("S") || dir.equals("W")) ? -decimal : decimal;
        }
        throw new IllegalArgumentException("Formato inválido. Ex: 22 15 26 S ou 221526S");
    }
}