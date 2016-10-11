package com.fdanesse.jamedia.Archivos;

/**
 * Created by flavio on 09/10/16.
 */
public class ItemFileChooser {

    private int imagen;
    private String filename;
    private String filepath;
    private String selected = "false";
    private String tipo;

    public ItemFileChooser(int imagen, String filename, String filepath, String tipo){
        this.imagen = imagen;
        this.filename = filename;
        this.filepath = filepath;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public String getSelected() {
        return selected;
    }
}