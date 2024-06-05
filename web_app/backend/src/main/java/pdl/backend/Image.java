package pdl.backend;

import java.io.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import org.springframework.http.MediaType;

import pdl.imageprocessing.ImageProcessed;

public class Image {
    private boolean posted = false;
    private static Long count = 0L;
    private final Long id;
    private String name;
    private final MediaType type;
    private final BufferedImage data;

    public Image(
            final String name,
            final BufferedImage data,
            MediaType mediaType,
            boolean posted) {
        id = count++;
        this.name = name;
        this.type = mediaType;
        this.data = data;
        this.posted = posted;

    }

    public Image(
            final String name,
            final BufferedImage data,
            MediaType mediaType) {
        id = count++;
        this.name = name;
        this.type = mediaType;
        this.data = data;

    }

    public MediaType getType() {
        return this.type;
    }

    public int getWidth() {
        return this.data.getWidth();
    }

    public int getHeight() {
        return this.data.getHeight();
    }

    public int getChanels() {
        return this.data.getColorModel().getComponentSize().length;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public byte[] getData() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (this.type.equals(MediaType.IMAGE_JPEG)) {
                ImageIO.write(this.data, "jpg", baos);
            } else if (this.type.equals(MediaType.IMAGE_PNG)) {
                ImageIO.write(this.data, "png", baos);
            } else {
                return null;
            }
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            return imageBytes;
        } catch (IOException e) {
            return null;
        }
    }

    public ImageProcessed toImagePross() {
        return new ImageProcessed(this.data);
    }

    public BufferedImage getBufferedImage(){
        return this.data;
    }

    public boolean isPosted() {
        return this.posted;
    }
}
