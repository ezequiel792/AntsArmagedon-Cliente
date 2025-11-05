package partida;

import Fisicas.Camara;
import Fisicas.Fisica;
import Fisicas.Mapa;
import Gameplay.Gestores.GestorTurno;
import Gameplay.Gestores.Logicos.*;
import Gameplay.Movimientos.MovimientoMelee;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.principal.Jugador;
import entidades.Entidad;
import entidades.PowerUps.CajaVida;
import entidades.PowerUps.PowerUp;
import entidades.personajes.Personaje;
import entradas.ControlesJugador;
import hud.Hud;
import screens.GameOverScreen;
import utils.RecursosGlobales;

import java.util.ArrayList;
import java.util.List;

public abstract class GestorJuegoBase {

    protected final List<Jugador> jugadores = new ArrayList<>();
    protected final GestorColisiones gestorColisiones;
    protected final GestorProyectiles gestorProyectiles;
    protected final GestorEntidades gestorEntidades;
    protected final GestorFisica gestorFisica;
    protected final GestorSpawn gestorSpawn;
    protected final GestorTurno gestorTurno;

    protected int turnosCompletados;
    protected int frecuenciaPowerUps;

    public GestorJuegoBase(List<Jugador> jugadores,
                           GestorColisiones gestorColisiones,
                           GestorProyectiles gestorProyectiles,
                           GestorSpawn gestorSpawn,
                           Fisica fisica,
                           int tiempoPorTurno,
                           int frecuenciaPowerUps) {

        this.jugadores.addAll(jugadores);
        this.gestorColisiones = gestorColisiones;
        this.gestorProyectiles = gestorProyectiles;
        this.gestorSpawn = gestorSpawn;
        this.frecuenciaPowerUps = frecuenciaPowerUps;

        this.gestorTurno = new GestorTurno(new ArrayList<>(jugadores), tiempoPorTurno);
        this.gestorFisica = new GestorFisica(fisica, gestorColisiones);
        this.gestorEntidades = new GestorEntidades(gestorFisica, gestorColisiones);

        for (Jugador jugador : this.jugadores) {
            for (Personaje personaje : jugador.getPersonajes()) {
                this.gestorEntidades.agregarEntidad(personaje);
            }
        }
    }

    public void actualizar(float delta, Mapa mapa) {
        int turnoAntes = gestorTurno.getTurnoActual();

        gestorTurno.correrContador(delta);
        revisarPersonajesMuertos();

        gestorEntidades.actualizar(delta);
        gestorProyectiles.actualizar(delta);

        int turnoActual = gestorTurno.getTurnoActual();
        if (turnoActual != turnoAntes) onCambioTurno(turnoAntes, turnoActual);
    }

    protected void onCambioTurno(int turnoAnterior, int turnoActual) {
        turnosCompletados++;

        Jugador anterior = jugadores.get(turnoAnterior);
        anterior.getPersonajeActivo().setEnTurno(false);

        Jugador actual = jugadores.get(turnoActual);
        actual.getPersonajeActivo().setEnTurno(true);

        if (turnosCompletados > 0 && turnosCompletados % frecuenciaPowerUps == 0)
            generarPowerUp();
    }

    protected void revisarPersonajesMuertos() {
        List<Jugador> muertos = new ArrayList<>();

        for (Jugador j : jugadores) {
            List<Personaje> aEliminar = j.getPersonajes().stream()
                .filter(p -> !p.getActivo())
                .toList();

            aEliminar.forEach(j::removerPersonaje);
            if (!j.estaVivo()) muertos.add(j);
        }

        jugadores.removeAll(muertos);
        if (jugadores.size() <= 1) indicarGanador();
    }

    protected void indicarGanador() {
        String msg = jugadores.isEmpty()
            ? "Empate"
            : "Jugador " + (jugadores.get(0).getIdJugador() + 1) + " gana!";
        GestorScreen.setScreen(new GameOverScreen(GestorScreen.returnJuego(), msg));
    }

    protected void generarPowerUp() {
        PowerUp dummy = new CajaVida(0, 0, gestorColisiones);
        Vector2 pos = gestorSpawn.generarSpawnPowerUp(dummy);
        if (pos != null) {
            agregarEntidad(new CajaVida(pos.x, pos.y, gestorColisiones));
            System.out.println("[GESTOR BASE] PowerUp generado en " + pos);
        }
    }

    public void agregarEntidad(Entidad entidad) {
        gestorEntidades.agregarEntidad(entidad);
    }

    //Metodo para mostrar los golpes de los personajes melee, no integrado
    public void renderDebug(ShapeRenderer shapeRenderer, Camara camara) {
        gestorEntidades.renderDebug(shapeRenderer, camara);
        for (Jugador j : jugadores)
            for (Personaje p : j.getPersonajes())
                if (p.getMovimientoSeleccionado() instanceof MovimientoMelee mm)
                    mm.renderGolpe(shapeRenderer, Gdx.graphics.getDeltaTime());
    }

    public void renderEntidades(SpriteBatch batch) { gestorEntidades.render(batch); }
    public void renderProyectiles(SpriteBatch batch) { gestorProyectiles.render(batch); }

    public void renderPersonajes(Hud hud) {
        for (Jugador j : jugadores)
            for (Personaje p : j.getPersonajes()) {
                p.render(RecursosGlobales.batch);
                hud.mostrarVida(p);
            }
    }

    public void dispose() {
        gestorProyectiles.dispose();
        gestorEntidades.dispose();
    }

    public List<Jugador> getJugadores() { return jugadores; }
    public GestorTurno getGestorTurno() { return gestorTurno; }
    public GestorProyectiles getGestorProyectiles() { return gestorProyectiles; }
    public GestorColisiones getGestorColisiones() { return gestorColisiones; }

    public Personaje getPersonajeActivo() {
        Jugador j = gestorTurno.getJugadorActivo();
        return j != null ? j.getPersonajeActivo() : null;
    }

    public abstract void procesarEntradaJugador(ControlesJugador control, float delta);

    public Jugador getJugadorActivo() { return gestorTurno.getJugadorActivo(); }
    public int getTurnoActual() { return gestorTurno.getTurnoActual(); }
    public float getTiempoActual() { return gestorTurno.getTiempoActual(); }

    public void moverRemoto(int numJugador, float direccion) {}
    public void saltarRemoto(int numJugador) {}
    public void apuntarRemoto(int numJugador, int direccion) {}
    public void dispararRemoto(int numJugador, float angulo, float potencia) {}
    public void cambiarMovimientoRemoto(int numJugador, int indice) {}
    public void procesarImpactoRemoto(float x, float y, int danio, boolean destruye) {}

}
