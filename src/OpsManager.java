package src;

import java.io.*;
import java.util.Scanner;

/**
 * OpsManager: Hovedorkestrator for onboarding-systemet.
 * Koordinerer brukeridentifisering, systemvalidering og eksportprosesser.
 */
public class OpsManager {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n================================================");
        System.out.println(">>> OPSCOMPANION: MODULAR ENGINEERING SYSTEM");
        System.out.println("================================================");
        
        System.out.print("Vennligst oppgi navn på ansatt: ");
        String targetUser = sc.nextLine();

        UserConfig user = findUserInCsv(targetUser);

        if (user != null) {
            // Delegerer oppgaver til spesialiserte moduler
            Exporter.generateMarkdown(user);
            Exporter.generateXMI(user);
            Exporter.log("SUCCESS: Onboarding fullført for " + user.navn);
        } else {
            System.out.println("[ERROR] Bruker ikke funnet i databasen.");
            Exporter.log("FAILED: Ukjent bruker forsøkt onboardet: " + targetUser);
        }
        sc.close();
    }

    /**
     * Leser rådata fra CSV-database. 
     */
    private static UserConfig findUserInCsv(String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            br.readLine(); // Skipper header
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
}