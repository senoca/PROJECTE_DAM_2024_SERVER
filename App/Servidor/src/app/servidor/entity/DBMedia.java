/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.entity;

import app.servidor.app.ServerException;
import app.servidor.app.Utils;
import app.model.Author;
import app.model.Media;
import app.model.ModelException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBMEDIA guarda les funcions per administrar les consultes, altes, baixes o modificacions referents a la taula MEDIA
 * @author Sergio
 */
public class DBMedia {
    private static PreparedStatement selectAuthorOfMediaStatement = null;
    private static PreparedStatement selectMediaByIdStatement = null;
    
    private static PreparedStatement insertMediaAuthor = null;
    private static PreparedStatement insertNewMedia = null;
    
    private static PreparedStatement updateMediaStatement = null;
    
    private static PreparedStatement deleteAllAuthorsFromMediaStatement = null;
    private static PreparedStatement deleteMediaByIdStatement = null;
    
    /**
     * insereix un nou media a la bd
     * @param media
     * @return
     */
    public static int insertNewMedia(Media media) {
        String statement = "INSERT INTO MEDIA (title, yearpublication, mediatype, media_description) values (?, ?, ?, ?)  returning workid";
        int mediaId = -1;
        if (insertNewMedia == null) {
            insertNewMedia = Utils.prepareStatement(statement);
        }
        try {
            insertNewMedia.setString(1, media.getTitle());
            insertNewMedia.setInt(2, media.getYearPublication());
            insertNewMedia.setString(3, media.getMediaTypeAsString());
            insertNewMedia.setString(4, media.getMedia_description());
            
            insertNewMedia.execute();
            ResultSet rs = insertNewMedia.getResultSet();
            if (rs.next()) {
                mediaId = rs.getInt("workid");
            }
            if (mediaId == -1) throw new ServerException("Error en insertNewMedia: no s'ha generat id");
            for (Author a : media.getAuthors()) {
                insertAuthorOfMedia(mediaId, a.getAuthorid());
            }
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return mediaId;
    }
    
    /**
     * insereix les relacions autor-obra corresponents a la taula media_creators
     * @param mediaId
     * @param authorId
     */
    public static void insertAuthorOfMedia(int mediaId, int authorId) {
        String statement = "insert into media_creators (workid, creatorid) values (?, ?)";
        if (insertMediaAuthor == null) {
            insertMediaAuthor = Utils.prepareStatement(statement);
        }
        try {
            insertMediaAuthor.setInt(1, mediaId);
            insertMediaAuthor.setInt(2, authorId);
            insertMediaAuthor.executeUpdate();
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * esborra un media identificat per id de la bd
     * @param mediaId
     */
    public static void deleteMedia(int mediaId) {
        deleteAllAuthorsFromMedia(mediaId);
        
        String statement = "delete from media where workid = ?";
        if (deleteMediaByIdStatement == null) {
            deleteMediaByIdStatement = Utils.prepareStatement(statement);
        }
        try {
            deleteMediaByIdStatement.setInt(1, mediaId);
            deleteMediaByIdStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * esborra totes les relacions author-media de media_creators pel media identificat per id mediaID
     * @param mediaId
     */
    public static void deleteAllAuthorsFromMedia(int mediaId) {
        System.out.println("executant deleteAllAuthorsFromMedia");
        String statement = "delete from media_creators where workid = ?";
        if (deleteAllAuthorsFromMediaStatement == null) {
            deleteAllAuthorsFromMediaStatement = Utils.prepareStatement(statement);
        }
        try {
            deleteAllAuthorsFromMediaStatement.setInt(1, mediaId);
            deleteAllAuthorsFromMediaStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        
    }
    
    /**
     * sobreescriu les dades d'un media identificat per id amb les dades de updatedMedia
     * @param mediaId
     * @param updatedMedia
     */
    public static void updateMedia(int mediaId, Media updatedMedia) {
        String statement = ""
                +   "update Media \n" +
                    "set\n" +
                    "	title = ?,\n" +
                    "	yearpublication = ?,\n" +
                    "	mediatype = ?,\n" +
                    "	media_description = ?\n" +
                    "where workid = ?";
        
        if (updateMediaStatement == null) {
            updateMediaStatement = Utils.prepareStatement(statement);
        }
        
        try {
            updateMediaStatement.setString(1, updatedMedia.getTitle());
            updateMediaStatement.setInt(2, updatedMedia.getYearPublication());
            updateMediaStatement.setString(3, updatedMedia.getMediaTypeAsString());
            updateMediaStatement.setString(4, updatedMedia.getMedia_description());
            updateMediaStatement.setInt(5, mediaId);
            
            updateMediaStatement.executeUpdate();
            System.out.println("UpdateMedia executat");
            updateAuthorsFromMedia(mediaId, updatedMedia);
            
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
    }
    
    private static void updateAuthorsFromMedia(int mediaId, Media updatedMedia) {
        System.out.println("Executant updateAuthorsFromMedia");
        deleteAllAuthorsFromMedia(mediaId);
        System.out.println("deleteAllAuthorsFromMedia executat");
        for(Author a : updatedMedia.getAuthors())
        {
            insertAuthorOfMedia(mediaId, a.getAuthorid());
        }
        System.out.println("UpdateAuthorsFromMedia executat");
    }
    
    /**
     * Crea un objecte Media a partir d'un resultset
     * @param rs
     * @return
     */
    public static Media buildMediaObject(ResultSet rs) {
        Media media = null;
        try {
            media = new Media(
                        rs.getInt("workid"),
                        rs.getString("title"),
                        rs.getInt("yearpublication"),
                        rs.getString("mediatype"),
                        rs.getString("media_description")
                );
        } catch ( ModelException | SQLException ex) {
            throw new ServerException(ex);
        }
        return media;
    }
    
    /**
     * Crea un objecte Media a partir d'un resultset i afegeix una llista d'autors com a creadors d'aquest media
     * @param rs
     * @param authors
     * @return
     */
    public static Media buildMediaObject(ResultSet rs, List<Author> authors) {
        Media media = null;
        try {
            media = new Media(
                        rs.getInt("workid"),
                        rs.getString("title"),
                        rs.getInt("yearpublication"),
                        rs.getString("mediatype"),
                        rs.getString("media_description"),
                        authors
                );
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return media;
    }
    
    /**
     * retorna un media identificat per id
     * @param mediaId
     * @return
     */
    public static Media getMediaById(int mediaId) {
        Media media = null;
        List<Author> authors = getMediaAuthors(mediaId);
        String statement = "select workid, title, yearpublication, mediatype, media_description from media where workid = ?";
        if (selectMediaByIdStatement == null) {
            selectMediaByIdStatement = Utils.prepareStatement(statement);
        }
        try {
            selectMediaByIdStatement.setInt(1, mediaId);
            ResultSet rs = selectMediaByIdStatement.executeQuery();
            while (rs.next()) {
                media = buildMediaObject(rs, authors);
            }
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return media;
    }
    
    /**
     * retorna un arraylist amb tot el media de la bd
     * @return
     */
    public static ArrayList<Media> getAllMedia() {
        ArrayList<Media> allMedia = new ArrayList<Media>();
        String statement = "select workid, title, yearpublication, mediatype, media_description from media";
        System.out.println("preparant query");
        ResultSet rs = Utils.getSelect(statement);
        System.out.println("query llençada");
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("workid") + " " + rs.getString("title"));
                Media media = buildMediaObject(rs);
                ArrayList<Author> authors = getMediaAuthors(media.getWorkId());
                media.setAuthors(authors);
                allMedia.add(media);
            }
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return allMedia;
    }
    
    /**
     * retorna un arraylist amb tots els autors d'un media identificat per id
     * @param mediaId
     * @return
     */
    public static ArrayList<Author> getMediaAuthors(int mediaId) {
        ArrayList<Author> authors = new ArrayList<>();
        try {
            //System.out.println("getMediaAuthors - Buscant autors...");
            String statement = "select authors.authorid, authors.authorname, authors.surname1, authors.surname2, authors.biography, authors.nationality, authors.yearbirth\n" +
                    "from authors\n" +
                    "join media_creators on authors.authorid = media_creators.creatorid\n" +
                    "where media_creators.workid = ?";
            if (selectAuthorOfMediaStatement == null) {
                selectAuthorOfMediaStatement = Utils.prepareStatement(statement);
            }
            selectAuthorOfMediaStatement.setInt(1, mediaId);
            ResultSet rs = selectAuthorOfMediaStatement.executeQuery();
            while (rs.next()) {
                Author a = DBAuthor.buildAuthorObject(rs);
                //System.out.println("getMediaAuthors - Autor trobat: " + a.getFullName());
                authors.add(a);
            }
            
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return authors;
    }

    
}
