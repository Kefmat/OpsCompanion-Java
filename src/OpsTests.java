package src;

/**
 * Enkel testklasse som verifiserer kjernefunksjonalitet uten 3. parts rammeverk.
 * Kjører fra en statisk main-metode og kaster AssertionError ved feil.
 */
public class OpsTests {
    public static void main(String[] args) {
        testUserCreation();
        testCsvLogic();
        System.out.println("[TEST] Alle tester bestått.");
    }

    private static void testUserCreation() {
        UserConfig user = new UserConfig("Ola Nordmann", "Ingeniør", "K-Plattform", "Jira");
        if (!"Ola Nordmann".equals(user.navn)) {
            throw new AssertionError("navn matcher ikke");
        }
        if (!"Ingeniør".equals(user.rolle)) {
            throw new AssertionError("rolle matcher ikke");
        }
    }

    private static void testCsvLogic() {
        // Her tester vi at UserConfig objekter kan bære data korrekt
        UserConfig user = new UserConfig("Navn", "Rolle", "Prosjekt", "Lisens");
        if (user.prosjekt == null) {
            throw new AssertionError("prosjekt bør ikke være null");
        }
    }
}