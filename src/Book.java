

public class Book {
    private String title;
    private String ISBN;
    private boolean available=true;
    private String imagePath;
    public Book(){
        title="";
        ISBN = "";
        imagePath="";
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Book(String ISBN) throws Exception {
        this.ISBN = ISBN.trim();
        if(ISBN.length()==0)
            throw new Exception("Error: ISBN cannot be blank.");
    }
    public Book(String ISBN, String title) throws Exception {
        this.ISBN = ISBN.trim();
        this.title = title.trim();
        if(ISBN.length()==0)
            throw new Exception("Error: ISBN cannot be blank.");
        else if(title.length()==0)
            throw new Exception("Error: title cannot be blank.");

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
