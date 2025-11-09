package network.paquetes.partida;

import network.paquetes.PaqueteRed;
import network.paquetes.utilidad.DatosJuego;

import java.io.Serializable;
import java.util.List;

public final class PaqueteEstadoPartida extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public List<DatosJuego.EntidadDTO> entidades;
    public List<DatosJuego.ProyectilDTO> proyectiles;

    public int jugadorEnTurno;
    public float tiempoRestante;
    public boolean enTransicion;
    public int personajeIndex;

    public PaqueteEstadoPartida(List<DatosJuego.EntidadDTO> entidades,
                                List<DatosJuego.ProyectilDTO> proyectiles,
                                int jugadorEnTurno,
                                float tiempoRestante,
                                boolean enTransicion,
                                int personajeIndex) {
        super(TipoPaquete.ESTADO_PARTIDA);
        this.entidades = entidades;
        this.proyectiles = proyectiles;
        this.jugadorEnTurno = jugadorEnTurno;
        this.tiempoRestante = tiempoRestante;
        this.enTransicion = enTransicion;
        this.personajeIndex = personajeIndex;
    }

    public PaqueteEstadoPartida() {
        super(TipoPaquete.ESTADO_PARTIDA);
    }
}
