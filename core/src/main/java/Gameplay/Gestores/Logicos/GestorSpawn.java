package Gameplay.Gestores.Logicos;

import Fisicas.Mapa;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class GestorSpawn {

    private final Mapa mapa;
    private final Random random = new Random();
    private final List<Vector2> puntosValidos = new ArrayList<>();

    private int saltoColumnas = 2;
    private int aireExtraSuperior = 6;
    private float alturaSpawnExtra = 20f;
    private int margenLateral = 20;

    public GestorSpawn(Mapa mapa) {
        this.mapa = mapa;
    }

    public void precalcularPuntosValidos(float anchoPersonaje, float altoPersonaje) {
        puntosValidos.clear();

        int anchoMapa = mapa.getWidth();
        int altoMapa = mapa.getHeight();

        int inicioX = (int) (margenLateral + anchoPersonaje / 2);
        int finX = (int) (anchoMapa - margenLateral - anchoPersonaje / 2);

        float distanciaMinimaEntrePuntos = anchoPersonaje * 2f;

        for (int x = inicioX; x < finX; x += saltoColumnas) {
            for (int y = altoMapa - 2; y >= altoPersonaje; y--) {
                if (mapa.esSolido(x, y)) {
                    int alturaLibre = calcularAlturaLibre(x, y + 1, (int) (altoPersonaje + aireExtraSuperior * 0.6f));

                    boolean areaApta = true;
                    int margenIrregularidad = 3;
                    for (int dx = -((int) anchoPersonaje / 2); dx <= ((int) anchoPersonaje / 2); dx++) {
                        int alturaBajo = 0;
                        while (y - alturaBajo > 0 && !mapa.esSolido(x + dx, y - alturaBajo)) alturaBajo++;
                        if (alturaBajo > margenIrregularidad) {
                            areaApta = false;
                            break;
                        }
                    }

                    if (alturaLibre >= altoPersonaje * 0.5f && areaApta) {
                        float ySpawn = y + altoPersonaje * 0.8f + alturaSpawnExtra;

                        ySpawn = Math.min(ySpawn, altoMapa - altoPersonaje / 2f);
                        ySpawn = Math.max(altoPersonaje / 2f, ySpawn);

                        Vector2 nuevo = new Vector2(
                            Math.max(inicioX, Math.min(x, finX)),
                            ySpawn
                        );

                        boolean muyCerca = false;
                        for (Vector2 existente : puntosValidos) {
                            if (existente.dst(nuevo) < distanciaMinimaEntrePuntos) {
                                muyCerca = true;
                                break;
                            }
                        }

                        if (!muyCerca) {
                            puntosValidos.add(nuevo);
                        }
                    }
                    break;
                }
            }
        }
        Collections.shuffle(puntosValidos, random);

        // 🧠 DEBUG
        if (!puntosValidos.isEmpty()) {
            float minX = Float.MAX_VALUE, maxX = 0, sumX = 0;
            for (Vector2 p : puntosValidos) {
                minX = Math.min(minX, p.x);
                maxX = Math.max(maxX, p.x);
                sumX += p.x;
            }
            float promedioX = sumX / puntosValidos.size();
            System.out.println("🟢 Puntos válidos de spawn: " + puntosValidos.size());
            System.out.println("   X mínima: " + minX + " | X máxima: " + maxX + " | Promedio X: " + promedioX);
        } else {
            System.out.println("⚠️ No se encontraron puntos válidos de spawn.");
        }
    }

    private int calcularAlturaLibre(int x, int yInicio, int maxAltura) {
        for (int i = 0; i < maxAltura; i++) {
            if (mapa.esSolido(x, yInicio + i) ||
                mapa.esSolido(x - 1, yInicio + i) ||
                mapa.esSolido(x + 1, yInicio + i)) {
                return i;
            }
        }
        return maxAltura;
    }

    private boolean esAreaLibre(int xCentro, int yBase, float ancho, float alto) {
        int mitadAncho = (int)(ancho / 2f);
        int altura = (int)alto;

        for (int dx = -mitadAncho; dx <= mitadAncho; dx++) {
            for (int dy = 0; dy < altura; dy++) {
                if (mapa.esSolido(xCentro + dx, yBase + dy)) return false;
            }
        }
        return true;
    }

    public Vector2 generarSpawnPersonaje(float anchoPersonaje, float altoPersonaje) {
        if (puntosValidos.isEmpty()) precalcularPuntosValidos(anchoPersonaje, altoPersonaje);
        if (puntosValidos.isEmpty()) return null;
        return puntosValidos.get(random.nextInt(puntosValidos.size()));
    }

    public List<Vector2> generarVariosSpawnsPersonajes(int cantidad, float ancho, float alto, float distanciaMinima) {
        if (puntosValidos.isEmpty()) precalcularPuntosValidos(ancho, alto);
        List<Vector2> seleccionados = new ArrayList<>();
        List<Vector2> disponibles = new ArrayList<>(puntosValidos);

        while (!disponibles.isEmpty() && seleccionados.size() < cantidad) {
            Vector2 candidato = disponibles.remove(random.nextInt(disponibles.size()));

            boolean muyCerca = false;
            for (Vector2 existente : seleccionados) {
                if (existente.dst(candidato) < distanciaMinima) {
                    muyCerca = true;
                    break;
                }
            }

            if (!muyCerca) seleccionados.add(candidato);
        }

        return seleccionados;
    }

    public Vector2 generarSpawnPowerUp(float anchoPowerUp) {
        int anchoMapa = mapa.getWidth();
        int altoMapa = mapa.getHeight();

        int maxIntentos = 300;

        for (int intento = 0; intento < maxIntentos; intento++) {
            float x = margenLateral + random.nextFloat() * (anchoMapa - 2 * margenLateral - anchoPowerUp);

            boolean tieneSueloDebajo = false;
            for (int y = altoMapa - 5; y > 0; y--) {
                if (mapa.esSolido((int) x, y)) {
                    tieneSueloDebajo = true;
                    break;
                }
            }

            if (!tieneSueloDebajo) continue;

            float ySpawn = altoMapa - 75f;
            //despues aumentar el - porque el mapa va a ser mas grande

            return new Vector2(x, ySpawn);
        }

        System.out.println("⚠️ No se encontró lugar para el PowerUp tras múltiples intentos.");
        return null;
    }

}

