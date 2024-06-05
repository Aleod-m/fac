package pdl.imageprocessing;

import java.awt.image.BufferedImage;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class ImageProcessed {

   protected Planar<GrayU8> rgb;

   public ImageProcessed(BufferedImage data) {
      this.rgb = ConvertBufferedImage.convertFromPlanar(data, null, true, GrayU8.class);
   }

   public BufferedImage getResult() {
      return ConvertBufferedImage.convertTo_U8(this.rgb, null, true);
   }

   public Planar<GrayU8> getRgb() {
      return rgb;
   }

}
