package fr.miage.sd.utils;

import java.io.File;

public class Folder {

    /**
     * Get Current Working Directory
     * @return
     */
    public static String getCWD(){
        return System.getProperty("user.dir");
    }

    /**
     * Get Resources Directory
     * @return
     */
    public static String getResDirectory(){
        return System.getProperty("user.dir") + File.separatorChar +
                "resources" + File.separatorChar;
    }
}
