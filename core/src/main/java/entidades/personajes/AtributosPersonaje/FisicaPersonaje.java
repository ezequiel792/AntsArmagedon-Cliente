package entidades.personajes.AtributosPersonaje;

import Gameplay.Gestores.Logicos.GestorColisiones;
import com.badlogic.gdx.math.Vector2;
import entidades.personajes.Personaje;
import utils.Constantes;

//Hacer que el resbale funcione en pendientes, y qyue no se pare en pixeles.
//Tal vez hacer que cuando esta en knockback rebote, en vez de resbalar

//Fijarse si podemos hacer que siempre que el personaje se este desplazando en x
//tenga tolerancia de subida o algo asi

public final class FisicaPersonaje {

    private static final float TIEMPO_RESBALE_DEFAULT = 0.6f;
    private static final float FACTOR_LOG_RESBALA = 50f;
    private static final float UMBRAL_MOVIMIENTO = 0.1f;
    private static final float UMBRAL_DETENER_RESBALA = 1f;
    private static final int ALTURA_MAX_ESCALON = 4;
    private static final float DIVISOR_MIN_KNOCKBACK = 0.5f;
    private static final float FACTOR_PESO_KNOCKBACK = 0.1f;

    private final Personaje personaje;
    private final GestorColisiones gestorColisiones;

    private boolean enKnockback = false;
    private float tiempoResbale = TIEMPO_RESBALE_DEFAULT;
    private float tiempoDesdeContacto = 0f;

    public FisicaPersonaje(Personaje personaje, GestorColisiones gestorColisiones) {
        this.personaje = personaje;
        this.gestorColisiones = gestorColisiones;
    }

    public void actualizar(float delta) {
        Vector2 vel = personaje.getVelocidad();

        if ((enKnockback || Math.abs(vel.x) > UMBRAL_MOVIMIENTO) && personaje.getSobreAlgo()) {
            tiempoDesdeContacto += delta;

            float k = (float) (Math.log(FACTOR_LOG_RESBALA) / tiempoResbale);
            float factor = (float) Math.exp(-k * tiempoDesdeContacto * delta);

            vel.x *= factor;

            if (tiempoDesdeContacto > tiempoResbale || Math.abs(vel.x) < UMBRAL_DETENER_RESBALA) {
                vel.x = 0;
                enKnockback = false;
                tiempoDesdeContacto = 0;
            }
        } else if (!personaje.getSobreAlgo()) {
            tiempoDesdeContacto = 0;
        }

        if (vel.x > Constantes.VEL_MAX_HORIZONTAL) {
            vel.x = Constantes.VEL_MAX_HORIZONTAL;
        } else if (vel.x < -Constantes.VEL_MAX_HORIZONTAL) {
            vel.x = -Constantes.VEL_MAX_HORIZONTAL;
        }

        if (vel.y > Constantes.VEL_MAX_VERTICAL) {
            vel.y = Constantes.VEL_MAX_VERTICAL;
        } else if (vel.y < -Constantes.VEL_MAX_VERTICAL) {
            vel.y = -Constantes.VEL_MAX_VERTICAL;
        }

        personaje.setVelocidad(vel);
    }

    public void moverHorizontal(float deltaX, float deltaTiempo) {
        if (enKnockback || deltaX == 0) return;

        if (deltaX < 0) {
            personaje.setDireccion(false);
            if (!personaje.getSprite().isFlipX()) personaje.getSprite().flip(true, false);
        } else {
            personaje.setDireccion(true);
            if (personaje.getSprite().isFlipX()) personaje.getSprite().flip(true, false);
        }

        float nuevaX = personaje.getX() + deltaX * personaje.getVelocidadX() * deltaTiempo;
        boolean libreEnX = gestorColisiones.verificarMovimiento(personaje, nuevaX, personaje.getY());

        if (libreEnX) {
            personaje.setX(nuevaX);
        } else {
            if (personaje.getSobreAlgo()) {
                boolean subio = false;

                for (int i = 1; i <= ALTURA_MAX_ESCALON; i++) {
                    float yCandidata = personaje.getY() + i;
                    if (gestorColisiones.verificarMovimiento(personaje, personaje.getX(), yCandidata) &&
                        gestorColisiones.verificarMovimiento(personaje, nuevaX, yCandidata)) {
                        personaje.setY(yCandidata);
                        personaje.setX(nuevaX);
                        subio = true;
                        break;
                    }
                }

                if (!subio) {
                    Vector2 vel = personaje.getVelocidad();
                    vel.x = 0;
                    personaje.setVelocidad(vel);
                }
            } else {
                Vector2 vel = personaje.getVelocidad();
                vel.x = 0;
                personaje.setVelocidad(vel);
            }
        }

        personaje.updateHitbox();
    }

    public void saltar(float fuerzaSalto) {
        if (personaje.getSobreAlgo() && !enKnockback) {
            Vector2 vel = personaje.getVelocidad();
            vel.y = fuerzaSalto;

            if (vel.y > Constantes.VEL_MAX_VERTICAL) {
                vel.y = Constantes.VEL_MAX_VERTICAL;
            } else if (vel.y < 0f) {
                vel.y = 0f;
            }

            personaje.setVelocidad(vel);
            personaje.setSobreAlgo(false);
            tiempoDesdeContacto = 0;
        }
    }

    public void aplicarKnockback(float fuerzaX, float fuerzaY) {
        float divisor = Math.max(DIVISOR_MIN_KNOCKBACK, personaje.getPeso() * FACTOR_PESO_KNOCKBACK);

        Vector2 vel = personaje.getVelocidad();
        vel.x = fuerzaX / divisor;
        vel.y = fuerzaY / divisor;

        if (vel.x > Constantes.VEL_MAX_HORIZONTAL) {
            vel.x = Constantes.VEL_MAX_HORIZONTAL;
        } else if (vel.x < -Constantes.VEL_MAX_HORIZONTAL) {
            vel.x = -Constantes.VEL_MAX_HORIZONTAL;
        }

        if (vel.y > Constantes.VEL_MAX_VERTICAL) {
            vel.y = Constantes.VEL_MAX_VERTICAL;
        } else if (vel.y < -Constantes.VEL_MAX_VERTICAL) {
            vel.y = -Constantes.VEL_MAX_VERTICAL;
        }

        personaje.setVelocidad(vel);
        enKnockback = true;
        personaje.setSobreAlgo(false);
        tiempoDesdeContacto = 0;
    }

    public float getTiempoResbale() { return this.tiempoResbale; }
    public boolean estaEnKnockback() { return this.enKnockback; }
}


