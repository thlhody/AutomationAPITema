package objectData.requests;

import lombok.Getter;
import lombok.Setter;
import objectData.model.CollectionOfIsbns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
public class RequestBook {
    private String userId;
    private List<CollectionOfIsbns> collectionOfIsbns;


    public RequestBook(HashMap<String, String> testData) {
        populateObject(testData);
    }

    public void populateObject(HashMap<String, String> testData) {
        for (String key : testData.keySet()) {
            switch (key) {
                case "userId":
                    setUserId(testData.get(key));
                    break;
                case "collectionOfIsbns":
                    //trebuie sa fac o metoda privata care sa parseze valoarea de la key si
                    // sa adauge elementel dupa split in lista de collections of isbn
                    setCollectionOfIsbns(getPreparedValue(testData.get(key)));
            }

        }
    }

    private List<CollectionOfIsbns> getPreparedValue(String text) {
        List<CollectionOfIsbns> result = new ArrayList<>();
        String[] valueSplit = text.split(",");
        for (String isbns : valueSplit) {
            result.add(new CollectionOfIsbns(isbns));
        }
        return result;
    }
}