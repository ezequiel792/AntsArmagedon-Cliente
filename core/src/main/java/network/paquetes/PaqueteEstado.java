package network.paquetes;

import network.paquetes.utilidad.DatosJuego;

import java.util.List;

import java.util.List;

public class PaqueteEstado extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int turnoActual;
    public final double tiempoRestante;
    public final List<DatosJuego.Jugador> jugadores;
    public final List<DatosJuego.PowerUp> powerUps;
    public final int ganadorId;

    public PaqueteEstado(int turnoActual, double tiempoRestante,
                         List<DatosJuego.Jugador> jugadores,
                         List<DatosJuego.PowerUp> powerUps,
                         int ganadorId) {
        super(TipoPaquete.ESTADO_JUEGO);
        this.turnoActual = turnoActual;
        this.tiempoRestante = tiempoRestante;
        this.jugadores = jugadores;
        this.powerUps = powerUps;
        this.ganadorId = ganadorId;
    }
}
