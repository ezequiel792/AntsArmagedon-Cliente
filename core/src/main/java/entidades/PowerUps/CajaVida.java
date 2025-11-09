package entidades.PowerUps;

import Gameplay.Gestores.Logicos.GestorColisiones;
import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import entidades.personajes.Personaje;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class CajaVida extends PowerUp {

    public CajaVida(float x, float y, GestorColisiones gestorColisiones) {
        super(x, y, GestorAssets.get(GestorRutas.ATLAS_CAJA_VIDA, TextureAtlas.class),
            "CajaVida", gestorColisiones);
    }

    @Override
    public void aplicarEfecto(Personaje personaje) {
        if (!activo) return;
        personaje.aumentarVida(25);
        desactivar();
    }
}
