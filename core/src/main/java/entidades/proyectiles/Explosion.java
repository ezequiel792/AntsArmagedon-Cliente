package entidades.proyectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Explosion {

    private float x, y;
    private float stateTime = 0f;
    private boolean activa = true;
    private final Animation<TextureRegion> anim;

    public Explosion(float x, float y, Animation<TextureRegion> anim) {
        this.x = x;
        this.y = y;
        this.anim = anim;
    }

    public void update(float delta) {
        if (!activa) return;
        stateTime += delta;
        if (anim.isAnimationFinished(stateTime)) {
            activa = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (!activa) return;
        TextureRegion frame = anim.getKeyFrame(stateTime);

        float ancho = frame.getRegionWidth();
        float alto = frame.getRegionHeight();
        batch.draw(frame, x - ancho / 2f, y - alto / 2f);
    }

    public boolean isActiva() { return this.activa; }
}


