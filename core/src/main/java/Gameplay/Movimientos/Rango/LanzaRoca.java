package Gameplay.Movimientos.Rango;

import Gameplay.Gestores.Logicos.GestorProyectiles;
import Gameplay.Gestores.GestorRutas;
import Gameplay.Movimientos.MovimientoRango;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import entidades.personajes.Personaje;
import entidades.proyectiles.Proyectil;
import entidades.proyectiles.ProyectilesBalisticos.Roca;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class LanzaRoca extends MovimientoRango {

    public LanzaRoca(GestorProyectiles gestorProyectiles) {
        super("Lanza Roca", GestorAssets.get(GestorRutas.ATLAS_MOVIMIENTO_ROCA, TextureAtlas.class),
            "RocaMovimiento", 550f, gestorProyectiles);
    }

    @Override
    protected Proyectil crearProyectil(float x, float y, float angulo, float velocidad, Personaje ejecutor) {
        return new Roca(x, y, angulo, velocidad, gestorProyectiles.getGestorColisiones(), ejecutor);
    }
}
