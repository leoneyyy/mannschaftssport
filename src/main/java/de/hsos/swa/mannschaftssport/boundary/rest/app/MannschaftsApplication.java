package de.hsos.swa.mannschaftssport.boundary.rest.app;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(title = "Mannschaftssport API",
                version = "1.0.0",
                description = "Verwaltung von Teams, Playern und Managern im Mannschaftssport. "
                                + "Teams unter /teams, Player unter /players, Manager unter /managers. "
                                + "Kader- und Managerzuordnung ueber /teams/{id}/players bzw. /teams/{id}/manager, "
                                + "Spielerpass-Vergabe unter /players/{id}/spielerpass.",

        contact = @Contact(name = "Leon Schlender und Vivien Prell",
        email = "test",
        url = "https://github.com/leoneyyy") ))

@ApplicationPath("/")
public class MannschaftsApplication extends Application {
}