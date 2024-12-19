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
 * Aquesta classe té les funcions per resoldre les peticions client-servidor relacionades amb media.
 * Inclou operacions per obtenir, afegir, modificar i esborrar registres de media a la base de dades.
 * @author Sergio
 */
public class MediaHandler {
    
    /**
     * Envia per socket una llista amb tot el media de la base de dades.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
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
     * Rep per socket un id i envia al client la media corresponent.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
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
     * Rep un objecte de media modificat i el sobreescriu a la base de dades amb les dades noves.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
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
     * Rep un id per socket i esborra el media corresponent de la base de dades.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
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
     * Rep per socket un nou objecte de media, l'afegeix a la base de dades i retorna al client l'id generat.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
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
