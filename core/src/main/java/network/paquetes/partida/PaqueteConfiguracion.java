package network.paquetes.partida;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteConfiguracion extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int jugadorId;
    public final String configString;

    public PaqueteConfiguracion(int jugadorId, String configString) {
        super(TipoPaquete.CONFIGURACION);
        this.jugadorId = jugadorId;
        this.configString = configString;
    }
}

