
package com.mindia.avisosnick.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "string1",
    "string2"
})
public class PojoDoubleString {

    @JsonProperty("string1")
    private String string1;
    @JsonProperty("string2")
    private String string2;

    @JsonProperty("string1")
    public String getString1() {
        return string1;
    }

    @JsonProperty("string1")
    public void setString1(String string1) {
        this.string1 = string1;
    }

    @JsonProperty("string2")
    public String getString2() {
        return string2;
    }

    @JsonProperty("string2")
    public void setString2(String string2) {
        this.string2 = string2;
    }

}
