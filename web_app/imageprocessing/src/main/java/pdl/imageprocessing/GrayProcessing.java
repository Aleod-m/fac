package pdl.imageprocessing;

import boofcv.struct.image.GrayF32;

class GrayProcessing {

	public static void brightness(GrayF32 input, float delta) {
		for (int y = 0; y < input.height; ++y)
			for (int x = 0; x < input.width; ++x) {
				float val = input.get(x, y);
				if (val + delta < 0) {
					val = 0;
				} else if (val + delta <= 1) {
					val += delta;
				} else {
					val = 1;
				}
				input.set(x, y, val);
			}
	}

	public static void contrast(GrayF32 input, int _min, int _max) {
		int[] lutT = new int[256];
		int[] hist = new int[256];
		int max = 0;
		int min = 255;
		for (int y = 0; y < input.height; ++y)
			for (int x = 0; x < input.width; ++x) {
				int gl = (int) (input.get(x, y) * 255);
				if (gl < min) {
					min = gl;
				}
				if (gl > max) {
					max = gl;
				}
				hist[gl]++;
			}

		for (int pixel = 0; pixel <= 255; pixel++) {
			lutT[pixel] = ((_max) / (max - min)) * (pixel - min);
		}

		for (int y = 0; y < input.height; ++y)
			for (int x = 0; x < input.width; ++x) {
				int i = (int) (input.get(x, y) * 255);
				input.set(x, y, (float) lutT[i] / 255);
			}

	}

	public static void equalize(GrayF32 input) {
		int[] histograme = new int[256];
		int max = 0;
		int min = 255;
		for (int y = 0; y < input.height; ++y)
			for (int x = 0; x < input.width; ++x) {
				int gl = Math.round(input.get(x, y) * 255f);
				if (gl < min) {
					min = gl;
				}
				if (gl > max) {
					max = gl;
				}
				histograme[gl]++;
			}

		int[] histogrameCumul = new int[256];
		histogrameCumul[0] = histograme[0];
		for (int i = 1; i < 256; i++) {
			histogrameCumul[i] += histogrameCumul[i - 1] + histograme[i];
		}

		for (int y = 0; y < input.height; ++y)
			for (int x = 0; x < input.width; ++x) {
				int i = (int) (input.get(x, y) * 255);
				int iprm = ((histogrameCumul[i]) * 255) / ((input.width) * (input.height));
				input.set(x, y, (float) iprm / 255f);
			}
	}

}
