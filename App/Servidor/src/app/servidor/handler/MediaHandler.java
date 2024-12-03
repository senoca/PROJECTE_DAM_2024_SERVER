/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

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
 *
 * @author Sergio
 */
public class MediaHandler {
    
    public static void getAllMedia(Socket clientSocket) {
            ObjectOutputStream objectOutput = null;
            try {
                ArrayList<Media> allMedia = DBMedia.getAllMedia();
                
                objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutput.writeObject((Object)allMedia);
                objectOutput.flush();
                if (allMedia != null) {
                    for (Media m : allMedia) {
                        System.out.println(m.getTitle());
                }
                }
                
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                Utils.closeObjectOutputStream(objectOutput);
            }
        }
    
    public static void getMediaById(Socket clientSocket) {
        ObjectInputStream objectInput = null;
        ObjectOutputStream objectOutput = null;
        try {
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            int mediaId = objectInput.readInt();
            Media media = DBMedia.getMediaById(mediaId);
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutput.writeObject((Object) media);
            objectOutput.flush();
        } catch (IOException ex) {
            throw new ServerException(ex);
        } finally {
            Utils.closeObjectInputStream(objectInput);
            Utils.closeObjectOutputStream(objectOutput);
        }
    }
    
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
