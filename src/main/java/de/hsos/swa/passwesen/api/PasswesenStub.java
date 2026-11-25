package de.hsos.swa.passwesen.api;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class PasswesenStub implements Passwesen {

    private final AtomicLong naechsteNummer = new AtomicLong(1001);

    @Override
    public String registriereSpielerpass(Long playerId, String name) {
        return "SP-" + naechsteNummer.getAndIncrement();
    }
}