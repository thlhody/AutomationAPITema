package objectData.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class ResponseBookSucces {

    @JsonProperty("isbn")
    private String isbn;
}
