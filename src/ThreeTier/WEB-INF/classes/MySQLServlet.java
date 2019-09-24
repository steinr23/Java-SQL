/*
Name: Robert Stein
Course: CNT 4714  Spring 2019  Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 7, 2019
*/


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

public class MySQLServlet extends HttpServlet {
    private Statement sqlStatement;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initiateDB(config);
    }

    public void initiateDB(ServletConfig config)
    {
        try {
            Class.forName(config.getInitParameter("driver"));
            Connection conn = DriverManager.getConnection(config.getInitParameter("name"),
                    config.getInitParameter("username"), config.getInitParameter("password"));
            sqlStatement = conn.createStatement();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public String getPrettyErrorBox(String content)
    {
        return "<div class='card bg-danger text-center'>"+content+"</div>";
    }

    private String doUpdateQuery(String inputString) throws SQLException {
        String responseContent;
        int updatedRowsCount;
        ResultSet initialResultSet = sqlStatement.executeQuery("select count(*) from shipments where quantity >= 100");
        initialResultSet.next();
        int currMoreThan100QtyShipments = initialResultSet.getInt(1);
        sqlStatement.executeUpdate("create table shipmentsTemp like shipments");
        sqlStatement.executeUpdate("insert into shipmentsTemp select * from shipments");
        updatedRowsCount = sqlStatement.executeUpdate(inputString);
        responseContent = "<div class='card bg-success text-white'><p>The statement executed successfully.</p><p>" + updatedRowsCount + " row(s) affected.</p>";
        ResultSet updatedResultSet = sqlStatement.executeQuery("select count(*) from shipments where quantity >= 100");
        updatedResultSet.next();
        int newShipmentsQtyGreaterThan100 = updatedResultSet.getInt(1);
        if(currMoreThan100QtyShipments < newShipmentsQtyGreaterThan100) {
            ResultSet sNumResultSet=sqlStatement.executeQuery("select snum, pnum, jnum, quantity from shipments where (snum, pnum, jnum, quantity) not in (select snum, pnum, jnum, quantity from shipmentsTemp)");
            String snumValue="";
            if (sNumResultSet.next())
                snumValue=sNumResultSet.getString(1);
            int numberOfRowsAffectedAfterIncrementBy5 =
                    sqlStatement.executeUpdate("update suppliers set status = status + 5 where snum  ='"+snumValue+"'");
            responseContent += "<p>Business Logic Detected! - Updating Supplier Status</p>";
            responseContent += "<p>Business Logic updated " + numberOfRowsAffectedAfterIncrementBy5 + " supplier status marks.</p></div>";
        }
        sqlStatement.executeUpdate("drop table shipmentsTemp");
        return responseContent;
    }


    public String doSelectQuery(String inputString) throws SQLException {
        String responseContent;
        ResultSet resultSet = sqlStatement.executeQuery(inputString);
        int colCount = resultSet.getMetaData().getColumnCount();
        String tableHeadings = "<thead><tr>";
        for (int i = 1; i <=colCount; i++)
            tableHeadings += "<th>" + resultSet.getMetaData().getColumnName(i) + "</th>";
        tableHeadings += "</tr></thead>";
        String tableCells = "<tbody>";
        while (resultSet.next()) {
            tableCells += "<tr>";
            for (int i = 1; i <= colCount; i++) {
                tableCells += "<td>" + resultSet.getString(i) + "</td>";
            }
            tableCells += "</tr>";
        }
        tableCells += "</tbody>";
        responseContent = "<table class='table table-striped'>" + tableHeadings + tableCells + "</table>";
        return responseContent;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String textBoxLowerCase = request.getParameter("sqlArea").toLowerCase(),sqlResults;
        if (textBoxLowerCase.contains("select")) {
            try {
                sqlResults = doSelectQuery(request.getParameter("sqlArea"));
            }
            catch (SQLException e) {
                sqlResults = getPrettyErrorBox(e.getMessage());
            }
        }
        else {
            try {
                sqlStatement.executeUpdate("drop table if exists shipmentsTemp");
                sqlResults = doUpdateQuery(request.getParameter("sqlArea"));
            }
            catch(SQLException e) {
                sqlResults = getPrettyErrorBox(e.getMessage());
            }
        }
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("result", sqlResults);
        httpSession.setAttribute("inputArea", request.getParameter("sqlArea"));
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/index.jsp");
        requestDispatcher.forward(request, response);
    }


}