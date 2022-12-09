<%--
  Created by IntelliJ IDEA.
  User: olico
  Date: 08-12-22
  Time: 11:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="Classe.Chambre" %>
<% ArrayList<Chambre> liste = (ArrayList<Chambre>) request.getSession(true).getAttribute("listeChambreReservee");
    System.out.println(liste);%>
<html>
<head>
    <title>Caddie</title>
</head>
<body>
<h1>Vos réservations</h1>
<form method="POST" action="hello-servlet">
    <table border="1" id="table">
        <thead>
        <tr>
            <th>///</th>
            <th>Numéro Chambre</th>
            <th>Prix</th>
        </tr>
        </thead>
        <tbody>
        <%
            if(liste != null) {
                for(int i = 0 ; i < liste.size() ; i ++)
                {
        %>
        <tr>
            <td><input type="radio" name="ChambrePayee" value="<%= i %>"/></td>
            <input type="hidden" name="<%= i %>" value="<%= liste.get(i).get_numeroChambre() %>"/>
            <td> <%= liste.get(i).get_numeroChambre() %> </td>
            <td> <%= liste.get(i).get_prixHTVA() %> </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>


    <h4>
        <INPUT type="submit" name="action" value="Payer" size=10>
        <INPUT type="submit" name="action" value="Continuer" size=10>
        <INPUT type="submit" name="action" value="Vider la liste" size=10>
    </h4>
</form>
</body>
</html>