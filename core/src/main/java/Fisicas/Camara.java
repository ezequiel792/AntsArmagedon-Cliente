package Fisicas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import entidades.personajes.Personaje;

public final class Camara {

    private OrthographicCamera camera;
    private Viewport viewport;

    private float mapWidth;
    private float mapHeight;

    private static final float LERP_FACTOR = 0.1f;
    private static final float TIEMPO_FIJO_IMPACTO = 1.5f;

    public Camara(float worldWidth, float worldHeight, float mapWidth, float mapHeight) {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(worldWidth, worldHeight, camera);

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);
        camera.update();
    }

    public void update(float x, float y) {
        float halfWidth = viewport.getWorldWidth() / 2f;
        float halfHeight = viewport.getWorldHeight() / 2f;

        float targetX = MathUtils.clamp(x, halfWidth, mapWidth - halfWidth);
        float targetY = MathUtils.clamp(y, halfHeight, mapHeight - halfHeight);

        Vector3 pos = camera.position;
        pos.x += (targetX - pos.x) * LERP_FACTOR;
        pos.y += (targetY - pos.y) * LERP_FACTOR;

        camera.update();
    }

    public void seguirPosicion(float x, float y) {
        update(x, y);
    }

    public void seguirPersonaje(Personaje personaje) {
        float centroX = personaje.getX() + personaje.getSprite().getWidth() / 2f;
        float centroY = personaje.getY() + personaje.getSprite().getHeight() / 2f;
        update(centroX, centroY);
    }

    public OrthographicCamera getCamera() { return camera; }
    public Viewport getViewport() { return viewport; }
}

