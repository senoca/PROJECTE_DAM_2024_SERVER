/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.entity;

import app.servidor.app.ServerException;
import app.servidor.app.Utils;
import app.model.Author;
import app.model.Media;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBAuthor guarda les funcions per administrar les consultes, altes, baixes o modificacions referents a la taula AUTHORS
 * @author Sergio
 */
public class DBAuthor {
    
    private static PreparedStatement insertAuthorDataStatement = null;
    private static PreparedStatement insertAuthorBibliographyStatement = null;
    private static PreparedStatement deleteAuthorStatement = null;
    private static PreparedStatement selectAuthorByIdStatement = null;
    private static PreparedStatement updateAuthorByIdStatement = null;
    
    public static int insertNewAuthor(Author author) {
        if (author == null)
        {
            throw new ServerException("Error en insertNewAuthor: author nul");
        }
        int authorID = insertNewAuthorData(author);
        //insertNewAuthorBibliography(author.getWorks());
        
        return authorID;
    }
    
    public static void deleteAuthor(int authorId) {
        try {
            /*
            Només s'esborrarà si l'autor no té cap obra a la base de dades,
            primer cal esborrar les obres
            */
            String statement = "delete from AUTHORS where authorid = ?";
            if (deleteAuthorStatement == null) {
                Utils.prepareStatement(statement);
            }
            deleteAuthorStatement.setInt(1, authorId);
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
    }
    
    public static void updateNewAuthor(int authorId, Author updatedAuthor) {
        try {
            String statement = ""
                    + "update AUTHORS set "
                    + "authorname = ?,"
                    + "surname1 = ?,"
                    + "surname2 = ?,"
                    + "biography = ?, "
                    + "nationality = ?,"
                    + "yearbirth = ? "
                    + "where authorid = ?";
            
            if (updateAuthorByIdStatement == null) {
                updateAuthorByIdStatement = Utils.prepareStatement(statement);
            }
            updateAuthorByIdStatement.setString(1, updatedAuthor.getAuthorname());
            updateAuthorByIdStatement.setString(2, updatedAuthor.getSurname1());
            updateAuthorByIdStatement.setString(3, updatedAuthor.getSurname2());
            updateAuthorByIdStatement.setString(4, updatedAuthor.getBiography());
            updateAuthorByIdStatement.setString(5, updatedAuthor.getNationality());
            updateAuthorByIdStatement.setInt(6, updatedAuthor.getYearbirth());
            updateAuthorByIdStatement.setInt(7, authorId);
            
            updateAuthorByIdStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
    }
    /*
    public static List<Media> getAuthorBibliography(int authorId) {
        List<Media> bibliography = new ArrayList<>();
        String statement =  "select media.workid, media.title, media.yearpublication, media.mediatype, media.media_description\n" +
                            "from media\n" +
                            "join media_creators on media.workid = media_creators.workid\n" +
                            "where media_creators.creatorid = ?";
        
        return bibliography;
    }
    */
    public static Author getAuthor(int authorId) {
        Author author = null;
        try {
            String statement = ""
                    + "select authorid, authorname, surname1, surname2, biography, nationality, yearbirth "
                    + "from authors "
                    + "where authorid = ?";
            if (insertAuthorDataStatement == null) {
                selectAuthorByIdStatement = Utils.prepareStatement(statement);
            }
            selectAuthorByIdStatement.setInt(1, authorId);
            ResultSet rs = selectAuthorByIdStatement.executeQuery();
            if (rs.next()) {
                author = buildAuthorObject(rs);
                System.out.println("Autor trobat: " + author.getAuthorname());
            } else {
                System.out.println("Autor " + authorId + " no trobat");
            }
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return author;
    }
    
    public static ArrayList<Author> getAllAuthors() {
        ArrayList<Author> authors = new ArrayList<>();
        String statement = ""
                    + "select authorid, authorname, surname1, surname2, biography, nationality, yearbirth "
                    + "from authors";
        ResultSet rs = Utils.getSelect(statement);
        try {
            while (rs.next()) {
                Author a = buildAuthorObject(rs);
                authors.add(a);
            }
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return authors;
    }

    private static int insertNewAuthorData(Author author) {
        int authorId = -1;
        try {
            String statement = "insert into AUTHORS"
                    + "("
                    + "authorname, "
                    + "surname1,"
                    + "surname2,"
                    + "biography,"
                    + "nationality,"
                    + "yearbirth"
                    + ") "
                    + "values (?,?,?,?,?,?) "
                    + "returning authorid";
            if (insertAuthorDataStatement == null) {
                insertAuthorDataStatement = Utils.prepareStatement(statement);
            }
            insertAuthorDataStatement.setString(1, author.getAuthorname());
            insertAuthorDataStatement.setString(2, author.getSurname1());
            insertAuthorDataStatement.setString(3, author.getSurname2());
            insertAuthorDataStatement.setString(4, author.getBiography());
            insertAuthorDataStatement.setString(5, author.getNationality());
            insertAuthorDataStatement.setInt(6, author.getYearbirth());
            
            insertAuthorDataStatement.execute();
            ResultSet rs = insertAuthorDataStatement.getResultSet();
            if (rs.next()) {
                authorId = rs.getInt("authorid");
            }
            if (authorId == -1) throw new ServerException("Error en insertNewAuthorData: no s'ha generat correctament authorid");
        } catch (SQLException ex) {
            throw new ServerException(ex);
        } 
        return authorId;
    }

    private static void insertNewAuthorBibliography(List<Media> works) {
        /*
        De moment he decidit que l'autoria de cada obra, s'introdueix al inserir 
        o modificar una obra, i no al inserir o modificar un autor.
        Si més endavant surt la necessitat, completar això.
        */
    }

    public static Author buildAuthorObject(ResultSet rs) {
        Author a = null;
        try {
            
            a = new Author(
                    rs.getInt("authorid"),
                    rs.getString("authorname"),
                    rs.getString("surname1"),
                    rs.getString("surname2"),
                    rs.getString("biography"),
                    rs.getString("nationality"),
                    rs.getInt("yearbirth")
            );
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return a;
    }
    
    
}
