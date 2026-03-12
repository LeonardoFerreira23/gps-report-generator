package br.com.inatel.leo.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateParser {
    public static double parse(String input) {
        if (input == null || input.isBlank()) throw new IllegalArgumentException("Campo vazio");
        input = input.trim().replace(",", ".");

        // 1. Caso Decimal (-21.02)
        if (input.matches("^-?\\d+(\\.\\d+)?$")) {
            return Double.parseDouble(input);
        }

        // 2. Caso GSM (18 14 4.12 S)
        // Usamos \\s* para espaços, assim ele não "engole" a letra da direção
        Pattern pattern = Pattern.compile("(\\d+)\\s+(\\d+)\\s+([\\d.]+)\\s*([NSEWnsew]?)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            double d = Double.parseDouble(matcher.group(1));
            double m = Double.parseDouble(matcher.group(2));
            double s = Double.parseDouble(matcher.group(3));
            String dir = matcher.group(4).toUpperCase(); // Agora a direção é o grupo 4

            double decimal = d + (m / 60.0) + (s / 3600.0);
            return (dir.equals("S") || dir.equals("W")) ? -decimal : decimal;
        }
        throw new IllegalArgumentException("Formato inválido! Use: 18 14 4.12 S");
    }
}