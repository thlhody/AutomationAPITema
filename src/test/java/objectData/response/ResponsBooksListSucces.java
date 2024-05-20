package objectData.response;

import lombok.Getter;
import objectData.model.BookModel;

import java.util.List;

@Getter
public class ResponsBooksListSucces {
    public List<BookModel> books;
}
