package hud;

import Gameplay.Gestores.GestorAudio;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import Gameplay.Gestores.Visuales.GestorAssets;
import java.util.Arrays;
import java.util.function.Consumer;

public enum FabricaBotones {

    JUGAR("jugar_up", "jugar_over", "jugar_down"),
    OPCIONES("opciones_up", "opciones_over", "opciones_down"),
    SALIR("salir_up", "salir_over", "salir_down"),
    TUTORIAL("tutorial_up", "tutorial_over", "tutorial_down"),
    VOLVER("volver_up", "volver_over", "volver_down"),
    REANUDAR("reanudar_up", "reanudar_over", "reanudar_down"),
    SONIDO("sonido_up", "sonido_over", "sonido_down"),
    MUTE("mute_up", "mute_over", "mute_down"),
    OPC1("opc_up", "opc_over", "opc_down"),
    RANDOM("random_up", "random_over", "random_down");

    private final String up;
    private final String over;
    private final String down;

    FabricaBotones(String up, String over, String down) {
        this.up = up;
        this.over = over;
        this.down = down;
    }

    public ImageButton crearBoton(String direccionAtlas, String direccionSonido, Runnable evento) {
        TextureAtlas atlas = GestorAssets.get(direccionAtlas, TextureAtlas.class);
        Sound sonido = GestorAssets.get(direccionSonido, Sound.class);
        ImageButton boton = new ImageButton(crearEstilo(atlas));
        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (sonido != null) GestorAudio.playSFX(sonido);
                if (evento != null) evento.run();
            }
        });
        return boton;
    }

    public static ImageButton crearBotonCiclico(
        String direccionAtlas,
        String direccionSonido,
        String[] nombresUp,
        String[] nombresOver,
        Consumer<Integer> eventoCambio
    ) {
        TextureAtlas atlas = GestorAssets.get(direccionAtlas, TextureAtlas.class);
        Sound sonido = GestorAssets.get(direccionSonido, Sound.class);

        TextureRegionDrawable[] arrUp = Arrays.stream(nombresUp)
            .map(r -> new TextureRegionDrawable(atlas.findRegion(r)))
            .toArray(TextureRegionDrawable[]::new);

        TextureRegionDrawable[] arrOver = Arrays.stream(nombresOver)
            .map(r -> new TextureRegionDrawable(atlas.findRegion(r)))
            .toArray(TextureRegionDrawable[]::new);

        ImageButton boton = new ImageButton(new ImageButton.ImageButtonStyle());
        boton.getStyle().imageUp = arrUp[0];
        boton.getStyle().imageOver = arrOver[0];
        boton.setDebug(true);

        final int[] indice = {0};
        boton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                indice[0] = (indice[0] + 1) % arrUp.length;
                boton.getStyle().imageUp = arrUp[indice[0]];
                boton.getStyle().imageOver = arrOver[indice[0]];
                if (sonido != null) GestorAudio.playSFX(sonido);
                if (eventoCambio != null) eventoCambio.accept(indice[0]);
            }
        });
        return boton;
    }

    public static ImageButton crearBotonHormiga(
        String direccionAtlas,
        String direccionSonido,
        Consumer<Integer> callbackCambio) {
        TextureAtlas atlas = GestorAssets.get(direccionAtlas, TextureAtlas.class);
        Sound sonido = GestorAssets.get(direccionSonido, Sound.class);

        TextureRegionDrawable[] up = {
            new TextureRegionDrawable(atlas.findRegion("Cuadro_HO_Up")),
            new TextureRegionDrawable(atlas.findRegion("Cuadro_HG_Up")),
            new TextureRegionDrawable(atlas.findRegion("Cuadro_HE_Up"))
        };
        TextureRegionDrawable[] over = {
            new TextureRegionDrawable(atlas.findRegion("Cuadro_HO_Over")),
            new TextureRegionDrawable(atlas.findRegion("Cuadro_HG_Over")),
            new TextureRegionDrawable(atlas.findRegion("Cuadro_HE_Over"))
        };
        TextureRegionDrawable vacio = new TextureRegionDrawable(atlas.findRegion("Cuadro_Vacio"));

        ImageButton boton = new ImageButton(new ImageButton.ImageButtonStyle());
        boton.getStyle().imageUp = up[0];
        boton.getStyle().imageOver = over[0];

        final int[] indice = {0};
        final boolean[] visible = {true};

        boton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    if (!visible[0]) visible[0] = true;
                    else indice[0] = (indice[0] + 1) % up.length;

                    boton.getStyle().imageUp = up[indice[0]];
                    boton.getStyle().imageOver = over[indice[0]];

                    if (callbackCambio != null)
                        callbackCambio.accept(indice[0]);
                } else if (button == Input.Buttons.RIGHT) {
                    visible[0] = false;
                    boton.getStyle().imageUp = vacio;
                    boton.getStyle().imageOver = vacio;

                    if (callbackCambio != null)
                        callbackCambio.accept(-1);
                }

                if (sonido != null) GestorAudio.playSFX(sonido);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        if (callbackCambio != null) callbackCambio.accept(0);

        return boton;
    }

    private ImageButton.ImageButtonStyle crearEstilo(TextureAtlas atlas) {
        ImageButton.ImageButtonStyle estilo = new ImageButton.ImageButtonStyle();
        estilo.up = new TextureRegionDrawable(atlas.findRegion(up));
        estilo.over = new TextureRegionDrawable(atlas.findRegion(over));
        if (down != null)
            estilo.down = new TextureRegionDrawable(atlas.findRegion(down));
        return estilo;
    }
}
