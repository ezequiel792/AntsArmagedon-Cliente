package network.paquetes;

import java.io.Serializable;

public class PaqueteConexion extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    private final int idJugador;
    private final boolean aprobado;

    public PaqueteConexion() {
        super(TipoPaquete.CONEXION);
        this.idJugador = -1;
        this.aprobado = false;
    }

    public PaqueteConexion(int idJugador, boolean aprobado) {
        super(TipoPaquete.CONEXION);
        this.idJugador = idJugador;
        this.aprobado = aprobado;
    }

    public int getIdJugador() { return idJugador; }
    public boolean esAprobado() { return aprobado; }
}
