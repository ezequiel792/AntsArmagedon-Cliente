package entidades.proyectiles;

import Fisicas.Colisionable;
import Gameplay.Gestores.GestorAudio;
import Gameplay.Gestores.GestorRutas;
import Gameplay.Gestores.Logicos.GestorColisiones;
import Gameplay.Gestores.Logicos.GestorFisica;
import Gameplay.Gestores.Visuales.GestorAssets;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import entidades.personajes.Personaje;

public class ProyectilBalistico extends Proyectil {

    public ProyectilBalistico(float x, float y, float angulo, float velocidad, int danio,
                              float fuerzaKnockback, GestorColisiones gestorColisiones,
                              Personaje ejecutor, Texture textura) {
        super(x, y, angulo, velocidad, danio, fuerzaKnockback, gestorColisiones, ejecutor, textura);
    }

    @Override
    public final void mover(float delta, GestorFisica gestorFisica) {
        if (!activo) return;

        super.mover(delta, gestorFisica);

        if (getImpacto() && activo) {
            impactar(centroHitbox().x, centroHitbox().y);
            setImpacto(false);
        }
    }

    @Override
    public final void impactar(float centroX, float centroY) {

        Sound sonidoExplosion = GestorAssets.get(GestorRutas.SONIDO_EXPLOSION, Sound.class);
        GestorAudio.playSFX(sonidoExplosion);

        for (Colisionable c : gestorColisiones.getColisionables()) {
            if (c == this) continue;

            if (c instanceof Personaje personaje && c.getHitbox().overlaps(hitbox)) {
                float centroProyectilX = centroHitbox().x;
                float centroProyectilY = centroHitbox().y;
                float dx = personaje.getX() + personaje.getWidth() / 2f - centroProyectilX;
                float dy = personaje.getY() + personaje.getHeight() / 2f - centroProyectilY;
                Vector2 dir = new Vector2(dx, dy).nor();

                float fuerzaX = dir.x * fuerzaKnockback;
                float fuerzaY = dir.y * fuerzaKnockback * 0.6f;

                personaje.recibirDanio(danio, fuerzaX, fuerzaY);

                desactivar();
                return;
            }
        }
        desactivar();
    }

}

