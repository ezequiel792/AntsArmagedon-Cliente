package network;

import com.badlogic.gdx.math.Vector2;
import partida.ConfiguracionPartida;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {

    private DatagramSocket socket;
    private final int serverPort = 5555;
    private final String ipServerStr = "127.0.0.1";
    private InetAddress ipServer;
    private volatile boolean end = false;

    private final GameController gameController;

    public ClientThread(GameController gameController) {
        this.gameController = gameController;
        try {
            ipServer = InetAddress.getByName(ipServerStr);
            socket = new DatagramSocket();
            socket.setSoTimeout(0);
        } catch (SocketException | UnknownHostException e) {
            System.err.println("[CLIENTE] Error iniciando socket: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (!end) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);
                socket.receive(packet);
                processMessage(packet);
            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                if (!end)
                    System.err.println("[CLIENTE] Error al recibir: " + e.getMessage());
            }
        }
        System.out.println("[CLIENTE] Hilo finalizado.");
    }

    private void processMessage(DatagramPacket packet) {
        String msg = new String(packet.getData(), 0, packet.getLength()).trim();
        System.out.println("[CLIENTE] Mensaje recibido: " + msg);

        try {
            String[] parts = msg.split(":");
            String cmd = parts[0];

            switch (cmd) {
                case "Connected" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    gameController.connect(numJugador);
                }

                case "Start" -> {
                    System.out.println("[CLIENTE] Recibido Start → enviando configuración automática...");
                    ConfiguracionPartida config = new ConfiguracionPartida();
                    config.normalizarEquipos();
                    sendMessage("CONFIG:" + config.toNetworkString());
                }

                case "StartGame" -> {
                    if (parts.length < 2) {
                        System.err.println("[CLIENTE] StartGame recibido sin datos.");
                        return;
                    }

                    String[] data = parts[1].split("\\|", 2);
                    ConfiguracionPartida config = ConfiguracionPartida.desdeString(data[0]);
                    List<Vector2> spawns = new ArrayList<>();

                    if (data.length > 1 && !data[1].isEmpty()) {
                        String[] puntos = data[1].split(";");
                        for (String p : puntos) {
                            String[] xy = p.split(",");
                            if (xy.length == 2) {
                                spawns.add(new Vector2(
                                    Float.parseFloat(xy[0]),
                                    Float.parseFloat(xy[1])
                                ));
                            }
                        }
                    }

                    gameController.startGame(config, spawns);
                }

                case "Disconnect" -> gameController.backToMenu();

                case "UpdateTurno" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    float tiempoRestante = Float.parseFloat(parts[2]);
                    gameController.updateTurno(numJugador, tiempoRestante);
                }

                case "ChangeTurn" -> {
                    int nuevoTurno = Integer.parseInt(parts[1]);
                    gameController.changeTurn(nuevoTurno);
                }

                case "Timeout" -> gameController.timeOut();

                case "Mover" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    float dir = Float.parseFloat(parts[2]);
                    gameController.mover(numJugador, dir);
                }

                case "Saltar" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    gameController.saltar(numJugador);
                }

                case "Apuntar" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    int direccion = Integer.parseInt(parts[2]);
                    gameController.apuntar(numJugador, direccion);
                }

                case "Disparar" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    float angulo = Float.parseFloat(parts[2]);
                    float potencia = Float.parseFloat(parts[3]);
                    gameController.disparar(numJugador, angulo, potencia);
                }

                case "CambioMovimiento" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    int indice = Integer.parseInt(parts[2]);
                    gameController.cambiarMovimiento(numJugador, indice);
                }

                case "Impacto" -> {
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    int danio = Integer.parseInt(parts[3]);
                    boolean destruye = Boolean.parseBoolean(parts[4]);
                    gameController.impactoProyectil(x, y, danio, destruye);
                }

                case "Danio" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    int idPersonaje = Integer.parseInt(parts[2]);
                    int danio = Integer.parseInt(parts[3]);
                    float fuerzaX = Float.parseFloat(parts[4]);
                    float fuerzaY = Float.parseFloat(parts[5]);
                    gameController.personajeRecibeDanio(numJugador, idPersonaje, danio, fuerzaX, fuerzaY);
                }

                case "Muerte" -> {
                    int numJugador = Integer.parseInt(parts[1]);
                    int idPersonaje = Integer.parseInt(parts[2]);
                    gameController.personajeMuere(numJugador, idPersonaje);
                }

                case "EndGame" -> {
                    int ganador = Integer.parseInt(parts[1]);
                    gameController.endGame(ganador);
                }

                default -> System.out.println("[CLIENTE] Comando desconocido: " + msg);
            }

        } catch (Exception e) {
            System.err.println("[CLIENTE] Error procesando mensaje: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (socket == null || socket.isClosed()) return;
        try {
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, ipServer, serverPort);
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("[CLIENTE] Error enviando mensaje: " + e.getMessage());
        }
    }

    public void terminate() {
        end = true;
        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (Exception ignored) {}
        interrupt();
    }
}
