package network.paquetes.personaje;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteCambioMovimiento extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int numJugador;
    public final int indiceMovimiento;

    public PaqueteCambioMovimiento(int numJugador, int indiceMovimiento) {
        super(TipoPaquete.CAMBIAR_MOVIMIENTO);
        this.numJugador = numJugador;
        this.indiceMovimiento = indiceMovimiento;
    }
}
