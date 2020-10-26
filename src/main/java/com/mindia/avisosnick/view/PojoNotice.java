
package com.mindia.avisosnick.view;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "title",
    "description",
    "author",
    "creationDate",
    "mails"
})
public class PojoNotice {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("author")
    private String author;
    @JsonProperty("creationDate")
    private String creationDate;
    @JsonProperty("mails")
    private List<String> mails;
    @JsonProperty("readed")
    private boolean readed;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty("creationDate")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creationDate")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("mails")
	public List<String> getMails() {
		return mails;
	}

    @JsonProperty("mails")
	public void setMails(List<String> mails) {
		this.mails = mails;
	}

    @JsonProperty("readed")
	public boolean isReaded() {
		return readed;
	}

    @JsonProperty("readed")
	public void setReaded(boolean readed) {
		this.readed = readed;
	}

}
