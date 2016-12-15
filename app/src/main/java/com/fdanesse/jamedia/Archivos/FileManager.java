package com.fdanesse.jamedia.Archivos;

import android.util.Log;

import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by flavio on 07/10/16.
 */
public class FileManager {


    public static ArrayList<ItemFileChooser> readDirPath(String dirpath){
        /**
         * Lee un directorio devolviendo la lista de directorios y archivos en él.
         */
        ArrayList<ItemFileChooser> lista = new ArrayList<ItemFileChooser>();
        File file = new File(dirpath);

        if (!file.exists()){return lista;}
        if (!file.isDirectory()){return lista;}

        File[] files = file.listFiles();
        for (File f: files){

            String filename = f.getName();
            if (f.getName().length() > 20){
                filename = f.getName().substring(0, 20) + "...";
            }

            if (f.isDirectory()){
                if (f.canRead()){
                    lista.add(new ItemFileChooser(R.drawable.folder, filename,
                            f.getPath(), "Directorio"));
                }
            }
            else if (f.isFile()){
                if (f.canRead()) {
                    //FIXME: completar comprobación de tipo de archivos
                    String mime = "Undetermined";
                    // URLConnection.guessContentTypeFromName(f.getPath());

                    try {
                        final URL url = new URL("file://" + f.getPath());
                        final URLConnection connection = url.openConnection();
                        mime = connection.getContentType();
                    }
                    catch (MalformedURLException badUrlEx) {continue;}
                    catch (IOException ioEx) {continue;}

                    if (mime.contains("audio")){
                        lista.add(new ItemFileChooser(R.drawable.audio, filename,
                                f.getPath(), "Archivo"));
                    }
                    else if(mime.contains("video")){
                        lista.add(new ItemFileChooser(R.drawable.video, filename,
                                f.getPath(), "Archivo"));
                    }
                    else {
                        Log.i("XXX Desconocido: ", mime);
                    }
                }
            }
        }

        return lista;
    }


    public static ArrayList<ListItem> get_ListItems(ArrayList<String> tracks) {

        ArrayList<ListItem> lista = new ArrayList<ListItem>();
        for (String path: tracks){
            File file = new File(path);
            String mime = "Undetermined";
            //FIXME: completar comprobación de tipo de archivos

            try {
                final URL url = new URL("file://" + file.getPath());
                final URLConnection connection = url.openConnection();
                mime = connection.getContentType();
            }
            catch (MalformedURLException badUrlEx) {continue;}
            catch (IOException ioEx) {continue;}

            String filename = file.getName();
            if (file.getName().length() > 15){
                filename = file.getName().substring(0, 15) + "...";
            }

            if (mime.contains("audio")){
                lista.add(new ListItem(R.drawable.audio, filename, file.getPath()));
            }
            else if(mime.contains("video")) {
                lista.add(new ListItem(R.drawable.video, filename, file.getPath()));
            }
        }
        return lista;
    }

    public static ArrayList<ListItem> get_radios(){
        ArrayList<ListItem> lista = new ArrayList<ListItem>();
        lista.add(new ListItem(R.drawable.jamedia, "Oceano FM 93.9 (Montevideo - Uruguay)", "http://radio4.oceanofm.com:8010/"));
        lista.add(new ListItem(R.drawable.jamedia, "RauteMusik JaM (Alemania)", "http://main-office.rautemusik.fm"));
        lista.add(new ListItem(R.drawable.jamedia, "Sawt Beirut International (Líbano)", "http://sawtbeirut.com:8068/"));
        lista.add(new ListItem(R.drawable.jamedia, "ABCLounge", "http://streaming208.radionomy.com:80/ABC-Lounge"));
        lista.add(new ListItem(R.drawable.jamedia, "ACZEN'HITS", "http://streaming208.radionomy.com:80/AC-ZEN-HITS"));
        lista.add(new ListItem(R.drawable.jamedia, "AMLO (Venezuela)", "http://stream.radioamlo.info:8010"));
        lista.add(new ListItem(R.drawable.jamedia, "ANTENA ZAGREB", "http://173.192.137.34:8050/"));
        lista.add(new ListItem(R.drawable.jamedia, "CW 41 (San José - Uruguay)", "http://server-uk1.radioseninternetuy.com:8148"));
        lista.add(new ListItem(R.drawable.jamedia, "AirCDHitsRadio", "http://streaming208.radionomy.com:80/AirCD-Hits-Radio"));
        lista.add(new ListItem(R.drawable.jamedia, "Amanecer 91.9 FM (Colonia - Uruguay)", "http://67.222.25.168:7028/"));
        lista.add(new ListItem(R.drawable.jamedia, "ArabMusicRadio", "http://streaming208.radionomy.com:80/ArabMusicRadio"));
        lista.add(new ListItem(R.drawable.jamedia, "CW 39 La Voz De Paysandú 1320 AM (Paysandú - Uruguay)", "http://todoserver.com:8939/"));
        lista.add(new ListItem(R.drawable.jamedia, "Continental 1600 AM (Atlántida - Uruguay)", "http://usa15.ciudaddigital.com.uy:8056/ContinentalAM"));
        lista.add(new ListItem(R.drawable.jamedia, "Corazón on line (Colombia)", "http://radiolatina.info:9456/"));
        lista.add(new ListItem(R.drawable.jamedia, "Del Hum 89.3 FM (Soriano - Uruguay)", "http://usa15.ciudaddigital.com.uy:8102/DelHumFM"));
        lista.add(new ListItem(R.drawable.jamedia, "Difusora Soriano 1210 AM (Soriano - Uruguay)", "http://usa15.ciudaddigital.com.uy:8094/DifusoraSoriano"));
        lista.add(new ListItem(R.drawable.jamedia, "El Libertador 1210 AM (Treinta Y Tres - Uruguay)", "http://s4.viastreaming.net:7200/"));
        lista.add(new ListItem(R.drawable.jamedia, "Emisora Cultura RAC (Colombia)", "http://univirtual.utp.edu.co:8000/"));
        lista.add(new ListItem(R.drawable.jamedia, "Espectador 810 AM (Montevideo - Uruguay)", "http://streaming.espectador.com/envivo"));
        lista.add(new ListItem(R.drawable.jamedia, "FM Principal 107.9 (San José - Uruguay)", "http://s7.myradiostream.com:10764"));
        lista.add(new ListItem(R.drawable.jamedia, "Maxima 97.7 FM (Paysandú - Uruguay)", "http://todoserver.com:8997/"));
        lista.add(new ListItem(R.drawable.jamedia, "Nosotros 90.7 FM (Lavalleja - Uruguay)", "http://usa15.ciudaddigital.com.uy:8082/NosotrosFM"));
        lista.add(new ListItem(R.drawable.jamedia, "One Love Hip Hop Radio (Colombia)", "http://listen.radionomy.com/one-love-hip-hop-radio"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Centenario 1250 AM (Montevideo - Uruguay)", "http://rfm.radio.netgate.com.uy:8000/centenario"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio City 95.1 FM (Durazno - Uruguay)", "http://usa15.ciudaddigital.com.uy:8030/CityFM"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Prado Sur (Montevideo - Uruguay)", "http://eu1.fastcast4u.com:3552/stream"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Rivera 1440 AM (Rivera - Uruguay)", "http://usa15.ciudaddigital.com.uy:8060/RiveraAM"));
        lista.add(new ListItem(R.drawable.jamedia, "Toros 91.9 FM (Paso De Los Toros - Tacuarembo - Uruguay)", "http://pasodelostoros.com:8008/"));
        lista.add(new ListItem(R.drawable.jamedia, "Universal 970 AM (Montevideo - Uruguay)", "http://panel.zwcast.com:9008/"));
        lista.add(new ListItem(R.drawable.jamedia, "Universidad de Bogotá (Colombia)", "http://hjut.utadeo.edu.co:8000"));
        return lista;
    }

    public static ArrayList<ListItem> get_tv() {
        ArrayList<ListItem> lista = new ArrayList<ListItem>();
        lista.add(new ListItem(R.drawable.jamedia, "Canal Rural 1 (Argentina)", "rtsp://streamrural.cmd.com.ar:554/liverural/crural/rural1"));
        lista.add(new ListItem(R.drawable.jamedia, "TN (Argentina)", "rtsp://stream.tn.com.ar/live/tnhd1"));
        return lista;
    }
}
