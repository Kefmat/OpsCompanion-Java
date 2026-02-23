package src;

import java.io.PrintWriter;

/**
 * Håndterer validering av det lokale systemmiljøet.
 * Utfører runtime-eksekvering av systemkommandoer og sjekker miljøvariabler.
 */
public class SystemValidator {

    /**
     * Utfører runtime-eksekvering av systemkommandoer for å validere installert programvare.
     */
    public static void checkTool(PrintWriter writer, String cmd, String toolName) {
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
    public static void checkEnvVar(PrintWriter writer, String varName) {
        String value = System.getenv(varName);
        if (value != null) {
            writer.println("- ✅ " + varName + " er korrekt konfigurert.");
        } else {
            writer.println("- ❌ " + varName + " er **IKKE** satt.");
        }
    }
}