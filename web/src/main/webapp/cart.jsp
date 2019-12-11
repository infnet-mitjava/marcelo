<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cart</title>
</head>
<body>
	<form action="cart" method="post">
		<div>
			<label for="clientName">Cliente</label> 
			<input type="text" name="clientName" id="clientName">
		</div>
		<div>
			<label for="productName">Produto</label> 
			<input type="text" name="productName" id="productName">
		</div>
		<input  type="submit" value="Finalizar Pedido">
	</form>
</body>
</html>