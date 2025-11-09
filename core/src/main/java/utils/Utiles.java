package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.HashMap;

public final class Utiles {

    private Utiles() {}

    public static void descomponerAtlas(String atlasPath, String outputFolder) {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        HashMap<Texture, Pixmap> pixmaps = new HashMap<>();

        for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
            Pixmap fullPixmap;

            if (pixmaps.containsKey(region.getTexture())) {
                fullPixmap = pixmaps.get(region.getTexture());
            } else {
                region.getTexture().getTextureData().prepare();
                fullPixmap = region.getTexture().getTextureData().consumePixmap();
                pixmaps.put(region.getTexture(), fullPixmap);
            }

            Pixmap pixmapRegion = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), Pixmap.Format.RGBA8888);
            pixmapRegion.drawPixmap(fullPixmap,
                region.getRegionX(), region.getRegionY(),
                region.getRegionWidth(), region.getRegionHeight(),
                0, 0,
                region.getRegionWidth(), region.getRegionHeight()
            );

            PixmapIO.writePNG(Gdx.files.local(outputFolder + "/" + region.name + ".png"), pixmapRegion);
            pixmapRegion.dispose();
        }

        for (Pixmap p : pixmaps.values()) p.dispose();

        atlas.dispose();
        System.out.println("Atlas descompuesto correctamente");
    }

    public static void descomponerSheet(String sheetPath, int cols, int rows, String outputFolder) {

        Texture sheet = new Texture(Gdx.files.internal(sheetPath));
        sheet.getTextureData().prepare();
        Pixmap pixmap = sheet.getTextureData().consumePixmap();

        int frameWidth  = pixmap.getWidth() / cols;
        int frameHeight = pixmap.getHeight() / rows;

        // crear carpeta si no existe
        FileHandle folder = Gdx.files.local(outputFolder);
        if(!folder.exists()) folder.mkdirs();

        int index = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {

                Pixmap frame = new Pixmap(frameWidth, frameHeight, Pixmap.Format.RGBA8888);

                frame.drawPixmap(
                    pixmap,
                    0, 0,
                    x * frameWidth, y * frameHeight,
                    frameWidth, frameHeight
                );

                PixmapIO.writePNG(
                    Gdx.files.local(outputFolder + "/explosion_" + index + ".png"),
                    frame
                );

                frame.dispose();
                index++;
            }
        }

        pixmap.dispose();
        sheet.dispose();

        System.out.println("✅ Sprite sheet descompuesto en: " + outputFolder);
    }

}
