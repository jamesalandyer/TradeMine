<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Sign Up"/>
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$.validator.addMethod('capitals', function(thing) {
							return thing.match(/[A-Z]/);
						});
						$("form")
								.validate(
										{

											rules : {
												userName : {
													required : true
												},
												password : {
													required : true,
													minlength : 15,
													capitals : true,
												},
												confirmPassword : {
													required : true,
													equalTo : "#password"
												}
											},
											messages : {
												password : {
													minlength : "Password too short, make it at least 15 characters",
													capitals : "Field must contain a capital letter",
												},
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
			<h5 class="grey-text text-darken-2">Sign Up For A TradeMine Account</h5>
			<br />
			<c:url var="formAction" value="/users" />
			<form method="POST" action="${formAction}">
				<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" />
				<div class="input-field">
					<input type="text" id="userName" name="userName"
						placeholder="Username" class="validate" required />
				</div>
				<div class="input-field">
					<input type="password" id="password" name="password"
						placeholder="Password" class="validate" minlength="15" required />
				</div>
				<div class="input-field">
					<input type="password" id="confirmPassword" name="confirmPassword"
						placeholder="Confirm Password" class="validate" required />
				</div>
				<button type="submit" class="btn btn-default">Create User</button>
			</form>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />