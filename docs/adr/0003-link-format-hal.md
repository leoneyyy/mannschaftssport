# ADR 0003: Links im HAL-Format im Response-Body statt im Link-Header

## Status
Akzeptiert, 25.11.2026

## Kontext
Die Anwendung soll Level 3 des Richardson Maturity Model erreichen. Dazu werden dem Client
Links auf Beziehungen (Team → Players, Team → Manager) und mindestens ein zustandsabhängiger
Link („Spielerpass registrieren“ nur ohne Spieler-ID) angeboten.

Die Aufgabenstellung lässt zwei Wege offen:
- a) Links im HTTP-Header (Web Linking nach RFC 8288)
- b) Links im Response-Body, z. B. im HAL-Format

Rahmenbedingungen:
- Standards sind wann immer möglich zu verwenden.
- JSON-Binding erfolgt mit JSON-B (siehe ADR 0001).
- Die API liefert sowohl Einzelobjekte als auch Listen (z. B. `GET /players`, `GET /teams/{id}/players`).

Diese Entscheidung legt das **Format** der Links fest. Wie die Links im Code erzeugt werden,
regelt ADR 0002.

## Untersuchte Optionen

### Option 1: Link-Header nach RFC 8288
```
Link: <http://localhost:8080/players/1>; rel="self",
      <http://localhost:8080/players/1/spielerpass>; rel="spielerpass-registrieren"
```
- IETF-Standard, direkt mit Jakarta REST umsetzbar (`jakarta.ws.rs.core.Link`, `Response.links(...)`),
  keine zusätzliche Extension nötig.
- Der Body bleibt unverändert.
- Links gelten für die gesamte Antwort. Bei Listen kann nicht pro Eintrag ein eigener Link
  angegeben werden.
- Links sind nur in den HTTP-Headern sichtbar, nicht im JSON.

### Option 2: HAL im Response-Body
```json
{
  "id": 1, "vorname": "Max", "spielerId": null,
  "_links": {
    "self": { "href": "http://localhost:8080/players/1" },
    "spielerpass-registrieren": { "href": "http://localhost:8080/players/1/spielerpass" }
  }
}
```
- Verbreitetes Format (Media-Type `application/hal+json`), von der Aufgabenstellung genannt.
- Unterstützt durch die Quarkus-Extension `quarkus-hal`.
- Bei Listen stehen die Einträge in `_embedded`, jeder mit eigenen `_links`.
- Kein Jakarta-Standard, sondern eine Quarkus-Extension.

### Option 3: eigenes Link-Format im DTO
```json
{ "id": 1, "links": [ { "rel": "self", "href": "..." } ] }
```
- Einfach selbst zu bauen, keine Extension nötig.
- Kein Standard: Jeder Client muss das Format eigens kennen.

### Option 4: JSON:API (Format des jax-Artikels)
- Standardisiertes Format mit `data`, `links`, `relationships` und `included`.
- Keine Unterstützung durch Quarkus, das gesamte Format müsste selbst gebaut werden.
- Laut Aufgabenstellung nicht zwingend umzusetzen.

### Vergleich

| Kriterium                              | Link-Header | HAL      | eigenes Format | JSON:API |
|----------------------------------------|-------------|----------|----------------|----------|
| Standard                               | RFC 8288    | verbreitetes Format | nein  | ja       |
| Unterstützung in Quarkus               | Jakarta REST | Extension | –             | keine    |
| Links pro Eintrag in Listen            | nein        | ja       | ja             | ja       |
| zustandsabhängige Links                | ja          | ja       | ja             | ja       |
| Links im JSON sichtbar                 | nein        | ja       | ja             | ja       |
| Aufwand                                | gering      | gering   | mittel         | hoch     |

## Untersuchung
- Analyse der Spezifikationen (RFC 8288, HAL) und der Quarkus-Dokumentation zu Web Links
  sowie des Vorlesungsbeispiels `04_Hateoas_ueber_Links`.
- HAL mit JSON-B wurde im Projekt erprobt: `_links` und `_embedded` erscheinen bei
  `Accept: application/hal+json` (siehe ADR 0001, Test `JsonbHalTest`).
- Der zustandsabhängige Link wurde im HAL-Format mit `LinkVarianteTest` nachgewiesen
  (siehe ADR 0002).
- Option 3 (eigenes Format) und Option 4 (JSON:API) wurden ebenfalls im Spike umgesetzt
  (`LinkFormatTest`, alle Tests grün). Beide können zustandsabhängige Links pro Eintrag abbilden.
  - Option 3 benötigt eigene DTOs für Links (`LinkDTO`) und ist kein Standard.
  - Option 4 benötigt ein selbst gebautes Dokumentformat (`data`, `attributes`, `links`)
    ohne Unterstützung durch Quarkus; bereits das Grundgerüst erfordert eigene Klassen,
    Relationships und `included` wären zusätzlicher Aufwand.

Entscheidender Befund: Die Liste `GET /players` enthält Player mit und ohne Spieler-ID.
Der Link „Spielerpass registrieren“ muss deshalb **pro Eintrag** gesetzt werden.
Mit dem Link-Header ist das nicht möglich, weil er nur für die Antwort als Ganzes gilt.

## Entscheidung
Wir übertragen die Links im HAL-Format im Response-Body (`application/hal+json`).

Ausschlaggebend ist, dass HAL Links pro Eintrag in Listen erlaubt und damit den
zustandsabhängigen Link auch in Listen abbilden kann. Gegenüber JSON:API und einem eigenen
Format spricht für HAL die vorhandene Unterstützung durch Quarkus und die Nennung in der
Aufgabenstellung.

## Konsequenzen

Positiv:
- Zustandsabhängige Links funktionieren auch in Listen, jeder Eintrag hat eigene Links.
- Links sind direkt im JSON sichtbar, für Entwicklung, Swagger und Vorführung gut nachvollziehbar.
- Fertige Unterstützung durch `HalEntityWrapper` und `HalCollectionWrapper`.

Negativ:
- HAL ist kein Jakarta-Standard. Gegen die Rahmenanforderung „Standards wann immer möglich“
  wurde hier bewusst abgewogen, weil der Standardweg (Link-Header) die fachliche Anforderung
  bei Listen nicht erfüllt.
- Abhängigkeit von der Quarkus-Extension `quarkus-hal`.
- Der Body ändert sich: Clients müssen die Felder `_links` und `_embedded` kennen
  bzw. ignorieren.
- Die HAL-Ausgabe erscheint nur, wenn der Client `Accept: application/hal+json` sendet.
- HAL liefert wie der Link-Header nur die URL, nicht die HTTP-Methode eines Links.