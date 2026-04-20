package ua.sopsany.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sopsany.Main;
import ua.sopsany.models.Faculty;
import ua.sopsany.models.Student;
import ua.sopsany.utils.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class UniversityServer {

    private static final Logger log = LoggerFactory.getLogger(UniversityServer.class);

    private final int port;
    private volatile boolean running = false;
    private ServerSocket serverSocket;

    public UniversityServer(int port) {
        this.port = port;
    }

    public void start() {
        Thread serverThread = new Thread(this::listenLoop, "TCP-Server");
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private void listenLoop() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            log.info("TCP server started on port {}", port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                log.info("TCP client connected: {}", clientSocket.getInetAddress());

                Thread handler = new Thread(() -> handleClient(clientSocket), "TCP-Client");
                handler.setDaemon(true);
                handler.start();
            }
        } catch (IOException e) {
            if (running) {
                log.error("TCP server error", e);
            }
        }
    }

    private void handleClient(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("=== DigiUni TCP Server ===");
            out.println("Type HELP to see available commands");
            out.println("END");

            String line;
            while ((line = in.readLine()) != null) {
                String trimmed = line.trim();
                log.info("TCP command received: '{}'", trimmed);
                String response = processCommand(trimmed);
                out.println(response);
                out.println("END");

                if (trimmed.equalsIgnoreCase("EXIT")) {
                    break;
                }
            }
        } catch (IOException e) {
            log.warn("TCP client I/O error: {}", e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
            log.info("TCP client disconnected");
        }
    }

    private String processCommand(String input) {
        if (input.isEmpty()) {
            return "(empty command) type HELP";
        }

        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0].toUpperCase();
        String arg = parts.length > 1 ? parts[1] : "";

        switch (cmd) {
            case "PING":
                return "PONG";
            case "HELP":
                return "Available commands:\n"
                        + "  PING                     - check if server is alive\n"
                        + "  HELP                     - show this message\n"
                        + "  LIST_STUDENTS            - list all students\n"
                        + "  LIST_FACULTIES           - list all faculties\n"
                        + "  FIND_STUDENT <ticketId>  - find student by their ticket ID\n"
                        + "  EXIT                     - close connection";
            case "LIST_STUDENTS":
                return listStudents();
            case "LIST_FACULTIES":
                return listFaculties();
            case "FIND_STUDENT":
                if (arg.isEmpty()) return "Usage: FIND_STUDENT <ticketId>";
                return findStudent(arg);
            case "EXIT":
                return "Goodbye!";
            default:
                return "Unknown command: '" + cmd + "'. Type HELP for the list.";
        }
    }

    private String listStudents() {
        List<Student> all = Repository.studentRepo.getAll();
        if (all.isEmpty()) return "(no students in the system)";
        StringBuilder sb = new StringBuilder("Total students: ").append(all.size());
        for (Student s : all) {
            sb.append("\n  ").append(s);
        }
        return sb.toString();
    }

    private String listFaculties() {
        if (Main.university == null) return "University is not initialized";
        List<Faculty> faculties = Main.university.getFaculties();
        if (faculties.isEmpty()) return "(no faculties)";
        StringBuilder sb = new StringBuilder("Total faculties: ").append(faculties.size());
        for (Faculty f : faculties) {
            sb.append("\n  ").append(f);
        }
        return sb.toString();
    }

    private String findStudent(String ticketId) {
        return Repository.studentRepo.getAll().stream()
                .filter(s -> ticketId.equalsIgnoreCase(s.getStudentID()))
                .findFirst()
                .map(s -> "FOUND: " + s)
                .orElse("Student with ticket ID '" + ticketId + "' not found");
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException ignored) {}
    }
}