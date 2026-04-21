package ua.sopsany.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHandler {

    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error, field is empty");
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (dd.MM.yyyy): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Помилка: дата не може бути порожньою.");
                continue;
            }

            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Помилка! Неправильний формат дати. Спробуйте дд.мм.рррр (наприклад, 25.01.2005)");
            }
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                try {
                    int value = Integer.parseInt(input);
                    if (value >= min && value <= max)
                        return value;
                    else
                        System.out.println("Error: number must be between " + min + " and " + max);
                } catch (NumberFormatException e) {
                    System.out.println("Error, input is not a number");
                }
            }

        }
    }

    public int readInt(String prompt){
        return readInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public String readEmail(String prompt) {
        while (true) {
            String value = readString(prompt);
            int at = value.indexOf('@');
            if (at > 0 && at < value.length() - 1) {
                return value;
            }
            System.out.println("Error: email must contain '@' with something before and after it");
        }
    }

}