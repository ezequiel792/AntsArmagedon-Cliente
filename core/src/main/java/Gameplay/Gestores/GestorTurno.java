package Gameplay.Gestores;

import com.principal.Jugador;
import entidades.personajes.Personaje;
import java.util.ArrayList;

public final class GestorTurno {

    private final float TIEMPO_POR_TURNO;
    private int turnoActual = 0;
    private float tiempoActual;
    private boolean enTransicion = false;
    private float tiempoTransicion = 0f;
    private final float DURACION_TRANSICION = 4f;

    ArrayList<Jugador> jugadores;

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

    public Jugador getJugadorActivo() { return jugadores.get(turnoActual); }
    public int getTurnoActual() { return this.turnoActual; }
    public float getTiempoActual() { return this.tiempoActual; }
    public boolean isEnTransicion() { return enTransicion; }
}

