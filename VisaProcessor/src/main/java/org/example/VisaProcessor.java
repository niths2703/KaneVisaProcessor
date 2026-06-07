package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class VisaProcessor {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static void main(String[] args) {

        String inputFile = "src/main/resources/input.txt";
        String outputFile = "src/main/resources/output.csv";

        try (
                BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile));
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))
        ) {

            writer.write("Name,Category");
            writer.newLine();

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length < 3) {
                    continue;
                }

                String name = parts[0].trim();
                String ageStr = parts[1].trim();

                // Email assumed to be last field
                String email = parts[parts.length - 1].trim();

                // Address = everything between age and email
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = 2; i < parts.length - 1; i++) {
                    if (!parts[i].trim().isEmpty()) {
                        addressBuilder.append(parts[i].trim()).append(" ");
                    }
                }

                String address = addressBuilder.toString().trim();

                // Rule 1: Age present
                if (ageStr.isEmpty()) {
                    continue;
                }

                int age;
                try {
                    age = Integer.parseInt(ageStr);
                } catch (NumberFormatException e) {
                    continue;
                }

                // Rule 2: Address present
                if (address.isEmpty()) {
                    continue;
                }

                // Rule 3: Valid email
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    continue;
                }

                // Rule 4: Must live in India
                if (!address.toLowerCase().contains("india")) {
                    continue;
                }

                String category = age >= 18 ? "Adult" : "Kid";

                writer.write(name + "," + category);
                writer.newLine();
            }

            System.out.println("CSV file generated successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
