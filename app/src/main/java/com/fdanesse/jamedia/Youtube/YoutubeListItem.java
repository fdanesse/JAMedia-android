package com.fdanesse.jamedia.Youtube;

/**
 * Created by flavio on 27/09/16.
 */
public class YoutubeListItem{

    private int imagen;
    private String nombre;
    private String url;

    public YoutubeListItem(int imagen, String nombre, String url){
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
