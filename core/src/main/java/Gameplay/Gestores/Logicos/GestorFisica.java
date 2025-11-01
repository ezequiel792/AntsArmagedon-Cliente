package Gameplay.Gestores.Logicos;

import Fisicas.Colisionable;
import Fisicas.Fisica;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import entidades.Entidad;
import entidades.personajes.Personaje;
import entidades.proyectiles.Granada;
import entidades.proyectiles.Proyectil;
import utils.Constantes;

public final class GestorFisica {

    private final Fisica fisica;
    private final GestorColisiones gestorColisiones;

    private final Vector2 tmpInicio = new Vector2();
    private final Vector2 tmpFin = new Vector2();
    private final Vector2 tmpImpacto = new Vector2();

    public GestorFisica(Fisica fisica, GestorColisiones gestorColisiones) {
        this.fisica = fisica;
        this.gestorColisiones = gestorColisiones;
    }

    public void aplicarFisica(Entidad entidad, float delta) {
        Rectangle hitbox = entidad.getHitbox();
        Vector2 velocidad = entidad.getVelocidad();

        boolean sobreAlgo = gestorColisiones.verificarSobreAlgo(entidad);
        entidad.setSobreAlgo(sobreAlgo);

        if (!sobreAlgo) fisica.aplicarGravedad(velocidad, delta);

        limitarVelocidad(velocidad);

        float nuevaX = hitbox.x + velocidad.x * delta;
        if (gestorColisiones.verificarMovimiento(entidad, nuevaX, hitbox.y)) {
            hitbox.x = nuevaX;
        } else {
            velocidad.x = 0;
        }

        float nuevaY = hitbox.y + velocidad.y * delta;
        if (gestorColisiones.verificarMovimiento(entidad, hitbox.x, nuevaY)) {
            hitbox.y = nuevaY;
        } else {
            if (velocidad.y < 0) {
                hitbox.y = gestorColisiones.buscarYsuelo(hitbox, 5);
                sobreAlgo = true;
            }
            velocidad.y = 0;
        }

        entidad.setVelocidad(velocidad);
        entidad.setPosicion(hitbox.x, hitbox.y);
        entidad.setSobreAlgo(sobreAlgo);
    }

    public Vector2 moverProyectilConRaycast(Proyectil proyectil, float delta, Personaje ignorar) {
        limitarVelocidad(proyectil.getVelocidadVector());

        tmpInicio.set(
            proyectil.getX() + proyectil.getHitbox().getWidth() / 2f,
            proyectil.getY() + proyectil.getHitbox().getHeight() / 2f
        );

        tmpFin.set(
            tmpInicio.x + proyectil.getVelocidadVector().x * delta,
            tmpInicio.y + proyectil.getVelocidadVector().y * delta
        );

        Colisionable impactado = gestorColisiones.verificarTrayectoria(tmpInicio, tmpFin, ignorar, proyectil);
        if (impactado != null) {
            proyectil.setImpacto(true);
            tmpImpacto.set(
                tmpFin.x - proyectil.getHitbox().getWidth() / 2f,
                tmpFin.y - proyectil.getHitbox().getHeight() / 2f
            );
            return tmpImpacto;
        }

        Vector2 impactoMapa = gestorColisiones.trayectoriaColisionaMapa(tmpInicio, tmpFin);
        if (impactoMapa != null) {
            proyectil.setImpacto(true);
            tmpImpacto.set(
                impactoMapa.x - proyectil.getHitbox().getWidth() / 2f,
                impactoMapa.y - proyectil.getHitbox().getHeight() / 2f
            );
            return tmpImpacto;
        }

        proyectil.setImpacto(false);
        tmpImpacto.set(
            proyectil.getX() + proyectil.getVelocidadVector().x * delta,
            proyectil.getY() + proyectil.getVelocidadVector().y * delta
        );
        return tmpImpacto;
    }

    public Vector2 moverGranadaConRaycast(Granada granada, float delta, Personaje ignorar) {
        limitarVelocidad(granada.getVelocidadVector());

        float startX = granada.getX() + granada.getHitbox().width / 2f;
        float startY = granada.getY() + granada.getHitbox().height / 2f;

        float endX = startX + granada.getVelocidadVector().x * delta;
        float endY = startY + granada.getVelocidadVector().y * delta;

        granada.setImpacto(false);

        Vector2 inicioX = new Vector2(startX, startY);
        Vector2 finX = new Vector2(endX, startY);
        if (gestorColisiones.verificarTrayectoria(inicioX, finX, ignorar, granada) != null
            || gestorColisiones.trayectoriaColisionaMapa(inicioX, finX) != null) {
            granada.setImpacto(true);
            granada.getVelocidadVector().x *= -granada.getCoeficienteRebote();
        } else {
            granada.setX(granada.getX() + granada.getVelocidadVector().x * delta);
        }

        Vector2 inicioY = new Vector2(granada.getX() + granada.getHitbox().width / 2f, startY);
        Vector2 finY = new Vector2(granada.getX() + granada.getHitbox().width / 2f, endY);
        if (gestorColisiones.verificarTrayectoria(inicioY, finY, ignorar, granada) != null
            || gestorColisiones.trayectoriaColisionaMapa(inicioY, finY) != null) {
            granada.setImpacto(true);
            granada.getVelocidadVector().y *= -granada.getCoeficienteRebote();
        } else {
            granada.setY(granada.getY() + granada.getVelocidadVector().y * delta);
        }

        tmpImpacto.set(granada.getX(), granada.getY());
        return tmpImpacto;
    }

    private void limitarVelocidad(Vector2 velocidad) {
        if (velocidad.x > Constantes.VEL_MAX_HORIZONTAL) {
            velocidad.x = Constantes.VEL_MAX_HORIZONTAL;
        } else if (velocidad.x < -Constantes.VEL_MAX_HORIZONTAL) {
            velocidad.x = -Constantes.VEL_MAX_HORIZONTAL;
        }

        if (velocidad.y > Constantes.VEL_MAX_VERTICAL) {
            velocidad.y = Constantes.VEL_MAX_VERTICAL;
        } else if (velocidad.y < -Constantes.VEL_MAX_VERTICAL) {
            velocidad.y = -Constantes.VEL_MAX_VERTICAL;
        }
    }

    public Fisica getFisica() { return this.fisica; }
}

