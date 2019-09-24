<%--
Name:
Course: CNT 4714 – Spring 2019 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 7, 2019
--%>
<!doctype html>
<%
    String inputAreaText = (String) session.getAttribute("inputArea");
    String resultsArea = (String) session.getAttribute("result");
    resultsArea = resultsArea == null ? " " : resultsArea;
    inputAreaText = inputAreaText == null ? " " : inputAreaText;
%>

<html lang="en">
<head>

    <meta charset="utf-8">
    <title>CNT 4717 Remote Database Management System</title>
    <style>
        .command_line {
            background-color: black !important;
            color: green !important;
        }

    </style>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>

<body>


<div class="container-fluid">
    <row class="row">
        <div class="col-lg-3 col-md-3"></div>
        <div class="text-center col-sm-12 col-md-6 col-lg-6 justify-content-center">
            <h1>Welcome to the Spring 2019 Project 4 Enterprise System </h1>
            <h1>A Remote Database Management System </h1>
            <div> You are connected to the Project 4 database.</div>
            <div> Please enter any valid SQL query or update statement.
            </div>
            <div> If no query/update command is provided
                the Execute button will display all supplier information in the database.
            </div>
            <div>All execution results will appear below.</div>
            <form id="sqlForm" action="MySQLServlet" method="post" style="margin-top: 15px;" >
                <div class="form-group row">
                    <textarea name="sqlArea" class="command_line form-control" id="sqlArea" rows="8" cols="50"><%=inputAreaText %></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Execute Command</button>
                <a href="reset.jsp" type="reset" class="btn btn-warning">Reset form</a>
            </form>
        </div>
        <div class="col-lg-3 col-md-3"></div>
    </row>
    <row class="row">
        <div class="col-lg-3 col-md-3"></div>
        <div class="text-center col-sm-12 col-md-6 col-lg-6">
            <p class="font-weight-bold text-uppercase">
                Database results:</p>
            <%= resultsArea %></div>
        <div class="col-lg-3 col-md-3"></div>
    </row>
</div>

</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"/>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"/>
</body>

</html>