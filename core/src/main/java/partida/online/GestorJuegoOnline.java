package partida.online;

import Fisicas.Fisica;
import Fisicas.Mapa;
import Gameplay.Gestores.Logicos.*;
import Gameplay.Movimientos.Movimiento;
import com.badlogic.gdx.Gdx;
import com.principal.Jugador;
import entidades.PowerUps.CajaVida;
import entidades.personajes.Personaje;
import entradas.ControlesJugador;
import network.ClientThread;
import network.paquetes.personaje.*;
import network.paquetes.utilidad.DatosJuego;
import partida.clases_base.GestorJuegoBase;
import utils.RecursosGlobales;

import java.util.List;

public final class GestorJuegoOnline extends GestorJuegoBase {

    private final ClientThread clientThread;
    private final int numJugador;

    public GestorJuegoOnline(
        List<Jugador> jugadores,
        GestorColisiones col,
        GestorProyectiles proy,
        GestorSpawn spawn,
        Fisica fisica,
        int tiempoPorTurno,
        int frecuenciaPowerUps,
        ClientThread clientThread,
        int numJugador
    ) {
        super(jugadores, col, proy, spawn, fisica, tiempoPorTurno, frecuenciaPowerUps);
        this.clientThread = clientThread;
        this.numJugador = numJugador;
    }

    @Override
    public void actualizar(float delta, Mapa mapa) {
        revisarPersonajesMuertos();
        gestorEntidades.actualizar(delta);
        gestorProyectiles.actualizar(delta);
    }

    @Override
    public void procesarEntradaJugador(ControlesJugador control, float delta) {
        if (control == null || gestorTurno.isEnTransicion()) return;

        Personaje activo = getPersonajeActivo();
        if (activo == null || activo.isTurnoTerminado()) return;

        control.procesarEntrada();

        if (control.getX() != 0f) {
            clientThread.enviarPaquete(new PaqueteMover(numJugador, control.getX()));
        }

        if (control.getSaltar()) {
            clientThread.enviarPaquete(new PaqueteSaltar(numJugador));
        }

        if (control.getApuntarDir() != 0) {
            clientThread.enviarPaquete(new PaqueteApuntar(numJugador, control.getApuntarDir()));
        }

        clientThread.enviarPaquete(new PaqueteCambioMovimiento(numJugador, control.getMovimientoSeleccionado()));

        if (control.getDisparoPresionado()) {
            float angulo = 0f;
            float potencia = 0f;
            clientThread.enviarPaquete(new PaqueteDisparar(numJugador, angulo, potencia));
            control.resetDisparoLiberado();
        }
    }

    @Override
    public void generarPowerUpRemoto(float x, float y) {
        if (gestorEntidades == null) {
            System.err.println("[CLIENTE] Error: gestorEntidades no inicializado.");
            return;
        }
        CajaVida powerUp = new CajaVida(x, y, gestorColisiones);
        gestorEntidades.agregarEntidad(powerUp);
        System.out.println("[CLIENTE] PowerUp remoto creado en (" + x + ", " + y + ")");
    }

    @Override
    public void moverRemoto(int numJugador, float dir) {
        Personaje p = getPersonaje(numJugador);
        if (p != null && p.getActivo()) p.mover(dir, Gdx.graphics.getDeltaTime());
    }

    @Override
    public void saltarRemoto(int numJugador) {
        Personaje p = getPersonaje(numJugador);
        if (p != null && p.getActivo()) p.saltar();
    }

    @Override
    public void apuntarRemoto(int numJugador, int direccion) {
        Personaje p = getPersonaje(numJugador);
        if (p != null && p.getActivo()) p.apuntar(direccion);
    }

    @Override
    public void dispararRemoto(int numJugador, float angulo, float potencia) {
        Personaje p = getPersonaje(numJugador);
        if (p != null && p.getActivo()) {
            p.getMirilla().setAngulo(angulo);
            Movimiento mov = p.getMovimientoSeleccionado();
            if (mov != null) mov.ejecutar(p);
            p.terminarTurno();
        }
    }

    @Override
    public void cambiarMovimientoRemoto(int numJugador, int indice) {
        Personaje p = getPersonaje(numJugador);
        if (p != null) p.setMovimientoSeleccionado(indice);
    }

    private Personaje getPersonaje(int numJugador) {
        if (numJugador < 0 || numJugador >= jugadores.size()) return null;
        return jugadores.get(numJugador).getPersonajeActivo();
    }

    @Override
    public void forzarPersonajeActivo(int jugadorId, int personajeIndex) {
        if (jugadorId < 0 || jugadorId >= jugadores.size()) return;

        limpiarTurnosCliente();

        Jugador j = jugadores.get(jugadorId);
        j.setIndicePersonajeActivo(personajeIndex);

        Personaje p = j.getPersonajeActivo();
        if (p != null) {
            p.setEnTurno(true);
            p.reiniciarTurno();
            RecursosGlobales.camaraJuego.seguirPersonaje(p);
        }
    }

    public void seguirPersonajeActivo() {
        Personaje p = getPersonajeActivo();
        if (p != null) RecursosGlobales.camaraJuego.seguirPersonaje(p);
    }


    private void limpiarTurnosCliente() {
        for (Jugador j : jugadores) {
            for (Personaje per : j.getPersonajes()) {
                per.setEnTurno(false);
            }
        }
    }

    public void sincronizarDesdeServidor(List<DatosJuego.EntidadDTO> entidades, List<DatosJuego.ProyectilDTO> proyectiles) {
        gestorEntidades.sincronizarRemotos(entidades);
        gestorProyectiles.sincronizarRemotos(proyectiles);
    }

}
