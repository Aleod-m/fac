package pdl.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    private final Map<String,ImageDao> imageList = new HashMap<>();
    private final Map<String, String> userList = new HashMap<>();

    public UserDao(){
        ClassPathResource userDb_path = new ClassPathResource("lib/userDB.csv");
        File userDb_file;
        try {
            userDb_file = new File(userDb_path.getFile().getPath());
            try {
                Scanner userDB_reader = new Scanner(userDb_file);
                while (userDB_reader.hasNextLine()) {
                    String line = userDB_reader.nextLine();
                    String[] row = line.split(";");
                    this.userList.put(row[0], row[1]);
                }
                userDB_reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageDao getUserImageDao(String user){
        if(this.imageList.containsKey(user))
            return this.imageList.get(user);
        return null;
    }

    public boolean logInUser(String user, String password){
        if(userList.containsKey(user)) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                String hexPwdHash = bytesToHex(encodedhash);
                if(userList.get(user).equals(hexPwdHash)) {
                    ImageDao imgd = new ImageDao(user);
                    this.imageList.put(user, imgd);
                    return true;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean registerUser(String user, String password){
        if(!userList.containsKey(user)) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                String hexPwdHash = bytesToHex(encodedhash);
                ImageDao imgd = new ImageDao(user);
                this.imageList.put(user, imgd);
                this.userList.put(user, hexPwdHash);
                this.writeUsrDB();
                return true;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean isUserLoggedIn(String user){
        return this.imageList.containsKey(user);
    }

    public void userLogOut(String user) {
        this.imageList.remove(user);
    }

    public List<String> toList() {
        List<String> list = new ArrayList<>();
  
        // Add each element of iterator to the List
        this.userList.keySet().iterator().forEachRemaining(list::add);
  
        // Return the List
        return list;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void writeUsrDB() {
        ClassPathResource userDb_path = new ClassPathResource("lib/userDB.csv");
        File userDb_file;
        FileWriter writer;
        try {
            userDb_file = new File(userDb_path.getFile().getPath());
            writer = new FileWriter(userDb_file);
            for(Map.Entry<String,String> e: this.userList.entrySet()) {
                String line = String.format("%s;%s;\n", e.getKey(), e.getValue());
                writer.write(line);
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
