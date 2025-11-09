package entidades.PowerUps;

import Fisicas.Camara;
import Gameplay.Gestores.Logicos.GestorColisiones;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import entidades.Entidad;
import entidades.personajes.Personaje;

public abstract class PowerUp extends Entidad {

    protected Rectangle areaRecoleccion;
    protected float extraArea = 5f;

    protected Animation<TextureRegion> anim;
    protected float stateTime = 0f;

    public PowerUp(float x, float y, TextureAtlas atlasPowerup,
                   String nombreAnimacion, GestorColisiones gestorColisiones) {
        super(x, y, null, gestorColisiones);

        this.velocidad.set(0, 0);

        Array<TextureAtlas.AtlasRegion> frames = atlasPowerup.findRegions(nombreAnimacion);

        anim = new Animation<>(0.35f, frames, Animation.PlayMode.LOOP);

        TextureRegion frameInicial = frames.first();
        sprite = new Sprite(frameInicial);

        hitbox = new Rectangle(x, y, frameInicial.getRegionWidth(), frameInicial.getRegionHeight());

        sprite.setPosition(x, y);

        areaRecoleccion = new Rectangle();
        actualizarAreaRecoleccion();
    }

    @Override
    public void actualizar(float delta) {
        if (!activo) return;

        stateTime += delta;

        TextureRegion frameActual = anim.getKeyFrame(stateTime, true);
        sprite.setRegion(frameActual);

        updateHitbox();
        actualizarAreaRecoleccion();

        Personaje personaje = gestorColisiones.buscarPersonajeEnArea(areaRecoleccion);
        if (personaje != null) {
            aplicarEfecto(personaje);
            desactivar();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!activo) return;
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    protected final void actualizarAreaRecoleccion() {
        areaRecoleccion.set(
            x - extraArea, y - extraArea,
            hitbox.width + 2 * extraArea,
            hitbox.height + 2 * extraArea
        );
    }

    public abstract void aplicarEfecto(Personaje personaje);

    @Override
    public final void renderHitbox(ShapeRenderer shapeRenderer, Camara camara) {
        if (!activo) return;

        shapeRenderer.setProjectionMatrix(camara.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(areaRecoleccion.x, areaRecoleccion.y,
            areaRecoleccion.width, areaRecoleccion.height);

        shapeRenderer.end();
    }
}
