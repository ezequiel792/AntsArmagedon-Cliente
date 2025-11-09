package network.paquetes.personaje;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteSaltar extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int numJugador;

    public PaqueteSaltar(int numJugador) {
        super(TipoPaquete.SALTAR);
        this.numJugador = numJugador;
    }
}
