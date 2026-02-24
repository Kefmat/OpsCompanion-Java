package src;

/**
 * Enkel testklasse som verifiserer kjernefunksjonalitet uten 3. parts rammeverk.
 * Dette fjerner feilmeldingene i VS Code siden vi ikke trenger JUnit-biblioteket.
 */
public class OpsTests {
    public static void main(String[] args) {
        System.out.println(">>> Starter enhetstester...");
        testUserCreation();
        testCsvLogic();
        System.out.println("[TEST] Alle tester bestått.");
    }

    private static void testUserCreation() {
        UserConfig user = new UserConfig("Ola Nordmann", "Ingeniør", "K-Plattform", "Jira");
        if (!"Ola Nordmann".equals(user.navn)) throw new AssertionError("Navn matcher ikke");
        if (!"Ingeniør".equals(user.rolle)) throw new AssertionError("Rolle matcher ikke");
    }

    private static void testCsvLogic() {
        UserConfig user = new UserConfig("Navn", "Rolle", "Prosjekt", "Lisens");
        if (user.prosjekt == null) throw new AssertionError("Prosjekt bør ikke være null");
    }
}