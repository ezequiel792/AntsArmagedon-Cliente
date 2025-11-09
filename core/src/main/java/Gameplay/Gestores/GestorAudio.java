package Gameplay.Gestores;

import Gameplay.Gestores.Visuales.GestorAssets;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public final class GestorAudio {

    private GestorAudio() { }

    public static float volumenMusica = 0.01f;
    public static float volumenSFX    = 0.5f;

    private static Music musica;

    public static void iniciarMusica() {
        musica = GestorAssets.get(GestorRutas.MUSICA_JUEGO, Music.class);
        musica.setLooping(true);
        musica.setVolume(volumenMusica);
        musica.play();
    }

    public static void setVolumenMusica(float valor) {
        volumenMusica = MathUtils.clamp(valor, 0f, 1f);
        if (musica != null) musica.setVolume(volumenMusica);
    }

    public static void setVolumenSFX(float valor) {
        volumenSFX = MathUtils.clamp(valor, 0f, 1f);
    }

    public static void playSFX(Sound sonido) {
        sonido.play(volumenSFX);
    }
}
