package Gameplay.Movimientos.Rango;

import Gameplay.Gestores.Logicos.GestorProyectiles;
import Gameplay.Gestores.GestorRutas;
import Gameplay.Movimientos.MovimientoRango;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import entidades.personajes.Personaje;
import entidades.proyectiles.Proyectil;
import entidades.proyectiles.ProyectilesBalisticos.GranadaMano;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class LanzaGranada extends MovimientoRango {

    public LanzaGranada(GestorProyectiles gestorProyectiles) {
        super("Lanza Granada", GestorAssets.get(GestorRutas.ATLAS_MOVIMIENTO_GRANADA, TextureAtlas.class),
            "GranadaMovimiento", 550f, gestorProyectiles);
    }

    @Override
    protected Proyectil crearProyectil(float x, float y, float angulo, float velocidad, Personaje ejecutor) {
        return new GranadaMano(x, y, angulo, velocidad, gestorProyectiles.getGestorColisiones(), ejecutor);
    }
}
