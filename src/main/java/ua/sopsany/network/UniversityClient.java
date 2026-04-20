package ua.sopsany.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
public class UniversityClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 9090;

        System.out.println("Connecting to " + host + ":" + port + "...");

        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner console = new Scanner(System.in)
        ) {
            System.out.println("Connected.\n");


            printResponse(in);

            while (true) {
                System.out.print("> ");
                if (!console.hasNextLine()) break;
                String line = console.nextLine().trim();
                if (line.isEmpty()) continue;

                out.println(line);
                printResponse(in);

                if (line.equalsIgnoreCase("EXIT")) {
                    System.out.println("Disconnecting...");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }

    private static void printResponse(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            if ("END".equals(line)) break;
            System.out.println(line);
        }
        System.out.println();
    }
}