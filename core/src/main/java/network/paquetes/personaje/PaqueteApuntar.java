package network.paquetes.personaje;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteApuntar extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int numJugador;
    public final int direccion;

    public PaqueteApuntar(int numJugador, int direccion) {
        super(TipoPaquete.APUNTAR);
        this.numJugador = numJugador;
        this.direccion = direccion;
    }
}
