package com.fdanesse.jamedia.Youtube;

/**
 * Created by flavio on 27/09/16.
 */
public class YoutubeListItem{

    private String nombre;
    private String id;
    private String url;

    public YoutubeListItem(String nombre, String id, String url){
        this.nombre = nombre;
        this.id = id;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
