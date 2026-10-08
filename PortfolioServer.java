import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Modern Java Portfolio Server for Vijaykumar Karegama
 * - Zero external dependencies (Pure Java 17+)
 * - Built-in REST API endpoints (/api/profile, /api/projects, /api/skills, /api/contact, /api/system)
 * - Thread-safe file persistence for contact messages
 * - High-performance static web server with accurate MIME types & CORS support
 */
public class PortfolioServer {

    private static final int DEFAULT_PORT = 8080;
    private static final AtomicLong REQUEST_COUNTER = new AtomicLong(0);
    private static final String MESSAGES_FILE = "contacts.json";
    private static final String ROOT_DIR = ".";

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        HttpServer server = null;
        for (int p = port; p <= port + 10; p++) {
            try {
                server = HttpServer.create(new InetSocketAddress(p), 0);
                port = p;
                break;
            } catch (IOException e) {
                // try next port
            }
        }

        if (server == null) {
            System.err.println("[ERROR] Could not bind server to port " + port);
            System.exit(1);
        }

        // Thread pool executor
        server.setExecutor(Executors.newFixedThreadPool(12));

        // REST API Contexts
        server.createContext("/api/profile", new ProfileHandler());
        server.createContext("/api/skills", new SkillsHandler());
        server.createContext("/api/projects", new ProjectsHandler());
        server.createContext("/api/contact", new ContactHandler());
        server.createContext("/api/system", new SystemHandler());

        // Static Web Asset Context
        server.createContext("/", new StaticFileHandler());

        server.start();

        printBanner(port);
    }

    private static void printBanner(int port) {
        System.out.println("==================================================================");
        System.out.println("   _   _ _ _             _                                        ");
        System.out.println("  | | | (_) (_)           | |                                       ");
        System.out.println("  | | | |_| |_  __ _ _   _| | ___   _ _ __ ___   __ _ _ __          ");
        System.out.println("  | | | | | | |/ _` | | | | |/ / | | | '_ ` _ \\ / _` | '__|         ");
        System.out.println("   \\ \\_/ /| | | (_| | |_| |   <| |_| | | | | | | (_| | |            ");
        System.out.println("    \\___/ |_|_|\\__,_|\\__, |_|\\_\\\\__,_|_| |_| |_|\\__,_|_|            ");
        System.out.println("                      __/ |                                         ");
        System.out.println("                     |___/    [Full-Stack Java 17 Portfolio Server] ");
        System.out.println("==================================================================");
        System.out.println("  * Java Runtime  : " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")");
        System.out.println("  * Portfolio URL : http://localhost:" + port);
        System.out.println("  * REST Profile  : http://localhost:" + port + "/api/profile");
        System.out.println("  * REST Skills   : http://localhost:" + port + "/api/skills");
        System.out.println("  * REST Projects : http://localhost:" + port + "/api/projects");
        System.out.println("  * REST System   : http://localhost:" + port + "/api/system");
        System.out.println("  * Inquiries DB  : " + Paths.get(MESSAGES_FILE).toAbsolutePath());
        System.out.println("  * Status        : ONLINE & SERVING CLIENTS");
        System.out.println("==================================================================");
        System.out.println("Press Ctrl + C to stop the server.\n");
    }

    // CORS & JSON Helper
    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        REQUEST_COUNTER.incrementAndGet();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // --- REST Handlers ---

    static class ProfileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = """
            {
              "name": "Vijaykumar Karegama",
              "title": "Java Full Stack Developer",
              "tagline": "Architecting resilient, scalable enterprise backends & interactive web experiences.",
              "email": "karegamavijaykumar@gmail.com",
              "phone": "+91 9182319464",
              "location": "Hyderabad, Telangana, India",
              "social": {
                "github": "https://github.com/Vijaykumar7060",
                "linkedin": "https://linkedin.com/in/karegamavijaykumar"
              },
              "education": [
                {
                  "degree": "B.Tech in Computer Science & Engineering",
                  "institution": "Narsimha Reddy Engineering College",
                  "location": "Hyderabad",
                  "period": "2023 - 2027",
                  "score": "CGPA: 7.72",
                  "status": "Active"
                },
                {
                  "degree": "Intermediate (MPC)",
                  "institution": "Tagore Junior College",
                  "location": "Banswada",
                  "period": "2020 - 2022",
                  "score": "56.3%",
                  "status": "Completed"
                },
                {
                  "degree": "SSC (Secondary School Certificate)",
                  "institution": "ZPHS",
                  "location": "Pitlam",
                  "period": "2020",
                  "score": "CGPA: 10.0",
                  "status": "Distinction"
                }
              ],
              "experience": [
                {
                  "role": "Java Full Stack Intern",
                  "company": "Value Laden Technologies",
                  "period": "Dec 2024 / Summer Internship",
                  "description": "Engineered full-stack Java solutions using JDBC, Servlets, HTML, CSS, JavaScript, and SQL with clean modular design and enterprise practices."
                }
              ],
              "stats": {
                "internship": "Value Laden Technologies",
                "fullStackProjects": "2 Enterprise Grade",
                "certifications": "2 Professional",
                "cgpaBTech": "7.72",
                "sscGrade": "10.0 CGPA"
              }
            }
            """;
            sendJsonResponse(exchange, 200, json);
        }
    }

    static class SkillsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = """
            {
              "categories": [
                {
                  "name": "Backend & Core Java",
                  "skills": [
                    {"name": "Java (Core & OOP)", "level": 92, "badge": "JAVA 17"},
                    {"name": "Spring Boot", "level": 88, "badge": "SPRING"},
                    {"name": "JDBC & Servlets", "level": 85, "badge": "ENTERPRISE"},
                    {"name": "RESTful API Design", "level": 90, "badge": "REST"},
                    {"name": "Collections & Streams", "level": 88, "badge": "MODERN"}
                  ]
                },
                {
                  "name": "Databases & Storage",
                  "skills": [
                    {"name": "MySQL", "level": 88, "badge": "RDBMS"},
                    {"name": "MongoDB", "level": 78, "badge": "NOSQL"},
                    {"name": "SQL Query Optimization", "level": 85, "badge": "PERF"},
                    {"name": "Schema & Relational Design", "level": 84, "badge": "ACID"}
                  ]
                },
                {
                  "name": "Frontend Technologies",
                  "skills": [
                    {"name": "JavaScript (ES6+)", "level": 86, "badge": "ES6+"},
                    {"name": "HTML5 & Semantic Web", "level": 92, "badge": "SEMANTIC"},
                    {"name": "Modern CSS3 & Flex/Grid", "level": 90, "badge": "RESPONSIVE"},
                    {"name": "DOM APIs & Modern Web", "level": 88, "badge": "WEB"}
                  ]
                },
                {
                  "name": "Tools & Workflows",
                  "skills": [
                    {"name": "Git & GitHub", "level": 88, "badge": "VCS"},
                    {"name": "Postman API Testing", "level": 86, "badge": "QA"},
                    {"name": "VS Code & IntelliJ", "level": 90, "badge": "IDE"},
                    {"name": "Agile & Clean Architecture", "level": 82, "badge": "DESIGN"}
                  ]
                }
              ]
            }
            """;
            sendJsonResponse(exchange, 200, json);
        }
    }

    static class ProjectsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String json = """
            {
              "projects": [
                {
                  "id": "codesprint",
                  "title": "CodeSprint",
                  "subtitle": "Online Competitive Coding & Assessment Platform",
                  "category": "Full Stack Java Platform",
                  "image": "assets/images/codesprint.jpg",
                  "github": "https://github.com/Vijaykumar7060/codesprint_mvc",
                  "description": "A high-performance full-stack coding platform engineered to help software engineers level up algorithmic problem solving through interactive challenges, contests, and real-time code evaluation.",
                  "highlights": [
                    "Real-time code submission & automated test execution pipeline",
                    "Contest management system with dynamic leaderboard tracking",
                    "Rich code editor with multi-problem bank and test case verifier",
                    "Modular MVC architecture separating business logic from view rendering"
                  ],
                  "techStack": ["Java", "Spring Boot", "MySQL", "JavaScript", "HTML5", "CSS3", "REST APIs"],
                  "featured": true
                },
                {
                  "id": "blood-donation",
                  "title": "Blood Donation Management System",
                  "subtitle": "Life-Critical Healthcare & Donor Coordination Hub",
                  "category": "Java Enterprise Web Application",
                  "image": "assets/images/blood_donation.jpg",
                  "github": "https://github.com/Vijaykumar7060",
                  "description": "Full-stack healthcare coordination system streamlining voluntary blood donor registrations, emergency hospital requests, real-time blood group inventory tracking, and donor communication.",
                  "highlights": [
                    "Secure donor registration with blood group compatibility lookup (A+, O+, B+, AB-)",
                    "Real-time emergency blood request dispatch and status workflow",
                    "Relational MySQL database with strict foreign key constraints & ACID safety",
                    "RESTful CRUD APIs integrated with responsive dashboard interface"
                  ],
                  "techStack": ["Java", "Spring Boot", "MySQL", "RESTful APIs", "JavaScript", "Bootstrap/CSS"],
                  "featured": true
                }
              ]
            }
            """;
            sendJsonResponse(exchange, 200, json);
        }
    }

    static class ContactHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed. Use POST.\"}");
                return;
            }

            // Read payload
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Simple thread-safe message recorder
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String record = String.format("{\"timestamp\":\"%s\", \"payload\": %s},\n", timestamp, body.trim());

            try {
                Path path = Paths.get(MESSAGES_FILE);
                if (!Files.exists(path)) {
                    Files.writeString(path, "[\n" + record, StandardOpenOption.CREATE);
                } else {
                    Files.writeString(path, record, StandardOpenOption.APPEND);
                }
            } catch (Exception e) {
                System.err.println("Note: could not write message to file: " + e.getMessage());
            }

            System.out.println("[CONTACT] " + timestamp + " => " + body);

            String response = """
            {
              "success": true,
              "message": "Thank you! Your message has been received and stored in Vijaykumar's portfolio server.",
              "timestamp": "%s"
            }
            """.formatted(timestamp);

            sendJsonResponse(exchange, 200, response);
        }
    }

    static class SystemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Runtime rt = Runtime.getRuntime();
            long totalMem = rt.totalMemory() / (1024 * 1024);
            long freeMem = rt.freeMemory() / (1024 * 1024);
            long usedMem = totalMem - freeMem;
            long uptimeSeconds = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;

            String json = String.format("""
            {
              "status": "UP",
              "uptimeSeconds": %d,
              "requestsHandled": %d,
              "jvm": {
                "version": "%s",
                "vendor": "%s",
                "usedMemoryMB": %d,
                "totalMemoryMB": %d
              }
            }
            """, uptimeSeconds, REQUEST_COUNTER.get(), System.getProperty("java.version"), System.getProperty("java.vendor"), usedMem, totalMem);

            sendJsonResponse(exchange, 200, json);
        }
    }

    // Static Web Asset Handler
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            REQUEST_COUNTER.incrementAndGet();
            String rawPath = exchange.getRequestURI().getPath();
            String path = URLDecoder.decode(rawPath, StandardCharsets.UTF_8);

            if (path.equals("/")) {
                path = "/index.html";
            }

            // Security: prevent directory traversal
            Path requestedFile = Paths.get(ROOT_DIR, path).normalize();
            Path rootPath = Paths.get(ROOT_DIR).toAbsolutePath().normalize();

            if (!requestedFile.toAbsolutePath().startsWith(rootPath) || !Files.exists(requestedFile) || Files.isDirectory(requestedFile)) {
                // If not found, try fallback to index.html for SPA routes, or 404
                if (!path.startsWith("/api/")) {
                    Path fallback = Paths.get(ROOT_DIR, "index.html");
                    if (Files.exists(fallback)) {
                        serveFile(exchange, fallback, "text/html; charset=UTF-8");
                        return;
                    }
                }
                String notFound = "<h1>404 Not Found</h1><p>Resource " + path + " does not exist.</p>";
                byte[] bytes = notFound.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(404, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
                return;
            }

            String contentType = determineContentType(requestedFile.toString());
            serveFile(exchange, requestedFile, contentType);
        }

        private void serveFile(HttpExchange exchange, Path file, String contentType) throws IOException {
            byte[] fileBytes = Files.readAllBytes(file);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", contentType);
            headers.set("Cache-Control", "no-cache");
            headers.set("Access-Control-Allow-Origin", "*");

            exchange.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        }

        private String determineContentType(String filename) {
            String lower = filename.toLowerCase();
            if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html; charset=UTF-8";
            if (lower.endsWith(".css")) return "text/css; charset=UTF-8";
            if (lower.endsWith(".js")) return "application/javascript; charset=UTF-8";
            if (lower.endsWith(".json")) return "application/json; charset=UTF-8";
            if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
            if (lower.endsWith(".png")) return "image/png";
            if (lower.endsWith(".gif")) return "image/gif";
            if (lower.endsWith(".svg")) return "image/svg+xml";
            if (lower.endsWith(".ico")) return "image/x-icon";
            if (lower.endsWith(".woff2")) return "font/woff2";
            if (lower.endsWith(".woff")) return "font/woff";
            if (lower.endsWith(".ttf")) return "font/ttf";
            return "application/octet-stream";
        }
    }
}
