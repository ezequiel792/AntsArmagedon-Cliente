package Gameplay.Movimientos.Rango;

import Gameplay.Gestores.Logicos.GestorProyectiles;
import Gameplay.Gestores.GestorRutas;
import Gameplay.Movimientos.MovimientoRango;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import entidades.personajes.Personaje;
import entidades.proyectiles.Proyectil;
import entidades.proyectiles.ProyectilesBalisticos.Nectar;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class LanzaNectar extends MovimientoRango {

    public LanzaNectar(GestorProyectiles gestorProyectiles) {
        super("Lanza Néctar", GestorAssets.get(GestorRutas.ATLAS_MOVIMIENTO_NECTAR, TextureAtlas.class),
            "NectarMovimiento", 550f, gestorProyectiles);
    }

    @Override
    protected Proyectil crearProyectil(float x, float y, float angulo, float velocidad, Personaje ejecutor) {
        return new Nectar(x, y, angulo, velocidad, gestorProyectiles.getGestorColisiones(), ejecutor);
    }
}
