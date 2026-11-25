# ADR 0001: JSON-Binding mit JSON-B statt Jackson

## Status
Akzeptiert, 23.11.2026

## Kontext
Die REST-API muss Java-Objekte (DTOs) in JSON umwandeln und umgekehrt.
Quarkus unterstützt dafür zwei Bibliotheken:
- JSON-B über die Extension `quarkus-rest-jsonb`
- Jackson über die Extension `quarkus-rest-jackson`

Rahmenbedingungen aus dem Projekt:
- Standards sind wann immer möglich zu verwenden.
- Die API soll RMM Level 3 erreichen, Links werden im HAL-Format übertragen
  (Extension `quarkus-hal`).
- Die DTOs sind einfache Java-Records ohne komplexe Strukturen.

## Untersuchte Optionen

### Option 1: JSON-B (Jakarta JSON Binding)
- Teil von Jakarta EE, also ein Standard. Referenzimplementierung ist Yasson.
- Annotationen aus `jakarta.json.bind.annotation`, z. B. `@JsonbProperty`, `@JsonbTransient`.
- Die Implementierung ist austauschbar, ohne den eigenen Code zu ändern.
- Bereits in AB02 und AB03 eingesetzt.

### Option 2: Jackson
- Kein Standard, aber in der Java-Welt sehr verbreitet.
- Mehr Funktionen und Konfigurationsmöglichkeiten.
- Eigene Annotationen aus `com.fasterxml.jackson`, z. B. `@JsonProperty`, `@JsonIgnore`.
- Viele Quarkus-Anleitungen und Bibliotheken setzen Jackson voraus.

### Vergleich

| Kriterium                   | JSON-B          | Jackson               |
|-----------------------------|-----------------|-----------------------|
| Jakarta-Standard            | ja              | nein                  |
| Implementierung austauschbar| ja              | nein                  |
| Funktionsumfang             | ausreichend     | größer                |
| Beispiele und Verbreitung   | geringer        | sehr hoch             |
| Erfahrung im Team           | AB02, AB03      | keine                 |

## Untersuchung
- DTOs als Java-Records mit JSON-B gelesen und geschrieben (POST /players,
  GET /players/{id}): JSON-B serialisiert und deserialisiert die Records ohne
  zusätzliche Konfiguration. Annotationen aus `jakarta.json.bind.annotation`
  (z. B. `@JsonbProperty`) werden berücksichtigt.
- HAL-Ausgabe mit `quarkus-hal` und `quarkus-rest-jsonb`: Beim Aufruf mit
  `Accept: application/hal+json` erzeugt `HalEntityWrapper` einen Body mit
  `_links`, `HalCollectionWrapper` zusätzlich `_embedded`. JSON-B und `quarkus-hal`
  funktionieren also zusammen.

## Entscheidung
Wir verwenden JSON-B.

Ausschlaggebend ist die Rahmenanforderung, Standards wann immer möglich zu verwenden.
JSON-B ist Teil von Jakarta EE, Jackson nicht. Die Zusatzfunktionen von Jackson
brauchen wir nicht, weil unsere DTOs einfache Records sind.

## Konsequenzen

Positiv:
- Standardkonform: Die Implementierung (Yasson) kann ausgetauscht werden,
  ohne den eigenen Code zu ändern.
- Einheitlich mit den anderen Jakarta-Standards im Projekt (Jakarta REST, CDI, Validation).
- Erfahrung aus AB02 und AB03 vorhanden.

Negativ:
- Weniger Beispiele im Netz, viele Quarkus-Anleitungen nutzen Jackson und müssen übertragen werden.
- Bibliotheken, die nur Jackson unterstützen, können nicht direkt eingesetzt werden.
  Neue Extensions müssen vor dem Einsatz auf JSON-B-Unterstützung geprüft werden.

Umsetzung:
- In der `pom.xml` ist `quarkus-rest-jsonb` eingetragen, `quarkus-rest-jackson` nicht.
- JSON-Annotationen im Code stammen ausschließlich aus `jakarta.json.bind.annotation`.