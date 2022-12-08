<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Olivier Collard et Adrien Gerez">
    <meta name="description" content="Page de connexion à notre application">
</head>

<body text="#dddddd" bgcolor="#353535">
<h2 align="center">Connexion to the website</h2>

<form method="POST" action="hello-servlet">
    <h4 align="center">
        <B>Username : </B><INPUT type="text" name="username" size=30>
    </h4>
    <h4 align="center">
        <B>Password : </B><INPUT type="password" name="password" size=30>
    </h4>
    <h4 align="center">
        <INPUT type="submit" name="action" value="Log in" size=10>
        <INPUT type="submit" name="action" value="Sign in" size=10>
    </h4>
</form>
</body>
</html>