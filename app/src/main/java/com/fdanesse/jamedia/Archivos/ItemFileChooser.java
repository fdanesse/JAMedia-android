package com.fdanesse.jamedia.Archivos;

/**
 * Created by flavio on 09/10/16.
 */
public class ItemFileChooser {

    private int imagen;
    private String filename;
    private String filepath;

    public ItemFileChooser(int imagen, String filename, String filepath){
        this.imagen = imagen;
        this.filename = filename;
        this.filepath = filepath;
    }

    public int getImagen() {
        return imagen;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }
}