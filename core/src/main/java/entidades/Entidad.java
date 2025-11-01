package entidades;

import Fisicas.Camara;
import Gameplay.Gestores.Logicos.GestorColisiones;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import Fisicas.Colisionable;
import com.badlogic.gdx.math.Vector2;

public abstract class Entidad implements Colisionable {

    protected float x, y;
    protected Rectangle hitbox;
    protected Sprite sprite;
    protected Texture textura;
    protected boolean sobreAlgo;
    protected boolean activo;
    protected Vector2 velocidad;
    protected GestorColisiones gestorColisiones;

    public Entidad(float x, float y, Texture textura, GestorColisiones gestorColisiones) {
        this.x = x;
        this.y = y;
        this.textura = textura;
        this.gestorColisiones = gestorColisiones;

        if (textura != null) {
            this.sprite = new Sprite(textura);
            this.sprite.setPosition(x, y);
        }

        float ancho = textura != null ? textura.getWidth() : 0;
        float alto = textura != null ? textura.getHeight() : 0;
        this.hitbox = new Rectangle(x, y, ancho, alto);

        this.activo = true;
        this.sobreAlgo = false;
        this.velocidad = new Vector2(0, 0);
    }

    public final void updateHitbox() { hitbox.setPosition(x, y); }

    public final void setPosicion(float x, float y) {
        this.x = x;
        this.y = y;
        updateHitbox();
        if (sprite != null) sprite.setPosition(x, y);
    }

    public abstract void actualizar(float delta);
    public abstract void render(SpriteBatch batch);

    public void dispose() {}

    @Override public final void desactivar() { this.activo = false; }
    @Override public final boolean getActivo() { return this.activo; }
    @Override public final Rectangle getHitbox() { return this.hitbox; }

    @Override public final Rectangle getHitboxPosicion(float x, float y) {
        return new Rectangle(x, y, hitbox.getWidth(), hitbox.getHeight());
    }

    public final float getX() { return x; }
    public final float getY() { return y; }
    public final void setX(float x) { this.x = x; }
    public final void setY(float y) { this.y = y; }
    public final Vector2 getVelocidad() { return this.velocidad; }
    public final void setVelocidad(Vector2 velocidad) { this.velocidad.set(velocidad); }
    public final float getWidth() { return this.hitbox.getWidth(); }
    public final float getHeight() { return this.hitbox.getHeight(); }
    public final boolean getSobreAlgo() { return this.sobreAlgo; }
    public final void setSobreAlgo(boolean sobreAlgo) { this.sobreAlgo = sobreAlgo; }
    public final Sprite getSprite() { return this.sprite; }

    public abstract void renderHitbox(ShapeRenderer shapeRenderer, Camara camara);
}

