package pdl.imageprocessing;

import boofcv.struct.image.GrayU8;

import boofcv.struct.image.Planar;

class ColorProcessing {

    public static void convolutionRGB(Planar<GrayU8> input, Planar<GrayU8> output, int[][] kernel) {

        int coef = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                coef += kernel[i][j];
            }
        }
        int size = (kernel.length - 1) / 2;
        for (int can = 0; can < input.getNumBands(); can++)
            for (int y = size; y < input.height - size; ++y) {
                for (int x = size; x < input.width - size; ++x) {
                    int inp = 0;
                    int somme = 0;
                    for (int i = -size; i <= size; i++) {
                        for (int j = -size; j <= size; j++) {
                            inp = input.getBand(can).get(x + i, y + j);
                            somme += inp * kernel[i + size][j + size];
                        }
                    }
                    output.getBand(can).set(x, y, (somme / coef));
                }
            }
    }

}
