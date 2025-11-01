package entidades.proyectiles.ProyectilesBalisticos;

import Gameplay.Gestores.Logicos.GestorColisiones;
import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.graphics.Texture;
import entidades.personajes.Personaje;
import entidades.proyectiles.ProyectilBalistico;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class Nectar extends ProyectilBalistico {

    public Nectar(float x, float y, float angulo, float velocidadBase,
                  GestorColisiones gestorColisiones, Personaje ejecutor) {
        super(x, y, angulo, velocidadBase, 25, 400f, gestorColisiones, ejecutor,
            GestorAssets.get(GestorRutas.NECTAR, Texture.class));
    }
}


