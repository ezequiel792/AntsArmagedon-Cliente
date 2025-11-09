package network.paquetes.partida;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteCambioTurno extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int turno;             // índice de turno (0..n)
    public final float tiempoRestante;  // segundos restantes
    public final int jugadorId;         // id del jugador activo
    public final int personajeIndex;    // índice del personaje activo del jugador

    public PaqueteCambioTurno(int turno, float tiempoRestante, int jugadorId, int personajeIndex) {
        super(TipoPaquete.CAMBIO_TURNO);
        this.turno = turno;
        this.tiempoRestante = tiempoRestante;
        this.jugadorId = jugadorId;
        this.personajeIndex = personajeIndex;
    }
}

