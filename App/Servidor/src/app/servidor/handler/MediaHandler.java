/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.crypto.CryptoUtils;
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
    public static void getAllMedia(Socket soc, String pswd) {
//            try {
//                ArrayList<Media> allMedia = DBMedia.getAllMedia();
//                CryptoUtils.sendObject(soc.getOutputStream(), allMedia, pswd);
//                
//            } catch (IOException ex) {
//                throw new ServerException(ex);
//            }
        }
    
    /**
     * rep per socket un id, i envia al client la media corresponent
     * @param soc
     */
    public static void getMediaById(Socket soc, String pswd) {
//        try {
//            int mediaId = CryptoUtils.readInt(soc.getInputStream(), pswd);
//            Media media = DBMedia.getMediaById(mediaId);
//            CryptoUtils.sendObject(soc.getOutputStream(), (Object)media, pswd);
//        } catch (IOException ex) {
//            throw new ServerException(ex);
//        }
    }
    
    /**
     * rep un media modificat i el sobreescriu a la bd amb les dades noves
     * @param clientSocket
     */
    public static void modifyMedia(Socket clientSocket) {
        ObjectInputStream objectInput = null;
        try {
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            Media updatedMedia = (Media) objectInput.readObject();
            DBMedia.updateMedia(updatedMedia.getWorkId(), updatedMedia);
        } catch (Exception ex) {
            throw new ServerException(ex);
        } finally {
            Utils.closeObjectInputStream(objectInput);
        }
    }
    
    /**
     * rep un id i esborra el media corresponent
     * @param clientSocket
     */
    public static void deleteMedia(Socket clientSocket) {
            ObjectInputStream objectInput = null;
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                int mediaId = objectInput.readInt();
                DBMedia.deleteMedia(mediaId);
            } catch (Exception ex) {
                throw new ServerException(ex);
            } finally {
                Utils.closeObjectInputStream(objectInput);
            }
        }
    
    /**
     * rep per socket un media nou, l'afegeix a la bd i retorna al client l'id generat
     * @param clientSocket
     */
    public static void addNewMedia(Socket clientSocket) {
            ObjectInputStream objectInput = null;
            ObjectOutputStream objectOutput = null;
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                Media media = (Media) objectInput.readObject();
                int mediaId = DBMedia.insertNewMedia(media);
                objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutput.writeInt(mediaId);
                objectOutput.flush();
            } catch (IOException | ClassNotFoundException ex) {
                throw new ServerException(ex);
            } finally {
                Utils.closeObjectInputStream(objectInput);
                Utils.closeObjectOutputStream(objectOutput);
            }
        }
}
