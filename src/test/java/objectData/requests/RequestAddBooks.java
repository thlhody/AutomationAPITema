package objectData.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import objectData.model.CollectionOfIsbns;

import java.util.List;
@Setter
@Getter
public class RequestAddBooks {

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("collectionOfIsbns")
    private List<CollectionOfIsbns> isbns;
}
