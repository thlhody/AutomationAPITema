package objectData.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionOfIsbns {

    @JsonProperty("isbn")
    private String isbn;

    public CollectionOfIsbns(String isbn){
        this.isbn = isbn;
    }
    public CollectionOfIsbns(){

    }
}
