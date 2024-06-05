package pdl.backend;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pdl.imageprocessing.*;
import pdl.imageprocessing.Process;

@RestController
public class ImageController {

    @Autowired
    private ObjectMapper mapper;


    UserDao userDao;

    @Autowired
    public ImageController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET , produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ArrayNode getUserList() {
        ArrayNode nodes = mapper.createArrayNode();
        List<String> users = this.userDao.toList();
        users.forEach(username -> {
            ObjectNode node = nodes.addObject();
            node.put("name" ,username);
        });
        return ResponseEntity.ok(nodes).getBody();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestParam Map<String,String> params){
        if(!params.containsKey("user") || !params.containsKey("pwd"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (userDao.logInUser(params.get("user"), params.get("pwd")))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>("Wrong credentials.", HttpStatus.UNAUTHORIZED);

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestParam Map<String,String> params){
        if(!params.containsKey("user") || !params.containsKey("pwd"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (userDao.registerUser(params.get("user"), params.get("pwd")))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>("User already exists.", HttpStatus.UNAUTHORIZED);

    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout(@RequestParam String user){
        if(!this.userDao.isUserLoggedIn(user))
            return new ResponseEntity<>("User not logged in.", HttpStatus.UNAUTHORIZED);
        this.userDao.getUserImageDao(user).saveImaged();
        this.userDao.userLogOut(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{user}/images/{id}", method = RequestMethod.GET, produces = { MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE })
    public ResponseEntity<?> getImage(@PathVariable String user, @PathVariable("id") long id, @RequestParam Map<String, String> params) {
        if(!this.userDao.isUserLoggedIn(user))
            return new ResponseEntity<>("User not logged in.", HttpStatus.UNAUTHORIZED);
        Optional<Image> imagechk = this.userDao.getUserImageDao(user).retrieve(id);
        // Check if an image is there
        if (imagechk.isEmpty())
            return new ResponseEntity<>("Image not found.", HttpStatus.NOT_FOUND);
        Image image = imagechk.get();
        // if no processing is required we send back the image.
        if (params.isEmpty()) {
            InputStreamResource inputstream = new InputStreamResource(new ByteArrayInputStream(image.getData()));
            return ResponseEntity.ok().contentType(image.getType()).body(inputstream);
        }

        ImageProcessed imagework = image.toImagePross();
        InputStreamResource inputstream;
        try{
            switch (params.get("algorithm")) {
                case "lum":
                    if (!params.containsKey("val"))
                        return new ResponseEntity<>("Wrong number of arguments for Luminosity check the api description.",
                                HttpStatus.BAD_REQUEST);
                    Process.adjustLum(imagework, Integer.parseInt(params.get("val")));
                    break;
                case "heq":
                    if (!params.keySet().contains("chan"))
                        return new ResponseEntity<>(
                                "Wrong number of arguments for Histogram Equalizing check the api description.",
                                HttpStatus.BAD_REQUEST);
                    switch (params.get("chan")) {
                        case "s":
                            Process.equalize(imagework, Process.EqChanel.SAT);
                            break;
                        case "v":
                            Process.equalize(imagework, Process.EqChanel.VAL);
                            break;
                        default:
                            return new ResponseEntity<>(
                                    "Chanel selection doesn't exist for Histogram Equalizing check the api description.",
                                    HttpStatus.BAD_REQUEST);
                    }
                    break;
                case "hue":
                    if (!params.keySet().contains("col"))
                        return new ResponseEntity<>("Wrong number of arguments for Hue changement check the api description.",
                                HttpStatus.BAD_REQUEST);
                    if (params.get("col").isBlank())
                        return new ResponseEntity<>("Miss arguments for Hue changement check the api description.",
                                HttpStatus.BAD_REQUEST);
                    Process.colorFilter(imagework, Integer.parseInt(params.get("col")));
                    break;
                case "blur":
                    if (!params.containsKey("method"))
                        return new ResponseEntity<>("Wrong number of arguments for Blur check the api description.",
                                HttpStatus.BAD_REQUEST);
                    switch (params.get("method")) {
                        case "g":
                            Process.blur(imagework, Process.BlurMethod.GAUSSIAN);
                            break;
                        case "m":
                            Process.blur(imagework, Process.BlurMethod.MEAN);
                            break;
                        default:
                            return new ResponseEntity<>(
                                    "Chanel selection doesn't exist for HueChangement check the api description.",
                                    HttpStatus.BAD_REQUEST);
                    }
                    break;
                case "edge":
                    Process.edgeDetect(imagework);
                    break;
                default:
                    return new ResponseEntity<>("No algorithm name found.", HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Parameters not match as it will. Check the api information.", HttpStatus.BAD_REQUEST);
        }

        Image imgResult = new Image(
                image.getName().concat("_mod"),
                imagework.getResult(),
                image.getType());
        inputstream = new InputStreamResource(new ByteArrayInputStream(imgResult.getData()));
        return ResponseEntity.ok().contentType(image.getType()).body(inputstream);
    }

    @RequestMapping(value = "/{user}/images/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImage(@PathVariable("user") String user, @PathVariable("id") long id) {
        if(!this.userDao.isUserLoggedIn(user))
            return new ResponseEntity<>("User not logged in.", HttpStatus.UNAUTHORIZED);
        Optional<Image> image = this.userDao.getUserImageDao(user).retrieve(id);
        if (image.isEmpty())
            return new ResponseEntity<>("Image not found.", HttpStatus.NOT_FOUND);
        this.userDao.getUserImageDao(user).delete(image.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{user}/images", method = RequestMethod.POST)
    public ResponseEntity<?> addImage(@PathVariable("user") String user, @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if(!this.userDao.isUserLoggedIn(user))
            return new ResponseEntity<>("User not logged in.", HttpStatus.UNAUTHORIZED);
        if (Objects.equals(file.getContentType(), MediaType.IMAGE_JPEG_VALUE)
                || Objects.equals(file.getContentType(), MediaType.IMAGE_PNG_VALUE)) {
            Image image;
            try {
                InputStream is = file.getInputStream();
                BufferedImage imageData = javax.imageio.ImageIO.read(is);
                is.close();
                image = new Image(
                        file.getOriginalFilename(),
                        imageData,
                        MediaType.valueOf(file.getContentType()),
                        true);
            } catch (IOException e) {
                return new ResponseEntity<>("Invalid Image data.", HttpStatus.BAD_REQUEST);
            }
            this.userDao.getUserImageDao(user).create(image);
            return new ResponseEntity<>(HttpStatus.OK);

        }
        return new ResponseEntity<>("Invalid file format. Expected: jpeg , png !", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @RequestMapping(value = "/{user}/images", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> getImageList(@PathVariable("user") String user) {

        if(!this.userDao.isUserLoggedIn(user))
            return new ResponseEntity<>("User not logged in.", HttpStatus.UNAUTHORIZED);

        ArrayNode nodes = mapper.createArrayNode();
        List<Image> images = this.userDao.getUserImageDao(user).retrieveAll();
        images.forEach(image -> {
            ObjectNode node = nodes.addObject();
            node.put("id", image.getId());
            node.put("name", image.getName());
            node.put("type", image.getType().getType());
            ObjectNode size = node.putObject("size");
            size.put("w", image.getWidth());
            size.put("h", image.getHeight());
            size.put("chanels", image.getChanels());
        });
        return ResponseEntity.ok(nodes);
    }

}
