package Gameplay.Movimientos.Melee;

import Gameplay.Gestores.Logicos.GestorColisiones;
import Gameplay.Gestores.GestorRutas;
import Gameplay.Movimientos.MovimientoMelee;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class Aranazo extends MovimientoMelee {

    public Aranazo(GestorColisiones gestorColisiones) {
        super("Arañazo", GestorAssets.get(GestorRutas.ATLAS_MIRA, TextureAtlas.class),
            "mira", 10f, 40f, 10, 50f, gestorColisiones);
    }
}
