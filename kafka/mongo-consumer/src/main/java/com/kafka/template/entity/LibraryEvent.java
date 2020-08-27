package com.kafka.template.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
@Document(collection = "events")
public class LibraryEvent {

    @Id
    private Integer libraryEventId;
    @JsonProperty("libraryEventType")
    @NonNull
    private LibraryEventType libraryEventType;
    @DBRef
    private Book book;

    public LibraryEvent(Integer libraryEventId, LibraryEventType libraryEventType, Book book) {
        this.libraryEventId = libraryEventId;
        this.libraryEventType = libraryEventType;
        this.book = book;
    }

    public LibraryEvent() {
    }

    public Integer getLibraryEventId() {
        return libraryEventId;
    }

    public LibraryEventType getLibraryEventType() {
        return libraryEventType;
    }

    public Book getBook() {
        return book;
    }

    public void setLibraryEventId(Integer libraryEventId) {
        this.libraryEventId = libraryEventId;
    }

    public void setLibraryEventType(LibraryEventType libraryEventType) {
        this.libraryEventType = libraryEventType;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}
