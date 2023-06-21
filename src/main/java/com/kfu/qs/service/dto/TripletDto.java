package com.kfu.qs.service.dto;

public class TripletDto {
    private String subject;
    private String object;
    private String predicate;

    public TripletDto(String subject, String object, String predicate) {
        this.subject = subject;
        this.object = object;
        this.predicate = predicate;
    }

    public TripletDto() {

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    // Constructor, getters, and setters

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }
}
