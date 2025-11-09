package partida.online.gestores;

import Gameplay.Gestores.GestorTurno;
import com.principal.Jugador;
import entidades.personajes.Personaje;

import java.util.ArrayList;

public final class GestorTurnoOnline extends GestorTurno {

    private final int idJugadorLocal;
    private boolean autoridadLocal;

    public GestorTurnoOnline(ArrayList<Jugador> jugadores, float tiempoPorTurno, int idJugadorLocal) {
        super(jugadores, tiempoPorTurno);
        this.idJugadorLocal = idJugadorLocal;
    }

    @Override
    public void correrContador(float delta) {
        super.correrContador(delta);
    }

    @Override
    public void sincronizarTurno(int nuevoTurno, float tiempoRestante) {
        super.sincronizarTurno(nuevoTurno, tiempoRestante);

        Jugador activo = getJugadorActivo();
        autoridadLocal = (activo.getIdJugador() == idJugadorLocal);

        for (Jugador j : jugadores) {
            for (Personaje p : j.getPersonajes()) {
                p.setEnTurno(j.getIdJugador() == activo.getIdJugador());
            }
        }
    }

    public boolean tieneAutoridadLocal() {
        return autoridadLocal;
    }

    public void reconciliarTiempo(float tiempoServidor) {
        float diff = Math.abs(getTiempoActual() - tiempoServidor);
        if (diff > 0.2f) setTiempoRestante(tiempoServidor);
    }
}
