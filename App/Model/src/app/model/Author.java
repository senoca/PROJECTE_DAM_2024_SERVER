/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.model;

import java.util.ArrayList;
import java.util.List;


public class Author {
    private int authorid;
    private String authorname;
    private String surname1;
    private String surname2;
    private String biography;
    private String nationality;
    private int yearbirth;
    //private List<Media> works;

    public Author(int authorid, String authorname, String biography, String nationality, int yearbirth) {
        setAuthorid(authorid);
        setAuthorname(authorname);
        setBiography(biography);
        setNationality(nationality);
        setYearbirth(yearbirth);
    }
    
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

    
    
    
    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        // ID ha de ser positiu , i únic
        if (authorid < 0) throw new ModelException("ERROR: ID ha de ser positiu");
        this.authorid = authorid;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        if (authorname == null) throw new ModelException("ERROR: authorname no pot ser nul");
        else if (authorname.isBlank()) throw new ModelException("ERROR: authorname no pot estar buit");
        this.authorname = authorname;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        if (biography == null) throw new ModelException("ERROR: biography no pot ser nul");
        else if (biography.isBlank()) throw new ModelException("ERROR: biography no pot estar buit");
        this.biography = biography;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        if (nationality == null) throw new ModelException("ERROR: nationality no pot ser nul");
        else if (nationality.isBlank()) throw new ModelException("ERROR: nationality no pot estar buit");
        this.nationality = nationality;
    }

    public int getYearbirth() {
        return yearbirth;
    }

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
