package network.paquetes.partida;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteFinPartida extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int ganador;
    public final String mensaje;

    public PaqueteFinPartida(int ganador) {
        super(TipoPaquete.FIN_PARTIDA);
        this.ganador = ganador;
        this.mensaje = "Jugador " + ganador + " gana!";
    }

    public PaqueteFinPartida(String mensaje) {
        super(TipoPaquete.FIN_PARTIDA);
        this.ganador = -1;
        this.mensaje = mensaje;
    }
}
