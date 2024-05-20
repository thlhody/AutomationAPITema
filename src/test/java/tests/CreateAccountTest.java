package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import objectData.model.BookModel;
import objectData.model.CollectionOfIsbns;
import objectData.requests.RequestAccount;
import objectData.requests.RequestAddBooks;
import objectData.requests.RequestBook;
import restClient.RestEndPoint;
import restClient.RestStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import propertiesUtility.PropertyUtilitiy;
import objectData.response.ResponsBooksListSucces;
import objectData.response.ResponseAccountSucces;
import objectData.response.ResponseTokenSucces;

import java.util.Collections;
import java.util.HashMap;


public class CreateAccountTest {
    public RequestAccount requestAccountBody;
    public RequestBook requestBook;
    public String userID;
    public String token;

    @Test
    public void testMethod() {
        System.out.println("==== STEP 1: CREATE ACCOUNT ====");
        createAccount();
        System.out.println("==== STEP 2: GENERATE TOKEN ====");
        generateToken();
        System.out.println("==== STEP 3: CHECK ACCOUNT  ====");
        checkAccountExists();
        System.out.println("==== STEP 4: GET BOOKSTORE BOOKS LIST ====");
        //getAllBookList();
        System.out.println("==== STEP 6: ADD BOOKS TO USER ====");
        addBooksToUser();
        System.out.println("==== STEP 7: CHECK ACCOUNT  ====");
        checkAccountExists();
        System.out.println("==== STEP 8: DELETE USER    ====");
        //deleteUser();
        System.out.println("==== STEP 9: CHECK ACCOUNT  ====");
        //checkAccountExists();

    }

    public void createAccount() {

        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        PropertyUtilitiy propertyUtilitiy = new PropertyUtilitiy("request/CreateAccountData");
        requestAccountBody = new RequestAccount(propertyUtilitiy.getAllData());

        requestSpecification.body(requestAccountBody);
        Response response = requestSpecification.post(RestEndPoint.ACCOUNT_USER);
        System.out.println(requestAccountBody.getUserName());
        System.out.println(requestAccountBody.getPassword());

        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_201));
        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_CREATED));
        System.out.println(response.getStatusLine());

        ResponseAccountSucces responseAccountSucces = response.body().as(ResponseAccountSucces.class);
        Assert.assertEquals(requestAccountBody.getUserName(), responseAccountSucces.getUsername());
        userID = responseAccountSucces.getUserID();
        System.out.println("UserID: "+userID);
    }

    public void generateToken() {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        requestSpecification.body(requestAccountBody);
        Response response = requestSpecification.post(RestEndPoint.ACCOUNT_TOKEN);
        System.out.println(response.getStatusLine());
        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_200));
        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_OK));
        ResponseTokenSucces responseTokenSucces = response.body().as(ResponseTokenSucces.class);
        token = responseTokenSucces.getToken();
        System.out.println(token);

    }
    public void checkAccountExists() {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");
        requestSpecification.header("Authorization", "Bearer " + token);

        Response response = requestSpecification.get(RestEndPoint.ACCOUNT_GET_USER + userID);

        if (response.getStatusLine().contains(RestStatus.SC_200)) {
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_200));
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_OK));
            System.out.println(response.getStatusLine());
        } else if (response.getStatusLine().contains(RestStatus.SC_502)) {
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_502));
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_BADGATEWAY));
            System.out.println(response.getStatusLine());
        } else {
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_401));
            Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_UNAUTHORIZED));
            System.out.println(response.getStatusLine());
        }
    }

    public void deleteUser (){
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");
        requestSpecification.header("Authorization", "Bearer " + token);
        Response response = requestSpecification.delete(RestEndPoint.ACCOUNT_DELETE_USER + userID);
        System.out.println(response.getStatusLine());
        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_204));
        Assert.assertTrue(response.getStatusLine().contains(RestStatus.SC_NOCONTENT));
    }

    public void getAllBookList() {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        Response response = requestSpecification.get(RestEndPoint.BOOKSTORE_GET_BOOKS);
        ResponsBooksListSucces responsBooksListSucces = response.body().as(ResponsBooksListSucces.class);
        System.out.println(responsBooksListSucces.getBooks().size());
        for (BookModel book : responsBooksListSucces.getBooks()) {
            System.out.println("isbn: " + book.getIsbn());
        }
    }

    public void authorizedUser(){
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");
        requestSpecification.body(requestAccountBody);
        Response responseAuth = requestSpecification.post(RestEndPoint.ACCOUNT_AUTHORIZED);
        System.out.println(responseAuth.getStatusLine());
    }


    public void addBooksToUser() {

        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");
        PropertyUtilitiy propertyUtilitiy = new PropertyUtilitiy("bookStoreBooks/AddBooksToUser");
        HashMap<String,String> addBooksData = propertyUtilitiy.getAllData();
        addBooksData.put("userId",userID);

        requestBook = new RequestBook(addBooksData);
        RequestAddBooks requestAddBooks = new RequestAddBooks();
        requestAddBooks.setUserId(requestBook.getUserId());
        requestAddBooks.setIsbns(requestBook.getCollectionOfIsbns());

        requestSpecification.header("Authorization", "Bearer " + token);
        requestSpecification.body(requestAddBooks);

        Response responseBody = requestSpecification.post(RestEndPoint.BOOKSTORE_POST_BOOKS);
        System.out.println(responseBody.getStatusLine());
        responseBody.body().prettyPrint();

    }
}
