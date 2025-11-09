package screens;

import Gameplay.Gestores.GestorRutas;
import Gameplay.Gestores.Visuales.GestorAssets;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;

public final class TutorialScreen extends ScreenMenus {

    private BitmapFont fuente;

    public TutorialScreen(AntsArmageddon juego) { super(juego); }

    @Override
    protected void construirUI() {
        fuente = GestorAssets.get(GestorRutas.FONT_VIDA, BitmapFont.class);
        Label.LabelStyle estiloTitulo = new Label.LabelStyle(fuente, null);
        Label.LabelStyle estiloTexto = new Label.LabelStyle(fuente, null);

        Label titulo = new Label("TUTORIAL", estiloTitulo);

        String textoControles =
            "CONTROLES\n\n" +
                "<- / ->   Mover\n\n" +
                "ESPACIO   Saltar\n\n" +
                "W / S   Apuntar\n\n" +
                "1, 2, 3, 4...   Habilidad\n\n" +
                "Mantener Z para cargar\n" +
                "Soltar Z para disparar";

        Label lblControles = new Label(textoControles, estiloTexto);
        lblControles.setWrap(true);
        lblControles.setAlignment(Align.center);

        String textoReglas =
            "\n\nREGLAS\n\n" +
                "Juego por turnos.\n\n" +
                "En tu turno podés:\n" +
                "- Moverte\n" +
                "- Saltar\n" +
                "- Disparar\n" +
                "- Pasar turno\n\n" +
                "Cuando termina el tiempo,\n" +
                "el turno pasa al rival.\n\n" +
                "Gana quien elimina\n" +
                "todas las hormigas enemigas.";

        Label lblReglas = new Label(textoReglas, estiloTexto);
        lblReglas.setWrap(true);
        lblReglas.setAlignment(Align.center);

        ImageButton btnVolver = FabricaBotones.VOLVER.crearBoton(
            GestorRutas.ATLAS_BOTONES, GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irScreenAnterior()
        );

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(20);

        Table columnas = new Table();
        columnas.center();
        columnas.add(lblControles).width(330).padRight(40);
        columnas.add(lblReglas).width(330);

        table.add(titulo).padBottom(25).row();
        table.add(columnas).padBottom(80).row();
        table.add(btnVolver).padBottom(10).row();

        escenario.addActor(table);
    }
}
