import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.math.BigInteger;
import java.sql.ClientInfoStatus;
import java.util.HashSet;
import java.util.Set;

public class DeprecatedMyTableModel implements TableModel {
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
    MyLinkedList<Book> list;
    public DeprecatedMyTableModel(MyLinkedList<Book> list)
    {
        this.list = list;
    }
    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getColumnCount() {
        return 3;
    }
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "ISBN";
            case 1:
                return "Title";
            case 2:
                return "Available";
        }
        return "";
    }
    public int getRowCount() {
        return list.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(list.isEmpty())
        {
            switch (columnIndex) {
                default:
                    return "";
            }
        }
        else{
            Book book = list.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return book.getISBN();
                case 1:
                    return book.getTitle();
                case 2:
                    return book.isAvailable();
            }

            return "";
        }

    }
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Book temp = new Book();
        switch (columnIndex) {
            case 0:
                temp.setISBN((String) value);break;
            case 1:
                temp.setTitle((String) value);break;
        }
        list.set(columnIndex,temp);
        //fireTableRowsUpdated();

    }



}
