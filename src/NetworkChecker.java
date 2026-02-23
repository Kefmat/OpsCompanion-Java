package src;

import java.io.PrintWriter;
import java.net.InetAddress;

/**
 * Connectivity Suite for verifisering av kritiske ingeniør-ressurser.
 * Simulerer sjekk av interne lisensservere eller database-noder.
 */
public class NetworkChecker {

    /**
     * Validerer nettverkstilkobling mot spesifikke hoster/IP-adresser via ICMP/Ping.
     */
    public static void checkConnectivity(PrintWriter writer, String host, String description) {
        try {
            boolean reachable = InetAddress.getByName(host).isReachable(3000);
            if (reachable) {
                writer.println("- ✅ " + description + " (" + host + "): Tilkoblet");
            } else {
                writer.println("- ❌ " + description + " (" + host + "): Ingen kontakt (Timeout)");
            }
        } catch (Exception e) {
            writer.println("- ❌ " + description + " (" + host + "): Ugyldig adresse eller DNS-feil");
        }
    }
}