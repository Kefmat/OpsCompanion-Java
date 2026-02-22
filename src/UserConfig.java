package src;

/**
 * Representerer en ingeniør-profil og nødvendige systemtilganger.
 * Brukes for å mappe roller mot spesifikke lisensbehov og prosjekter.
 */
public class UserConfig {
    public String navn, rolle, prosjekt, lisenser;

    public UserConfig(String navn, String rolle, String prosjekt, String lisenser) {
        this.navn = navn;
        this.rolle = rolle;
        this.prosjekt = prosjekt;
        this.lisenser = lisenser;
    }
}