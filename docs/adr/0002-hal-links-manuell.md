# ADR 0002: HAL-Links manuell mit HalEntityWrapper statt deklarativ mit @RestLink
 
## Status
Akzeptiert, 25.11.2026
 
## Kontext
Die Anwendung soll Level 3 des Richardson Maturity Model erreichen (HATEOAS).
Die Aufgabenstellung verlangt:
- Links auf Beziehungen, z. B. Team → Players, Team → Manager
- mindestens einen zustandsabhängigen Link: „Spielerpass registrieren“ wird nur angeboten,
  solange dem Player noch keine Spieler-ID durch das Modul Passwesen zugewiesen wurde.
Die Links werden im HAL-Format im Response-Body übertragen (Extensions `quarkus-rest-links`
und `quarkus-hal`). Quarkus bietet zwei Wege, die Links zu erzeugen.
 
## Untersuchte Optionen
 
### Option 1: deklarativ mit `@RestLink` und `@InjectRestLinks`
- Zielmethoden werden mit `@RestLink(rel = "...", entityType = ...)` als Link-Ziel für einen Typ angemeldet.
- `@InjectRestLinks` an der Methode hängt alle für den Typ registrierten Links an die Antwort.
- Sehr wenig Code, so im Vorlesungsbeispiel umgesetzt.
### Option 2: manuell mit `HalEntityWrapper` und `HalCollectionWrapper`
- Die Resource verpackt das DTO in einen `HalEntityWrapper` und fügt die Links selbst hinzu
  (`jakarta.ws.rs.core.Link`).
- Bedingungen auf den Zustand sind mit normalem Java-Code (`if`) möglich.
- Mehr Code, die Link-Logik wird in Hilfsklassen gebündelt.
### Vergleich
 
| Kriterium                                 | deklarativ (`@RestLink`) | manuell (`HalEntityWrapper`) |
|-------------------------------------------|--------------------------|------------------------------|
| Codeumfang                                | gering                   | höher                        |
| Links `self` / `list`                     | automatisch              | von Hand                     |
| Links auf andere Ressourcen (Team → Players) | nur über passenden `entityType` | frei wählbar          |
| zustandsabhängige Links                   | nicht möglich            | möglich                      |
| Erfüllt die Aufgabenstellung              | nein                     | ja                           |
 
## Untersuchung
Beide Varianten wurden mit denselben Daten (eine Person ohne Spielerpass, eine mit
Spielerpass) ausprobiert und die Antworten mit `Accept: application/hal+json` verglichen.

Bei der deklarativen Variante hängt Quarkus jeden für den Typ registrierten Link an jede
Instanz. Eine Bedingung auf den Zustand des Objekts lässt sich dort nicht ausdrücken, der
Link `spielerpass-registrieren` erscheint also auch bei Spielern, die bereits einen Pass
haben. Die geforderte zustandsabhängige Verlinkung ist nur mit der manuellen Variante
umsetzbar.
 
## Entscheidung
Wir erzeugen die HAL-Links manuell mit `HalEntityWrapper` (Einzelobjekte) und
`HalCollectionWrapper` (Listen).
 
Die Link-Logik wird pro Ressource in einer Hilfsklasse in `boundary.rest.links` gebündelt
(`PlayerLinks`, `TeamLinks`, `ManagerLinks`). Alle Resource-Methoden, die ein Objekt
zurückgeben, nutzen diese Hilfsklassen, damit dieselbe Ressource überall dieselben Links hat.
 
Umgesetzte Links:
 
| Ressource | Links                                                                         |
|-----------|-------------------------------------------------------------------------------|
| Player    | `self`; `spielerpass-registrieren` nur, wenn `spielerId == null`              |
| Team      | `self`, `players`; `manager` nur, wenn ein Manager zugeordnet ist             |
| Manager   | `self`                                                                        |
| Listen    | `self` der Liste, jeder Eintrag mit seinen eigenen Links in `_embedded`       |
 
## Konsequenzen
 
Positiv:
- Die Pflichtanforderung (zustandsabhängiger Link) ist erfüllt und durch Tests belegt.
- Volle Kontrolle über Namen und Ziele der Links, auch zu anderen Ressourcen.
- Die Geschäftsregel „Registrierung nur ohne Spieler-ID“ bleibt auf dem Server;
  der Client muss nur prüfen, ob der Link vorhanden ist.
- Die Link-Logik liegt pro Ressource an einer Stelle.

Negativ:
- Mehr Code als mit Annotationen.
- Die Pfade stehen als Strings in den Hilfsklassen und müssen bei Änderungen der
  `@Path`-Angaben mitgepflegt werden.
- HAL liefert nur die URL eines Links, nicht die HTTP-Methode. Der Client muss wissen,
  dass `spielerpass-registrieren` per POST aufgerufen wird. Level 3 wird damit
  „in Ansätzen“ erreicht, wie in der Aufgabenstellung gefordert.
 