import java.util.logging.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import utils.DatabaseConnector;
import utils.ConnectConfig;
import queries.*;
import entities.*;
import java.util.List;
import java.util.ArrayList;

class CardHandler implements HttpHandler {

    private DatabaseConnector connector;
    private LibraryManagementSystem library;
    private static ConnectConfig connectConfig = null;
    static {
        try {
            connectConfig = new ConnectConfig();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public CardHandler() {
        try {
            // library = new LibraryManagementSystemImpl(connector);
            System.out.println("CardHandler successfully init class BookTest.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder requestBodyBuilder = new StringBuilder();
        String line, name = "", department = "", type = "", id = "";
        Card newCard = new Card();
        line = reader.readLine();
        requestBodyBuilder.append(line);
        // line : {"name":"1","department":"1","type":"Student"}
        // System.err.println(line);
        line = line.substring(1, line.length() - 1);
        String[] parts = line.split(",");
        if (parts.length == 3) {
            name = parts[0].split(":")[1].replace("\"", "");
            department = parts[1].split(":")[1].replace("\"", "");
            type = parts[2].split(":")[1].replace("\"", "");
            newCard.setDepartment(department);
            newCard.setName(name);
            newCard.setType(Card.CardType.values(type.substring(0, 1)));
        } else if (parts.length == 4){ // modify
            id = parts[0].split(":")[1].replace("\"", "");
            name = parts[1].split(":")[1].replace("\"", "");
            department = parts[2].split(":")[1].replace("\"", "");
            type = parts[3].split(":")[1].replace("\"", "");
            newCard.setCardId(Integer.parseInt(id));
            newCard.setDepartment(department);
            newCard.setName(name);
            newCard.setType(Card.CardType.values(type.substring(0, 1)));
        } else { // parts == 1, remove
            id = parts[0].split(":")[1].replace("\"", "");
            newCard.setCardId(Integer.parseInt(id));
        }
        // System.err.println(name); // Student -> S
        // System.err.println(department);
        // System.err.println(type.substring(0, 1));
        
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);
        // System.err.println("---------------");
        if (id != "" && name != "") {
            System.err.println(library.modifyCard(newCard).message);
        } else if (id != "") {
            System.err.println(library.removeCard(newCard.getCardId()).message); 
        } else {
            System.err.println(library.registerCard(newCard).message);
        }
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write("Card created successfully".getBytes());
        outputStream.close();
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();
        ApiResult result = library.showCards();
        String response = ((CardList) result.payload).toJson();
        System.out.println(response);
        outputStream.write(response.getBytes());
        outputStream.close();
    }
    private void handleOptionRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();
        String response = "Option request received";
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        connector = new DatabaseConnector(connectConfig);
        connector.connect();
        library = new LibraryManagementSystemImpl(connector);
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            handleGetRequest(exchange);
        } else if (requestMethod.equals("POST")) {
            handlePostRequest(exchange);
        } else if (requestMethod.equals("OPTIONS")) {
            handleOptionRequest(exchange);
            
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
        connector.release();
    }
}

class BorrowHandler implements HttpHandler {

    private DatabaseConnector connector;
    private LibraryManagementSystem library;
    private static ConnectConfig connectConfig = null;
    static {
        try {
            connectConfig = new ConnectConfig();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public BorrowHandler() {
        try {
            // library = new LibraryManagementSystemImpl(connector);
            System.out.println("BorrowHandler successfully init class BookTest.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {

        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder requestBodyBuilder = new StringBuilder();
        String line = reader.readLine(), category = "", title = "", press = "", author = "", price = "", stock = "", publishYear = "";
        requestBodyBuilder.append(line);
        // borrow or return a book    
        /*
            borrowBookInfo: {
                bookID: 0,
                cardID: 0,
                borrowTime: ''
            }
            returnBookInfo: {
                bookID: 0,
                cardID: 0,
                borrowTime: '',
                returnTime: ''
            }
         */

        line = line.substring(1, line.length() - 1);
        String[] parts = line.split(",");
        Borrow newBorrow = new Borrow();
        int out = 0;
        if (parts.length == 3) {
            int bookID = Integer.parseInt(parts[0].split(":")[1].replace("\"", ""));
            int cardID = Integer.parseInt(parts[1].split(":")[1].replace("\"", ""));                                     
            String borrowTime = parts[2].split(":")[1].replace("\"", "");
            newBorrow.setBookId(bookID);
            newBorrow.setCardId(cardID);
            newBorrow.setBorrowTime(Long.parseLong(borrowTime));
            ApiResult res = library.borrowBook(newBorrow);
            out = res.ok == false ? 0 : 1;            
            System.err.println(res.message);
        } else {
            int bookID = Integer.parseInt(parts[0].split(":")[1].replace("\"", ""));
            int cardID = Integer.parseInt(parts[1].split(":")[1].replace("\"", ""));
            String borrowTime = parts[2].split(":")[1].replace("\"", "");
            String returnTime = parts[3].split(":")[1].replace("\"", "");
            System.err.println(bookID + " " + cardID + " " + borrowTime + " " + returnTime);
            newBorrow.setBookId(bookID);
            newBorrow.setCardId(cardID);
            newBorrow.setBorrowTime(Long.parseLong(borrowTime));
            newBorrow.setReturnTime(Long.parseLong(returnTime));
            ApiResult res = library.returnBook(newBorrow);
            out = res.ok == false ? 0 : 1;            
            System.err.println(res.message);

        }
        // System.out.println("Received POST request to create card with data: " + requestBodyBuilder.toString());

        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(String.valueOf(out).getBytes());
        outputStream.close();

    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        int cardID = Integer.parseInt(query.substring(7));
        System.out.println("Received GET request to show borrow history with card ID: " + cardID);
        String response = "No card ID provided";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();        
        response = ((BorrowHistories)library.showBorrowHistory(cardID).payload).toJson();        
        System.out.println(response);
        outputStream.write(response.getBytes());
        outputStream.close();
    }
    private void handleOptionRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();
        String response = "Option request received";
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        connector = new DatabaseConnector(connectConfig);
        connector.connect();
        library = new LibraryManagementSystemImpl(connector);
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            handleGetRequest(exchange);
        } else if (requestMethod.equals("POST")) {
            handlePostRequest(exchange);
        } else if (requestMethod.equals("OPTIONS")) {
            handleOptionRequest(exchange);
            
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
        connector.release();
    }
}

class BookHandler implements HttpHandler {

    private DatabaseConnector connector;
    private LibraryManagementSystem library;
    private static ConnectConfig connectConfig = null;
    static {
        try {
            connectConfig = new ConnectConfig();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public BookHandler() {
        try {
            System.out.println("BookHandler successfully init class BookTest.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        connector = new DatabaseConnector(connectConfig);
        connector.connect();

        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder requestBodyBuilder = new StringBuilder();
        String line = reader.readLine(), category = "", title = "", press = "", author = "", price = "", stock = "", publishYear = "";
        requestBodyBuilder.append(line);
        line = line.substring(1, line.length() - 1);
        String[] parts = line.split(",");
        Book newBook = new Book();
        int out = 0;
        if (parts.length == 7) {
            category = parts[0].split(":")[1].replace("\"", "");
            title = parts[1].split(":")[1].replace("\"", "");
            press = parts[2].split(":")[1].replace("\"", "");
            publishYear = parts[3].split(":")[1].replace("\"", "");
            author = parts[4].split(":")[1].replace("\"", "");
            price = parts[5].split(":")[1].replace("\"", "");
            stock = parts[6].split(":")[1].replace("\"", "");
            newBook.setCategory(category);
            newBook.setTitle(title);
            newBook.setPress(press);
            newBook.setPublishYear(Integer.parseInt(publishYear));
            newBook.setAuthor(author);
            newBook.setPrice(Double.parseDouble(price));
            newBook.setStock(Integer.parseInt(stock));
            out = library.storeBook(newBook).ok == false ? 0 : 1;
        } else if (parts.length == 1) {
            int bookID = Integer.parseInt(parts[0].split(":")[1].replace("\"", ""));
            out = library.removeBook(bookID).ok == false ? 0 : 1;
        } else {
            int bookID = Integer.parseInt(parts[0].split(":")[1].replace("\"", ""));
            category = parts[1].split(":")[1].replace("\"", "");
            title = parts[2].split(":")[1].replace("\"", "");
            press = parts[3].split(":")[1].replace("\"", "");
            publishYear = parts[4].split(":")[1].replace("\"", "");
            author = parts[5].split(":")[1].replace("\"", "");
            price = parts[6].split(":")[1].replace("\"", "");
            stock = parts[7].split(":")[1].replace("\"", "");
            newBook.setBookId(bookID);
            newBook.setCategory(category);
            newBook.setTitle(title);
            newBook.setPress(press);
            newBook.setPublishYear(Integer.parseInt(publishYear));
            newBook.setAuthor(author);
            newBook.setPrice(Double.parseDouble(price));
            newBook.setStock(Integer.parseInt(stock));
            ApiResult res = library.modifyBookInfo_super(newBook);
            out = res.ok == false ? 0 : 1;
            System.err.println(res.message);
        }
        // System.out.println("Received POST request to create card with data: " + requestBodyBuilder.toString());

        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(String.valueOf(out).getBytes());
        outputStream.close();
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        String line = exchange.getRequestURI().getQuery();
        // System.err.println(line);
        String[] parts = line.split("&");
        BookQueryConditions conditions = new BookQueryConditions();
        for (String part : parts) {
            String[] keyValue = part.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";
            // System.err.println(key + " " + value);
            if (value == "") continue;
            if (key.equals("category")) {
                conditions.setCategory(value);
            } else if (key.equals("title")) {
                conditions.setTitle(value);
            } else if (key.equals("press")) {
                conditions.setPress(value);
            } else if (key.equals("minPublishYear")) {
                conditions.setMinPublishYear(Integer.parseInt(value));
            } else if (key.equals("maxPublishYear")) {
                conditions.setMaxPublishYear(Integer.parseInt(value));
            } else if (key.equals("author")) {
                conditions.setAuthor(value);
            } else if (key.equals("minPrice")) {
                conditions.setMinPrice(Double.parseDouble(value));
            } else if (key.equals("maxPrice")) {
                conditions.setMaxPrice(Double.parseDouble(value));
            } else if (key.equals("sortBy")) {
                switch (value) {
                    case "bookID":
                        conditions.setSortBy(Book.SortColumn.BOOK_ID);
                        break;
                    case "category":
                        conditions.setSortBy(Book.SortColumn.CATEGORY);
                        break;
                    case "title":
                        conditions.setSortBy(Book.SortColumn.TITLE);
                        break;
                    case "press":
                        conditions.setSortBy(Book.SortColumn.PRESS);
                        break;
                    case "publishYear":
                        conditions.setSortBy(Book.SortColumn.PUBLISH_YEAR);
                        break;
                    case "author":
                        conditions.setSortBy(Book.SortColumn.AUTHOR);
                        break;
                    case "price":
                        conditions.setSortBy(Book.SortColumn.PRICE);
                        break;
                    case "stock":
                        conditions.setSortBy(Book.SortColumn.STOCK);
                        break;
                }
            } else if (key.equals("sortOrder")) {
                switch (value) {
                    case "ASC":
                        conditions.setSortOrder(SortOrder.ASC);
                        break;
                    case "DESC":
                        conditions.setSortOrder(SortOrder.DESC);
                        break;
                }
            }
        }
        ApiResult result = library.queryBook(conditions);
        OutputStream outputStream = exchange.getResponseBody();        
        String response = ((BookQueryResults)result.payload).toJsonString();      
        System.err.println(response);
        outputStream.write(response.getBytes());
        outputStream.close();
    }
    private void handleOptionRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();
        String response = "Option request received";
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        connector = new DatabaseConnector(connectConfig);
        library = new LibraryManagementSystemImpl(connector);
        connector.connect();
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            handleGetRequest(exchange);
        } else if (requestMethod.equals("POST")) {
            handlePostRequest(exchange);
        } else if (requestMethod.equals("OPTIONS")) {
            handleOptionRequest(exchange);
            
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
        connector.release();
    }
}


class BooksHandler implements HttpHandler {

    private DatabaseConnector connector;
    private LibraryManagementSystem library;
    private static ConnectConfig connectConfig = null;
    static {
        try {
            connectConfig = new ConnectConfig();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public BooksHandler() {
        try {
            System.out.println("BookHandler successfully init class BookTest.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        connector = new DatabaseConnector(connectConfig);
        connector.connect();

        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        
        StringBuilder requestBodyBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBodyBuilder.append(line);
        }
        String jsonString = requestBodyBuilder.toString();
        System.out.println("Received POST request to create books with data: " + jsonString);
        List<Book> books = new ArrayList<>();
        String[] parts = jsonString.split("},");
        for (String part : parts) {
            part = part.substring(1);
            String[] fields = part.split(",");
            Book newBook = new Book();
            for (String field : fields) {
                String[] keyValue = field.split(":");
                String key = keyValue[0].replace("\"", "");
                String value = keyValue[1].replace("\"", "");
                if (key.equals("category")) {
                    newBook.setCategory(value);
                } else if (key.equals("title")) {
                    newBook.setTitle(value);
                } else if (key.equals("press")) {
                    newBook.setPress(value);
                } else if (key.equals("publishYear")) {
                    newBook.setPublishYear(Integer.parseInt(value));
                } else if (key.equals("author")) {
                    newBook.setAuthor(value);
                } else if (key.equals("price")) {
                    newBook.setPrice(Double.parseDouble(value));
                } else if (key.equals("stock")) {
                    newBook.setStock(Integer.parseInt(value));
                }
            }
            books.add(newBook);
        }
        ApiResult result = library.storeBook(books);
        int out = result.ok == false ? 0 : 1;

        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(String.valueOf(out).getBytes());
        outputStream.close();
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        connector = new DatabaseConnector(connectConfig);
        library = new LibraryManagementSystemImpl(connector);
        connector.connect();
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "POST");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("POST")) {
            handlePostRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
        connector.release();
    }
}

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/card", new CardHandler());
            server.createContext("/borrow", new BorrowHandler());
            server.createContext("/book", new BookHandler());
            server.createContext("/books", new BooksHandler());
            server.start();
            System.out.println("Server is listening on port 8000");
        } catch (IOException e) {
            log.severe("Error starting the server: " + e.getMessage());
        }
    }  

}
