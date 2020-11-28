import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dialog extends JDialog {
    JTextArea text = new JTextArea(3,100);
    JTextArea systemM = new JTextArea(2,100);
    Book book;
    JPanel middleOut = new JPanel(new BorderLayout());
    JPanel buttons = new JPanel(new FlowLayout());
    JButton borrow = new JButton("Borrow");
    JButton returnB = new JButton("Return");
    JButton reserve = new JButton("Reserve");
    JButton waitQ = new JButton("Waiting Queue");

    public Dialog(Frame frame,Book book) {
        super(frame,book.getTitle(),true);
        setSize(850, 500);
        this.book = book;
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        text.setEditable(false);
        initTextArea();
        initButtonPanel();
        middleOut.add(buttons,BorderLayout.NORTH);
        this.add(text,BorderLayout.NORTH);
        this.add(middleOut,BorderLayout.CENTER);
        this.add(systemM,BorderLayout.SOUTH);
        returnB.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MyLinkedList<String> tempList = book.getReservedQueue().getList();

                    String temp = "The book is returned.";
                    if(tempList.isEmpty())
                    {
                        String x = book.getReservedQueue().dequeue();
                        book.setAvailable(true);
                        resetButtons();
                        initButtonPanel();
                        initTextArea();

                    }
                    else
                    {
                        String x = book.getReservedQueue().dequeue();
                        temp+="\nThe book is now borrowed by "+x+".";
                    }
                    initSystemDisplay(temp);

                }
            }
        );
        waitQ.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String temp = "The waiting queue:\n";
                    MyLinkedList<String> tempList = book.getReservedQueue().getList();
                    for(int i = 0;i<tempList.size();i++)
                    {
                        temp+=tempList.get(i)+"\n";
                    }
                    initSystemDisplay(temp);
                }
            }
        );
        reserve.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String tempUser = JOptionPane.showInputDialog("What`s your name?");
                    book.getReservedQueue().enqueue(tempUser);
                    initSystemDisplay("The book is reserved by "+tempUser+".");
                }
            }
        );
        borrow.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    book.setAvailable(false);
                    initTextArea();
                    toggleButtons();
                    initSystemDisplay("The book is borrowed");
                }
            }
        );
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void clearTextArea(){
        text.setText("");
    }
    private void clearSystemDisplay(){
        systemM.setText("");
    }
    private void initTextArea(){
        clearTextArea();
        text.append("ISBN: "+book.getISBN()+"\n"+"Title: "+book.getTitle()+"\n"+"Available: "+book.isAvailable());
    }
    private void initSystemDisplay(String str){
        clearSystemDisplay();
        systemM.setText(str);
    }
    private void initButtonPanel(){
        buttons.add(borrow);
        buttons.add(returnB);
        buttons.add(reserve);
        buttons.add(waitQ);
        toggleButtons();


    }
    private void resetButtons(){
        buttons.removeAll();
        buttons.revalidate();
        buttons.repaint();
    }
    private void toggleButtons(){
        if(book.isAvailable())
        {
            borrow.setEnabled(true);
            returnB.setEnabled(false);
            reserve.setEnabled(false);
            waitQ.setEnabled(false);

        }
        else{
            borrow.setEnabled(false);
            returnB.setEnabled(true);
            reserve.setEnabled(true);
            waitQ.setEnabled(true);
        }
    }

}
