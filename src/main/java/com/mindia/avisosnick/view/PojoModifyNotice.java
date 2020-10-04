
package com.mindia.avisosnick.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "idNotice",
    "title",
    "description"
})
public class PojoModifyNotice {

    @JsonProperty("idNotice")
    private String idNotice;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;

    @JsonProperty("idNotice")
    public String getIdNotice() {
        return idNotice;
    }

    @JsonProperty("idNotice")
    public void setIdNotice(String idNotice) {
        this.idNotice = idNotice;
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
