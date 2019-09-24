/*  Name: Robert Stein  
 *  Course: CNT 4714 Spring 2019 
 *  Assignment title: Project 3 
 *  Date:  3/14/2019
 *  Class:  AppFrame 
  */ 

package ucf;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.*;

public class AppFrame extends JFrame
{
    String[] dbURLS = {"jdbc:mysql://localhost:3306/project3"};
    String[] driversList = {"com.mysql.jdbc.Driver"};
    Connection conn;
    TableModel tableModel = null;
    boolean isDBConnected = false; //we shall use this to check for the connection at all places before making db calls
    JTextField usernameTF=new JTextField();
    JPasswordField passwordPF=new JPasswordField();
    JComboBox dbListCB, driversListCB;
    JTextArea sqlTA;
    JButton executeCommandBtn = new JButton("Execute SQL");
    JButton clearAllBtn = new JButton("Clear Results");
    JButton connectDBBtn = new JButton("Connect to DB");
    JButton resetSQLBtn = new JButton("Clear All SQL");
    JLabel databaseLabel,usernameLabel,passwordLabel,connStatusLabel,driverLabel,dbDetailsLabel,sqlTextAreaLabel;
    JTable displayTable = new JTable();

    public void onErrorOccurred(Exception e)
    {
        displayTable.setModel(new DefaultTableModel());
        tableModel = null;
        JOptionPane.showMessageDialog(null,
                e.getMessage(), "Error encountered!",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    public void stylizeBtn(JButton button)
    {
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(new Color(28, 12,240));
        button.setForeground(new Color(255,255,255));
    }
    public void connectToDB()
    {
        try {
            Class.forName(String.valueOf(driversListCB.getSelectedItem()));
            if (isDBConnected) {
                conn.close();
                isDBConnected = false;
                tableModel = null;
                displayTable.setModel(new DefaultTableModel());
                connStatusLabel.setText("No Connections");
                connStatusLabel.setForeground(new Color(28, 12, 240));
                connectDBBtn.setText("Connect to DB");
            }
            else
            {
                conn = DriverManager.getConnection(String.valueOf(dbListCB.getSelectedItem()),
                        usernameTF.getText(), String.valueOf(passwordPF.getPassword())); //getText for passwordfield is deprecated
                isDBConnected = true;
                connStatusLabel.setText("Successful connection established for " + String.valueOf(dbListCB.getSelectedItem()));
                connStatusLabel.setForeground(new Color(255, 130,0));
                connectDBBtn.setText("Disconnect");
            }
        } catch (Exception e) {
            connStatusLabel.setText("No Connections");
            connStatusLabel.setForeground(new Color(28, 12, 240));
            e.printStackTrace();
            displayTable.setModel(new DefaultTableModel());
            tableModel = null;
        }
    }

    public void executeSQL()
    {
        if (isDBConnected)
        {
            if (tableModel == null)
            {
                try {
                    tableModel = new TableModel(sqlTA.getText(), conn);
                    displayTable.setModel(tableModel);
                    tableModel.fireTableStructureChanged();
                } catch (Exception e) {
                    onErrorOccurred(e);
                }
            }
            else
            {
                String query = sqlTA.getText();
                if (query.toLowerCase().contains("select"))
                {
                    try {
                        tableModel.sqlExecute(query);
                        tableModel.fireTableStructureChanged();
                    } catch (Exception e) {
                        onErrorOccurred(e);
                    }
                }
                else
                {
                    try {
                        tableModel.sqlUpdate(query);
                        tableModel.fireTableStructureChanged();
                        displayTable.setModel(new DefaultTableModel());
                        tableModel = null;
                    } catch (Exception e) {
                        onErrorOccurred(e);
                    }
                }
            }
        }

    }

    public void setActionListeners()
    {
        executeCommandBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSQL();
            }
        });
        resetSQLBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sqlTA=new JTextArea();
                sqlTA.setText("");
            }
        });

        clearAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel = null;
                displayTable.setModel(new DefaultTableModel());
            }
        });

        connectDBBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToDB();
            }
        });
    }
    public AppFrame() throws IOException,ClassNotFoundException,SQLException
    {
        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        sqlTA = new JTextArea(3, 75);
        sqlTA.setLineWrap(true);
        JScrollPane sqlAreaPane=new JScrollPane(sqlTA);
        databaseLabel = new JLabel("DB URL");
        dbDetailsLabel = new JLabel("Database Details");
        sqlTextAreaLabel = new JLabel("Type a SQL Command");
        connStatusLabel = new JLabel("No Connections");
        connStatusLabel.setForeground(new Color(28, 12, 240));
        driverLabel = new JLabel("JDBC Driver");
        driversListCB = new JComboBox(driversList);
        dbListCB = new JComboBox(dbURLS);
        driversListCB.setSelectedIndex(0);
        dbListCB.setSelectedIndex(0);
        stylizeBtn(clearAllBtn);
        stylizeBtn(connectDBBtn);
        stylizeBtn(executeCommandBtn);
        stylizeBtn(resetSQLBtn);

        JPanel topLabels=new JPanel(new GridLayout(1,2));
        topLabels.add(dbDetailsLabel);
        topLabels.add(sqlTextAreaLabel);

        JPanel labelsLeft=new JPanel(new GridLayout(4,1));
        labelsLeft.add(driverLabel);
        labelsLeft.add(databaseLabel);
        labelsLeft.add(usernameLabel);
        labelsLeft.add(passwordLabel);

        JPanel dbChooseSectionsRight=new JPanel(new GridLayout(4,1));
        dbChooseSectionsRight.add(driversListCB);
        dbChooseSectionsRight.add(dbListCB);
        dbChooseSectionsRight.add(usernameTF);
        dbChooseSectionsRight.add(passwordPF);

        JPanel inputsPanel = new JPanel(new GridLayout(1, 2));
        inputsPanel.add(labelsLeft);
        inputsPanel.add(dbChooseSectionsRight);

        JPanel actionBtnsPanel = new JPanel(new GridLayout(1, 4,10,0));
        actionBtnsPanel.add(connStatusLabel);
        actionBtnsPanel.add(connectDBBtn);
        actionBtnsPanel.add(executeCommandBtn);
        actionBtnsPanel.add(resetSQLBtn);

        JPanel menuPanel = new JPanel(new GridLayout(2, 2,0,0));
        menuPanel.add(dbDetailsLabel);
        menuPanel.add(sqlTextAreaLabel);
        menuPanel.add(inputsPanel);
        menuPanel.add(sqlAreaPane);

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout(40, 0));
        JScrollPane paneForResult=new JScrollPane(displayTable);
        resultsPanel.add(paneForResult, BorderLayout.NORTH);
        resultsPanel.add(clearAllBtn, BorderLayout.SOUTH);
        add(actionBtnsPanel, BorderLayout.CENTER);
        add(resultsPanel, BorderLayout.SOUTH);
        add(menuPanel, BorderLayout.NORTH);
        setActionListeners();
    }
}