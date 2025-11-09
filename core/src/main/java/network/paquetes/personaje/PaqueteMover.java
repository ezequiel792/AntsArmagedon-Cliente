package network.paquetes.personaje;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteMover extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int numJugador;
    public final float direccion;

    public PaqueteMover(int numJugador, float direccion) {
        super(TipoPaquete.MOVER);
        this.numJugador = numJugador;
        this.direccion = direccion;
    }
}
