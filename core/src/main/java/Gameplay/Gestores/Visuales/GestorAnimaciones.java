package Gameplay.Gestores.Visuales;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.Map;

public final class GestorAnimaciones {

    private GestorAnimaciones() { }

    private static final Map<String, Animation<TextureRegion>> cache = new HashMap<>();

    public static Animation<TextureRegion> obtener(TextureAtlas atlas, String nombreBase, float duracionFrame, boolean loop) {
        String atlasId = atlas.toString();
        String key = atlasId + "_" + nombreBase + "_" + duracionFrame + "_" + loop;

        if (cache.containsKey(key)) return cache.get(key);

        Array<TextureRegion> frames = new Array<>(atlas.findRegions(nombreBase));
        if (frames == null || frames.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron frames para: " + nombreBase);
        }

        Animation.PlayMode modo = loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL;
        Animation<TextureRegion> anim = new Animation<>(duracionFrame, frames, modo);
        cache.put(key, anim);
        return anim;
    }

    public static void limpiar() { cache.clear(); }
    public static int cantidadCacheadas() { return cache.size(); }
}
