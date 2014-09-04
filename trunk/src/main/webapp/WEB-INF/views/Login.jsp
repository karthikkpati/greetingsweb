<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Greet Invite</title>
<link rel="stylesheet" href="resources/Content/bootstrap.css" />
<link rel="stylesheet" href="resources/Content/bootstrap-responsive.css" />
<link rel="stylesheet" href="resources/Content/font-awesome.css" />
<link rel="stylesheet" href="resources/Content/social-buttons.css" />
<link rel="stylesheet" href="resources/Content/bootstrap.css" />

<link rel="stylesheet" href="resources/Styles/MainStyle.css" />
</head>
<body>


     
<div class="box signin">



<button class="btn btn-facebook loginOptionJS"><i class="icon-facebook"></i> | Connect with Facebook</button>

<button class="btn btn-google-plus loginOptionJS"><i class="icon-google-plus"></i> | Connect with Google Plus</button>

<button class="btn btn-linkedin loginOptionJS"><i class="icon-linkedin"></i> | Connect with LinkedIn</button>




<!-- socialauth?id=facebook -->





</div>



<script src="resources/Scripts/jquery-2.0.3.js"></script>
<script src="resources/Scripts/bootstrap.js"></script>

<script src="resources/Scripts/knockout-2.0.3.debug.js"></script>

<script src="resources/Scripts/Login.js"></script>



<% if (session.getAttribute("errorProfile") == null) { %>
    <p>No Value Error Dev Testing </p>
<% } else if (session.getAttribute("errorProfile").equals("0")) {%>
    <p> Key problem check Key </p>
<% } 
else if (session.getAttribute("errorProfile").equals("1")) {%>
   <p> Error Occurred We are trying to resolve </p>
<% }%>



</body>
</html>