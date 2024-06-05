package pdl.imageprocessing;

import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

class Convert {

    public static Planar<GrayF32> rgbToHsv(Planar<GrayU8> input) {
        Planar<GrayF32> hsvImage = new Planar<GrayF32>(GrayF32.class, input.width, input.height, 3);
        for (int i = 0; i < input.getWidth(); i++)
            for (int j = 0; j < input.getHeight(); j++) {
                int r = input.getBand(0).get(i, j);
                int g = input.getBand(1).get(i, j);
                int b = input.getBand(2).get(i, j);
                float[] hsv = new float[3];
                rgbToHsv(r, g, b, hsv);
                hsvImage.getBand(0).set(i, j, hsv[0]);
                hsvImage.getBand(1).set(i, j, hsv[1]);
                hsvImage.getBand(2).set(i, j, hsv[2]);
            }
        return hsvImage;
    }

    public static Planar<GrayU8> hsvToRgb(Planar<GrayF32> input) {
        Planar<GrayU8> rgbImage = new Planar<GrayU8>(GrayU8.class, input.width, input.height, 3);
        for (int i = 0; i < input.getWidth(); i++)
            for (int j = 0; j < input.getHeight(); j++) {
                float h = input.getBand(0).get(i, j);
                float s = input.getBand(1).get(i, j);
                float v = input.getBand(2).get(i, j);
                int[] rgb = new int[3];
                hsvToRgb(h, s, v, rgb);
                rgbImage.getBand(0).set(i, j, rgb[0]);
                rgbImage.getBand(1).set(i, j, rgb[1]);
                rgbImage.getBand(2).set(i, j, rgb[2]);
            }
        return rgbImage;
    }

    public static GrayU8 toGrayLevels(Planar<GrayU8> input) {
        GrayU8 glvl = new GrayU8(input.width, input.height);
        for (int i = 0; i < input.getWidth(); i++)
            for (int j = 0; j < input.getHeight(); j++) {
                int r = input.getBand(0).get(i, j);
                int g = input.getBand(1).get(i, j);
                int b = input.getBand(2).get(i, j);
                glvl.set(i, j, (int) (0.299 * r + 0.587 * g + 0.114 * b));
            }
        return glvl;
    }

    private static void rgbToHsv(int r, int g, int b, float[] hsv) {
        double rD = (double) r / 255.0;
        double gD = (double) g / 255.0;
        double bD = (double) b / 255.0;
        double cmax = Math.max(rD, Math.max(gD, bD));
        double cmin = Math.min(rD, Math.min(gD, bD));
        double diff = cmax - cmin;
        double h = -1, s = -1;
        if (cmax == cmin)
            h = 0;
        else if (cmax == rD)
            h = (60 * ((gD - bD) / diff) + 360) % 360;
        else if (cmax == gD)
            h = (60 * ((bD - rD) / diff) + 120) % 360;
        else if (cmax == bD)
            h = (60 * ((rD - gD) / diff) + 240) % 360;
        if (cmax == 0)
            s = 0;
        else
            s = (diff / cmax);
        double v = cmax;
        hsv[0] = (float) h;
        hsv[1] = (float) s;
        hsv[2] = (float) v;
    }

    private static void remplirTorgb(float r, float g, float b, int[] rgb) {
        rgb[0] = Math.round(r * 255f);
        rgb[1] = Math.round(g * 255f);
        rgb[2] = Math.round(b * 255f);

    }

    private static void hsvToRgb(float h, float s, float v, int[] rgb) {
        int hue;
        hue = (int) (h / 60) % 6;
        float f = h / 60 - hue;
        float l = v * (1 - s);
        float m = v * (1 - f * s);
        float n = v * (1 - (1 - f) * s);
        if (hue == 0)
            remplirTorgb(v, n, l, rgb);
        if (hue == 1)
            remplirTorgb(m, v, l, rgb);
        if (hue == 2)
            remplirTorgb(l, v, n, rgb);
        if (hue == 3)
            remplirTorgb(l, m, v, rgb);
        if (hue == 4)
            remplirTorgb(n, l, v, rgb);
        if (hue == 5)
            remplirTorgb(v, l, m, rgb);

    }
}
