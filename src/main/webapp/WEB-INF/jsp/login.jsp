<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Login"/>
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(function () {
	
		$("form").validate({
			
			rules : {
				userName : {
					required : true
				},
				password : {
					required : true
				}
			},
			messages : {			
				confirmPassword : {
					equalTo : "Passwords do not match"
				}
			},
			errorClass : "error"
		});
	});
</script>


 <div class="row center-align">
    <div class="col s12 m6 offset-m3">
      <div class="card-panel white darken-1">
      	<h5 class="grey-text text-darken-2">Welcome To TradeMine!</h5>
      	<br />
        <c:url var="formAction" value="/login" />
		<form method="POST" action="${formAction}">
		<input type="hidden" name="destination" value="${param.destination}"/>
		<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}"/>
			<div class="input-field">
				<input type="text" id="userName" name="userName" placeholder="Username" class="validate" required />
			</div>
			<div class="input-field">
				<input type="password" id="password" name="password" placeholder="Password" class="validate" required />
			</div>
			<button type="submit" class="btn btn-default">Login</button>
		</form>
		<br />
		<a href="/capstone/users/new" class="waves-effect waves-teal btn-flat">Don't Have An Account? Sign Up!</a>
      </div>
    </div>
  </div>
        
<c:import url="/WEB-INF/jsp/common/footer.jsp" />