package Fisicas;

import com.badlogic.gdx.math.Vector2;
import utils.Constantes;

public final class Fisica {

    public void aplicarGravedad(Vector2 velocidad, float delta) {
        velocidad.y += Constantes.GRAVEDAD * delta;
    }

}
