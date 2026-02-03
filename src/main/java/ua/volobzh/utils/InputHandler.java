package ua.volobzh.utils;

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
                    return Integer.parseInt(input);
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
