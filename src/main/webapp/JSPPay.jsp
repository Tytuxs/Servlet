<%--
  Created by IntelliJ IDEA.
  User: olico
  Date: 13-12-22
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="Classe.Chambre" %>
<% ArrayList<Chambre> liste = (ArrayList<Chambre>) request.getSession(true).getAttribute("listeChambreReservee");
    System.out.println(liste);
%>
<html>
<head>
    <title>Paiement</title>
</head>
<body>
<h1>Vos réservations</h1>
<form method="POST" action="hello-servlet">

    <B>Carte Bancaire</B><INPUT type="text" name="CB" size=30>

    <h4>
        <INPUT type="submit" name="action" value="accepter" size=10>
        <INPUT type="submit" name="action" value="annuler" size=10>
    </h4>
</form>
</body>
</html>
