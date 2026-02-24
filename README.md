# OpsCompanion: Modular System Administration & MBSE Integration

<img width="659" height="437" alt="1f4dc0542015970ee05b55596209479b" src="https://github.com/user-attachments/assets/8d51e7e8-51f4-42c0-9ad8-cccff275d75b" />

## Project Overview
OpsCompanion is a professional Java-based utility designed to streamline technical onboarding and system validation. The project has been refactored into a modular architecture to demonstrate high-quality software engineering principles such as **Separation of Concerns (SoC)** and **Single Responsibility**.

<img width="636" height="268" alt="e2532272471f6bef250a2af31c7c6bfd" src="https://github.com/user-attachments/assets/fdbb600b-3cf3-462f-b725-2cd593f3da93" />

## System Architecture
The application is divided into specialized modules, ensuring the system is scalable and easy to maintain.

```mermaid
graph TD
    Main[OpsManager] --> Auth[CSV Database]
    Main --> Val[SystemValidator]
    Main --> Net[NetworkChecker]
    Main --> Exp[Exporter]
    
    Val -->|OS Checks| MD[Markdown Report]
    Net -->|Connectivity| MD
    Exp -->|IO Operations| MD
    Exp -->|SysML Standards| XMI[XMI Model Export]
    Exp -->|Security| Log[Audit Log]
```

### Modular Structure

- **OpsManager (Orchestrator):** The main entry point that coordinates the workflow between modules.

- **SystemValidator:** Executes runtime diagnostics of the local environment (JDK, Git, Environment Variables).

- **NetworkChecker:** A connectivity suite that verifies access to critical infrastructure and license servers.

- **Exporter:** Handles multi-format data generation, including human-readable documentation and machine-readable models.

- **UserConfig:** The data model representing engineer profiles and project-specific requirements.


### Technical Features

- **MBSE Interoperability:** Generates XMI (XML Metadata Interchange) files compatible with UML/SysML tools like Eclipse Papyrus.

- **Infrastructure Verification:** Real-time ICMP/Ping diagnostics to ensure developer workstations can reach required servers.

- **Automated Documentation:** Generates personalized Markdown guides based on role-specific data from a CSV backend.

- **Traceability:** Implements a persistent audit log for administrative compliance and security tracking.


### Process Flow
The application follows a structured sequence to ensure data integrity:

```mermaid
sequenceDiagram
    participant Admin
    participant Main as OpsManager
    participant OS as Operating System
    participant Net as Network
    participant FS as File System

    Admin->>Main: Provide Employee Name
    Main->>FS: Query users.csv
    Main->>OS: Validate Toolchain (Java/Git)
    Main->>Net: Verify License Server Access
    Main->>FS: Generate .md & .xmi files
    Main-->>Admin: Onboarding Successful
```

## Requirements and Execution

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- System `PATH` access for target tools (Git, Node.js)

### Compilation

From the project root, execute the following command:

```bash
javac src/*.java
```

### Execution

Run the application using the classpath flag:

```bash
java -cp . src.OpsManager
```

## DevOps & Systems Engineering Context

This tool addresses the operational requirements of maintaining large-scale engineering platforms. By automating the verification of the local development environment, it reduces the mean time to onboard (MTTO) and ensures that all engineers operate on a standardized toolset.
