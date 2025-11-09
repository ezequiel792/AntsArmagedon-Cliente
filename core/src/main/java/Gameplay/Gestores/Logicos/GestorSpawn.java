package Gameplay.Gestores.Logicos;

import Fisicas.Mapa;
import com.badlogic.gdx.math.Vector2;
import entidades.Entidad;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class GestorSpawn {

    private final Mapa mapa;
    private final Random random = new Random();

    private final List<Vector2> puntosValidos = new ArrayList<>();
    private final List<Vector2> puntosPowerUps = new ArrayList<>();

    private int saltoColumnas = 2;
    private int aireExtraSuperior = 6;
    private float alturaSpawnExtra = 20f;
    private int margenLateral = 20;

    private float distanciaMinimaEntreSpawns = 40f;

    private float alturaCaidaPowerUp = 150f;

    public GestorSpawn(Mapa mapa) {
        this.mapa = mapa;
    }

    public void precalcularPuntosValidos(float anchoEntidad, float altoEntidad) {
        puntosValidos.clear();
        puntosPowerUps.clear();

        int anchoMapa = mapa.getWidth();
        int altoMapa = mapa.getHeight();

        int inicioX = (int) (margenLateral + anchoEntidad / 2);
        int finX = (int) (anchoMapa - margenLateral - anchoEntidad / 2);
        float distanciaMinimaEntrePuntos = anchoEntidad * 2f;

        for (int x = inicioX; x < finX; x += saltoColumnas) {
            for (int y = altoMapa - 2; y >= altoEntidad; y--) {
                if (mapa.esSolido(x, y)) {
                    int alturaLibre = calcularAlturaLibre(
                        x, y + 1, (int) (altoEntidad + aireExtraSuperior * 0.6f)
                    );

                    if (!esAreaApta(x, y, anchoEntidad)) continue;
                    if (alturaLibre < altoEntidad * 0.5f) continue;

                    float ySpawn = y + altoEntidad * 0.8f + alturaSpawnExtra;
                    ySpawn = Math.min(ySpawn, altoMapa - altoEntidad / 2f);
                    ySpawn = Math.max(altoEntidad / 2f, ySpawn);

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

                    if (!muyCerca) puntosValidos.add(nuevo);
                    break;
                }
            }
        }

        Collections.shuffle(puntosValidos, random);

        float distanciaPowerUp = distanciaMinimaEntreSpawns * 0.5f;
        for (Vector2 punto : puntosValidos) {
            boolean muyCerca = false;
            for (Vector2 existente : puntosPowerUps) {
                if (existente.dst(punto) < distanciaPowerUp) {
                    muyCerca = true;
                    break;
                }
            }
            if (!muyCerca) puntosPowerUps.add(punto);
        }
    }

    private boolean esAreaApta(int x, int y, float anchoEntidad) {
        int margenIrregularidad = 3;
        for (int dx = -((int) anchoEntidad / 2); dx <= ((int) anchoEntidad / 2); dx++) {
            int alturaBajo = 0;
            while (y - alturaBajo > 0 && !mapa.esSolido(x + dx, y - alturaBajo)) alturaBajo++;
            if (alturaBajo > margenIrregularidad) return false;
        }
        return true;
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

    public Vector2 generarSpawnEntidad(Entidad entidad) {
        if (puntosValidos.isEmpty()) precalcularPuntosValidos(entidad.getWidth(), entidad.getHeight());
        if (puntosValidos.isEmpty()) return null;

        int index = random.nextInt(puntosValidos.size());
        return puntosValidos.remove(index);
    }

    public Vector2 generarSpawnEntidad(float ancho, float alto) {
        if (puntosPowerUps.isEmpty()) precalcularPuntosValidos(ancho, alto);
        if (puntosPowerUps.isEmpty()) return null;

        return puntosPowerUps.get(random.nextInt(puntosPowerUps.size()));
    }

    public Vector2 generarSpawnPowerUp(Entidad entidad) {
        float ancho = (entidad != null) ? entidad.getWidth() : 20f;
        float alto = (entidad != null) ? entidad.getHeight() : 20f;

        Vector2 base = generarSpawnEntidad(ancho, alto);
        if (base == null) return null;

        float yCielo = mapa.getHeight() - alturaCaidaPowerUp;;
        return new Vector2(base.x, yCielo);
    }

    public void precalcularPuntosValidos() { precalcularPuntosValidos(50f, 50f); }
    public void setDistanciaMinimaEntreSpawns(float distancia) { this.distanciaMinimaEntreSpawns = distancia; }
    public void setAlturaCaidaPowerUp(float altura) { this.alturaCaidaPowerUp = altura; }
    public List<Vector2> getPuntosValidos() { return Collections.unmodifiableList(puntosValidos); }
    public List<Vector2> getPuntosPowerUps() { return Collections.unmodifiableList(puntosPowerUps); }
}
