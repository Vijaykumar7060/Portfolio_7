# ⚡ Vijaykumar Karegama - Java Full Stack Developer Portfolio

> **Architecting Resilient Enterprise Backends & Interactive Web Applications**

An ultra-modern, high-performance portfolio website built specifically for **Vijaykumar Karegama**, featuring a zero-dependency **Java 17 backend server (`PortfolioServer.java`)**, built-in **REST API endpoints**, live interactive developer tools (an embedded Java terminal and CodeSprint sandbox runner), and a cyber-dark glassmorphism design.

---

## 🌟 Key Features

1. **Dual-Mode Operation**:
   - **Standalone Java 17 Application**: Run locally using the included `PortfolioServer.java` with built-in REST API endpoints and zero external dependencies.
   - **Static Web / Cloud Ready**: Fully deployable to GitHub Pages, Vercel, or Netlify with seamless offline fallbacks.
2. **Interactive Developer Terminal ("VijayShell v2.4")**:
   - An interactive command-line environment supporting commands like `help`, `skills`, `projects`, `run codesprint`, `run blooddonation`, `curl stats`, and `sudo hire`.
3. **Interactive CodeSprint Sandbox**:
   - Embedded Java code runner demonstrating algorithmic solutions (*TwoSum*, *MaxSubarray*, *ReverseString*) with live automated test-case evaluation.
4. **Blood Donation Compatibility Explorer**:
   - Interactive medical blood type matching widget showing donor compatibility, inventory tracking, and simulated REST endpoints.
5. **Interactive Resume & Print View**:
   - Clean printable executive CV formatted with `@media print` styling for one-click PDF generation.
6. **Web Audio API Feedback Engine**:
   - Lightweight synthesized micro-audio clicks and chimes for user interactions without needing heavy audio files.
7. **REST API Endpoints**:
   - `GET  /api/profile` : Full profile and academic records.
   - `GET  /api/skills`  : Categorized skills with mastery metrics.
   - `GET  /api/projects`: Project metadata and architectural highlights.
   - `POST /api/contact` : Validates and records contact submissions to `contacts.json`.
   - `GET  /api/system`  : Live JVM memory, active threads, and server uptime telemetry.

---

## 🚀 Quick Start Guide

### Option 1: Run with Java 17 (Recommended)

1. Open PowerShell or Command Prompt in this folder:
   ```bash
   javac PortfolioServer.java
   java PortfolioServer
   ```
2. Or simply double-click:
   - `run.bat` (Windows Command Line)
   - Or run `.\run.ps1` in PowerShell
3. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

### Option 2: Run via Any Static Web Server or Direct Browser

You can also double click `index.html` or use Python / Node:
```bash
npx serve .
# OR
python -m http.server 8080
```

---

## 📁 Project Structure

```
Portfolio/
├── PortfolioServer.java       # Production-ready Java 17 HTTP & REST server
├── index.html                 # Semantic, SEO-optimized portfolio frontend
├── run.bat                    # 1-Click Windows execution script
├── run.ps1                    # PowerShell launch script
├── README.md                  # Detailed documentation & portfolio guide
├── css/
│   └── style.css              # Cyber-dark glassmorphism design system
├── js/
│   └── main.js                # Particle canvas, audio synthesizer, terminal, & API client
└── assets/
    └── images/
        ├── vijaykumar.jpg     # Professional portrait of Vijaykumar Karegama
        ├── codesprint.jpg     # High-resolution CodeSprint platform preview
        └── blood_donation.jpg # Blood Donation Management System UI preview
```

---

## 👤 Profile Details

- **Full Name**: Vijaykumar Karegama
- **Role**: Java Full Stack Developer
- **Education**: B-Tech in Computer Science & Engineering, Narsimha Reddy Engineering College, Hyderabad (2023–2027) | **CGPA: 7.72**
- **Internship**: Java Full Stack Intern at Value Laden Technologies (Dec 2024 / Summer)
- **Projects**:
  - [CodeSprint](https://github.com/Vijaykumar7060/codesprint_mvc) (Online Competitive Coding Platform)
  - Blood Donation Management System (Java Full Stack Healthcare Hub)
- **Certificates**:
  - NamasteDev JavaScript Certification
  - EdiGlobe Full Stack Web Development Certification
- **Email**: `karegamavijaykumar@gmail.com`
- **Phone**: `+91 9182319464`
- **LinkedIn**: `linkedin.com/in/karegamavijaykumar`
- **GitHub**: `github.com/Vijaykumar7060`
