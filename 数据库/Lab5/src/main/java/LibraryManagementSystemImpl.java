import entities.Book;
import entities.Borrow;
import entities.Card;
import entities.Card.CardType;
import queries.*;
import utils.DBInitializer;
import utils.DatabaseConnector;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class LibraryManagementSystemImpl implements LibraryManagementSystem {

    private final DatabaseConnector connector;

    public LibraryManagementSystemImpl(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public ApiResult storeBook(Book book) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM book WHERE category = ? AND title = ? AND press = ? AND publish_year = ? AND author = ?"
            );
            checkStmt.setString(1, book.getCategory());
            checkStmt.setString(2, book.getTitle());
            checkStmt.setString(3, book.getPress());
            checkStmt.setInt(4, book.getPublishYear());
            checkStmt.setString(5, book.getAuthor());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new ApiResult(false, "Book already exists.");
            }
            
            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO book (category, title, press, publish_year, author, price, stock) VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            insertStmt.setString(1, book.getCategory());
            insertStmt.setString(2, book.getTitle());
            insertStmt.setString(3, book.getPress());
            insertStmt.setInt(4, book.getPublishYear());
            insertStmt.setString(5, book.getAuthor());
            insertStmt.setDouble(6, book.getPrice());
            insertStmt.setInt(7, book.getStock());
            insertStmt.executeUpdate();
            rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                book.setBookId(rs.getInt(1));
            }
            commit(conn);
            return new ApiResult(true, "Book stored successfully.");
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, "Error storing book: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResult incBookStock(int bookId, int deltaStock) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE book SET stock = stock + ? WHERE book_id = ? AND stock + ? >= 0"
            );
            stmt.setInt(1, deltaStock);
            stmt.setInt(2, bookId);
            stmt.setInt(3, deltaStock);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return new ApiResult(false, "Stock update failed, check book id or insufficient stock.");
            }
            commit(conn);
            return new ApiResult(true, "Stock updated successfully.");
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, "Error updating stock: " + e.getMessage());
        }
    }

    @Override
    public ApiResult storeBook(List<Book> books) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO book (category, title, press, publish_year, author, price, stock) VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            for (Book book : books) {
                insertStmt.setString(1, book.getCategory());
                insertStmt.setString(2, book.getTitle());
                insertStmt.setString(3, book.getPress());
                insertStmt.setInt(4, book.getPublishYear());
                insertStmt.setString(5, book.getAuthor());
                insertStmt.setDouble(6, book.getPrice());
                insertStmt.setInt(7, book.getStock());
                insertStmt.addBatch();
            }

            insertStmt.executeBatch();
            ResultSet rs = insertStmt.getGeneratedKeys();
            int index = 0;
            while (rs.next()) {
                // System.out.println(index);
                books.get(index).setBookId(rs.getInt(1));
                index++;
            }
            commit(conn);
            return new ApiResult(true, "Books batch stored successfully.");
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, "Error storing books batch: " + e.getMessage());
        }
    }

    @Override
    public ApiResult removeBook(int bookId) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM borrow WHERE book_id = ? AND return_time = 0"
            );
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new ApiResult(false, "Cannot remove book, it has not been returned by all borrowers.");
            }
            
            PreparedStatement deleteStmt = conn.prepareStatement(
                "DELETE FROM book WHERE book_id = ?"
            );
            deleteStmt.setInt(1, bookId);
            int affectedRows = deleteStmt.executeUpdate();
            if (affectedRows == 0) {
                return new ApiResult(false, "No book found with given ID or delete failed.");
            }
            commit(conn);
            return new ApiResult(true, "Book removed successfully.");
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, "Error removing book: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResult modifyBookInfo_super(Book book){
        // you can use this function to simutanously modify book information and modify the stock
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement updateStmt = conn.prepareStatement(
                "UPDATE book SET category = ?, title = ?, press = ?, publish_year = ?, author = ?, price = ?, stock = ? WHERE book_id = ?"
            );
            updateStmt.setString(1, book.getCategory());
            updateStmt.setString(2, book.getTitle());
            updateStmt.setString(3, book.getPress());
            updateStmt.setInt(4, book.getPublishYear());
            updateStmt.setString(5, book.getAuthor());
            updateStmt.setDouble(6, book.getPrice());
            updateStmt.setInt(7, book.getStock());
            updateStmt.setInt(8, book.getBookId());
            int affectedRows = updateStmt.executeUpdate();
            if (affectedRows == 0) {
                return new ApiResult(false, "No book found with given ID or update failed.");
            }
            commit(conn);
            return new ApiResult(true, "Book information updated successfully.");
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, "Error updating book information: " + e.getMessage());
        }

    }
    
    @Override
    public ApiResult modifyBookInfo(Book book) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement updateStmt = conn.prepareStatement(
                "UPDATE book SET category = ?, title = ?, press = ?, publish_year = ?, author = ?, price = ? WHERE book_id = ?"
            );
            updateStmt.setString(1, book.getCategory());
            updateStmt.setString(2, book.getTitle());
            updateStmt.setString(3, book.getPress());
            updateStmt.setInt(4, book.getPublishYear());
            updateStmt.setString(5, book.getAuthor());
            updateStmt.setDouble(6, book.getPrice());
            updateStmt.setInt(7, book.getBookId());
            int affectedRows = updateStmt.executeUpdate();
            if (affectedRows == 0) {
                return new ApiResult(false, "No book found with given ID or update failed.");
            }
            commit(conn);
            return new ApiResult(true, "Book information updated successfully.");
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, "Error updating book information: " + e.getMessage());
        }
    }
    

    @Override
    public ApiResult queryBook(BookQueryConditions conditions) {
        Connection conn = connector.getConn();
        try {
            String sql = "SELECT * FROM book WHERE ";
            List<Object> params = new ArrayList<Object>();
            int flag = 0;
            if (conditions.getCategory() != null) {
                sql += "category = ? AND ";
                params.add(conditions.getCategory());
                flag = 1;
            }
            if (conditions.getTitle() != null) {
                sql += "title LIKE ? AND ";
                params.add("%" + conditions.getTitle() + "%");
                flag = 1;
            }
            if (conditions.getPress() != null) {
                sql += "press LIKE ? AND ";
                params.add("%" + conditions.getPress() + "%");
                flag = 1;
            }
            if (conditions.getMinPublishYear() != null) {
                sql += "publish_year >= ? AND ";
                params.add(conditions.getMinPublishYear());
                flag = 1;
            }
            if (conditions.getMaxPublishYear() != null) {
                sql += "publish_year <= ? AND ";
                params.add(conditions.getMaxPublishYear());
                flag = 1;
            }
            if (conditions.getAuthor() != null) {
                sql += "author LIKE ? AND ";
                params.add("%" + conditions.getAuthor() + "%");
                flag = 1;
            }
            if (conditions.getMinPrice() != null) {
                sql += "price >= ? AND ";
                params.add(conditions.getMinPrice());
                flag = 1;
            }
            if (conditions.getMaxPrice() != null) {
                sql += "price <= ? AND ";
                params.add(conditions.getMaxPrice());
                flag = 1;
            }

            String sortBy = conditions.getSortBy().getValue(); 
            String sortOrder = conditions.getSortOrder().getValue();

            if (flag == 1) {
                sql = sql.substring(0, sql.length() - 4); 
            } else {
                sql = sql.substring(0, sql.length() - 6); 
            }

            sql += " ORDER BY " + sortBy + " " + sortOrder + ", book_id ASC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            List<Book> results = new ArrayList<>();
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setCategory(rs.getString("category"));
                book.setTitle(rs.getString("title"));
                book.setPress(rs.getString("press"));
                book.setPublishYear(rs.getInt("publish_year"));
                book.setAuthor(rs.getString("author"));
                book.setPrice(rs.getDouble("price"));
                book.setStock(rs.getInt("stock"));
                results.add(book);
            }
            BookQueryResults final_result = new BookQueryResults(results);
            return new ApiResult(true, final_result);
        } catch (SQLException e) {
            return new ApiResult(false, "Error querying books: " + e.getMessage());
        }
    }

    @Override
    public ApiResult borrowBook(Borrow borrow) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement checkStock = conn.prepareStatement(
                "SELECT stock FROM book WHERE book_id = ?"
            );
            checkStock.setInt(1, borrow.getBookId());
            ResultSet rs = checkStock.executeQuery();
            if (rs.next() && rs.getInt("stock") > 0 ) {
                PreparedStatement checkBorrow = conn.prepareStatement(
                    "SELECT count(*) FROM borrow WHERE card_id = ? AND book_id = ? AND return_time = 0"
                );
                checkBorrow.setInt(1, borrow.getCardId());
                checkBorrow.setInt(2, borrow.getBookId());
                // checkBorrow.setLong(3, borrow.getBorrowTime());
                ResultSet rs_ = checkBorrow.executeQuery();
                if (rs_.next() && rs_.getInt(1) == 0) {
                    // check card
                    PreparedStatement checkCard = conn.prepareStatement(
                        "SELECT COUNT(*) FROM card WHERE card_id = ?"
                    );
                    checkCard.setInt(1, borrow.getCardId());
                    ResultSet rs__ = checkCard.executeQuery();
                    if (rs__.next() && rs__.getInt(1) > 0) {
                        PreparedStatement updateStock = conn.prepareStatement(
                            "UPDATE book SET stock = stock - 1 WHERE book_id = ?"
                        );
                        updateStock.setInt(1, borrow.getBookId());
                        updateStock.executeUpdate();
            
                        PreparedStatement insertBorrow = conn.prepareStatement(
                            "INSERT INTO borrow (card_id, book_id, borrow_time, return_time) VALUES (?, ?, ?, 0)"
                        );
                        insertBorrow.setInt(1, borrow.getCardId());
                        insertBorrow.setInt(2, borrow.getBookId());
                        insertBorrow.setLong(3, borrow.getBorrowTime());
                        insertBorrow.executeUpdate();
                        commit(conn);
                        return new ApiResult(true, "Book borrowed successfully.");
                    }
                    return new ApiResult(false, "Card not found.");
                }
                return new ApiResult(false, "Book has been already borrowed.");

            }

            return new ApiResult(false, "Book is out of stock.");
        } catch (SQLException e) {
            rollback(conn);
            return new ApiResult(false, "Error borrowing book: " + e.getMessage());
        }
    }

    @Override
    public ApiResult returnBook(Borrow borrow) {
        if (borrow.getReturnTime() <= borrow.getBorrowTime()) {
            return new ApiResult(false, "Error return time.");
        }
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement updateBorrow = conn.prepareStatement(
                "UPDATE borrow SET return_time = ? WHERE card_id = ? AND book_id = ? AND return_time = 0 AND borrow_time = ?"
            );
            updateBorrow.setLong(1, borrow.getReturnTime());
            updateBorrow.setInt(2, borrow.getCardId());
            updateBorrow.setInt(3, borrow.getBookId());
            updateBorrow.setLong(4, borrow.getBorrowTime());
            int affectedRows = updateBorrow.executeUpdate();
            if (affectedRows == 0) {
                return new ApiResult(false, "No matching borrow record found or book already returned.");
            }
    
            PreparedStatement updateStock = conn.prepareStatement(
                "UPDATE book SET stock = stock + 1 WHERE book_id = ?"
            );
            updateStock.setInt(1, borrow.getBookId());
            updateStock.executeUpdate();
            commit(conn);
            return new ApiResult(true, "Book returned successfully.");
        } catch (SQLException e) {
            rollback(conn);
            return new ApiResult(false, "Error returning book: " + e.getMessage());
        }
    }

    @Override
    public ApiResult showBorrowHistory(int cardId) {
        Connection conn = connector.getConn();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM borrow NATURAL JOIN book WHERE card_id = ? ORDER BY borrow_time DESC, book_id ASC"
            );
            stmt.setInt(1, cardId);
            ResultSet rs = stmt.executeQuery();
            List<BorrowHistories.Item> histories = new ArrayList<BorrowHistories.Item>(); 
            while (rs.next()) {
                BorrowHistories.Item history = new BorrowHistories.Item();
                
                history.setCategory(rs.getString("category"));
                history.setTitle(rs.getString("title"));
                history.setPress(rs.getString("press"));
                history.setPublishYear(rs.getInt("publish_year"));
                history.setAuthor(rs.getString("author"));
                history.setPrice(rs.getDouble("price"));
                history.setCardId(rs.getInt("card_id"));
                history.setBookId(rs.getInt("book_id"));
                history.setBorrowTime(rs.getLong("borrow_time"));
                history.setReturnTime(rs.getLong("return_time"));
                histories.add(history);
            }
            BorrowHistories final_histories = new BorrowHistories(histories);
            return new ApiResult(true, final_histories);
        } catch (SQLException e) {
            return new ApiResult(false, "Error fetching borrow history: " + e.getMessage());
        }
    }

    @Override
    public ApiResult registerCard(Card card) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement checkCard = conn.prepareStatement(
                "SELECT COUNT(*) FROM card WHERE department = ? AND type = ? AND name = ?"
            );
            checkCard.setString(1, card.getDepartment());
            checkCard.setString(2, String.valueOf(card.getType())); 
            checkCard.setString(3, card.getName());
            // System.out.println(String.valueOf(card.getType().getStr()));
            ResultSet rs = checkCard.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new ApiResult(false, "Card already exists.");
            }
    
            PreparedStatement insertCard = conn.prepareStatement(
                "INSERT INTO card (name, department, type) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            insertCard.setString(1, card.getName());
            insertCard.setString(2, card.getDepartment());
            insertCard.setString(3, String.valueOf(card.getType().getStr()));
            insertCard.executeUpdate();
            rs = insertCard.getGeneratedKeys();
            if (rs.next()) {
                card.setCardId(rs.getInt(1));
            }
            commit(conn);
            return new ApiResult(true, "Card registered successfully.");
        } catch (SQLException e) {
            rollback(conn);
            return new ApiResult(false, "Error registering card: " + e.getMessage());
        }
    }

    @Override
    public ApiResult modifyCard(Card card) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement updateCard = conn.prepareStatement(
                "UPDATE card SET name = ?, department = ?, type = ? WHERE card_id = ?"
            );
            updateCard.setString(1, card.getName());
            updateCard.setString(2, card.getDepartment());
            updateCard.setString(3, String.valueOf(card.getType().getStr()));
            updateCard.setInt(4, card.getCardId());
            int affectedRows = updateCard.executeUpdate();
            if (affectedRows == 0) {
                return new ApiResult(false, "No card found with given ID or update failed.");
            }
            commit(conn);
            return new ApiResult(true, "Card information updated successfully.");
        } catch (SQLException e) {
            rollback(conn);
            return new ApiResult(false, "Error updating card information: " + e.getMessage());
        }
    }

    @Override
    public ApiResult removeCard(int cardId) {
        Connection conn = connector.getConn();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement checkBorrow = conn.prepareStatement(
                "SELECT COUNT(*) FROM borrow WHERE card_id = ? AND return_time = 0"
            );
            checkBorrow.setInt(1, cardId);
            ResultSet rs = checkBorrow.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new ApiResult(false, "Cannot remove card, there are unreturned books.");
            }
    
            PreparedStatement deleteCard = conn.prepareStatement(
                "DELETE FROM card WHERE card_id = ?"
            );
            deleteCard.setInt(1, cardId);
            int affectedRows = deleteCard.executeUpdate();
            if (affectedRows == 0) {
                return new ApiResult(false, "No card found with given ID or delete failed.");
            }
            commit(conn);
            return new ApiResult(true, "Card removed successfully.");
        } catch (SQLException e) {
            rollback(conn);
            return new ApiResult(false, "Error removing card: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResult showCards() {
        Connection conn = connector.getConn();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM card ORDER BY card_id");
            ResultSet rs = stmt.executeQuery();
            List<Card> cards = new ArrayList<Card>();
            while (rs.next()) {
                Card card = new Card();
                card.setCardId(rs.getInt("card_id"));
                card.setName(rs.getString("name"));
                card.setDepartment(rs.getString("department"));
                card.setType(CardType.values(rs.getString("type"))); 
                cards.add(card);
            }
            CardList final_cards = new CardList(cards);
            return new ApiResult(true, final_cards);
        } catch (SQLException e) {
            return new ApiResult(false, "Error fetching cards: " + e.getMessage());
        }
    }


    @Override
    public ApiResult resetDatabase() {
        Connection conn = connector.getConn();
        try {
            Statement stmt = conn.createStatement();
            DBInitializer initializer = connector.getConf().getType().getDbInitializer();
            stmt.addBatch(initializer.sqlDropBorrow());
            stmt.addBatch(initializer.sqlDropBook());
            stmt.addBatch(initializer.sqlDropCard());
            stmt.addBatch(initializer.sqlCreateCard());
            stmt.addBatch(initializer.sqlCreateBook());
            stmt.addBatch(initializer.sqlCreateBorrow());
            stmt.executeBatch();
            commit(conn);
        } catch (Exception e) {
            rollback(conn);
            return new ApiResult(false, e.getMessage());
        }
        return new ApiResult(true, null);
    }

    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commit(Connection conn) {
        try {
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
