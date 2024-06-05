package pdl.imageprocessing;

import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;

class Convolution {

    public static void convolution(GrayU8 input, GrayU8 output, Kernel kernel) {
        float coef = kernel.nfactor;
        int size = (kernel.k.length - 1) / 2;
        for (int y = size; y < input.height - size; ++y) {
            for (int x = size; x < input.width - size; ++x) {
                int inp = 0;
                int somme = 0;
                for (int i = -size; i <= size; i++) {
                    for (int j = -size; j <= size; j++) {
                        inp = input.get(x + i, y + j);
                        somme += inp * kernel.k[i + size][j + size];
                    }
                }
                int val = Math.max(0,Math.min(255,((int) (somme * coef))));
                output.set(x, y, val);
            }
        }
    }
}