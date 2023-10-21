package ku.cs.util;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ProjectUtility {

    public static final int programHeight = 600;
    public static final int programWidth = 800;

    public static final boolean isDebug = false;

    public static void debug(String... msgs) {
        debug(String.join("\n", msgs), "\n");
    }

    public static void debug(String msg, String end){
        if (!isDebug) return;
        System.out.print(msg + end);
    }

    /* https://alfatoxin.github.io/Code4Sec/Java/ */
    public static String hashString(String str){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {}
        byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean isEmailFormatRight(String email){
        //String of regex that should work most of the time - Wr3nch_ren
        /**
         * Edition :
         * It will not allow most special characters, unlike RFC 5322.
         * It will also not allow underscore, hyphen. ("_","-")
         * Only lowercase, uppercase, number and dots will be allowed.
         * Before "@" minimum is 1 character, maximum is 64 characters.
         */
        String regex = "^(?=.{1,64}@)\\w+(?:\\.\\w+)*@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z]{2,6}$";
        //Check if email are valid by matching email with regex - Wr3nch_ren
        return Pattern.compile(regex).matcher(email).matches();
    }

    public static String getDataBaseDirName(){
        if (isDebug) return "data-test";
        return "data";
    }

    public static String getDateTime(){
        return (new Timestamp(System.currentTimeMillis())).toString();
    }

    public static String getDateTime(String format){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timestamp);
    }


    public static void copyFile(String from, String to){
        File file = new File(from);
        Path path = Paths.get(to);
        try {
            Files.copy(file.toPath(), path,REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File pictureChooser(){
        ArrayList<String> fileExtensionList = new ArrayList<>();
        fileExtensionList.add("png");
        fileExtensionList.add("jpg");
        fileExtensionList.add("jpeg");
        HashMap<String, ArrayList<String>> fileType = new HashMap<>();
        fileType.put("Image", fileExtensionList);
        return fileChooser(fileType);
    }

    public static File fileChooser(){
        ArrayList<String> fileExtensionList = new ArrayList<>();
        fileExtensionList.add("*");
        HashMap<String, ArrayList<String>> fileType = new HashMap<>();
        fileType.put("All Files", fileExtensionList);
        return fileChooser(fileType);
    }

    public static File fileChooser(HashMap<String, ArrayList<String>> fileType){
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file.");

        ArrayList<ExtensionFilter> fileFilter = new ArrayList<>();
        for (String i: fileType.keySet()) {
            ArrayList<String> modifiedFileExtension = new ArrayList<>();
            for (String j: fileType.get(i)) modifiedFileExtension.add("*." + j);
            fileType.put(i, modifiedFileExtension);
        }
        for (String i: fileType.keySet()) fileFilter.add(new ExtensionFilter(i, fileType.get(i)));
        fileChooser.getExtensionFilters().addAll(fileFilter);

        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile;
    }

    public static File dirChooser(){
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory.");
        //directoryChooser.setInitialDirectory(new File(File.separator));

        File selectedDir = directoryChooser.showDialog(stage);
        return selectedDir;
    }



    public static Image getProgramIcon(){
        return new Image(ProjectUtility.class.getResource("/ku/cs/fxml/images/project-logo.png").toExternalForm());
    }


}