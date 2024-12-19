/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.servidor.app.ClientThread;
import app.model.Media;
import app.servidor.app.ServerException;
import app.servidor.app.Utils;
import app.servidor.entity.DBMedia;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Aquesta classe té les funcions per resoldre les peticions client-servidor relacionades amb media
 * @author Sergio
 */
public class MediaHandler {
    
    /**
     * envia per socket una llista amb tot el media de la db
     * @param soc
     */
    public static void getAllMedia(Stream stream, String pswd) {
            try {
                ArrayList<Media> allMedia = DBMedia.getAllMedia();
                CryptoUtils.sendObject(stream, allMedia, pswd);
                
            } catch (Exception ex) {
                throw new ServerException(ex);
            }
        }
    
    /**
     * rep per socket un id, i envia al client la media corresponent
     * @param soc
     */
    public static void getMediaById(Stream stream, String pswd) {
        try {
            int mediaId = CryptoUtils.readInt(stream, pswd);
            Media media = DBMedia.getMediaById(mediaId);
            CryptoUtils.sendObject(stream, (Object)media, pswd);
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * rep un media modificat i el sobreescriu a la bd amb les dades noves
     * @param clientSocket
     */
    public static void modifyMedia(Stream stream, String pswd) {
        try {
            Media updatedMedia = (Media) CryptoUtils.readObject(stream, pswd);
            DBMedia.updateMedia(updatedMedia.getWorkId(), updatedMedia);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServerException(ex);
        } 
    }
    
    /**
     * rep un id i esborra el media corresponent
     * @param clientSocket
     */
    public static void deleteMedia(Stream stream, String pswd) {
            try {
                int mediaId = CryptoUtils.readInt(stream, pswd);
                DBMedia.deleteMedia(mediaId);
            } catch (Exception ex) {
                throw new ServerException(ex);
            }
        }
    
    /**
     * rep per socket un media nou, l'afegeix a la bd i retorna al client l'id generat
     * @param clientSocket
     */
    public static void addNewMedia(Stream stream, String pswd) {
            try {
                Media media = (Media) CryptoUtils.readObject(stream, pswd);
                int mediaId = DBMedia.insertNewMedia(media);
                CryptoUtils.sendInt(stream, mediaId, pswd);
            } catch (Exception ex) {
                throw new ServerException(ex);
            } 
        }
}
