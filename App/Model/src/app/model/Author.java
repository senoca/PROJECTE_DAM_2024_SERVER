/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergio
 * Aquesta classe guarda la informació referent a cada autor de les obres desades a la biblioteca
 */
public class Author implements Serializable {
    private int authorid;
    private String authorname;
    private String surname1;
    private String surname2;
    private String biography;
    private String nationality;
    private int yearbirth;
    //private List<Media> works;

    /**
     * Constructor de la classe Autor
     * @param authorid identificador de l'autor
     * @param authorname nom de l'autor
     * @param biography breu biografia de l'autor
     * @param nationality nacionalitat de l'autor
     * @param yearbirth any de naixement de l'autor
     */
    public Author(int authorid, String authorname, String biography, String nationality, int yearbirth) {
        setAuthorid(authorid);
        setAuthorname(authorname);
        setBiography(biography);
        setNationality(nationality);
        setYearbirth(yearbirth);
    }
    
    /**
     * Constructor de la classe Autor
     * @param authorid identificador de l'autor
     * @param authorname nom de l'autor
     * @param surname1 cognom 1 de l'autor
     * @param surname2 cognom 2 de l'autor
     * @param biography breu biografia de l'autor
     * @param nationality nacionalitat de l'autor
     * @param yearbirth any de naixement de l'autor
     */
    public Author(int authorid, String authorname, String surname1, String surname2, String biography, String nationality, int yearbirth/*, List<Media> works*/) {
        setAuthorid(authorid);
        setAuthorname(authorname);
        setSurname1(surname1);
        setSurname2(surname2);
        setBiography(biography);
        setNationality(nationality);
        setYearbirth(yearbirth);
        //setWorks(works);
    }

    /**
     * Constructor de la classe Autor
     * @param authorname nom de l'autor
     * @param biography breu biografia de l'autor
     * @param nationality nacionalitat de l'autor
     * @param yearbirth any de naixement de l'autor
     */
    public Author(String authorname, String biography, String nationality, int yearbirth) {
        setAuthorname(authorname);
        setBiography(biography);
        setNationality(nationality);
        setYearbirth(yearbirth);
    }

    /**
     * Getter de authorid
     * @return authorid
     */
    public int getAuthorid() {
        return authorid;
    }

    /**
     * Setter de authorid
     * @param authorid ha de ser major de 0
     */
    public void setAuthorid(int authorid) {
        // ID ha de ser positiu , i únic
        if (authorid < 0) throw new ModelException("ERROR: ID ha de ser positiu");
        this.authorid = authorid;
    }

    /**
     * Getter de authorname
     * @return authorname
     */
    public String getAuthorname() {
        return authorname;
    }

    /**
     * setter d'authorname
     * @param authorname no pot ser buit o nul
     */
    public void setAuthorname(String authorname) {
        if (authorname == null) throw new ModelException("ERROR: authorname no pot ser nul");
        else if (authorname.isBlank()) throw new ModelException("ERROR: authorname no pot estar buit");
        this.authorname = authorname;
    }

    /**
     * getter de surname1
     * @return
     */
    public String getSurname1() {
        return surname1;
    }

    /**
     * setter de surname1
     * @param surname1
     */
    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    /**
     * getter de surname2
     * @return
     */
    public String getSurname2() {
        return surname2;
    }

    /**
     * setter de surname2
     * @param surname2
     */
    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    /**
     * getter de biography
     * @return
     */
    public String getBiography() {
        return biography;
    }

    /**
     * setter de biography
     * @param biography no pot ser buit o nul
     */
    public void setBiography(String biography) {
        if (biography == null) throw new ModelException("ERROR: biography no pot ser nul");
        else if (biography.isBlank()) throw new ModelException("ERROR: biography no pot estar buit");
        this.biography = biography;
    }

    /**
     * getter de nationality
     * @return
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * setter de nationality
     * @param nationality no pot ser null o buit
     */
    public void setNationality(String nationality) {
        if (nationality == null) throw new ModelException("ERROR: nationality no pot ser nul");
        else if (nationality.isBlank()) throw new ModelException("ERROR: nationality no pot estar buit");
        this.nationality = nationality;
    }

    /**
     * getter de yearbirth
     * @return yearbirth
     */
    public int getYearbirth() {
        return yearbirth;
    }

    /**
     * setter de yearbirth
     * @param yearbirth
     */
    public void setYearbirth(int yearbirth) {
        this.yearbirth = yearbirth;
    }
/*
    public List<Media> getWorks() {
        return works;
    }

    public void setWorks(List<Media> works) {
        if (works == null) this.works = new ArrayList<Media>();
        else this.works = works;
    }
    
    public void addWorkToBibliography(Media work) {
        works.add(work);
    }
  */  
    
}
