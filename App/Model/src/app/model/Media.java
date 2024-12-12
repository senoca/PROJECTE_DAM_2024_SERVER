/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que emmagatzema les dades d'una obra de la biblioteca. 
 * Cada instància d'aquesta classe representa un exemplar d'una obra 
 * (si hi ha tres exemplars d'un llibre, hi haurà tres instàncies d'aquesta 
 * classe encara que siguin el mateix llibre)
 * @author Sergio
 */
public class Media implements Serializable {
    private int workId;
    private String title;
    private Integer yearPublication;
    private MediaType mediaType;
    private String media_description;
    private List<Author> authors = new ArrayList<Author>();

    /**
     *
     * @param title string titol
     * @param yearPublication  int any de publicació
     * @param mediaType  enum mediatype que indica el tipus d'obra
     * @param media_description string breu descripció de l'obra
     */
    public Media(String title, Integer yearPublication, MediaType mediaType, String media_description) {
        setTitle(title);
        setYearPublication(yearPublication);
        setMediaType(mediaType);
        setMedia_description(media_description);
    }
    
    /**
     * Constructor de Media
     * @param title string titol
     * @param yearPublication int any de publicació
     * @param mediaTypeAsString string convertible a mediatype
     * @param media_description string breu descripció de l'obra
     */
    public Media(String title, Integer yearPublication, String mediaTypeAsString, String media_description) {
        setTitle(title);
        setYearPublication(yearPublication);
        setMediaType(getMediaTypeFromString(mediaTypeAsString));
        setMedia_description(media_description);
    }
    
    /**
     * Constructor de Media
     * @param workId int identificador
     * @param title string titol
     * @param yearPublication int any de publicació
     * @param mediaType enum mediatype que indica el tipus d'obra
     * @param media_description string breu descripció de l'obra
     */
    public Media(int workId, String title, Integer yearPublication, MediaType mediaType, String media_description) {
        setWorkId(workId);
        setTitle(title);
        setYearPublication(yearPublication);
        setMediaType(mediaType);
        setMedia_description(media_description);
    }
    
    /**
     * Constructor de Media
     * @param workId int identificador
     * @param title string titol
     * @param yearPublication int any de publicació
     * @param mediaTypeAsString string convertible a mediatype
     * @param media_description string breu descripció de l'obra
     */
    public Media(int workId, String title, Integer yearPublication, String mediaTypeAsString, String media_description) {
        setWorkId(workId);
        setTitle(title);
        setYearPublication(yearPublication);
        setMediaType(getMediaTypeFromString(mediaTypeAsString));
        setMedia_description(media_description);
    }

    /**
     * Constructor de Media
     * @param workId int identificador
     * @param title String titol
     * @param yearPublication int any de publicació
     * @param mediaType enum mediatype, descriu tipus de media
     * @param media_description string descripció de l'obra
     * @param authors arraylist<Author> llista d'autors
     */
    public Media(int workId, String title, Integer yearPublication, MediaType mediaType, String media_description, List<Author> authors) {
        setWorkId(workId);
        setTitle(title);
        setYearPublication(yearPublication);
        setMediaType(mediaType);
        setMedia_description(media_description);
        setAuthors(authors);
    }

    /**
     * Constructor de Media
     * @param workId int identificador
     * @param title String titol
     * @param yearPublication int any de publicació
     * @param mediaTypeAsString string convertible a mediatype
     * @param media_description string breu descripció de l'obra
     * @param authors arraylist<Author> llista d'autors
     */
    public Media(int workId, String title, Integer yearPublication, String mediaTypeAsString, String media_description, List<Author> authors) {
        setWorkId(workId);
        setTitle(title);
        setYearPublication(yearPublication);
        setMediaType(getMediaTypeFromString(mediaTypeAsString));
        setMedia_description(media_description);
        setAuthors(authors);
    }

    /**
     * Converteix un string a mediatype
     * @param txt string a convertir, ha de ser "BOOK", "MAGAZINE" o "MULTIMEDIA"
     * @return enum mediatype
     */
    public static MediaType getMediaTypeFromString(String txt) {
        if (txt == null || txt.isBlank())
        {
            throw new ModelException("Error en getMediatypeFromString, txt null o empty");
        }
        else if (txt.equals("BOOK")) return MediaType.BOOK;
        else if (txt.equals("MAGAZINE")) return MediaType.BOOK;
        else if (txt.equals("MULTIMEDIA")) return MediaType.MULTIMEDIA;
        throw new ModelException("Error en getMediatypeFromString, txt té un valor invàlid");
    }
    
    /**
     * Converteix mediatype a un string
     * @return string equivalent a mediatype
     */
    public String getMediaTypeAsString() {
        if (this.mediaType == MediaType.BOOK) return "BOOK";
        else if (this.mediaType == MediaType.MAGAZINE) return "MAGAZINE";
        return "MULTIMEDIA";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.workId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Media other = (Media) obj;
        return this.workId == other.workId;
    }
    
    
    
    /**
     * 
     * @return
     */
    public int getWorkId() {
        return workId;
    }

    /**
     *
     * @param workId
     */
    public void setWorkId(int workId) {
        this.workId = workId;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title no pot ser null o buit
     */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ModelException("Títol de media no pot ser nul o buit");
        }
        this.title = title;
    }

    /**
     *
     * @return
     */
    public Integer getYearPublication() {
        return yearPublication;
    }

    /**
     *
     * @param yearPublication
     */
    public void setYearPublication(Integer yearPublication) {
        this.yearPublication = yearPublication;
    }

    /**
     *
     * @return
     */
    public MediaType getMediaType() {
        return mediaType;
    }

    /**
     *
     * @param mediaType no pot ser null
     */
    public void setMediaType(MediaType mediaType) {
        if (mediaType == null) {
            throw new ModelException("Mediatype no pot ser nul");
        }
        this.mediaType = mediaType;
    }

    /**
     *
     * @return
     */
    public String getMedia_description() {
        return media_description;
    }

    /**
     *
     * @param media_description
     */
    public void setMedia_description(String media_description) {
        this.media_description = media_description;
    }

    /**
     *
     * @return
     */
    public List<Author> getAuthors() {
        return authors;
    }
    
    /**
     * Afegeix un autor a la llista d'autors
     * @param author Author a afegir a authors. No pot ser null
     */
    public void addAuthor(Author author) {
        if (author != null) {
            this.authors.add(author);
        }
    }
    
    public void addAuthors(ArrayList<Author> authors) 
    {
        if (authors != null) {
            for (Author a : authors) addAuthor(a);
        }
    }

    /**
     * incorpora una llista d'autors a this.authors, i inicialitza this.authors. Si this.authors ja tenia autors abans, els esborra primer!
     * @param authors
     */
    public void setAuthors(List<Author> authors) {
        if (authors != null) {
            for (Author author : authors) {
                this.authors.add(author);
            }
        }
    }
}
