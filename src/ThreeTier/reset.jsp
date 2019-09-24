<%--
Name:
Course: CNT 4714 – Spring 2019 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 7, 2019
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    session.invalidate();
    response.sendRedirect("index.jsp");
    return;
%>