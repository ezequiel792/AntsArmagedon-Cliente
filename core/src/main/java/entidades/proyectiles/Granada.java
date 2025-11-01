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
import utils.Constantes;

public class Granada extends Proyectil {

    private float tiempoVida;
    private float tiempoTranscurridoExplosion = 0f;
    private float coeficienteRebote = 0.6f;
    private static final float VELOCIDAD_MINIMA = 5f;

    private int radioDestruccion;
    private int radioExpansion;

    public Granada(float x, float y, float angulo, float velocidad, int danio,
                   float fuerzaKnockback, GestorColisiones gestorColisiones, Personaje ejecutor,
                   int radioDestruccion, int radioExpansion, Texture textura, float tiempoVida) {
        super(x, y, angulo, velocidad, danio, fuerzaKnockback, gestorColisiones, ejecutor, textura);

        this.radioDestruccion = radioDestruccion;
        this.radioExpansion = radioExpansion;
        this.tiempoVida = tiempoVida;
    }

    @Override
    public final void mover(float delta, GestorFisica gestorFisica) {
        if (!activo) return;

        tiempoTranscurrido += delta;
        tiempoTranscurridoExplosion += delta;

        if (tiempoTranscurridoExplosion >= tiempoVida) {
            explotar();
            return;
        }

        Personaje ignorar = (ejecutor != null && tiempoTranscurrido < Constantes.TIEMPO_GRACIA) ? ejecutor : null;

        gestorFisica.moverGranadaConRaycast(this, delta, ignorar);

        if (velocidadVector.len2() < VELOCIDAD_MINIMA)
            velocidadVector.setZero();

        updateHitbox();
        if (sprite != null) sprite.setPosition(x, y);
    }

    private final void explotar() {

        Sound sonidoExplosion = GestorAssets.get(GestorRutas.SONIDO_EXPLOSION, Sound.class);
        GestorAudio.playSFX(sonidoExplosion);

        gestorColisiones.getMapa().destruir(centroHitbox().x, centroHitbox().y, radioDestruccion);
        for (Colisionable c : gestorColisiones.getColisionablesRadio(centroHitbox().x, centroHitbox().y, radioExpansion)) {
            if (c instanceof Personaje personaje && personaje.getActivo()) {
                float distancia = personaje.distanciaAlCentro(x, y);
                float factor = (distancia <= radioDestruccion) ? 1f : factorDeDanio(distancia);
                int danioFinal = (int) (danio * factor);
                if (danioFinal > 0) {
                    Vector2 dir = new Vector2(
                        personaje.getX() + personaje.getWidth() / 2f - x,
                        personaje.getY() + personaje.getHeight() / 2f - y
                    ).nor();
                    float fuerzaX = dir.x * fuerzaKnockback * factor;
                    float fuerzaY = dir.y * fuerzaKnockback * factor;
                    personaje.recibirDanio(danioFinal, fuerzaX, fuerzaY);
                }
            } else if (!(c instanceof Personaje)) {
                c.desactivar();
            }
        }
        desactivar();
    }

    private float factorDeDanio(float distancia) {
        if (distancia <= radioDestruccion) return 1f;
        if (distancia >= radioExpansion) return 0f;
        return 1f - (distancia - radioDestruccion) / (radioExpansion - radioDestruccion);
    }

    @Override
    public final void impactar(float centroX, float centroY) { }

    public float getCoeficienteRebote() { return this.coeficienteRebote; }
}

