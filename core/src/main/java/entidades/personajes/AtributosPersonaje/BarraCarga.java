package entidades.personajes.AtributosPersonaje;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import utils.RecursosGlobales;

public final class BarraCarga {

    private float cargaActual = 0f;
    private final float CARGA_MAXIMA = 1f;
    private final float TIEMPO_CARGA = 1.5f;
    private final float VELOCIDAD_CARGA = CARGA_MAXIMA / TIEMPO_CARGA;
    private boolean cargando = false;

    public BarraCarga() {}

    public void update(float delta) {
        if (cargando) {
            cargaActual += VELOCIDAD_CARGA * delta;
            if (cargaActual > CARGA_MAXIMA) cargaActual = CARGA_MAXIMA;
        }
    }

    public void render(float x, float y, float ancho, float alto) {
        ShapeRenderer sr = RecursosGlobales.shapeRenderer;

        sr.setProjectionMatrix(RecursosGlobales.camaraJuego.getCamera().combined);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.setColor(Color.GRAY);
        sr.rect(x, y, ancho, alto);

        sr.setColor(Color.GREEN);
        sr.rect(x, y, ancho * getCargaNormalizada(), alto);

        sr.end();
    }

    public void reset() {
        cargaActual = 0f;
        cargando = false;
    }

    public float getCargaNormalizada() { return this.cargaActual / CARGA_MAXIMA; }
    public void start() { this.cargando = true; }
    public void stop() { this.cargando = false; }
    public float getCargaActual() { return this.cargaActual; }
}
