# ADR-001: JSON-Binding mit JSON-B

## Status

Akzeptiert (25.11.2026)

## Kontext

Die REST-API der Anwendung "Mannschaftssport" tauscht Daten im JSON-Format aus.
Quarkus bietet dafür zwei Extensions an: `quarkus-rest-jsonb` (Jakarta JSON Binding, JSON-B)
und `quarkus-rest-jackson` (Jackson, kein Jakarta-Standard).

Laut technischer Rahmenanforderung sollen Standards wann immer möglich verwendet werden.
Technische Basis sind Quarkus 3.33 (LTS) und Java 25. Die DTOs werden als Java-Records umgesetzt.
Das JSON-Binding muss mit `quarkus-http-problem` (Fehler nach RFC 9457), `quarkus-rest-links`,
`quarkus-hal` (Links für Richardson Level 3) und `quarkus-hibernate-validator` zusammenarbeiten.
Die Spieler-ID (`spielerId`) ist fachlich auch dann bedeutsam, wenn sie `null` ist,
weil davon abhängt, ob der Link "Spielerpass registrieren" angeboten wird.

Um die Frage zu klären, wurde ein Architektur-Spike durchgeführt: In einem separaten
Quarkus-Projekt (`spike-json`) wurde eine minimale Ressource `/players` mit dem Record
`PlayerDTO(Long id, String name, String spielerId)` umgesetzt. Dieselben Anfragen wurden
einmal mit JSON-B und einmal mit Jackson ausgeführt:

| Prüffrage | JSON-B | Jackson |
|---|---|---|
| Records | Ausgabe und Einlesen funktionieren (200 bzw. 201 mit `Location`-Header) | Ausgabe und Einlesen funktionieren (200 bzw. 201 mit `Location`-Header) |
| `null`-Ausgabe | Standardmäßig wird das Feld im normalen JSON weggelassen, bei HAL aber ausgegeben (uneinheitlich). Mit `@JsonbNillable` am Record wird `"spielerId":null` auch im normalen JSON ausgegeben (einheitlich). | Feld wird immer ausgegeben, bei normalem JSON und bei HAL (einheitlich) |
| Unbekannte Felder | werden ignoriert, kein Fehler | werden ignoriert, kein Fehler |
| Problem-JSON | `application/problem+json` bei 404, 400 (mit `violations`) und 500 (ohne interne Fehlermeldung) | identisch zu JSON-B |
| HAL-Links | `_links` mit `self` und `list`, Liste unter `_embedded.items`, zusätzlich `Link`-Header | identisch zu JSON-B |

Beide Varianten erfüllen alle technischen Anforderungen, insbesondere funktioniert
`quarkus-http-problem` mit JSON-B. Der einzige Unterschied ist die Ausgabe von `null`-Werten.

## Entscheidung

Wir verwenden JSON-B (`quarkus-rest-jsonb`) für das JSON-Binding.

DTOs, deren `null`-Werte fachlich bedeutsam sind (z. B. `PlayerDTO`), annotieren wir mit
`@JsonbNillable`, damit `null`-Felder einheitlich ausgegeben werden.

Wir entscheiden uns für JSON-B, weil es Teil von Jakarta EE ist und damit der Vorgabe
"Standards wann immer möglich" entspricht. Der Spike hat gezeigt, dass JSON-B alle benötigten
Funktionen gleichwertig zu Jackson erfüllt. Den einzigen Unterschied gleichen wir mit einer
Standard-Annotation aus, ohne herstellerspezifische Konfiguration.

## Konsequenzen

Die Anwendung nutzt durchgängig Jakarta-Standards (REST, CDI, Bean Validation, JSON Binding).
Der Code enthält keine Jackson-spezifischen Annotationen und bleibt unabhängig von einer
konkreten Implementierung. Unbekannte Felder in Requests werden toleriert, was die
Kompatibilität mit Clients erhöht.

Bei jedem neuen DTO muss bewusst entschieden werden, ob `@JsonbNillable` nötig ist,
da JSON-B `null`-Felder sonst weglässt. Viele Beispiele, Tutorials und einige
Quarkus-Erweiterungen sind auf Jackson ausgelegt (z. B. die in der Aufgabe genannte Quelle
zu RFC 9457) und müssen teilweise angepasst werden. Die Community rund um JSON-B ist
kleiner als die von Jackson.

Jackson als Alternative wäre technisch gleichwertig gewesen und hätte `null`-Felder ohne
zusätzliche Annotation einheitlich ausgegeben. Dieser Vorteil wiegt den fehlenden Standard
jedoch nicht auf, da er sich mit Standardmitteln ausgleichen lässt.