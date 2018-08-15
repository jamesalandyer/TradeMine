<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>${pageTitle} | TradeMine</title>
<script src="/capstone/js/jquery.min.js"></script>
<script src="/capstone/js/jquery.validate.min.js"></script>
<script src="/capstone/js/additional-methods.min.js "></script>
<script src="/capstone/js/moment.min.js"></script>
<script src="/capstone/js/materialize.min.js"></script>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="/capstone/css/materialize.min.css">
<link rel="stylesheet" type="text/css" href="/capstone/css/styles.css">
<link rel="icon" type="image/png" href="/capstone/img/favicon.png"/>

<script type="text/javascript">
	$(document).ready(function() {
		$(".logoutLink").click(function(event) {
			$("#logoutForm").submit();
		});

		var pathname = window.location.pathname;
		$("nav a[href='" + pathname + "']").parent().addClass("active");
		$('.sidenav').sidenav();
	});
</script>

</head>
<body>
	<header>
		<nav>
			<div class="nav-wrapper teal lighten-1">
				<div class="container">
					<img src="/capstone/img/logo.png" height="100%" class="brand-logo" />
					<a href="#" data-target="mobile-demo" class="sidenav-trigger"><i
						class="material-icons">menu</i></a>
					<ul id="nav-mobile" class="right hide-on-med-and-down">
						<c:url var="homePageHref" value="/" />
						<li><a
							href="${homePageHref}">Home</a></li>
						<c:if test="${not empty currentUser}">
							<c:url var="dashboardHref" value="/users/${currentUser}" />
							<li><a href="${dashboardHref}">Private Messages</a></li>
							<c:url var="newMessageHref"
								value="/users/${currentUser}/messages/new" />
							<li><a href="${newMessageHref}">New Message</a></li>
							<c:url var="sentMessagesHref"
								value="/users/${currentUser}/messages" />
							<li><a href="${sentMessagesHref}">Sent Messages</a></li>
							<c:url var="changePasswordHref"
								value="/users/${currentUser}/changePassword" />
							<li><a href="${changePasswordHref}">Change Password</a></li>
							<c:url var="logoutAction" value="/logout" />
							<li>
								<form id="logoutForm" action="${logoutAction}" method="POST">
									<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" />
								</form> <a class="logoutLink" href="#">Log Out</a>
							</li>
						</c:if>
						<c:if test="${empty currentUser}">
							<c:url var="newUserHref" value="/users/new" />
							<li><a href="${newUserHref}">Sign Up</a></li>
							<c:url var="loginHref" value="/login" />
							<li><a href="${loginHref}">Log In</a></li>
						</c:if>
					</ul>
					<ul class="sidenav" id="mobile-demo">
						<c:url var="homePageHref" value="/" />
						<li><a href="${homePageHref}">Home</a></li>
						<c:if test="${not empty currentUser}">
							<c:url var="dashboardHref" value="/users/${currentUser}" />
							<li><a href="${dashboardHref}">Private Messages</a></li>
							<c:url var="newMessageHref"
								value="/users/${currentUser}/messages/new" />
							<li><a href="${newMessageHref}">New Message</a></li>
							<c:url var="sentMessagesHref"
								value="/users/${currentUser}/messages" />
							<li><a href="${sentMessagesHref}">Sent Messages</a></li>
							<c:url var="changePasswordHref"
								value="/users/${currentUser}/changePassword" />
							<li><a href="${changePasswordHref}">Change Password</a></li>
							<c:url var="logoutAction" value="/logout" />
							<li><a class="logoutLink" href="#">Log Out</a></li>
						</c:if>
						<c:if test="${empty currentUser}">
							<c:url var="newUserHref" value="/users/new" />
							<li><a href="${newUserHref}">Sign Up</a></li>
							<c:url var="loginHref" value="/login" />
							<li><a href="${loginHref}">Log In</a></li>
						</c:if>
					</ul>
				</div>
			</div>
		</nav>
	</header>
	<br />
	<c:if test="${not empty currentUser}">
		<p id="currentUser">Current User: ${currentUser}</p>
	</c:if>
	<div class="container">