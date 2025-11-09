package network.paquetes.partida;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteDesconexion extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final String motivo;

    public PaqueteDesconexion(String motivo) {
        super(TipoPaquete.DESCONEXION);
        this.motivo = motivo;
    }
}
