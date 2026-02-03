package ua.sopsany.models;

import java.util.Scanner;

public class InputHandler {

    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt + ":");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error, field is empty");
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ":");
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

}
