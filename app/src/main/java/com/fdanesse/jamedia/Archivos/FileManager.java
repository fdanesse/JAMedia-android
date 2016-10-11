package com.fdanesse.jamedia.Archivos;

import android.app.Activity;
import android.support.design.widget.Snackbar;
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
        ArrayList<ItemFileChooser> lista = new ArrayList<ItemFileChooser>();
        File file = new File(dirpath);

        if (file.exists()){
            File[] files = file.listFiles();

            for (File f: files){

                String filename = f.getName();
                if (f.getName().length() > 15){
                    filename = f.getName().substring(0, 15) + "...";
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
                            Log.i("XXXXX", mime);
                        }
                    }
                }
            }
        }

        return lista;
    }


    public static ArrayList<ListItem> get_music() {

        ArrayList<ListItem> lista = new ArrayList<ListItem>();
        File file = new File("/mnt/sdcard/Musica/Alika");
        File[] files = file.listFiles();
        for (File f: files){
            //FIXME: agregar comprobación de tipo de archivos
            if (f.isFile()) {
                //FIXME: Nombres no deben ser demasiado largos
                lista.add(new ListItem(R.drawable.audio, f.getName(), f.getPath()));
            }
        }
        return lista;
    }

    /*
    public static ArrayList<ListItem> get_videos(){

        ArrayList<ListItem> lista = new ArrayList<ListItem>();
        File[] files = new File(Environment.DIRECTORY_MOVIES).listFiles();
        for (File file: files){
            //FIXME: agregar comprobacion de tipo de archivos
            if (file.isFile()) {
                lista.add(new ListItem(R.drawable.video, file.getName(), file.getAbsolutePath()));
            }
        }
        return lista;
    }
    */

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
        lista.add(new ListItem(R.drawable.jamedia, "Ado FM", "http://broadcast.infomaniak.net/start-adofm-low.mp3"));
        lista.add(new ListItem(R.drawable.jamedia, "AirCDHitsRadio", "http://streaming208.radionomy.com:80/AirCD-Hits-Radio"));
        lista.add(new ListItem(R.drawable.jamedia, "aliens-radio", "http://streaming203.radionomy.com:80/aliens-radio"));
        lista.add(new ListItem(R.drawable.jamedia, "AlloLaRadio", "http://streaming203.radionomy.com:80/Allo-La-Radio"));
        lista.add(new ListItem(R.drawable.jamedia, "Alphatune974", "http://streaming203.radionomy.com:80/Alpha-tune-974"));
        lista.add(new ListItem(R.drawable.jamedia, "Amanecer 91.9 FM (Colonia - Uruguay)", "http://67.222.25.168:7028/"));
        lista.add(new ListItem(R.drawable.jamedia, "AnUrbanRadio", "http://streaming203.radionomy.com:80/An-Urban-Radio"));
        lista.add(new ListItem(R.drawable.jamedia, "Antiguos pero buenos", "http://streaming203.radionomy.com:80/Antiguos--pero-buenos"));
        lista.add(new ListItem(R.drawable.jamedia, "ArabMusicRadio", "http://streaming208.radionomy.com:80/ArabMusicRadio"));
        lista.add(new ListItem(R.drawable.jamedia, "AŞK FM", "http://yayin.asymedya.com:8020/"));
        lista.add(new ListItem(R.drawable.jamedia, "aufilduson80s", "http://streaming203.radionomy.com:80/aufilduson80s"));
        lista.add(new ListItem(R.drawable.jamedia, "BEATLES", "http://streaming206.radionomy.com:80/100-BEATLES"));
        lista.add(new ListItem(R.drawable.jamedia, "Babel 97.1 FM (Montevideo - Uruguay)", "http://radio.sodreuruguay.com:9170/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bogotá Beer Company Radio (Colombia)", "http://99.198.110.162:8052/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bulo FM - Montevideo (Uruguay)", "http://bulofm.com:19000/"));
        lista.add(new ListItem(R.drawable.jamedia, "CW 39 La Voz De Paysandú 1320 AM (Paysandú - Uruguay)", "http://todoserver.com:8939/"));
        lista.add(new ListItem(R.drawable.jamedia, "Cadena del Mar 106.5 (Maldonado - Uruguay)", "http://streamingraddios.com:9455/"));
        lista.add(new ListItem(R.drawable.jamedia, "Campana Bar (Colombia)", "http://listen.radionomy.com/campana-bar"));
        lista.add(new ListItem(R.drawable.jamedia, "Club4total80s", "http://streaming201.radionomy.com:80/1Club4total80s"));
        lista.add(new ListItem(R.drawable.jamedia, "Continental 1600 AM (Atlántida - Uruguay)", "http://usa15.ciudaddigital.com.uy:8056/ContinentalAM"));
        lista.add(new ListItem(R.drawable.jamedia, "Corazón on line (Colombia)", "http://radiolatina.info:9456/"));
        lista.add(new ListItem(R.drawable.jamedia, "Crazy 98.3 FM (Montevideo - Uruguay)", "http://streamingraddios.com:7671/"));
        lista.add(new ListItem(R.drawable.jamedia, "Cristal 1470 AM (Canelones - Uruguay)", "http://usa15.ciudaddigital.com.uy:8126/CristalAM"));
        lista.add(new ListItem(R.drawable.jamedia, "Del Hum 89.3 FM (Soriano - Uruguay)", "http://usa15.ciudaddigital.com.uy:8102/DelHumFM"));
        lista.add(new ListItem(R.drawable.jamedia, "Difusora Soriano 1210 AM (Soriano - Uruguay)", "http://usa15.ciudaddigital.com.uy:8094/DifusoraSoriano"));
        lista.add(new ListItem(R.drawable.jamedia, "El Libertador 1210 AM (Treinta Y Tres - Uruguay)", "http://s4.viastreaming.net:7200/"));
        lista.add(new ListItem(R.drawable.jamedia, "Emisora Cultura RAC (Colombia)", "http://univirtual.utp.edu.co:8000/"));
        lista.add(new ListItem(R.drawable.jamedia, "Emisora Del Sur 1290 AM / 94.7 FM (Montevideo - Uruguay)", "http://radio.sodreuruguay.com:9160/"));
        lista.add(new ListItem(R.drawable.jamedia, "Es más Música Radio (Colombia)", "http://listen.radionomy.com/esmasmusica"));
        lista.add(new ListItem(R.drawable.jamedia, "Escenario Radio (Colombia)", "http://190.145.116.36:8000/"));
        lista.add(new ListItem(R.drawable.jamedia, "Espectador 810 AM (Montevideo - Uruguay)", "http://streaming.espectador.com/envivo"));
        lista.add(new ListItem(R.drawable.jamedia, "FM Principal 107.9 (San José - Uruguay)", "http://s7.myradiostream.com:10764"));
        lista.add(new ListItem(R.drawable.jamedia, "FRIENDSRADIO", "http://streaming203.radionomy.com:80/4-FRIENDS-RADIO"));
        lista.add(new ListItem(R.drawable.jamedia, "Horizonte 89.3 FM (Rivera - Uruguay)", "http://usa15.ciudaddigital.com.uy:8078/HorizonteFM"));
        lista.add(new ListItem(R.drawable.jamedia, "Imparcial 1090 AM (Montevideo - Uruguay)", "http://usa15.ciudaddigital.com.uy:8034/ImparcialAM"));
        lista.add(new ListItem(R.drawable.jamedia, "La Cantina de Sergio Zapata (Colombia)", "http://www.lacantinadesergiozapata.com:8000/lacantina.mp3"));
        lista.add(new ListItem(R.drawable.jamedia, "La Catorce 10 1410 AM (Montevideo - Uruguay)", "http://184.164.137.98:8060/"));
        lista.add(new ListItem(R.drawable.jamedia, "La Voz Del Aire 107.3 FM (Canelones - Uruguay)", "http://streamingraddios.com:7923/"));
        lista.add(new ListItem(R.drawable.jamedia, "Maxima 97.7 FM (Paysandú - Uruguay)", "http://todoserver.com:8997/"));
        lista.add(new ListItem(R.drawable.jamedia, "Metrópolis FM 104.9 (Canelones - Uruguay)", "http://radios.cxnlive.com:8000/"));
        lista.add(new ListItem(R.drawable.jamedia, "Milenium 88.7 FM (Punta Del Este - Maldonado - Uruguay)", "http://usa15.ciudaddigital.com.uy:8134/MileniumFM"));
        lista.add(new ListItem(R.drawable.jamedia, "Nachoteca Radio (Colombia)", "http://listen.radionomy.com/nachoteca-radio"));
        lista.add(new ListItem(R.drawable.jamedia, "Nosotros 90.7 FM (Lavalleja - Uruguay)", "http://usa15.ciudaddigital.com.uy:8082/NosotrosFM"));
        lista.add(new ListItem(R.drawable.jamedia, "Oldies 90.3 FM (Montevideo - Uruguay)", "http://radios.cxnlive.com:8004/"));
        lista.add(new ListItem(R.drawable.jamedia, "One Love Hip Hop Radio (Colombia)", "http://listen.radionomy.com/one-love-hip-hop-radio"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio 70's", "http://31.12.64.204:8000/A-RADIO-70s-JUICE"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio 80's", "http://31.12.64.204:8000/A-RADIO-80s-JUICE"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio 90's", "http://31.12.64.204:8000/A-RADIO-90s-JUICE"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Celeste Corazón (Montevideo - Uruguay)", "http://streamingraddios.com:7907/"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Centenario 1250 AM (Montevideo - Uruguay)", "http://rfm.radio.netgate.com.uy:8000/centenario"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio City 95.1 FM (Durazno - Uruguay)", "http://usa15.ciudaddigital.com.uy:8030/CityFM"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Fenix 1330 AM (Montevideo - Uruguay)", "http://usa15.ciudaddigital.com.uy:28060/B-fenix"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Maldonado 1560 AM (Maldonado - Uruguay)", "http://radios.puntahosting.com:8122/stream"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Prado Sur (Montevideo - Uruguay)", "http://eu1.fastcast4u.com:3552/stream"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Rivera 1440 AM (Rivera - Uruguay)", "http://usa15.ciudaddigital.com.uy:8060/RiveraAM"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Sur 107.1 FM (Flores - Uruguay)", "http://72.29.80.105:7082/"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Top 40", "http://31.12.64.204:8000/A-RADIO-TOP-40"));
        lista.add(new ListItem(R.drawable.jamedia, "Radio Ventura 90.7 FM (Canelones - Uruguay)", "http://198.100.152.234:8232/"));
        lista.add(new ListItem(R.drawable.jamedia, "Salsa Sabor y Bembé (Colombia)", "http://salsasaborybembe.sytes.net:8000/"));
        lista.add(new ListItem(R.drawable.jamedia, "San Carlos 1510 AM (San Carlos - Uruguay)", "http://usa15.ciudaddigital.com.uy:8062/SanCarlosAM"));
        lista.add(new ListItem(R.drawable.jamedia, "Toros 91.9 FM (Paso De Los Toros - Tacuarembo - Uruguay)", "http://pasodelostoros.com:8008/"));
        lista.add(new ListItem(R.drawable.jamedia, "U70s", "http://streaming204.radionomy.com:80/4U-70s"));
        lista.add(new ListItem(R.drawable.jamedia, "Universal 970 AM (Montevideo - Uruguay)", "http://panel.zwcast.com:9008/"));
        lista.add(new ListItem(R.drawable.jamedia, "Universidad de Bogotá (Colombia)", "http://hjut.utadeo.edu.co:8000"));
        lista.add(new ListItem(R.drawable.jamedia, "Uruguay 1050 AM (Montevideo - Uruguay)", "http://radio.sodreuruguay.com:9120/"));
        lista.add(new ListItem(R.drawable.jamedia, "Vida 104.7 FM (Rocha - Uruguay)", "http://usa15.ciudaddigital.com.uy:8048/VidaFM"));
        lista.add(new ListItem(R.drawable.jamedia, "Virtual DJ (Colombia)", "http://radio2.virtualdj.com:7000/"));
        lista.add(new ListItem(R.drawable.jamedia, "YÖN RADYO TÜRKÜ", "http://yonturku.radyolarburada.com:8070/"));
        return lista;
    }
}
