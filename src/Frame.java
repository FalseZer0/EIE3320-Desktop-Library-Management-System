import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Frame extends JFrame {
    private static boolean isbnReversed = false;
    private static boolean titleReversed = false;
    private MyLinkedList<Book> bookList = new MyLinkedList<>();
    private JTextArea text = new JTextArea(2, 50);
    private String[] columnames = {"ISBN", "Title", "Available"};
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z YYYY", Locale.ENGLISH);
    private JPanel userPanel = new JPanel(new GridLayout(3, 0));
    private JPanel userR1 = new JPanel();
    private JPanel userR2 = new JPanel();
    private JPanel userR3 = new JPanel();
    private JTextField isbn = new JTextField(10);
    private JTextField title = new JTextField(15);
    private JButton add = new JButton("Add");
    private JButton edit = new JButton("Edit");
    private JButton save = new JButton("Save");
    private JButton delete = new JButton("Delete");
    private JButton search = new JButton("Search");
    private JButton more = new JButton("More>>");
    private JButton loadTest = new JButton("Load Test Data");
    private JButton display = new JButton("Display All");
    private JButton displayISBN = new JButton("Display All by ISBN");
    private JButton displayTitle = new JButton("Display All by Title");
    private JButton exit = new JButton("Exit");
    private JButton savesData = new JButton("Save Data");
    private JButton retrieveData = new JButton("Load Data");
    private JTable table;
    private String tempISBN;
    private String dataPath = "src\\data.txt";
    private Dialog dialog;

    public Frame() {
        super("Library Admin System");
        setLayout(new GridLayout(3, 0));
        // table
        DefaultTableModel tableModel = new DefaultTableModel(columnames,0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        //1st panel
        initTextArea();
        //user panel
        initUserPanel();
        this.add(text);
        this.add(scrollPane);
        this.add(userPanel);
        //events
        add.addActionListener(new MyEventListener());
        loadTest.addActionListener(new MyEventListener());
        delete.addActionListener(new MyEventListener());
        edit.addActionListener(new MyEventListener());
        save.addActionListener(new MyEventListener());
        search.addActionListener(new MyEventListener());
        exit.addActionListener(new MyEventListener());
        display.addActionListener(new MyEventListener());
        displayISBN.addActionListener(new MyEventListener());
        displayTitle.addActionListener(new MyEventListener());
        more.addActionListener(new MyEventListener());
        savesData.addActionListener(new MyEventListener());
        retrieveData.addActionListener(new MyEventListener());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel target = (DefaultTableModel) table.getModel();
                int indexSelectedRow = table.getSelectedRow();
                if(!bookList.isEmpty()&&e.getClickCount()==1&&table.getSelectedRow()!=-1){
                    isbn.setText(target.getValueAt(indexSelectedRow,0).toString());
                    title.setText(target.getValueAt(indexSelectedRow,1).toString());
                }
            }
        });
        //init of frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 500);
        setVisible(true);
        setLocationRelativeTo(null);

    }

    private void resetTextFields() {
        isbn.setText("");
        title.setText("");
        table.getSelectionModel().clearSelection();
    }

    class MyEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == add) {

                if (getISBN().isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: ISBN cannot be blank");
                    resetTextFields();
                    return;
                }
                if (getTITLE().isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: title cannot be blank");
                    resetTextFields();
                    return;
                }

                Book temp = new Book();
                temp.setISBN(getISBN());
                if (!bookList.contains(temp)) {
                    temp.setTitle(getTITLE());
                    bookList.add(temp);
                    addsRow(table, temp);
                } else if (bookList.contains(temp)) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: Book already exists");
                    resetTextFields();
                    return;
                }
                resetTextFields();
            } else if (e.getSource() == loadTest) {

                Book test1 = new Book();
                Book test2 = new Book();
                Book test3 = new Book();
                test1.setISBN("0131450913");
                test2.setISBN("0131857576");
                test3.setISBN("0132222205");
                if (!bookList.contains(test1)) {
                    test1.setTitle("HTML How to Program");
                    bookList.add(test1);
                } else {
                    JOptionPane.showMessageDialog(Frame.this, "Error: book ISBN" + test1.getISBN() + " is already in the database");
                }
                if (!bookList.contains(test2)) {
                    test2.setTitle("C++ How to Program");
                    bookList.add(test2);
                } else {
                    JOptionPane.showMessageDialog(Frame.this, "Error: book ISBN" + test2.getISBN() + " is already in the database");
                }
                if (!bookList.contains(test3)) {
                    test3.setTitle("Java How to Program");
                    bookList.add(test3);
                } else {
                    JOptionPane.showMessageDialog(Frame.this, "Error: book ISBN" + test2.getISBN() + " is already in the database");
                }
                showAllRecords(bookList);
                resetTextFields();
            } else if (e.getSource() == delete) {
                if (bookList.isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "Error: Database is empty");
                    return;
                }
                if (getISBN().isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: ISBN cannot be blank");
                    resetTextFields();
                    return;
                }
                Book temp = new Book();
                temp.setISBN(getISBN());
                if (!bookList.isEmpty() && bookList.contains(temp)) {
                    int tempIndex = bookList.indexOf(temp);
                    bookList.remove(tempIndex);
                    ((DefaultTableModel) table.getModel()).removeRow(tempIndex);
                    resetTextFields();
                }else {
                    JOptionPane.showMessageDialog(Frame.this, "Error: book ISBN is not in the database");
                }
                resetTextFields();

            } else if (e.getSource() == edit)
            {
                if (bookList.isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "Error: Database is empty");
                    return;
                }
                if (getISBN().isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: ISBN cannot be blank");
                    resetTextFields();
                    return;
                }
                Book temp = new Book();
                temp.setISBN(getISBN());
                if (!bookList.isEmpty() && bookList.contains(temp)) {
                    Book book = bookList.get(bookList.indexOf(temp));
                    if (getTITLE().isEmpty())
                        title.setText(book.getTitle());
                    save.setEnabled(true);
                    add.setEnabled(false);
                    edit.setEnabled(false);
                    delete.setEnabled(false);
                    search.setEnabled(false);
                    more.setEnabled(false);
                    loadTest.setEnabled(false);
                    display.setEnabled(false);
                    displayISBN.setEnabled(false);
                    displayTitle.setEnabled(false);
                    exit.setEnabled(false);
                    tempISBN = temp.getISBN();
                } else {
                    JOptionPane.showMessageDialog(Frame.this, "Error: book ISBN is not in the database");
                }

            } else if (e.getSource() == save) {
                if (getISBN().isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: ISBN cannot be blank");
                    return;
                }
                if (getTITLE().isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: title cannot be blank");
                    return;
                }
                Book temp = new Book();
                temp.setISBN(tempISBN);
                Book temp2 = new Book();
                temp2.setISBN(getISBN());
                temp2.setTitle(getTITLE());
                if (!getISBN().equals(tempISBN) && bookList.contains(temp2)) {
                    JOptionPane.showMessageDialog(Frame.this, "Error: book ISBN exists in the current database");
                } else {
                    Book book = bookList.get(bookList.indexOf(temp));
                    book.setISBN(getISBN());
                    book.setTitle(getTITLE());
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setValueAt(book.getISBN(), bookList.indexOf(book), 0);
                    model.setValueAt(book.getTitle(), bookList.indexOf(book), 1);
                    save.setEnabled(false);
                    add.setEnabled(true);
                    edit.setEnabled(true);
                    delete.setEnabled(true);
                    search.setEnabled(true);
                    more.setEnabled(true);
                    loadTest.setEnabled(true);
                    display.setEnabled(true);
                    displayISBN.setEnabled(true);
                    displayTitle.setEnabled(true);
                    exit.setEnabled(true);
                    resetTextFields();
                }
            } else if (e.getSource() == search) {
                if (getISBN().isEmpty() && getTITLE().isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: Both ISBN and Title cannot be blank");
                    resetTextFields();
                    return;
                }
                clearTable();
                if (!getISBN().isEmpty() && !getTITLE().isEmpty()) {
                    Iterator<Book> iterator = bookList.iterator();
                    Pattern patternISBN = Pattern.compile(getISBN());
                    Matcher matcherISBN;
                    Pattern patternTitle = Pattern.compile(getTITLE());
                    Matcher matcherTitle;
                    Set listOfIndexes = new HashSet();
                    while (iterator.hasNext()) {
                        Book temp = iterator.next();
                        matcherISBN = patternISBN.matcher(temp.getISBN());
                        matcherTitle = patternTitle.matcher(temp.getTitle());
                        if (matcherISBN.find())
                            listOfIndexes.add(bookList.indexOf(temp));
                        if (matcherTitle.find())
                            listOfIndexes.add(bookList.indexOf(temp));
                    }
                    Iterator<Integer> it = listOfIndexes.iterator();
                    while (it.hasNext()) {
                        Book temp = bookList.get(it.next());
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.addRow(new Object[]{temp.getISBN(), temp.getTitle(), temp.isAvailable()});
                    }
                } else if (!getISBN().isEmpty()) {
                    Iterator<Book> iterator = bookList.iterator();
                    Pattern patternISBN = Pattern.compile(getISBN());
                    Matcher matcherISBN;
                    Set listOfIndexes = new HashSet();
                    while (iterator.hasNext()) {
                        Book temp = iterator.next();
                        matcherISBN = patternISBN.matcher(temp.getISBN());
                        if (matcherISBN.find())
                            listOfIndexes.add(bookList.indexOf(temp));
                    }
                    Iterator<Integer> it = listOfIndexes.iterator();
                    while (it.hasNext()) {
                        Book temp = bookList.get(it.next());
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.addRow(new Object[]{temp.getISBN(), temp.getTitle(), temp.isAvailable()});
                    }
                } else if (!getTITLE().isEmpty()) {
                    Iterator<Book> iterator = bookList.iterator();
                    Pattern patternTitle = Pattern.compile(getTITLE());
                    Matcher matcherTitle;
                    Set listOfIndexes = new HashSet();
                    while (iterator.hasNext()) {
                        Book temp = iterator.next();
                        matcherTitle = patternTitle.matcher(temp.getTitle());
                        if (matcherTitle.find())
                            listOfIndexes.add(bookList.indexOf(temp));
                    }
                    Iterator<Integer> it = listOfIndexes.iterator();
                    while (it.hasNext()) {
                        Book temp = bookList.get(it.next());
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.addRow(new Object[]{temp.getISBN(), temp.getTitle(), temp.isAvailable()});
                    }
                }
                    resetTextFields();
            } else if (e.getSource() == display)
            {
                showAllRecords(bookList);

            } else if (e.getSource() == displayTitle) {
                Book[] temp = bookList.toArray(new Book[bookList.size()]);

                if (titleReversed) {
                    Arrays.sort(temp, Comparator.comparing(Book::getTitle).reversed());
                } else {
                    Arrays.sort(temp, Comparator.comparing(Book::getTitle));
                }
                titleReversed = !titleReversed;
                showAllRecords(new MyLinkedList<Book>(temp));
            } else if (e.getSource() == displayISBN) {
                Book[] temp = bookList.toArray(new Book[bookList.size()]);
                if (isbnReversed) {
                    Arrays.sort(temp, Collections.reverseOrder((s1, s2) -> {
                        BigInteger t1 = new BigInteger(s1.getISBN());
                        BigInteger t2 = new BigInteger(s2.getISBN());
                        return t1.compareTo(t2);
                    }));
                    showAllRecords(new MyLinkedList<Book>(temp));
                } else {
                    Arrays.sort(temp, (s1, s2) -> {
                        BigInteger t1 = new BigInteger(s1.getISBN());
                        BigInteger t2 = new BigInteger(s2.getISBN());
                        return t1.compareTo(t2);
                    });
                    showAllRecords(new MyLinkedList<Book>(temp));

                }
                isbnReversed=!isbnReversed;


            } else if (e.getSource() == exit) {
                dispose();
            } else if(e.getSource() == more){
                if (getISBN().isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "java.lang.Exception: Error: ISBN cannot be blank");
                    resetTextFields();
                    return;
                }
                Book temp = new Book();
                temp.setISBN(getISBN());
                if(!bookList.contains(temp)){
                    JOptionPane.showMessageDialog(Frame.this, "Error: book ISBN is not in the database");
                    resetTextFields();
                    return;
                }
                Book book = bookList.get(bookList.indexOf(temp));

                dialog= new Dialog(Frame.this,book);
                resetTextFields();
                showAllRecords(bookList);
            }
            else if(e.getSource() == savesData){
                if (bookList.isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "Error: Database is empty");
                    return;
                }

                BufferedWriter writer = null;
                try{
                    writer= new BufferedWriter(new FileWriter(dataPath));
                    for(Book entry: bookList){
                        writer.write(entry.getISBN()+"\n");
                        writer.write(entry.getTitle()+"\n");
                        writer.write(entry.isAvailable()+"\n");
                        writer.write(entry.getImagePath()+"\n");
                        if(entry.getReservedQueue().getList().isEmpty())
                        {
                            writer.write("\n");
                        }
                        else{
                            for(String name:entry.getReservedQueue().getList())
                            {
                                writer.write(name+";");
                            }
                            writer.write("\n");//extra
                        }

                    }
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                finally {
                    if(writer!=null)
                    {
                        try {
                            writer.close();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
            else if(e.getSource()==retrieveData)
            {
                if(!isFileEmpty())
                {
                    BufferedReader reader = null;
                    try{
                        reader = new BufferedReader(new FileReader(dataPath));
                        String line;
                        String tempisbn ="";
                        String temptitle="";
                        String tempavailable="";
                        String tempimagepath="";
                        String tempWaitQ = "";
                        int count = 0;

                        while(true){
                            if(count>4)
                            {
                                count=0;
                                Book temp = new Book();
                                temp.setISBN(tempisbn);
                                temp.setTitle(temptitle);
                                temp.setAvailable(Boolean.parseBoolean(tempavailable));
                                temp.setImagePath(tempimagepath);
                                if(tempWaitQ.length()!=0)
                                {
                                    String[] arr = tempWaitQ.split(";");
                                    MyQueue<String> q = new MyQueue<>();
                                    for(String str:arr)
                                    {
                                        q.enqueue(str);
                                    }
                                    temp.setReservedQueue(q);
                                }
                                bookList.add(temp);
                            }
                            if((line=reader.readLine())==null)
                                break;

                            switch (count){
                                case 0:
                                    tempisbn = line.trim();
                                    break;
                                case 1:
                                    temptitle = line.trim();
                                    break;
                                case 2:
                                    tempavailable = line.trim();
                                    break;
                                case 3:
                                    tempimagepath = line.trim();
                                    break;
                                case 4:
                                    tempWaitQ = line.trim();
                                    break;
                            }

                            count++;
                        }
                        System.out.println(bookList);
                        showAllRecords(bookList);
                    }
                    catch (IOException exception)
                    {
                        exception.printStackTrace();
                    }
                    finally {
                        if(reader!=null)
                        {
                            try {
                                reader.close();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                }
            }

        }
    }
    private boolean isFileEmpty(){
        File file = new File(dataPath);
        if(file.length()==0)
            return true;
        else
            return false;
    }
    private void clearData() throws IOException{
        FileWriter fwOb = new FileWriter(dataPath, false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }
    private String getISBN(){
        return isbn.getText().trim();
    }
    private String getTITLE(){
        return title.getText().trim();
    }
    private void addsRow(JTable table, Book book) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{book.getISBN(), book.getTitle(), book.isAvailable()});
    }


    private void clearTable() {
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }

    }

    private void showAllRecords(MyLinkedList<Book> bookLinkedList) {
        clearTable();
        for (Book entry : bookLinkedList) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.addRow(new Object[]{entry.getISBN(), entry.getTitle(), entry.isAvailable()});
        }
    }

    private void initUserPanel() {
        userR1.add(new JLabel("ISBN"));
        userR1.add(isbn);
        userR1.add(new JLabel("Title"));
        userR1.add(title);
        //adding buttons
        save.setEnabled(false);
        userR2.add(add);
        userR2.add(edit);
        userR2.add(save);
        userR2.add(delete);
        userR2.add(search);
        userR2.add(more);
        userR3.add(loadTest);
        userR3.add(display);
        userR3.add(displayISBN);
        userR3.add(displayTitle);
        userR3.add(savesData);
        userR3.add(retrieveData);
        userR3.add(exit);
        userPanel.add(userR1);
        userPanel.add(userR2);
        userPanel.add(userR3);
    }

    private void initTextArea() {
        text.setEditable(false);
        text.append("Student Name and ID: Seksembayev Kairat (18078689d)\n" + dateFormat.format(new Date()));
    }
}
