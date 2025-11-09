package network;

import network.paquetes.PaqueteConexion;
import network.paquetes.PaqueteRed;
import network.paquetes.entidades.PaqueteImpacto;
import network.paquetes.entidades.PaquetePowerUp;
import network.paquetes.partida.*;
import network.paquetes.personaje.*;
import network.paquetes.utilidad.PaqueteMensaje;
import partida.offline.ConfiguracionPartidaOffline;
import partida.online.ConfiguracionPartidaOnline;

import java.io.IOException;
import java.net.*;

public class ClientThread extends Thread {

    private DatagramSocket socket;
    private final int puertoServidor = 5555;
    private final String ipServidorStr = "127.0.0.1";
    private InetAddress ipServidor;
    private volatile boolean finalizar = false;

    private final ConfiguracionPartidaOnline configJugador;
    private final GameControllerEventos controlador;
    private int idJugador = -1;

    public ClientThread(GameControllerEventos controlador, ConfiguracionPartidaOnline configJugador) {
        this.controlador = controlador;
        this.configJugador = configJugador;

        try {
            ipServidor = InetAddress.getByName(ipServidorStr);
            socket = new DatagramSocket();
            socket.setSoTimeout(0);

            enviarPaquete(new PaqueteConexion());
            System.out.println("[CLIENTE] Intentando conectar con el servidor...");
        } catch (SocketException | UnknownHostException e) {
            System.err.println("[CLIENTE] Error iniciando socket: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("[CLIENTE] Hilo de cliente iniciado.");
        while (!finalizar) {
            try {
                byte[] buffer = new byte[4096];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                byte[] data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());
                PaqueteRed paquete = PaqueteRed.deserializar(data);

                procesarPaquete(paquete);

            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                if (!finalizar) System.err.println("[CLIENTE] Error al recibir paquete: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("[CLIENTE] Error procesando paquete: " + e.getMessage());
            }
        }
        System.out.println("[CLIENTE] Hilo detenido.");
    }

    private void procesarPaquete(PaqueteRed paquete) {
        if (paquete == null) return;

        switch (paquete.getTipo()) {

            case CONEXION -> {
                PaqueteConexion p = (PaqueteConexion) paquete;
                if (p.esAprobado()) {
                    controlador.conectar(p.getIdJugador());
                    idJugador = p.getIdJugador();
                } else {
                    System.err.println("[CLIENTE] Conexión rechazada por servidor.");
                    finalizar = true;
                }
            }

            case MENSAJE -> {
                PaqueteMensaje msg = (PaqueteMensaje) paquete;
                System.out.println("[CLIENTE] MSG: " + msg.mensaje);

                if (msg.mensaje.equals("LISTO")) {
                    System.out.println("[CLIENTE] Enviando configuración al servidor...");
                    String configStr = configJugador.toNetworkString();
                    enviarPaquete(new PaqueteConfiguracion(idJugador, configStr));
                }
            }

            case INICIO_PARTIDA -> {
                PaqueteInicioPartida p = (PaqueteInicioPartida) paquete;

                ConfiguracionPartidaOffline configFinal = ConfiguracionPartidaOffline.desdeString(p.configString);

                controlador.iniciarPartida(configFinal, p.spawns, p.jugadorId);
            }

            case CAMBIO_TURNO -> {
                PaqueteCambioTurno p = (PaqueteCambioTurno) paquete;
                controlador.actualizarTurno(p.jugadorId, p.tiempoRestante);
                controlador.forzarPersonajeActivo(p.jugadorId, p.personajeIndex);
            }

            case ESTADO_PARTIDA -> {
                PaqueteEstadoPartida p = (PaqueteEstadoPartida) paquete;
                controlador.sincronizarEstado(p);
            }
            case DISPARO -> {
                PaqueteDisparar p = (PaqueteDisparar) paquete;
                controlador.disparar(p.numJugador, p.angulo, p.potencia);
            }

            case POWER_UP -> {
                PaquetePowerUp p = (PaquetePowerUp) paquete;
                controlador.generarPowerUp(p.x, p.y);
            }

            case FIN_PARTIDA -> {
                PaqueteFinPartida p = (PaqueteFinPartida) paquete;
                controlador.finalizarPartida(p.ganador);
            }
            case MOVER -> {
                PaqueteMover p = (PaqueteMover) paquete;
                controlador.mover(p.numJugador, p.direccion);
            }

            case SALTAR -> {
                PaqueteSaltar p = (PaqueteSaltar) paquete;
                controlador.saltar(p.numJugador);
            }

            case APUNTAR -> {
                PaqueteApuntar p = (PaqueteApuntar) paquete;
                controlador.apuntar(p.numJugador, p.direccion);
            }

            case CAMBIAR_MOVIMIENTO -> {
                PaqueteCambioMovimiento p = (PaqueteCambioMovimiento) paquete;
                controlador.cambiarMovimiento(p.numJugador, p.indiceMovimiento);
            }

            case IMPACTO -> {
                PaqueteImpacto p = (PaqueteImpacto) paquete;
                controlador.impactoProyectil(p.x, p.y, p.danio, p.destruye);
            }
            case DESCONEXION -> controlador.volverAlMenu();

            default -> System.out.println("[CLIENTE] Paquete no manejado: " + paquete.getTipo());
        }
    }

    public void enviarPaquete(PaqueteRed paquete) {
        try {
            byte[] data = paquete.serializar();
            DatagramPacket packet = new DatagramPacket(data, data.length, ipServidor, puertoServidor);
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("[CLIENTE] Error enviando paquete: " + e.getMessage());
        }
    }

    public void terminar() {
        finalizar = true;
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception ignored) {}
        interrupt();
    }
}
