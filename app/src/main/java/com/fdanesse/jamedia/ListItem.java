package com.fdanesse.jamedia;

/**
 * Created by flavio on 27/09/16.
 */
public class ListItem {

    private int imagen;
    private String nombre;
    private String url;

    public ListItem(int imgagen, String nombre, String url){
        this.imagen = imagen;
        this.nombre = nombre;
        this.url = url;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }
}
