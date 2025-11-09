package Gameplay.Gestores;

import com.principal.Jugador;
import entidades.personajes.Personaje;
import java.util.ArrayList;

public class GestorTurno {

    protected final float TIEMPO_POR_TURNO;
    protected int turnoActual = 0;
    protected float tiempoActual;
    protected boolean enTransicion = false;
    protected float tiempoTransicion = 0f;
    protected final float DURACION_TRANSICION = 4f;

    protected ArrayList<Jugador> jugadores;

    public GestorTurno(ArrayList<Jugador> jugadores, float tiempoPorTurno) {
        this.jugadores = jugadores;
        this.TIEMPO_POR_TURNO = tiempoPorTurno;
        this.tiempoActual = tiempoPorTurno;
        iniciarTurnoActual();
    }

    public void correrContador(float delta) {

        if (jugadores.get(turnoActual).getPersonajeActivo().isTurnoTerminado()) {
            iniciarTransicion();
            return;
        }

        if (enTransicion) {
            transicionarTurno(delta);
        } else {
            tiempoActual -= delta;
            if (tiempoActual <= 0) {
                iniciarTransicion();
                tiempoActual = 0;
            }
        }
    }


    private void iniciarTransicion() {
        this.enTransicion = true;
        this.tiempoTransicion = 0f;

        Personaje saliente = jugadores.get(turnoActual).getPersonajeActivo();
        saliente.setEnTurno(false);
        saliente.reiniciarTurno();
    }

    private void transicionarTurno(float delta){
        tiempoTransicion += delta;

        if(tiempoTransicion >= DURACION_TRANSICION){
            tiempoTransicion = 0;
            enTransicion = false;
            actualizarTurno();
        }
    }

    private void actualizarTurno() {
        this.turnoActual++;

        if(turnoActual >= jugadores.size()){
            this.turnoActual = 0;
        }

        Jugador j = jugadores.get(turnoActual);
        j.avanzarPersonaje();

        Personaje entrante = j.getPersonajeActivo();
        entrante.setEnTurno(true);
        entrante.reiniciarTurno();

        this.tiempoActual = TIEMPO_POR_TURNO;
    }

    public void iniciarTurnoActual() {
        if (!jugadores.isEmpty()) {
            Jugador jugador = jugadores.get(turnoActual);
            if (!jugador.getPersonajes().isEmpty()) {
                jugador.getPersonajeActivo().setEnTurno(true);
            }
        }
    }

    public void sincronizarTurno(int nuevoTurno, float tiempoRestante) {
        this.turnoActual = nuevoTurno;
        this.tiempoActual = tiempoRestante;
    }

    public void forzarCambioTurno(int nuevoTurno) {
        if (nuevoTurno < 0 || nuevoTurno >= jugadores.size()) return;

        Jugador actual = jugadores.get(turnoActual);
        if (actual.getPersonajeActivo() != null) {
            actual.getPersonajeActivo().setEnTurno(false);
            actual.getPersonajeActivo().reiniciarTurno();
        }

        this.turnoActual = nuevoTurno;
        Jugador siguiente = jugadores.get(turnoActual);
        if (siguiente.getPersonajeActivo() != null) {
            siguiente.getPersonajeActivo().setEnTurno(true);
            siguiente.getPersonajeActivo().reiniciarTurno();
        }

        this.tiempoActual = TIEMPO_POR_TURNO;
        this.enTransicion = false;
        this.tiempoTransicion = 0f;
    }

    public void forzarFinTurno() {
        if (enTransicion) return;

        Jugador actual = jugadores.get(turnoActual);
        if (actual.getPersonajeActivo() != null) {
            actual.getPersonajeActivo().terminarTurno();
            actual.getPersonajeActivo().setEnTurno(false);
        }

        iniciarTransicion();
    }

    public void setTurnoActual(int nuevoTurno) {
        this.turnoActual = nuevoTurno;
    }

    public void setTiempoRestante(float nuevoTiempo) {
        this.tiempoActual = nuevoTiempo;
    }

    public Jugador getJugadorActivo() { return jugadores.get(turnoActual); }
    public int getTurnoActual() { return this.turnoActual; }
    public float getTiempoActual() { return this.tiempoActual; }
    public boolean isEnTransicion() { return enTransicion; }
}
