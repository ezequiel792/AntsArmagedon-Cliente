package screens;

import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.principal.AntsArmageddon;
import Gameplay.Gestores.Visuales.GestorAssets;
import utils.Constantes;
import utils.RecursosGlobales;

public abstract class ScreenMenus implements Screen {

    protected final AntsArmageddon juego;
    protected Stage escenario;
    protected FitViewport viewport;
    protected OrthographicCamera camara;
    protected Sprite spriteFondo;

    public ScreenMenus(AntsArmageddon juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        camara = new OrthographicCamera();
        viewport = new FitViewport(Constantes.RESOLUCION_ANCHO, Constantes.RESOLUCION_ALTO, camara);
        escenario = new Stage(viewport);
        Gdx.input.setInputProcessor(escenario);

        Texture textura = GestorAssets.get(GestorRutas.FONDO_PANTALLA, Texture.class);
        spriteFondo = new Sprite(textura);
        spriteFondo.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        construirUI();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        RecursosGlobales.batch.setProjectionMatrix(camara.combined);
        RecursosGlobales.batch.begin();
        spriteFondo.draw(RecursosGlobales.batch);
        RecursosGlobales.batch.end();

        escenario.act(delta);
        escenario.draw();
    }

    @Override
    public void resize(int ancho, int alto) {
        viewport.update(ancho, alto, true);
        spriteFondo.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        camara.update();
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override public void dispose() { escenario.dispose(); }

    protected abstract void construirUI();
}
