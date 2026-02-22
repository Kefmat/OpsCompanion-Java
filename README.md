# OpsCompanion: System Administration & Onboarding Automator

**OpsCompanion** er et Java-verktøy designet for å strømlinjeforme oppstartsprosessen for ingeniører i komplekse prosjektmiljøer. Verktøyet automatiserer teknisk feilsøking og genererer prosjektspesifikk dokumentasjon.

## 🌟 Hovedfunksjoner
- **Automatisk Systemdiagnose:** Sjekker om kritiske verktøy som Java, Node.js og Git er operative via system-calls.
- **Miljøkontroll:** Verifiserer miljøvariabler (f.eks. `JAVA_HOME`) for å redusere manuelle konfigurasjonsfeil.
- **Dynamisk Onboarding:** Genererer skreddersydde Markdown-guider basert på brukerens rolle og prosjekt (hentet fra CSV).
- **Audit Logging:** Logger alle administrative handlinger til `audit.log` for full sporbarhet og sikkerhet.

## 🛠️ Teknisk Arkitektur
Verktøyet er bygget i Java for å demonstrere kompetanse innen:
- **Runtime Execution:** Interaksjon med operativsystemet.
- **I/O Stream Handling:** Avansert lesing og skriving av filer (CSV/Markdown).
- **Feilhåndtering:** Robust logikk for å håndtere manglende verktøy eller korrupte data.

## 🚀 Kom i gang
1. Sørg for at `users.csv` er oppdatert med ansattdata.
2. Kompiler:
   ```bash
   javac src/*.java