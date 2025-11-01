package entidades.personajes;

import Fisicas.Camara;
import Gameplay.Gestores.Logicos.GestorProyectiles;
import Gameplay.Movimientos.Movimiento;
import Gameplay.Movimientos.MovimientoRango;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import Gameplay.Gestores.Logicos.GestorColisiones;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import entidades.Entidad;
import entidades.personajes.AtributosPersonaje.BarraCarga;
import entidades.personajes.AtributosPersonaje.FisicaPersonaje;
import entidades.personajes.AtributosPersonaje.Mirilla;
import utils.Constantes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Personaje extends Entidad {

    public enum Estado {
        IDLE, WALK, JUMP, HIT, MUERTE;
    }

    private int idJugador;
    private Color colorJugador;

    protected Map<Estado, Animation<TextureRegion>> animaciones = new HashMap<>();
    protected Animation<TextureRegion> animActual;
    protected float stateTime = 0f;
    protected Estado estadoActual = Estado.IDLE;

    protected GestorProyectiles gestorProyectiles;
    protected Mirilla mirilla;
    protected List<Movimiento> movimientos;
    protected boolean direccion;
    protected int vida;
    protected float velocidadX;
    protected BarraCarga barraCarga;
    protected FisicaPersonaje fisicas;

    private int movimientoSeleccionado = 0;
    private boolean estaDisparando = false;

    private boolean enTurno = false;
    private boolean turnoTerminado = false;

    private float lastX = 0f;

    protected float fuerzaSalto;
    protected float peso;

    public Personaje(Texture textura, GestorColisiones gestorColisiones, GestorProyectiles
                         gestorProyectiles, float x, float y, int vida, float velocidadMovimiento,
                     float fuerzaSalto, float peso, int idJugador) {
        super(x, y, textura, gestorColisiones);

        this.gestorProyectiles = gestorProyectiles;
        this.textura = textura;
        this.sprite = new Sprite(textura);
        this.sprite.setPosition(x, y);

        this.lastX = x;

        this.vida = vida;
        this.activo = true;
        this.barraCarga = new BarraCarga();

        this.velocidadX = velocidadMovimiento;
        if (this.velocidadX > Constantes.VEL_MAX_HORIZONTAL) {
            this.velocidadX = Constantes.VEL_MAX_HORIZONTAL;
        } else if (this.velocidadX < 0f) {
            this.velocidadX = 0f;
        }

        this.fuerzaSalto = fuerzaSalto;
        if (this.fuerzaSalto > Constantes.VEL_MAX_VERTICAL) {
            this.fuerzaSalto = Constantes.VEL_MAX_VERTICAL;
        } else if (this.fuerzaSalto < 0f) {
            this.fuerzaSalto = 0f;
        }

        this.peso = peso;

        this.mirilla = new Mirilla(this);
        this.movimientos = new ArrayList<>();
        this.fisicas = new FisicaPersonaje(this, gestorColisiones);

        this.direccion = MathUtils.randomBoolean();

        if (!direccion && !sprite.isFlipX()) sprite.flip(true, false);
        else if (direccion && sprite.isFlipX()) sprite.flip(true, false);

        this.idJugador = idJugador;
        this.colorJugador = (idJugador == 0) ? Color.BLUE : Color.RED;

        inicializarMovimientos();
        inicializarAnimaciones();

        this.stateTime = MathUtils.random(0f, animActual.getAnimationDuration());

        float anchoSprite = sprite.getWidth();
        float altoSprite  = sprite.getHeight();

        float factorAncho = 0.7f;
        float factorAlto  = 0.85f;

        float anchoHitbox = anchoSprite * factorAncho;
        float altoHitbox  = altoSprite * factorAlto;

        float offsetX = (anchoSprite - anchoHitbox) / 2f;
        float offsetY = (altoSprite - altoHitbox) * 0.35f;

        this.hitbox.set(x + offsetX, y + offsetY, anchoHitbox, altoHitbox);

    }

    protected abstract void inicializarAnimaciones();

    @Override
    public final void actualizar(float delta) {
        if (!activo) return;

        stateTime += delta;
        fisicas.actualizar(delta);
        mirilla.update(delta);
        mirilla.actualizarPosicion();

        if (estadoActual == Estado.MUERTE) {
            if (animActual.isAnimationFinished(stateTime)) desactivar();
            return;
        }

        if (estadoActual == Estado.HIT && fisicas.estaEnKnockback()) {
            return;
        }

        float dx = Math.abs(getX() - lastX);
        boolean caminando = dx > 0.1f && getSobreAlgo();

        if (vida <= 0) cambiarEstado(Estado.MUERTE);

        else if (!getSobreAlgo()) cambiarEstado(Estado.JUMP);

        else if (caminando) cambiarEstado(Estado.WALK);

        else cambiarEstado(Estado.IDLE);

        if (!enTurno || estadoActual == Estado.MUERTE || estadoActual == Estado.HIT) {
            ocultarMirilla();
        }

        else if (estaDisparando) mostrarMirilla();

        else if (!caminando && getSobreAlgo()) {
            mostrarMirilla();
        } else {
            ocultarMirilla();
        }

        lastX = getX();
    }

    @Override
    public final void render(SpriteBatch batch) {
        if (!activo) return;

        TextureRegion frame = animActual.getKeyFrame(stateTime, true);

        if (direccion && frame.isFlipX()) frame.flip(true, false);
        else if (!direccion && !frame.isFlipX()) frame.flip(true, false);

        batch.draw(frame, sprite.getX(), sprite.getY());

        if (enTurno) mirilla.render(batch);
    }

    protected final void cambiarEstado(Estado nuevoEstado) {
        if (estadoActual != nuevoEstado) {
            estadoActual = nuevoEstado;
            animActual = animaciones.get(estadoActual);
            stateTime = 0f;
        }
    }

    public final void mover(float deltaX, float deltaTiempo) {
        if (!puedeActuar()) return;
        fisicas.moverHorizontal(deltaX, deltaTiempo);
    }

    public final void saltar() {
        if (!puedeActuar()) return;
        fisicas.saltar(fuerzaSalto);
    }

    public final void apuntar(int direccion) {
        if (!getSobreAlgo()) return;
        mirilla.mostrarMirilla();
        mirilla.cambiarAngulo(direccion);
    }

    public final void usarMovimiento() {

        if (!getSobreAlgo() || !activo) return;

        Movimiento movimiento = getMovimientoSeleccionado();
        if (movimiento == null) return;

        if (movimiento instanceof MovimientoRango movimientoRango) {

            if (estaDisparando) {
                float potencia = barraCarga.getCargaNormalizada();
                if (potencia > 0f) movimientoRango.ejecutar(this, potencia);

                barraCarga.reset();
                estaDisparando = false;
                terminarTurno();
                return;
            }

            barraCarga.start();
            estaDisparando = true;
            return;
        }

        movimiento.ejecutar(this);
        terminarTurno();
    }

    public final void actualizarDisparo(float delta) {
        if (estaDisparando) barraCarga.update(delta);
    }

    public final void recibirDanio(int danio, float fuerzaX, float fuerzaY) {
        float danoFinal = danio;

        if (danoFinal < 0) {
            danoFinal = 0;
        } else if (danoFinal > Constantes.DANO_MAXIMO) {
            danoFinal = Constantes.DANO_MAXIMO;
        }

        this.vida -= danoFinal;

        if (this.vida <= 0) {
            this.vida = 0;
            cambiarEstado(Estado.MUERTE);
            terminarTurno();
            return;
        }

        fisicas.aplicarKnockback(fuerzaX, fuerzaY);
        cambiarEstado(Estado.HIT);
    }

    public final void aumentarVida(int vidaRecogida) {
        if (vidaRecogida <= 0) return;
        this.vida += vidaRecogida;

        if (this.vida > Constantes.VIDA_MAXIMA) {
            this.vida = Constantes.VIDA_MAXIMA;
        }
    }


    public final float distanciaAlCentro(float x, float y) {
        float centroX = this.getX() + this.getSprite().getWidth() / 2f;
        float centroY = this.getY() + this.getSprite().getHeight() / 2f;
        float dx = centroX - x;
        float dy = centroY - y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public final void setMovimientoSeleccionado(int indice) {
        if (indice >= 0 && indice < this.movimientos.size()) this.movimientoSeleccionado = indice;
    }

    public final boolean puedeActuar() {
        return this.activo && !estaDisparando;
    }

    public final void setEnTurno(boolean enTurno) {
        this.enTurno = enTurno;
        if (!enTurno) ocultarMirilla();
    }

    public final boolean isEnTurno() { return this.enTurno; }
    public final void terminarTurno() {
        this.turnoTerminado = true;
        ocultarMirilla();
    }
    public final boolean isTurnoTerminado() { return this.turnoTerminado; }
    public final void reiniciarTurno() { this.turnoTerminado = false; }

    public final void mostrarMirilla() { this.mirilla.mostrarMirilla(); }
    public final void ocultarMirilla() { this.mirilla.ocultarMirilla(); }
    public final BarraCarga getBarraCarga() { return this.barraCarga; }
    public final int getDireccionMultiplicador() { return this.direccion ? -1 : 1; }
    public final int getVida() { return this.vida; }
    public final boolean getDireccion() { return this.direccion; }
    public final Mirilla getMirilla() { return this.mirilla; }
    public final List<Movimiento> getMovimientos() { return this.movimientos; }
    public final float getVelocidadX() { return this.velocidadX; }
    public final void setDireccion(boolean direccion) { this.direccion = direccion; }
    public final FisicaPersonaje getFisicas() { return this.fisicas;}
    public final boolean isDisparando() { return this.estaDisparando; }
    public final void setDisparando(boolean disparando) { this.estaDisparando = disparando; }
    public final int getIdJugador() { return this.idJugador; }
    public final float getFuerzaSalto() { return this.fuerzaSalto; }
    public final float getPeso() { return this.peso; }

    public final Movimiento getMovimientoSeleccionado() {
        if (movimientoSeleccionado < 0 || movimientoSeleccionado >= movimientos.size()) return null;
        return movimientos.get(movimientoSeleccionado);
    }

    protected abstract void inicializarMovimientos();
    @Override public final void dispose() {}

    public final void renderHitbox(ShapeRenderer shapeRenderer, Camara camara) {
        if (!activo) return;
        shapeRenderer.setProjectionMatrix(camara.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shapeRenderer.end();
    }
}

