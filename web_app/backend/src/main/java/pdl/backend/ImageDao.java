package pdl.backend;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

public class ImageDao implements Dao<Image> {

    private final Map<Long, Image> images = new HashMap<>();
    private final String path;

    public ImageDao(String name) {
        ClassPathResource resources_path = new ClassPathResource("lib/");
        File img_lib;
        this.path = resources_path.getPath() + name;
        try {
            img_lib = new File(this.path);
            if (!img_lib.exists()) {
                File userDir = new File(resources_path.getFile().getParent() + "/usertemplate");
                File user_lib = new File(userDir.getPath());
                FileUtils.copyDirectory(user_lib, img_lib);
            }
            // List of all files and directories
            String[] contents = img_lib.list();
            assert contents != null;
            for (String image : contents) {
                String[] imagename = image.split("\\.");
                MediaType mediaType;
                switch (imagename[1]) {
                    case "jpg":
                        mediaType = MediaType.IMAGE_JPEG;
                        break;
                    case "jpeg":
                        mediaType = MediaType.IMAGE_JPEG;
                        break;
                    case "png":
                        mediaType = MediaType.IMAGE_PNG;
                        break;
                    default:
                        mediaType = MediaType.IMAGE_JPEG;
                }
                // byte[] fileContent = Files.readAllBytes(Paths.get());
                File imgPath = new File(img_lib.getPath().concat("/").concat(image));
                BufferedImage buffImage = null;
                try {
                    buffImage = javax.imageio.ImageIO.read(new FileInputStream(imgPath.getPath()));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Image img = new Image(image, buffImage, mediaType);
                images.put(img.getId(), img);

            }
        } catch (IOException el) {
            el.printStackTrace();
        }


    }

    @Override
    public Optional<Image> retrieve(final long id) {
        Image image = this.images.get(id);
        if (image == null) {
            return Optional.empty();
        } else {
            return Optional.of(image);
        }
    }

    @Override
    public List<Image> retrieveAll() {
        return new ArrayList<Image>(this.images.values());
    }

    @Override
    public void create(final Image img) {
        this.images.put(img.getId(), img);
    }

    @Override
    public void update(final Image img, final String[] params) {
        return;
    }

    @Override
    public void delete(final Image img) {
        this.images.remove(img.getId());
    }

    public void saveImaged() {
        for (Image image : images.values()) {
            if (image.isPosted()) {
                File image_file = new File(this.path + "/" + image.getName());
                if (image_file.exists()) {
                    String name = new String();
                    String[] name_witht_extention = image.getName().split("\\.");
                    int buff = 0;
                    if (name_witht_extention[0].charAt(name_witht_extention[0].length() - 1) == ')') {
                        int power = 1;
                        int pos = name_witht_extention[0].length() - 2;
                        boolean run = true;
                        while (run) {
                            char caracter = name_witht_extention[0].charAt(pos);
                            if (caracter == '(')
                                run = false;
                            else {
                                buff += Integer.parseInt(Character.toString(caracter)) * power;
                                power *= 10;
                                pos --;
                            }
                        }
                        name =  name_witht_extention[0] + "(" + buff + ")." + name_witht_extention[1];
                    }else{
                        buff = 1;
                        name =  name_witht_extention[0] + "(" + buff + ")." + name_witht_extention[1];
                        while(new File(path + "/" + name).exists()){
                            buff ++;
                            name =  name_witht_extention[0] + "(" + buff + ")." + name_witht_extention[1];
                        }
                    }
                    image_file = new File(path + "/" + name);

                }
                try {
                    BufferedImage bi = image.getBufferedImage();
                    ImageIO.write(bi, MediaType.valueOf(image.getType().toString()).toString().split("/")[1], image_file);
                } catch (IOException e){
                
                }
            
            }
        }
    }
}
