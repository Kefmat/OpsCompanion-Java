package src;

import java.io.*;
import java.util.Scanner;

/**
 * OpsManager: Hovedorkestrator for onboarding-systemet.
 * Koordinerer brukeridentifisering, systemvalidering og eksportprosesser.
 * Inkluderer nå en interaktiv loop for bedre brukeropplevelse under demo.
 */
public class OpsManager {

    // ANSI fargekoder for profesjonell terminal-output
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println(BLUE + "\n================================================");
        System.out.println(">>> OPSCOMPANION: MODULAR ENGINEERING SYSTEM");
        System.out.println("================================================" + RESET);
        
        UserConfig user = null;
        
        // Interaktiv loop for å håndtere brukersøk
        while (user == null) {
            System.out.print("\nVennligst oppgi navn på ansatt (eller 'exit' for å avslutte): ");
            String targetUser = sc.nextLine();

            // Mulighet for å avbryte programmet
            if (targetUser.equalsIgnoreCase("exit")) {
                System.out.println(YELLOW + "Avbryter onboarding..." + RESET);
                break;
            }

            user = findUserInCsv(targetUser);

            if (user != null) {
                System.out.println(GREEN + "[OK] Bruker funnet: " + user.navn + RESET);
                System.out.println(BLUE + "Starter generering av systemmodeller og dokumentasjon..." + RESET);
                
                // Delegerer oppgaver til spesialiserte moduler
                Exporter.generateMarkdown(user);
                Exporter.generateXMI(user);
                Exporter.generateHTMLDashboard(user); // Genererer den visuelle rapporten
                
                Exporter.log("SUCCESS: Onboarding fullført for " + user.navn);
                System.out.println(GREEN + "\n>>> Prossess fullført. Alle filer er generert i rotmappen." + RESET);
            } else {
                System.out.println(RED + "[ERROR] '" + targetUser + "' ble ikke funnet i databasen. Vennligst sjekk skrivemåte." + RESET);
                Exporter.log("FAILED: Ukjent bruker forsøkt onboardet: " + targetUser);
                // Loopen fortsetter siden 'user' fortsatt er null
            }
        }
        
        System.out.println(BLUE + "Takk for at du bruker OpsCompanion. Programmet avsluttes.\n" + RESET);
        sc.close();
    }

    /**
     * Leser rådata fra CSV-database. 
     * Implementerer feilhåndtering for filaksess.
     */
    private static UserConfig findUserInCsv(String name) {
        // Vi antar at 'users.csv' ligger i rotmappen til prosjektet
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            br.readLine(); // Skipper header-rad (Navn;Rolle;Prosjekt;Lisenser)
            while ((line = br.readLine()) != null) {
                String[] v = line.split(";");
                // Sjekker om navnet matcher (case-insensitive)
                if (v[0].equalsIgnoreCase(name)) {
                    return new UserConfig(v[0], v[1], v[2], v[3]);
                }
            }
        } catch (IOException e) {
            System.err.println(RED + "[CRITICAL] Kunne ikke lese users.csv. Sjekk at filen eksisterer." + RESET);
        }
        return null;
    }
}