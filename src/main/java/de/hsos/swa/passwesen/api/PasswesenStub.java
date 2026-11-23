package de.hsos.swa.passwesen.api;

import java.util.concurrent.atomic.AtomicLong;

public class PasswesenStub implements Passwesen {

    private final AtomicLong zaehler = new AtomicLong(1);

    @Override
    public String registriereSpielerpass(long playerId) {
        return String.format("SP-%04d", zaehler.getAndIncrement());
    }
}
