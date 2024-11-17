/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergio
 */
public class Media {
    private int workId;
    private String title;
    private Integer yearPublication;
    private MediaType mediaType;
    private String media_description;
    private List<Author> authors;

    public Media(int workId, String title, Integer yearPublication, MediaType mediaType, String media_description) {
        setWorkId(workId);
        setTitle(title);
        setYearPublication(yearPublication);
        setMediaType(mediaType);
        setMedia_description(media_description);
        setAuthors(authors);
    }

    public Media(int workId, String title, Integer yearPublication, MediaType mediaType, String media_description, List<Author> authors) {
        setWorkId(workId);
        setTitle(title);
        setYearPublication(yearPublication);
        setMediaType(mediaType);
        setMedia_description(media_description);
        setAuthors(authors);
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ModelException("Títol de media no pot ser nul o buit");
        }
        this.title = title;
    }

    public Integer getYearPublication() {
        return yearPublication;
    }

    public void setYearPublication(Integer yearPublication) {
        this.yearPublication = yearPublication;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        if (mediaType == null) {
            throw new ModelException("Mediatype no pot ser nul");
        }
        this.mediaType = mediaType;
    }

    public String getMedia_description() {
        return media_description;
    }

    public void setMedia_description(String media_description) {
        this.media_description = media_description;
    }

    public List<Author> getAuthors() {
        return authors;
    }
    
    public void addAuthor(Author author) {
        if (author != null) {
            this.authors.add(author);
        }
    }

    public void setAuthors(List<Author> authors) {
        if (this.authors == null) {
            authors = new ArrayList<Author>();
        }
        
        if (authors != null) {
            for (Author author : authors) {
                this.authors.add(author);
            }
        }
    }
}
