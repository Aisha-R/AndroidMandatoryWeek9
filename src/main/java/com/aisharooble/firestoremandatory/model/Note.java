package com.aisharooble.firestoremandatory.model;

public class Note {

    String head = "";
    String body = "";
    String id = "";

    public Note(String id, String head, String body) {
        this.head = head;
        this.body = body;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
