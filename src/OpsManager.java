package src;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.Date;

/**
 * OpsManager: Et profesjonelt verktøy for automatisert onboarding og feilsøking.
 * Verktøyet validerer lokal maskinkonfigurasjon og genererer tekniske guider.
 * * Inkluderer nå MBSE-støtte via XMI-eksport for integrasjon mot modelleringsverktøy
 * som Eclipse Papyrus og MagicDraw.
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
            // Genererer menneske-lesbar dokumentasjon
            generateOnboardingReport(user);
            
            // Genererer maskin-lesbar systemmodell (SysML/XMI)
            exportToXMI(user);
            
            logAction("SUCCESS: Onboarding rapport og XMI-modell generert for " + targetUser);
        } else {
            System.out.println("[ERROR] Bruker ikke funnet i databasen.");
            logAction("FAILED: Forsøk på onboarding av ukjent bruker: " + targetUser);
        }
        sc.close();
    }

    /**
     * Leser rådata fra CSV-database. 
     * Implementerer grunnleggende parsing av semikolon-separerte verdier.
     */
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

    /**
     * Produserer en personlig Markdown-guide.
     * Bruker system-diagnostikk for å gi sanntids feedback til brukeren.
     */
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

    /**
     * Eksporterer brukerdata til XMI (XML Metadata Interchange).
     * Dette muliggjør interoperabilitet med MBSE-verktøy og Eclipse Modeling Framework (EMF).
     */
    private static void exportToXMI(UserConfig user) {
        String fileName = "User_Model_" + user.navn.replace(" ", "_") + ".xmi";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<xmi:XMI xmi:version=\"2.1\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/3.0.0/UML\">");
            writer.println("  <uml:Package xmi:id=\"_" + System.currentTimeMillis() + "\" name=\"OnboardingModels\">");
            writer.println("    <packagedElement xmi:type=\"uml:Actor\" xmi:id=\"user_" + user.navn.hashCode() + "\" name=\"" + user.navn + "\">");
            writer.println("      <eAnnotations source=\"Metadata\">");
            writer.println("        <details key=\"Role\" value=\"" + user.rolle + "\"/>");
            writer.println("        <details key=\"Project\" value=\"" + user.prosjekt + "\"/>");
            writer.println("      </eAnnotations>");
            writer.println("    </packagedElement>");
            writer.println("  </uml:Package>");
            writer.println("</xmi:XMI>");
            
            System.out.println("[MBSE] XMI Modell-fil generert for SysML-import: " + fileName);
        } catch (IOException e) {
            System.err.println("[ERROR] Kunne ikke generere XMI-fil.");
        }
    }

    /**
     * Utfører runtime-eksekvering av systemkommandoer for å validere installert programvare.
     */
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

    /**
     * Verifiserer systemets miljøvariabler for å sikre korrekt verktøy-konfigurasjon.
     */
    private static void checkEnvVar(PrintWriter writer, String varName) {
        String value = System.getenv(varName);
        if (value != null) {
            writer.println("- ✅ " + varName + " er korrekt konfigurert.");
        } else {
            writer.println("- ❌ " + varName + " er **IKKE** satt.");
        }
    }

    /**
     * Logger administrative hendelser til en audit-fil for sporbarhet og sikkerhet.
     */
    private static void logAction(String action) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("audit.log", true)))) {
            out.println("[" + new Date() + "] " + action);
        } catch (IOException e) {
            System.err.println("[ERROR] Kunne ikke skrive til audit.log");
        }
    }
}