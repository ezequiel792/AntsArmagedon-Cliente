package Gameplay.Gestores.Logicos;

import Fisicas.Camara;
import Fisicas.Fisica;
import Fisicas.Mapa;
import Gameplay.Gestores.GestorTurno;
import Gameplay.Movimientos.Movimiento;
import Gameplay.Movimientos.MovimientoMelee;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.principal.Jugador;
import entidades.Entidad;
import entidades.personajes.Personaje;
import entidades.PowerUps.CajaVida;
import entidades.PowerUps.PowerUp;
import entradas.ControlesJugador;
import hud.Hud;
import screens.GameOverScreen;
import utils.RecursosGlobales;
import java.util.ArrayList;
import java.util.List;

public final class GestorJuego {

    private final List<Jugador> jugadores = new ArrayList<>();
    private final GestorColisiones gestorColisiones;
    private final GestorProyectiles gestorProyectiles;
    private final GestorEntidades gestorEntidades;
    private final GestorFisica gestorFisica;
    private final GestorSpawn gestorSpawn;
    private final GestorTurno gestorTurno;

    private int turnoTranscurriendo;
    private int turnosCompletados;
    private int frecuenciaPowerUps;

    public GestorJuego(List<Jugador> jugadores, GestorColisiones gestorColisiones, GestorProyectiles gestorProyectiles,
                       GestorSpawn gestorSpawn, Fisica fisica, int tiempoPorTurno, int frecuenciaPowerUps) {

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

        int turnoTranscurriendo = gestorTurno.getTurnoActual();

        if (turnoTranscurriendo != turnoAntes) {
            turnosCompletados++;

            Jugador jugadorAnterior = jugadores.get(turnoAntes);
            jugadorAnterior.getPersonajeActivo().setEnTurno(false);

            Jugador jugadorActual = jugadores.get(turnoTranscurriendo);
            jugadorActual.getPersonajeActivo().setEnTurno(true);

            if (turnosCompletados > 0 && turnosCompletados % frecuenciaPowerUps == 0) {
                generarPowerUp();
            }
        }

    }

    public void procesarEntradaJugador(ControlesJugador control, float delta) {
        if (control == null) return;
        if (gestorTurno.isEnTransicion())  return;

        Personaje activo = getPersonajeActivo(); if (activo == null) return;

        if (activo.isTurnoTerminado()) return;

        control.procesarEntrada();

        if (activo.isDisparando()) {

            if (control.getApuntarDir() != 0)
                activo.apuntar(control.getApuntarDir());

            if (control.getDisparoLiberado()) {
                activo.usarMovimiento();
                control.resetDisparoLiberado();
            }

            activo.actualizarDisparo(delta);
            return;
        }

        float x = control.getX();
        activo.mover(x, delta);

        if (control.getSaltar())
            activo.saltar();

        if (control.getApuntarDir() != 0)
            activo.apuntar(control.getApuntarDir());

        activo.setMovimientoSeleccionado(control.getMovimientoSeleccionado());
        if (control.getDisparoPresionado())
            activo.usarMovimiento();
    }

    private void revisarPersonajesMuertos() {
        List<Jugador> jugadoresSinPersonajes = new ArrayList<>();

        for (Jugador jugador : jugadores) {
            List<Personaje> muertos = new ArrayList<>();
            for (Personaje personaje : jugador.getPersonajes()) {
                if (!personaje.getActivo()) muertos.add(personaje);
            }
            for (Personaje personaje : muertos) jugador.removerPersonaje(personaje);
            if (!jugador.estaVivo()) jugadoresSinPersonajes.add(jugador);
        }

        jugadores.removeAll(jugadoresSinPersonajes);

        if (jugadores.size() <= 1) indicarGanador();

    }

    private void indicarGanador() {
        String mensajeFinal;

        if (jugadores.isEmpty()) {
            mensajeFinal = "Empate";
        } else {
            Jugador ganador = jugadores.get(0);
            mensajeFinal = "Jugador " + (ganador.getIdJugador() + 1) + " gana!";
        }

        GestorScreen.setScreen(new GameOverScreen(GestorScreen.returnJuego(), mensajeFinal));
    }

    private void generarPowerUp() {
            Vector2 spawnPower = gestorSpawn.generarSpawnPowerUp(8f);
            if (spawnPower != null) {
                PowerUp nuevoPower = new CajaVida(spawnPower.x, spawnPower.y, gestorColisiones);
                agregarEntidad(nuevoPower);
                System.out.println("PowerUp generado en: " + spawnPower);
            }
    }

    public void renderDebug(ShapeRenderer shapeRenderer, Camara camara) {
        gestorEntidades.renderDebug(shapeRenderer, camara);

        //Para mostrar los movimientos melee momentaneamente
        for (Jugador j : jugadores) {
            for (Personaje p : j.getPersonajes()) {
                Movimiento m = p.getMovimientoSeleccionado();
                if (m instanceof MovimientoMelee mm) {
                    mm.renderGolpe(shapeRenderer, Gdx.graphics.getDeltaTime());
                }
            }
        }
    }

    public void renderEntidades(SpriteBatch batch) { gestorEntidades.render(batch); }
    public void renderProyectiles(SpriteBatch batch) { gestorProyectiles.render(batch); }

    public void renderPersonajes(Hud hud) {
        for (Jugador jugador : jugadores) {
            for (Personaje personaje : jugador.getPersonajes()) {
                personaje.render(RecursosGlobales.batch);
                hud.mostrarVida(personaje);
            }
        }
    }

    public Personaje getPersonajeActivo() {
        Jugador jugadorActivo = getJugadorActivo();
        return (jugadorActivo != null) ? jugadorActivo.getPersonajeActivo() : null;
    }

    public void agregarEntidad(Entidad entidad) {
        gestorEntidades.agregarEntidad(entidad);
    }

    public void dispose() {
        gestorProyectiles.dispose();
        gestorEntidades.dispose();
    }

    public Jugador getJugadorActivo() { return gestorTurno.getJugadorActivo(); }
    public int getTurnoActual() { return gestorTurno.getTurnoActual(); }
    public float getTiempoActual() { return gestorTurno.getTiempoActual(); }
    public List<Jugador> getJugadores() { return jugadores; }
    public GestorColisiones getGestorColisiones() { return gestorColisiones; }
    public GestorProyectiles getGestorProyectiles() { return this.gestorProyectiles; }
    public GestorTurno getGestorTurno() { return this.gestorTurno; }
}


