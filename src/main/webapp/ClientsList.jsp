<%@page import="hei.tp01.model.*, java.util.List" contentType="text/html" pageEncoding="UTF-8"%>
<%! List<Client> clients; %>
<%clients = (List<Client>) request.getAttribute("clients"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<body>
	<h1>Gestion des clients</h1>
	<table>
		<tr>
			<th>Nom</th>
			<th>Prénom</th>
		</tr>
		<% for(Client clientCourant:clients){ %>
		<tr>
			<td><%=clientCourant.getNom() %></td>
			<td><%=clientCourant.getPrenom() %></td>
		</tr>
		<%} %>
	</table>
	<hr />
	<h2>Ajouter un nouveau client</h2>
	<form method="post" action="clients">
		<p><label>Nom</label><input type="text" name="nom" /></p>
		<p><label>Prénom</label> <input type="text" name="prenom" /></p>
		<p><input type="submit" value="envoyer" /></p>
	</form>
	<hr />
	<a href="login?logout">déconnexion</a>
</body>
</html>
