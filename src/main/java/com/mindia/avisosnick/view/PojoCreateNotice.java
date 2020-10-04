
package com.mindia.avisosnick.view;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mails",
    "sendNotification",
    "title",
    "description"
})
public class PojoCreateNotice {

    @JsonProperty("mails")
    private List<String> mails = null;
    @JsonProperty("sendNotification")
    private boolean sendNotification;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;

    @JsonProperty("mails")
    public List<String> getMails() {
        return mails;
    }

    @JsonProperty("mails")
    public void setMails(List<String> mails) {
        this.mails = mails;
    }

    @JsonProperty("sendNotification")
    public boolean isSendNotification() {
        return sendNotification;
    }

    @JsonProperty("sendNotification")
    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
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

}
