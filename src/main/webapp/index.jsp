<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Connexion</title>
</head>
<body>
	<h1>Bienvenue sur l'interface d'administration</h1>
	<%
        if(request.getAttribute("loginError")!=null) {
            out.print((String)request.getAttribute("loginError"));
        }else{
        	out.print("Merci de vous connecter !");
        }                  
    %>
	<form method="post" action="login">
		<table>
			<tr>
				<td>Votre login :</td>
				<td><input type="text" name="login" /></td>
			</tr>
			<tr>
				<td>Votre mot de passe :</td>
				<td><input type="password" name="password" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Me connecter !" /></td>
			</tr>
		</table>

	</form>

	

</body>
</html>
