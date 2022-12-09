<%--
  Created by IntelliJ IDEA.
  User: olico
  Date: 08-12-22
  Time: 09:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.ArrayList"%>
<%@page import="Classe.Chambre"%>
<% ArrayList<Chambre> liste = (ArrayList<Chambre>) request.getSession(true).getAttribute("chambres");
System.out.println(liste);%>
<html>
<head>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
</head>
<body>
    <h1>Réservation chambre</h1>
    <form method="POST" action="hello-servlet">
        <div style=" background-color: gray; justify-content: space-around">
            <h1 align="right">
                <input type="submit" name="action" value="Caddie"/>
                <input type="submit" name="action" value="Log out"/>
            </h1>
        </div>
        <br>
        <h4>
            <B>catégorie (Motel ou Village)</B><INPUT type="text" name="categorie" size=30>
        </h4>
        <h4>
            <B>type de chambre (Simple, Double, Familiale)</B><INPUT type="text" name="typeChambre" size=30>
        </h4>
        <h4>
            <B>date arrivée</B><INPUT type="text" name="dateArrivee" size=30>
        </h4>
        <h4>
            <B>nombre de nuits</B><INPUT type="text" name="nbNuits" size=30>
        </h4>
        <h4>
            <B>nom Client référent</B><INPUT type="text" name="persRef" size=30>
        </h4>
        <h4>
            <INPUT type="submit" name="action" value="recherche" size=10>
            <INPUT type="submit" name="action" value="valider" size=10>
        </h4>

        <table border="1" id="table">
            <thead>
            <tr>
                <th>///</th>
                <th>TypeChambre</th>
                <th>catégorie</th>
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
                <td><input type="radio" name="ChambreReservee" value="<%= i %>"/></td>
                <input type="hidden" name="<%= i %>" value="<%= liste.get(i).get_numeroChambre() %>"/>
                <td> <%= liste.get(i).get_typeChambre() %> </td>
                <td> <%= liste.get(i).get_categorie() %> </td>
                <td> <%= liste.get(i).get_numeroChambre() %> </td>
                <td> <%= liste.get(i).get_prixHTVA() %> </td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </form>
</body>
</html>
