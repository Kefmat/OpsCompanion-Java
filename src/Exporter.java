package src;

import java.io.*;
import java.util.Date;

/**
 * Ansvarlig for eksport av data til ulike formater (Markdown, XMI, Logg).
 * Sikrer interoperabilitet med MBSE-verktøy og administrativ sporbarhet.
 */
public class Exporter {

    /**
     * Produserer en personlig Markdown-guide med systemdiagnose og nettverksstatus.
     */
    public static void generateMarkdown(UserConfig user) {
        String fileName = "Setup_Guide_" + user.navn.replace(" ", "_") + ".md";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("# 🛡️ Onboarding Guide: " + user.navn);
            writer.println("**Rolle:** " + user.rolle + " | **Prosjekt:** " + user.prosjekt);
            writer.println("\n---");
            
            writer.println("\n## 🛠️ Systemdiagnose");
            SystemValidator.checkTool(writer, "java -version", "Java JDK");
            SystemValidator.checkTool(writer, "git --version", "Git VCS");
            
            writer.println("\n## ⚙️ Miljøvariabler");
            SystemValidator.checkEnvVar(writer, "JAVA_HOME");

            writer.println("\n## 🌐 Nettverkstilkobling");
            NetworkChecker.checkConnectivity(writer, "8.8.8.8", "Lisensserver (Primær)");
            
            writer.println("\n## 🔑 Lisens-oversikt");
            for (String l : user.lisenser.split(",")) {
                writer.println("- [ ] **" + l.trim() + "** (Aktiveres via lisensserver)");
            }

            writer.println("\n## 📘 Merknader");
            writer.println("Denne guiden er autogenerert for å sikre etterlevelse av tekniske standarder.");

            System.out.println("\n[SUCCESS] Dokumentasjon generert: " + fileName);
        } catch (IOException e) {
            System.err.println("[ERROR] Feil under generering av dokumentasjon.");
        }
    }

    /**
     * Eksporterer brukerdata til XMI (XML Metadata Interchange) for SysML-import.
     * Muliggjør integrasjon mot verktøy som Eclipse Papyrus eller MagicDraw.
     */
    public static void generateXMI(UserConfig user) {
        String fileName = "User_Model_" + user.navn.replace(" ", "_") + ".xmi";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<xmi:XMI xmi:version=\"2.1\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/3.0.0/UML\">");
            writer.println("  <uml:Package xmi:id=\"_" + System.currentTimeMillis() + "\" name=\"OnboardingModels\">");
            writer.println("    <packagedElement xmi:type=\"uml:Actor\" xmi:id=\"user_" + user.navn.hashCode() + "\" name=\"" + user.navn + "\">");
            writer.println("      <eAnnotations source=\"Metadata\">");
            writer.println("        <details key=\"Role\" value=\"" + user.rolle + "\"/>");
            writer.println("      </eAnnotations>");
            writer.println("    </packagedElement>");
            writer.println("  </uml:Package>");
            writer.println("</xmi:XMI>");
            System.out.println("[MBSE] XMI Modell-fil generert: " + fileName);
        } catch (IOException e) {
            System.err.println("[ERROR] Kunne ikke generere XMI-fil.");
        }
    }

    /**
     * Logger administrative hendelser til en audit-fil for sporbarhet og sikkerhet.
     */
    public static void log(String action) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("audit.log", true)))) {
            out.println("[" + new Date() + "] " + action);
        } catch (IOException e) {
            System.err.println("[ERROR] Kunne ikke skrive til audit.log");
        }
    }
}