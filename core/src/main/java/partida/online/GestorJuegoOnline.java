package partida.online;

import Fisicas.Fisica;
import Gameplay.Gestores.Logicos.*;
import Gameplay.Movimientos.Movimiento;
import com.badlogic.gdx.Gdx;
import com.principal.Jugador;
import entidades.personajes.Personaje;
import entradas.ControlesJugador;
import network.ClientThread;
import partida.GestorJuegoBase;
import java.util.List;

public final class GestorJuegoOnline extends GestorJuegoBase {

    private final ClientThread clientThread;
    private final int numJugador;

    public GestorJuegoOnline(List<Jugador> jugadores,
                             GestorColisiones col,
                             GestorProyectiles proy,
                             GestorSpawn spawn,
                             Fisica fisica,
                             int tiempoPorTurno,
                             int frecuenciaPowerUps,
                             ClientThread clientThread,
                             int numJugador) {
        super(jugadores, col, proy, spawn, fisica, tiempoPorTurno, frecuenciaPowerUps);
        this.clientThread = clientThread;
        this.numJugador = numJugador;
    }

    @Override
    public void procesarEntradaJugador(ControlesJugador control, float delta) {
        if (control == null || gestorTurno.isEnTransicion()) return;

        Personaje activo = getPersonajeActivo();
        if (activo == null || activo.isTurnoTerminado()) return;

        control.procesarEntrada();

        if (control.getX() != 0) {
            clientThread.sendMessage("Mover:" + numJugador + ":" + control.getX());
        }

        if (control.getSaltar()) {
            clientThread.sendMessage("Saltar:" + numJugador);
        }

        if (control.getApuntarDir() != 0) {
            clientThread.sendMessage("Apuntar:" + numJugador + ":" + control.getApuntarDir());
        }

        clientThread.sendMessage("CambioMovimiento:" + numJugador + ":" + control.getMovimientoSeleccionado());

        if (control.getDisparoPresionado()) {
            clientThread.sendMessage("Disparar:" + numJugador + ":0:0");
        }

        control.resetDisparoLiberado();
    }

    public void moverRemoto(int numJugador, float dir) {
        Personaje p = getPersonaje(numJugador);
        if (p != null && p.getActivo()) p.mover(dir, Gdx.graphics.getDeltaTime());
    }

    public void saltarRemoto(int numJugador) {
        Personaje p = getPersonaje(numJugador);
        if (p != null && p.getActivo()) p.saltar();
    }

    public void apuntarRemoto(int numJugador, int direccion) {
        Personaje p = getPersonaje(numJugador);
        if (p != null && p.getActivo()) p.apuntar(direccion);
    }

    public void dispararRemoto(int numJugador, float angulo, float potencia) {
        Personaje p = getPersonaje(numJugador);
        if (p != null && p.getActivo()) {
            p.getMirilla().setAngulo(angulo);
            Movimiento mov = p.getMovimientoSeleccionado();
            if (mov != null) mov.ejecutar(p);
            p.terminarTurno();
        }
    }

    public void cambiarMovimientoRemoto(int numJugador, int indice) {
        Personaje p = getPersonaje(numJugador);
        if (p != null) p.setMovimientoSeleccionado(indice);
    }

    private Personaje getPersonaje(int numJugador) {
        if (numJugador < 0 || numJugador >= jugadores.size()) return null;
        return jugadores.get(numJugador).getPersonajeActivo();
    }
}
