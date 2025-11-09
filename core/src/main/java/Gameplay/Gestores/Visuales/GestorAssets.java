package Gameplay.Gestores.Visuales;

import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class GestorAssets {

    private GestorAssets() { }

    private static final AssetManager assetManager = new AssetManager();

    public static void load() {

        assetManager.load("explosion.png", Texture.class);
        assetManager.load(GestorRutas.ATLAS_EXPLOSION, TextureAtlas.class);

        //Sonidos
        assetManager.load(GestorRutas.SONIDO_CLICK_BOTON, Sound.class);
        assetManager.load(GestorRutas.SONIDO_CLICK_HORMIGA, Sound.class);
        assetManager.load(GestorRutas.SONIDO_EXPLOSION, Sound.class);
        assetManager.load(GestorRutas.MUSICA_JUEGO, Music.class);

        // Botones
        assetManager.load(GestorRutas.ATLAS_BOTONES, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_CUADRO_PERSONAJES, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_OPCIONES, TextureAtlas.class);

        // Fondos
        assetManager.load(GestorRutas.FONDO_PANTALLA, Texture.class);
        assetManager.load(GestorRutas.FONDO_JUEGO, Texture.class);
        assetManager.load(GestorRutas.GAME_OVER, Texture.class);

        // Mapas
        assetManager.load(GestorRutas.MAPA_1, Texture.class);
        assetManager.load(GestorRutas.MAPA_2, Texture.class);
        assetManager.load(GestorRutas.MAPA_3, Texture.class);
        assetManager.load(GestorRutas.MAPA_4, Texture.class);
        assetManager.load(GestorRutas.MAPA_5, Texture.class);
        assetManager.load(GestorRutas.MAPA_6, Texture.class);

        // Movimientos
        assetManager.load(GestorRutas.ATLAS_MOVIMIENTO_ROCA, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_MOVIMIENTO_NECTAR, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_MOVIMIENTO_GRANADA, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_MOVIMIENTO_PASAR, TextureAtlas.class);

        // Proyectiles
        assetManager.load(GestorRutas.ROCA, Texture.class);
        assetManager.load(GestorRutas.NECTAR, Texture.class);
        assetManager.load(GestorRutas.GRANADA, Texture.class);

        // Fuentes
        assetManager.load(GestorRutas.FONT_CONTADOR, BitmapFont.class);
        assetManager.load(GestorRutas.FONT_VIDA, BitmapFont.class);

        // PowerUps
        assetManager.load(GestorRutas.CAJA_VIDA, Texture.class);
        assetManager.load(GestorRutas.ATLAS_CAJA_VIDA, TextureAtlas.class);

        // Mira
        assetManager.load(GestorRutas.ATLAS_MIRA, TextureAtlas.class);

        // Prueba
        assetManager.load(GestorRutas.ATLAS_PRUEBA, TextureAtlas.class);

        // Personajes
        assetManager.load(GestorRutas.HORMIGA_OBRERA, Texture.class);
        assetManager.load(GestorRutas.HORMIGA_GUERRERA, Texture.class);
        assetManager.load(GestorRutas.HORMIGA_EXPLORADORA, Texture.class);

        // Atlas Obrera
        assetManager.load(GestorRutas.ATLAS_HO_IDLE, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HO_WALKING, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HO_JUMPING, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HO_DAÑO, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HO_MUERTE, TextureAtlas.class);

        // Atlas Exploradora
        assetManager.load(GestorRutas.ATLAS_HE_IDLE, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HE_MUERTE, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HE_DAÑO, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HE_JUMPING, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HE_WALKING, TextureAtlas.class);

        // Atlas Guerrera
        assetManager.load(GestorRutas.ATLAS_HG_IDLE, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HG_MUERTE, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HG_DAÑO, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HG_JUMPING, TextureAtlas.class);
        assetManager.load(GestorRutas.ATLAS_HG_WALKING, TextureAtlas.class);

        // Barra carga
        assetManager.load(GestorRutas.PNG_MARCO_BARRA_CARGA, Texture.class);
        assetManager.load(GestorRutas.PNG_RELLENO_BARRA_CARGA, Texture.class);

        // Otros
        assetManager.load(GestorRutas.TUTORIAL_IMAGEN, Texture.class);

        assetManager.finishLoading();
    }

    public static boolean update() { return assetManager.update(); }
    public static float getProgress() { return assetManager.getProgress(); }
    public static <T> T get(String path, Class<T> type) { return assetManager.get(path, type); }
    public static void dispose() { assetManager.dispose(); }
}
