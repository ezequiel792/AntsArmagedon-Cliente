package network.paquetes.partida;

import com.badlogic.gdx.math.Vector2;
import network.paquetes.PaqueteRed;

import java.io.Serializable;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class PaqueteInicioPartida extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int jugadorId;
    public final String configString;
    public final List<Vector2> spawns;

    public PaqueteInicioPartida(int jugadorId, String configString, List<Vector2> spawns) {
        super(TipoPaquete.INICIO_PARTIDA);
        this.jugadorId = jugadorId;
        this.configString = configString;
        this.spawns = spawns;
    }
}
