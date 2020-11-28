

public class Book {
    private String title;
    private String ISBN;
    private boolean available;
    public Book(){
        title="";
        ISBN = "";
        available = true;
    }
    private MyQueue<String> reservedQueue = new MyQueue<>();

    public String getTitle() {
        return title;
    }

    public String getISBN() {
        return ISBN;
    }

    public boolean isAvailable() {
        return available;
    }

    public MyQueue<String> getReservedQueue() {
        return reservedQueue;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setReservedQueue(MyQueue<String> reversedQueue) {
        this.reservedQueue = reversedQueue;
    }

    @Override
    public boolean equals(Object obj) {
        Book book = (Book) obj;

        return !book.getISBN().trim().isEmpty()&&ISBN.equals(book.getISBN().trim());
    }

}
