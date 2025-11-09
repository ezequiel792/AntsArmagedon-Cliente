package entidades.personajes.tiposPersonajes;

import Gameplay.Gestores.Visuales.GestorAnimaciones;
import Gameplay.Gestores.Logicos.GestorColisiones;
import Gameplay.Gestores.Logicos.GestorProyectiles;
import Gameplay.Gestores.GestorRutas;
import Gameplay.Movimientos.Melee.Aranazo;
import Gameplay.Movimientos.Otros.PasarTurno;
import Gameplay.Movimientos.Rango.LanzaGranada;
import Gameplay.Movimientos.Rango.LanzaNectar;
import Gameplay.Movimientos.Rango.LanzaRoca;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import entidades.personajes.Personaje;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class HormigaGuerrera extends Personaje {

    public HormigaGuerrera(GestorColisiones gestorColisiones, GestorProyectiles gestorProyectiles,
                           float x, float y, int idJugador) {
        super(GestorAssets.get(GestorRutas.HORMIGA_GUERRERA, Texture.class), gestorColisiones,
            gestorProyectiles, x, y, 120, 150, 400, 10f,
            idJugador);
    }

    @Override
    protected void inicializarMovimientos() {
        //movimientos.add(new Aranazo(gestorColisiones));
        movimientos.add(new LanzaRoca(gestorProyectiles));
        movimientos.add(new LanzaNectar(gestorProyectiles));
        movimientos.add(new LanzaGranada(gestorProyectiles));
        movimientos.add(new PasarTurno());

    }

    @Override
    protected void inicializarAnimaciones() {

        TextureAtlas atlasIdle   = GestorAssets.get(GestorRutas.ATLAS_HG_IDLE, TextureAtlas.class);
        TextureAtlas atlasWalk   = GestorAssets.get(GestorRutas.ATLAS_HG_WALKING, TextureAtlas.class);
        TextureAtlas atlasJump   = GestorAssets.get(GestorRutas.ATLAS_HG_JUMPING, TextureAtlas.class);
        TextureAtlas atlasHit    = GestorAssets.get(GestorRutas.ATLAS_HG_DAÑO, TextureAtlas.class);
        TextureAtlas atlasMuerte = GestorAssets.get(GestorRutas.ATLAS_HG_MUERTE, TextureAtlas.class);

        Animation<TextureRegion> animIdle   = GestorAnimaciones.obtener(atlasIdle,   "HG_Idle", 0.25f, true);
        Animation<TextureRegion> animWalk   = GestorAnimaciones.obtener(atlasWalk,   "HG_Walking",    0.10f, true);
        Animation<TextureRegion> animJump   = GestorAnimaciones.obtener(atlasJump,   "HG_Jumping",    0.50f, false);
        Animation<TextureRegion> animHit    = GestorAnimaciones.obtener(atlasHit,    "HG_Daño",       999f,  false);
        Animation<TextureRegion> animMuerte = GestorAnimaciones.obtener(atlasMuerte, "HG_Muerte",     0.25f, false);

        animaciones.put(Estado.IDLE,   animIdle);
        animaciones.put(Estado.WALK,   animWalk);
        animaciones.put(Estado.JUMP,   animJump);
        animaciones.put(Estado.HIT,    animHit);
        animaciones.put(Estado.MUERTE, animMuerte);

        animActual = animaciones.get(Estado.IDLE);
        stateTime = MathUtils.random(0f, animActual.getAnimationDuration());
    }

}
