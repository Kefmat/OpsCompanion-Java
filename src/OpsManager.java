package src;

import java.io.*;
import java.util.*;
import java.util.Date;

/**
 * OpsManager: Et profesjonelt verktøy for automatisert onboarding og feilsøking.
 * Verktøyet validerer lokal maskinkonfigurasjon og genererer tekniske guider.
 */
public class OpsManager {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n================================================");
        System.out.println(">>> OPSCOMPANION: ENGINEERING ONBOARDING SYSTEM");
        System.out.println("================================================");
        
        System.out.print("Vennligst oppgi navn på ansatt: ");
        String targetUser = sc.nextLine();

        UserConfig user = findUserInCsv(targetUser);

        if (user != null) {
            generateOnboardingReport(user);
            logAction("SUCCESS: Onboarding rapport generert for " + targetUser);
        } else {
            System.out.println("[ERROR] Bruker ikke funnet i databasen.");
            logAction("FAILED: Forsøk på onboarding av ukjent bruker: " + targetUser);
        }
        sc.close();
    }

    private static UserConfig findUserInCsv(String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            br.readLine(); // Hopper over overskriftsrad
            while ((line = br.readLine()) != null) {
                String[] v = line.split(";");
                if (v[0].equalsIgnoreCase(name)) {
                    return new UserConfig(v[0], v[1], v[2], v[3]);
                }
            }
        } catch (IOException e) {
            System.err.println("[CRITICAL] Kunne ikke lese users.csv");
        }
        return null;
    }

    private static void generateOnboardingReport(UserConfig user) {
        String fileName = "Setup_Guide_" + user.navn.replace(" ", "_") + ".md";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("# 🛡️ Onboarding Guide: " + user.navn);
            writer.println("**Rolle:** " + user.rolle + " | **Prosjekt:** " + user.prosjekt);
            writer.println("\n---");
            
            writer.println("\n## 🛠️ Systemdiagnose");
            writer.println("Automatisk sjekk av lokal arbeidsstasjon:");
            checkTool(writer, "java -version", "Java JDK");
            checkTool(writer, "node -v", "Node.js");
            checkTool(writer, "git --version", "Git VCS");
            
            writer.println("\n## ⚙️ Miljøvariabler");
            checkEnvVar(writer, "JAVA_HOME");

            writer.println("\n## 🔑 Lisens-oversikt");
            writer.println("Følgende tilganger er reservert for din profil:");
            for (String l : user.lisenser.split(",")) {
                writer.println("- [ ] **" + l.trim() + "** (Aktiveres via bedriftens lisensserver)");
            }

            writer.println("\n## 📘 Merknader");
            writer.println("Denne guiden er autogenerert for å sikre etterlevelse av tekniske standarder.");
            writer.println("Dersom noen av sjekkene viser (❌), vennligst kontakt IT-support.");

            System.out.println("\n[SUCCESS] Dokumentasjon generert: " + fileName);
        } catch (IOException e) {
            System.err.println("[ERROR] Feil under generering av dokumentasjon.");
        }
    }

    private static void checkTool(PrintWriter writer, String cmd, String toolName) {
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            if (p.waitFor() == 0) {
                writer.println("- ✅ " + toolName + ": Operativ");
            } else {
                writer.println("- ❌ " + toolName + ": Installert, men returnerte feilkode");
            }
        } catch (Exception e) {
            writer.println("- ❌ " + toolName + ": Ikke funnet i systemets PATH");
        }
    }

    private static void checkEnvVar(PrintWriter writer, String varName) {
        String value = System.getenv(varName);
        if (value != null) {
            writer.println("- ✅ " + varName + " er korrekt konfigurert.");
        } else {
            writer.println("- ❌ " + varName + " er **IKKE** satt.");
        }
    }

    private static void logAction(String action) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("audit.log", true)))) {
            out.println("[" + new Date() + "] " + action);
        } catch (IOException e) {
            System.err.println("[ERROR] Kunne ikke skrive til audit.log");
        }
    }
}