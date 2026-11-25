# SWA AB04 – Mannschaftssport: Alles Wichtige

Abgabe: Gr. 1 am 24.11., Gr. 2 am 26.11. · 10 Punkte, mindestens 7
 
---

## 1. Aufgabe in Kürze

- RESTful API für **Teams, Player, Manager** nach dem jax-Artikel „RESTful APIs richtig gemacht“
- Quarkus 3.33, Java 25, Jakarta REST, CDI, JSON-B, alles **im Arbeitsspeicher**
- **Modulith**: eigenes Modul `mannschaftssport` + fremdes Modul `passwesen` (nur Schnittstelle + Stub)
- **RMM Level 3**: Links auf Beziehungen + mindestens ein **zustandsabhängiger** Link
- **Architektur-Spikes** + **ADRs**, danach Entwurf und Umsetzung
### Fachliche Regeln

| Regel | Umsetzung |
|---|---|
| Player hat technische ID (dauerhaft) | `long id`, vom Repository vergeben |
| Player hat Spieler-ID, anfangs `null` | `String spielerId` |
| Spieler-ID vergibt nur das Passwesen | `weiseSpielerIdZu()`, kein Setter |
| Spieler-ID nur einmal | sonst `SpielerIdBereitsVergebenException` → 409 |
| Team-Zuordnung nur mit Spieler-ID | `Team.nimmAuf()` → `KeineSpielerIdException` → 409 |
| Player nur in einem Team | `KaderVerwaltung` → `PlayerSchonImTeamException` → 409 |
| Manager braucht keinen Spielerpass | Manager hat keine Spieler-ID |
 
---

## 2. Begriffe

| Begriff | Bedeutung |
|---|---|
| **Monolith** | eine Anwendung, ein Prozess, ein Deployment |
| **Modulith** | modularer Monolith: ein Prozess, aber klare Module, Zugriff nur über deren Schnittstelle |
| **Microservices** | viele einzelne Prozesse, Kommunikation über das Netzwerk |
| **ADR** | Architecture Decision Record: kurzes Dokument zu einer Entscheidung (Kontext, Optionen, Entscheidung, Konsequenzen) |
| **Spike** | kleines Wegwerf-Projekt, um eine offene Frage vor der Umsetzung zu klären |
| **HATEOAS** | Hypermedia as the Engine of Application State: Server sagt per Links, was der Client als Nächstes tun kann |
| **HAL** | Hypertext Application Language: JSON-Format mit `_links` und `_embedded`, Media-Type `application/hal+json` |
| **Port** | Interface in unserem Modul, das beschreibt, was wir von außen brauchen (`SpielerpassRegister`) |
| **Adapter** | Implementierung des Ports, spricht mit dem fremden Modul (`PasswesenAdapter`) |
| **Stub** | einfacher Platzhalter für eine noch nicht gelieferte Komponente (`PasswesenStub`) |
| **Aggregat** | Gruppe, die über eine Root-Entity verändert wird; Team, Player, Manager sind je ein eigenes Aggregat |
| **Idempotent** | mehrfach ausführen = gleiche Wirkung wie einmal (GET, PUT, DELETE ja; POST nein; PATCH nicht garantiert) |
 
---

## 3. RMM-Stufen

| Level | Inhalt |
|---|---|
| 0 | ein Endpunkt, alles per POST |
| 1 | Ressourcen mit eigenen URIs |
| 2 | HTTP-Verben + Statuscodes |
| **3** | **Hypermedia-Links (HATEOAS)** |
 
---

## 4. Architektur (ECB + DDD, Modulith)

```
de.hsos.swa.mannschaftssport
├── boundary
│   ├── rest     Resources, Link-Hilfsklassen, Mapper, MannschaftsApplication
│   └── dto      TeamDTO, PlayerDTO, ManagerDTO (+ Eingabe-DTOs), Records
├── control      KaderVerwaltung, SpielerpassVergabe
├── entity       Team, Player, Manager, Teamkategorie,
│   │            TeamKatalog, PlayerKatalog, ManagerKatalog, SpielerpassRegister (Port)
│   └── exception
└── gateway
    ├── memory     TeamRepository, PlayerRepository, ManagerRepository
    └── passwesen  PasswesenAdapter
 
de.hsos.swa.passwesen            (fremdes Modul)
├── api      Passwesen (Interface)
└── stub     PasswesenStub
```

### Weg „Spielerpass registrieren“
```
SpielerpassResource → SpielerpassVergabe → SpielerpassRegister (Port)
   → PasswesenAdapter → Passwesen (API) → PasswesenStub  ("SP-0001")
```

### Warum so?
- **Lose Kopplung**: Unser Modul kennt nur den Port. Nur der Adapter kennt das Passwesen. Austausch des Stubs ändert unseren Code nicht.
- **DIP**: Interfaces (Kataloge, Port) liegen innen in `entity`, Implementierungen außen im `gateway`.
- **Control nur für echte Use Cases**: mehrere Aggregate oder das Passwesen beteiligt (Zuordnung, Spielerpass, ggf. Löschen mit Aufräumen). Reines CRUD geht direkt an den Katalog.
- **Ein Katalog pro Aggregat**: Team, Player, Manager existieren unabhängig voneinander.
- **Team speichert IDs statt Objekte**: Aggregate verweisen nur über IDs aufeinander. `Set<Long>` verhindert Duplikate.
- **Exceptions in `entity`, Mapper in `boundary`**: Fehler sind fachlich, Statuscodes sind HTTP-Technik.
### Entities ohne Setter
Änderungen nur über **fachliche Methoden**, die Regeln prüfen:
`aendere(...)`, `weiseSpielerIdZu(...)`, `nimmAuf(player)`, `entferne(id)`, `setzeManager(id)`.
Kein `setName`, kein `setSpielerId`. Sonst wäre es ein anämisches Domänenmodell.
 
---

## 5. REST-API

| Methode | Pfad | Zweck | Erfolg | Fehler |
|---|---|---|---|---|
| GET | `/teams` | alle Teams (`?name=`) | 200 | |
| POST | `/teams` | Team anlegen | 201 + Location | 400 |
| GET | `/teams/{id}` | ein Team | 200 | 404 |
| PUT | `/teams/{id}` | Team ändern | 200 | 400, 404 |
| DELETE | `/teams/{id}` | Team löschen | 204 | 404 |
| GET | `/teams/{id}/players` | Player des Teams | 200 | 404 |
| PUT | `/teams/{id}/players/{playerId}` | Player aufnehmen | 200 | 404, **409** |
| DELETE | `/teams/{id}/players/{playerId}` | Player entfernen | 204 | 404 |
| GET | `/teams/{id}/manager` | Manager des Teams | 200 | 404 |
| PUT | `/teams/{id}/manager/{managerId}` | Manager setzen | 200 | 404 |
| DELETE | `/teams/{id}/manager` | Manager entfernen | 204 | 404 |
| GET | `/players` | alle Player (`?name=`) | 200 | |
| POST | `/players` | Player anlegen (ohne Spieler-ID) | 201 + Location | 400 |
| GET | `/players/{id}` | ein Player | 200 | 404 |
| PUT | `/players/{id}` | Player ändern | 200 | 400, 404 |
| DELETE | `/players/{id}` | Player löschen | 204 | 404 |
| POST | `/players/{id}/spielerpass` | Spielerpass registrieren | 200 | 404, **409** |
| GET/POST | `/managers` | Manager auflisten / anlegen | 200 / 201 | 400 |
| GET/PUT/DELETE | `/managers/{id}` | ein Manager | 200 / 200 / 204 | 400, 404 |

### Anpassungen gegenüber dem jax-Artikel
| Artikel | wir | Grund |
|---|---|---|
| PATCH | **PUT** | idempotent, Ressourcen klein, vom Blatt erlaubt |
| JSON:API | **HAL** | siehe ADR 0003 |
| `/relationships/players` | `/teams/{id}/players/{playerId}` | einfacher, PUT ist idempotent |
| Verb im Pfad (`notifyInjury`) abgelehnt | `POST /players/{id}/spielerpass` | Aktion als eigene Ressource, wie im Artikel empfohlen |

### PUT vs. PATCH
- **PUT** ersetzt die ganze Ressource, idempotent.
- **PATCH** ändert nur Teile, nach RFC 5789 **nicht garantiert** idempotent (der Artikel ist hier ungenau).
### Statuscodes und Mapper
| Exception (entity) | Mapper (boundary) | Status |
|---|---|---|
| `NichtGefundenException` + Unterklassen | `NichtGefundenMapper` | 404 |
| `RegelverletzungException` + Unterklassen | `KonfliktMapper` | 409 |
| `IllegalArgumentException` | `EingabeFehlerMapper` | 400 |
 
---

## 6. HAL / Level 3

### Links
| Ressource | Links |
|---|---|
| Player | `self`; **`spielerpass-registrieren` nur, wenn `spielerId == null`** |
| Team | `self`, `players`; `manager` nur, wenn vorhanden |
| Manager | `self` |
| Listen | `self` der Liste, Einträge in `_embedded`, jeder mit eigenen Links |

### Beispiel
```json
{
  "id": 1, "vorname": "Max", "nachname": "Muster", "spielerId": null,
  "_links": {
    "self": { "href": "http://localhost:8080/players/1" },
    "spielerpass-registrieren": { "href": "http://localhost:8080/players/1/spielerpass" }
  }
}
```
Nach der Registrierung: `spielerId: "SP-0001"`, nur noch `self`.

### Umsetzung
```java
HalEntityWrapper<PlayerDTO> wrapper = new HalEntityWrapper<>(PlayerDTO.von(p));
wrapper.addLinks(Link.fromUri(self).rel("self").build());
if (!p.hatSpielerId()) {
    wrapper.addLinks(Link.fromUri(pass).rel("spielerpass-registrieren").build());
}
```
- Media-Type im `@Produces`: `MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON`
- Client muss `Accept: application/hal+json` senden
- Listen mit `HalCollectionWrapper`
### Deklarativ vs. manuell
| | `@RestLink` + `@InjectRestLinks` | `HalEntityWrapper` |
|---|---|---|
| wie | Annotationen, Quarkus baut Links | Links selbst bauen |
| zustandsabhängig | **nein** | **ja** |
| Code | wenig | mehr |

`@RestLink(rel, entityType)` meldet eine Methode als Link-Ziel an. `@InjectRestLinks` hängt die Links an. `RestLinkType.INSTANCE` = auch Links pro Objekt (z. B. `self` mit ID).

### Was bringen die Links?
- Geschäftsregel bleibt auf dem Server: Client prüft nur, ob der Link da ist.
- URLs können sich ändern, Client folgt den Links.
- API ist selbsterklärend.
- Grenze: HAL liefert nur die URL, nicht die HTTP-Methode → Level 3 „in Ansätzen“.
---

## 7. ADRs

| ADR | Entscheidung | Spike / Test |
|---|---|---|
| 0001 | **JSON-B** statt Jackson | `JsonbHalTest` |
| 0002 | Links **manuell** mit `HalEntityWrapper` statt `@RestLink` | `LinkVarianteTest` |
| 0003 | **HAL** im Body statt Link-Header / eigenem Format / JSON:API | `LinkFormatTest` |

### ADR 0001 – JSON-B
- Rahmenanforderung „Standards wann immer möglich“: JSON-B ist Jakarta-Standard, Jackson nicht.
- Implementierung (Yasson) austauschbar.
- Nachteil: weniger Beispiele, Jackson-only-Bibliotheken nicht direkt nutzbar.
- `@JsonbNillable`: JSON-B lässt `null` sonst weg; damit erscheint `"spielerId": null`.
### ADR 0002 – manuell statt deklarativ
- Spike-Ergebnis: Deklarativ bekommt auch der Player **mit** Pass den Registrier-Link.
- Nachteile: mehr Code, weniger übersichtlich, längere Methoden, Pfade als Strings.
### ADR 0003 – HAL statt Link-Header
- Spike-Ergebnis: Link-Header kann bei **Listen** keine Links **pro Eintrag** setzen.
- Bewusste Abweichung von „Standards wann immer möglich“, weil der Standardweg die fachliche Anforderung nicht erfüllt.
- HAL statt eigenem Format/JSON:API: verbreitet, von Quarkus unterstützt, in der Aufgabe genannt.
### Weitere Frage aus dem Blatt (nicht umgesetzt)
**RFC 9457 Problem Details**: einheitliches Fehlerformat `application/problem+json` mit `type`, `title`, `status`, `detail`, `instance`. Unsere Mapper liefern ein einfaches `{"fehler": ...}`.
 
---

## 8. Tests

### Test-Pyramide
| Ebene | Test | braucht |
|---|---|---|
| Unit | `TeamTest`, `SpielerpassVergabeTest` (Fake statt Passwesen) | nur JUnit |
| Integration | `@QuarkusTest` mit RestAssured | laufendes Quarkus |
| End-to-End | `@QuarkusIntegrationTest` | gebaute Anwendung |

### Wichtigste Tests
1. Player ohne Spieler-ID ins Team → 409, nach Registrierung → 200
2. Spielerpass registrieren → Link verschwindet
3. Spielerpass zweimal → 409
4. `SpielerpassVergabe` mit Lambda-Fake → lose Kopplung bewiesen
5. `JsonbHalTest` → JSON-B + HAL funktioniert
```java
vergabe.register = playerId -> "TEST-123";   // Fake statt Passwesen
```

RestAssured: Schlüssel mit Bindestrich in Anführungszeichen: `_links.'spielerpass-registrieren'`
 
---

## 9. Vorführung (.http)

```http
### neuer Player
POST http://localhost:8080/players
Content-Type: application/json
 
{ "vorname": "Max", "nachname": "Muster", "position": "Sturm" }
 
> {% client.global.set("playerId", response.body.id); %}
 
### mit Links: spielerpass-registrieren vorhanden
GET http://localhost:8080/players/{{playerId}}
Accept: application/hal+json
 
### Spielerpass registrieren
POST http://localhost:8080/players/{{playerId}}/spielerpass
Accept: application/hal+json
 
### Link ist weg
GET http://localhost:8080/players/{{playerId}}
Accept: application/hal+json
 
### zweites Mal: 409
POST http://localhost:8080/players/{{playerId}}/spielerpass
```
 
---

## 10. Befehle

| Zweck | Befehl |
|---|---|
| Dev-Mode | `.\mvnw quarkus:dev` |
| Tests | `.\mvnw clean test` |
| Extensions | `.\mvnw quarkus:add-extension "-Dextensions=rest,rest-jsonb,hal"` |
| Abhängigkeiten prüfen | `.\mvnw dependency:tree \| Select-String "jackson\|jsonb\|hal"` |
| Swagger | `http://localhost:8080/q/swagger-ui` |

### Git (zwei Branches)
```
git checkout -b leon && git push -u origin leon
git checkout -b partner main && git push -u origin partner
```
Zusammenführen per Pull Request nach `main`, Ruleset: PR + 1 Approval, kein Force-Push.
Regelmäßig: `git checkout main; git pull; git checkout leon; git merge main`
 
---

## 11. Häufige Fehler

| Fehler | Ursache |
|---|---|
| `Unsatisfied dependency` | Klasse ohne `@ApplicationScoped` (z. B. Adapter, Stub) |
| `Ambiguous dependencies` | zwei Klassen implementieren dasselbe Interface (alter Klassenname nach Umbenennen → `mvn clean`) |
| `Failed to delete ... -dev.jar` | Dev-Mode läuft noch → `q` drücken |
| Link fehlt im Test | Tippfehler im `rel`-Namen, falscher Import (`org.wildfly...Assert`) |
| 409 beim ersten Spielerpass | Player hat schon eine Spieler-ID (Beispieldaten oder zweiter Aufruf) |
| `_links` fehlen | `Accept: application/hal+json` nicht gesetzt |
 
---

## 12. Testat-Sätze

- **Modulith:** „Ein Prozess mit klaren Modulgrenzen. Unser Modul kennt das Passwesen nur über den Port `SpielerpassRegister`, nur der Adapter spricht mit dem fremden Modul.“
- **Lose Kopplung:** „Im Test ersetzen wir das Passwesen durch eine eigene Implementierung des Ports. Die Fachlogik ist ohne Passwesen testbar.“
- **Level 3:** „Der Server steuert über Links, welche Aktionen möglich sind. Der Link zum Registrieren erscheint nur ohne Spieler-ID.“
- **Manuelle Links:** „Die Annotationen kennen den Zustand nicht, deshalb bauen wir die Links mit `HalEntityWrapper`.“
- **HAL statt Header:** „Der Link-Header gilt nur für die ganze Antwort; in Listen brauchen wir Links pro Eintrag.“
- **JSON-B:** „Jakarta-Standard, erfüllt die Rahmenanforderung, Implementierung austauschbar.“
- **PUT statt PATCH:** „PUT ersetzt die ganze Ressource und ist idempotent, PATCH ist es nach RFC 5789 nicht garantiert.“
- **Control:** „Nur Use Cases über mehrere Aggregate oder das Passwesen; reines CRUD geht direkt an den Katalog.“
- **Keine Setter:** „Zustandsänderungen nur über fachliche Methoden, die die Regeln selbst prüfen.“
- **Exceptions:** „Fachliche Fehler in `entity`, Abbildung auf HTTP-Statuscodes in den Mappern der Boundary.“
 