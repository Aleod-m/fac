package pdl.imageprocessing;

import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Process {

    public static void adjustLum(ImageProcessed img, int delta) {
        Planar<GrayF32> hsv = Convert.rgbToHsv(img.rgb);
        float d = (float) delta / 255f;
        GrayProcessing.brightness(hsv.getBand(2), d);
        img.rgb = Convert.hsvToRgb(hsv);
    }

    public enum EqChanel {
        SAT,
        VAL;
    }

    public static void equalize(ImageProcessed img, EqChanel ch) {
        Planar<GrayF32> hsv = Convert.rgbToHsv(img.rgb);
        switch (ch) {
            case SAT:
                GrayProcessing.equalize(hsv.getBand(1));
                break;
            case VAL:
                GrayProcessing.equalize(hsv.getBand(2));
                break;
            default:
                break;
        }
        img.rgb = Convert.hsvToRgb(hsv);
    }

    public static void colorFilter(ImageProcessed img, int hue) {
        Planar<GrayF32> hsv = Convert.rgbToHsv(img.rgb);
        hue = hue % 360;
        for (int i = 0; i < hsv.width; i++)
            for (int j = 0; j < hsv.height; j++) {
                hsv.getBand(0).set(i, j, (float) hue);
            }
        img.rgb = Convert.hsvToRgb(hsv);
    }

    public static void edgeDetect(ImageProcessed img) {
        GrayU8 input = Convert.toGrayLevels(img.rgb);
        GrayU8 outputh = input.createSameShape();
        GrayU8 outputv = input.createSameShape();
        GrayU8 output = input.createSameShape();
        
        Convolution.convolution(input, outputh, Kernels.SOBELH.k);
        Convolution.convolution(input, outputv, Kernels.SOBELV.k);
        for (int i = 0; i < input.width; i++){
            for (int j = 0; j < input.height; j++) {
                int vh = outputh.get(i, j);
                int vv = outputv.get(i, j);
                output.set(i, j, Math.max(0, Math.min((int) Math.sqrt(vh * vh + vv * vv), 255)));
                
            }
            
        }
        img.rgb.setBand(0, output);
                img.rgb.setBand(1, output);
                img.rgb.setBand(2, output);
    }

    public enum BlurMethod {
        GAUSSIAN,
        MEAN;
    }

    public static void blur(ImageProcessed img, BlurMethod bm) {
        Planar<GrayU8> output = img.rgb.createSameShape();
        Kernel k;
        switch (bm) {
            case GAUSSIAN:
                k = Kernels.GAUSIAN3.k;
                break;
            case MEAN:
                k = Kernels.MEAN.k;
                break;
            default:
                k = Kernels.GAUSIAN3.k;
        }

        for (int ch = 0; ch < img.rgb.bands.length; ch++)
            Convolution.convolution(img.rgb.getBand(ch), output.getBand(ch), k);

        img.rgb = output;
    }
}
