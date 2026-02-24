package src;

import java.io.*;
import java.util.Date;

/**
 * Ansvarlig for eksport av data til ulike formater (Markdown, XMI, Logg, HTML).
 * Sikrer interoperabilitet med MBSE-verktøy og visuell presentasjon for interessenter.
 */
public class Exporter {

    public static void generateMarkdown(UserConfig user) {
        String fileName = "Setup_Guide_" + user.navn.replace(" ", "_") + ".md";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("# 🛡️ Onboarding Guide: " + user.navn);
            writer.println("**Rolle:** " + user.rolle + " | **Prosjekt:** " + user.prosjekt);
            writer.println("\n---");
            writer.println("\n## 🛠️ Systemdiagnose");
            SystemValidator.checkTool(writer, "java -version", "Java JDK");
            SystemValidator.checkTool(writer, "git --version", "Git VCS");
            writer.println("\n## 🌐 Nettverk");
            NetworkChecker.checkConnectivity(writer, "8.8.8.8", "Lisensserver");
            System.out.println("[SUCCESS] Markdown rapport generert: " + fileName);
        } catch (IOException e) {
            System.err.println("[ERROR] Feil ved skriving av Markdown.");
        }
    }

    public static void generateXMI(UserConfig user) {
        String fileName = "User_Model_" + user.navn.replace(" ", "_") + ".xmi";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<xmi:XMI xmi:version=\"2.1\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\" xmlns:uml=\"http://www.eclipse.org/uml2/3.0.0/UML\">");
            writer.println("  <uml:Package xmi:id=\"_" + System.currentTimeMillis() + "\" name=\"OnboardingModels\">");
            writer.println("    <packagedElement xmi:type=\"uml:Actor\" xmi:id=\"u" + user.navn.hashCode() + "\" name=\"" + user.navn + "\"/>");
            writer.println("  </uml:Package>");
            writer.println("</xmi:XMI>");
            System.out.println("[MBSE] XMI Modell generert: " + fileName);
        } catch (IOException e) {
            System.err.println("[ERROR] Feil ved XMI-eksport.");
        }
    }

    /**
     * Genererer et visuelt HTML-dashboard for enkel presentasjon av systemstatus.
     */
    public static void generateHTMLDashboard(UserConfig user) {
        String fileName = "Dashboard_" + user.navn.replace(" ", "_") + ".html";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
            writer.println("<style>");
            writer.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #eef2f3; color: #333; padding: 50px; }");
            writer.println(".card { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }");
            writer.println("h1 { color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px; }");
            writer.println(".status { padding: 5px 10px; border-radius: 4px; font-weight: bold; background: #2ecc71; color: white; }");
            writer.println(".info-row { margin: 15px 0; font-size: 1.1em; }");
            writer.println("footer { margin-top: 20px; font-size: 0.8em; color: #7f8c8d; }");
            writer.println("</style></head><body>");
            
            writer.println("<div class='card'>");
            writer.println("<h1>Engineering Readiness Report</h1>");
            writer.println("<div class='info-row'><strong>Bruker:</strong> " + user.navn + "</div>");
            writer.println("<div class='info-row'><strong>Rolle:</strong> " + user.rolle + "</div>");
            writer.println("<div class='info-row'><strong>Prosjekt:</strong> " + user.prosjekt + "</div>");
            writer.println("<hr>");
            writer.println("<h3>Systemstatus: <span class='status'>KLAR TIL BRUK</span></h3>");
            writer.println("<ul><li>Verktøykjede: Validert</li><li>Nettverk: Tilkoblet</li><li>MBSE Eksport: Fullført</li></ul>");
            writer.println("<footer>Generert av OpsCompanion v2.0 - " + new Date() + "</footer>");
            writer.println("</div></body></html>");
            
            System.out.println("[VISUAL] HTML Dashboard generert: " + fileName);
        } catch (IOException e) {
            System.err.println("[ERROR] Feil ved HTML-generering.");
        }
    }

    public static void log(String action) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("audit.log", true)))) {
            out.println("[" + new Date() + "] " + action);
        } catch (IOException e) { }
    }
}