/*  Name: Robert Stein  
 *  Course: CNT 4714 Spring 2019 
 *  Assignment title: Project 3 
 *  Date:  3/14/2019
 *  Class:  TableModel
  */ 

package ucf;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;


public class TableModel extends AbstractTableModel
{
    Statement statement;
    boolean isDBConnected = false;
    ResultSet set;
    Connection conn;
    ResultSetMetaData resultSetMetaData;
    int rowsCount;
    public Class getColumnClass(int col) {
        Class target=Object.class;
        try {
            if (!isDBConnected)
                throw new Exception("No DB Connection");
            String name = resultSetMetaData.getColumnClassName(col+1);
            target= Class.forName(name);
            return target;
        }
        catch (Exception e)
        {
            return target;
        }
    }

    public void sqlExecute(String command) throws Exception
    {
        if (!isDBConnected)
            throw new Exception("No DB Connection");
        set = statement.executeQuery(command);
        resultSetMetaData = set.getMetaData();
        set.last();
        rowsCount = set.getRow();
    }
    public int getColumnCount()
    {
        int count=0;
        try {
            if (!isDBConnected)
                throw new IllegalStateException("No DB Connection");
            count= resultSetMetaData.getColumnCount();
            return count;
        }

        catch (Exception e)
        {
            return count;
        }
    }
    public String getColumnName(int col)
    {
        try {
            if (!isDBConnected)
                throw new IllegalStateException("No DB Connection");
            return resultSetMetaData.getColumnName(col+1);
        }

        catch (Exception e)
        {
            return null;
        }
    }

    public int getRowCount()
    {
        if (isDBConnected)
            return rowsCount;
        else return 0;
    }

    public void sqlUpdate(String command)
            throws Exception
    {
        statement.executeUpdate(command);
    }

    public Object getValueAt(int row, int col)
    {
        try {
            if (!isDBConnected)
                throw new IllegalStateException("No DB Connection");
            set.next();
            set.absolute(row + 1);
            return set.getObject( col + 1 ); //+1 because for ResultSet indexing is from 1
        }
        catch (Exception e)
        {
            return null;
        }

    }

    public TableModel(String query, Connection connection)
            throws Exception
    {
        try {
            this.conn = connection;
            if(!this.conn.isClosed())
            {
                statement= connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
                isDBConnected= true;
                try
                {
                    if(query.toLowerCase().contains("select")) sqlExecute(query);
                    else sqlUpdate(query);
                    this.fireTableStructureChanged();
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null,
                            e.toString(), "There was some error in DB",
                            JOptionPane.ERROR_MESSAGE );
                }
            }
        }
        catch (Exception e)
        {

        }
    }
}