package entidades.proyectiles.ProyectilesBalisticos;

import Gameplay.Gestores.Logicos.GestorColisiones;
import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.graphics.Texture;
import entidades.personajes.Personaje;
import entidades.proyectiles.Granada;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class GranadaMano extends Granada {

    public GranadaMano(float x, float y, float angulo, float velocidadBase,
                       GestorColisiones gestorColisiones, Personaje ejecutor) {
        super(x, y, angulo, velocidadBase, 20, 500f, gestorColisiones, ejecutor,
            100, 150, GestorAssets.get(GestorRutas.GRANADA, Texture.class),
            3f);
    }
}


