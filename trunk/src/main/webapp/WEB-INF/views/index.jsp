<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Greetings.com</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<noscript>
<link rel="stylesheet" href="resources/css/5grid/core.css" />
<link rel="stylesheet" href="resources/css/5grid/core-desktop.css" />
<link rel="stylesheet" href="resources/css/5grid/core-1200px.css" />
<link rel="stylesheet" href="resources/css/5grid/core-noscript.css" />
<link rel="stylesheet" href="resources/css/style.css" />
<link rel="stylesheet" href="resources/css/style-desktop.css" />
</noscript>
<script src="resources/css/5grid/jquery.js"></script>
<script src="resources/css/5grid/init.js?use=mobile,desktop,1000px&amp;mobileUI=1&amp;mobileUI.theme=none"></script>
<!--[if IE 9]><link rel="stylesheet" href="css/style-ie9.css" /><![endif]-->
</head>
<body class="homepage">
<div id="header-wrapper">
	<header id="header">
		<div class="5grid-layout">
			<div class="row">
				<div class="4u" id="logo">
					<h1><a href="#" class="mobileUI-site-name">Greetings.com</a></h1>
					<p>by Solweaver</p>
				</div>
				<div class="8u" id="menu">
					<nav class="mobileUI-site-nav">
						<ul>
							<li class="current_page_item"><a href="socialauth?id=facebook">FaceBook</a></li>
							<li><a href="socialauth?id=linkedin">LinkedIn</a></li>
							<li><a href="socialauth?id=google">Google</a></li>
							<li><a href="twocolumn2.html">Two Column #2</a></li>
							<li><a href="onecolumn.html">One Column</a></li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</header>
</div>
<div class="5grid-layout">
	<div class="row">
		<p>Hi ${userProfile.firstName }</p>
		<p>Your email is ${userProfile.email }</p>
		<p>You are logged in using ${userProfile.providerId}</p>
	</div>
	<div class="row">
		<div id="banner" class="12u">
			<div class="container"><a href="#"><img src="resources/images/pics01.jpg" alt=""></a></div>
		</div>
	</div>
</div>

<div>
	<div class="5grid-layout" id="copyright">
		<div class="row">
			<div class="12u">
				<p>&copy; Greeting.com </a></p>
			</div>
		</div>
	</div>
</div>
</body>
</html>