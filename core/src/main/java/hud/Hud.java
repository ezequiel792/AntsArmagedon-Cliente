package hud;

import Fisicas.Camara;
import Gameplay.Gestores.Visuales.GestorAnimaciones;
import Gameplay.Gestores.GestorRutas;
import Gameplay.Movimientos.Movimiento;
import Gameplay.Movimientos.MovimientoRango;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import entidades.personajes.AtributosPersonaje.BarraCarga;
import entidades.personajes.Personaje;
import Gameplay.Gestores.Visuales.GestorAssets;
import utils.RecursosGlobales;
import java.util.List;

public final class Hud {

    private final BitmapFont fuenteContador, fuenteVida;
    private final GlyphLayout layout;

    private float stateTime = 0f;
    private int movimientoPrevio = -1;

    public Hud() {
        this.fuenteContador = GestorAssets.get(GestorRutas.FONT_CONTADOR, BitmapFont.class);
        this.fuenteVida = GestorAssets.get(GestorRutas.FONT_VIDA, BitmapFont.class);
        this.layout = new GlyphLayout();
    }

    public void mostrarAnimSelectorMovimientos(Personaje personaje, Camara camara, float delta) {
        if (!personaje.getActivo() || !personaje.isEnTurno()) return;

        List<Movimiento> movimientos = personaje.getMovimientos();
        if (movimientos.isEmpty()) return;

        int seleccionado = personaje.getMovimientos().indexOf(personaje.getMovimientoSeleccionado());
        if (seleccionado < 0 || seleccionado >= movimientos.size()) return;

        if (seleccionado != movimientoPrevio) {
            movimientoPrevio = seleccionado;
            stateTime = 0f;
        } else {
            stateTime += delta;
        }

        Movimiento mov = movimientos.get(seleccionado);
        TextureAtlas atlas = mov.getAtlas();
        if (atlas == null) return;

        Animation<TextureRegion> anim = GestorAnimaciones.obtener(atlas, mov.getNombreAnimacion(), 0.05f, false);
        if (anim == null) return;

        TextureRegion frame = anim.getKeyFrame(stateTime, false);
        if (frame == null) return;

        OrthographicCamera camera = camara.getCamera();
        Viewport viewport = camara.getViewport();

        float camX = camera.position.x;
        float camY = camera.position.y;

        float size = 32f;
        float padding = 8f;

        float baseX = camX - viewport.getWorldWidth() / 2f + 40f;
        float baseY = camY - viewport.getWorldHeight() / 2f + 40f;

        float x = baseX + seleccionado * (size + padding);
        float y = baseY;

        RecursosGlobales.batch.draw(frame, x - 4f, y - 4f, size + 8f, size + 8f);
    }

    public void mostrarVida(Personaje personaje) {
        if (!personaje.getActivo()) return;

        String texto = String.valueOf(personaje.getVida());
        layout.setText(fuenteVida, texto);

        float posX = personaje.getX() + personaje.getSprite().getWidth() / 2f - layout.width / 2f;
        float posY = personaje.getY() + personaje.getSprite().getHeight() + 20f;
        float offset = 1f;

        Color original = fuenteVida.getColor().cpy();

        fuenteVida.setColor(Color.BLACK);
        fuenteVida.draw(RecursosGlobales.batch, texto, posX - offset, posY - offset);
        fuenteVida.draw(RecursosGlobales.batch, texto, posX + offset, posY - offset);
        fuenteVida.draw(RecursosGlobales.batch, texto, posX - offset, posY + offset);
        fuenteVida.draw(RecursosGlobales.batch, texto, posX + offset, posY + offset);

        fuenteVida.setColor(personaje.getIdJugador() == 0 ? Color.BLUE : Color.RED);
        fuenteVida.draw(RecursosGlobales.batch, texto, posX, posY);

        fuenteVida.setColor(original);
    }


    public void mostrarContador(float contador, Camara camara) {
        String texto = String.format("%.2f", contador);
        layout.setText(fuenteContador, texto);

        Color original = fuenteContador.getColor().cpy();

        OrthographicCamera camera = camara.getCamera();
        Viewport viewport = camara.getViewport();

        float camX = camera.position.x;
        float camY = camera.position.y;

        float offsetX = viewport.getWorldWidth() / 2f - 200f;
        float offsetY = viewport.getWorldHeight() / 2f - 50f;

        fuenteContador.setColor(Color.WHITE);
        fuenteContador.draw(RecursosGlobales.batch, texto, camX + offsetX, camY + offsetY);

        fuenteContador.setColor(original);
    }

    public void mostrarBarraCarga(Personaje personaje) {
        if (!personaje.getActivo() || !personaje.isDisparando()) return;

        Movimiento mov = personaje.getMovimientoSeleccionado();
        if (!(mov instanceof MovimientoRango)) return;

        BarraCarga barra = personaje.getBarraCarga();
        if (barra.getCargaActual() <= 0f) return;

        float x = personaje.getX();
        float y = personaje.getY() - 7f;
        float ancho = personaje.getSprite().getWidth();
        float alto = 5f;

        barra.render(x, y, ancho, alto);
    }

    public void dispose() { }
}
