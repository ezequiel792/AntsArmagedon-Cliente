package Gameplay.Movimientos;

import Gameplay.Gestores.Logicos.GestorProyectiles;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import entidades.personajes.Personaje;
import entidades.proyectiles.Proyectil;

public abstract class MovimientoRango extends Movimiento {

    protected float velocidadBase;
    protected GestorProyectiles gestorProyectiles;

    private static final float OFFSET_DISPARO = 13f;
    private static final float FACTOR_MIN_VEL = 0.5f;
    private static final float FACTOR_MAX_VEL = 2.0f;

    public MovimientoRango(String nombre, TextureAtlas atlas, String nombreAnimacion,
                           float velocidadBase, GestorProyectiles gestorProyectiles) {
        super(nombre, atlas, nombreAnimacion);
        this.velocidadBase = velocidadBase;
        this.gestorProyectiles = gestorProyectiles;
    }

    public void ejecutar(Personaje personaje, float potencia) {
        float angulo = personaje.getMirilla().getAnguloRad();

        float poscX = personaje.getX() + personaje.getSprite().getWidth() / 2f;
        float poscY = personaje.getY() + personaje.getSprite().getHeight() / 2f;

        float x = poscX + MathUtils.cos(angulo) * OFFSET_DISPARO * personaje.getDireccionMultiplicador();
        float y = poscY + MathUtils.sin(angulo) * OFFSET_DISPARO;

        float factorVelocidad = MathUtils.lerp(FACTOR_MIN_VEL, FACTOR_MAX_VEL, potencia * potencia);
        float velocidadFinal = velocidadBase * factorVelocidad;

        Proyectil proyectil = crearProyectil(x, y, angulo, velocidadFinal, personaje);
        gestorProyectiles.agregar(proyectil);
    }

    protected abstract Proyectil crearProyectil(float x, float y, float angulo, float velocidad, Personaje ejecutor);

    public float getVelocidadBase() { return velocidadBase; }
}
