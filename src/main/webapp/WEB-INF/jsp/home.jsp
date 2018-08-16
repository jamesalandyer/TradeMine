<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Home" />
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('.fixed-action-btn').floatingActionButton().on('click', function() {
			console.log('here');
		});
	});
</script>

<div class="row">
	<div class="col s12 m12 xl6">
		<div class="card grey lighten-5 grey-text text-darken-2">
			<div class="card-content">
				<span class="card-title">FAANG COMP</span>
				<hr />
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">show_chart</i>
					$4,390,399.00
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">group</i>
					4 People
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">close</i>
					November 6, 2019
				</h5>
			</div>
			<div class="card-action">
				<a href="/capstone/game" class="btn waves-effect waves-light">View Game</a>
			</div>
		</div>
	</div>
	<div class="col s12 m12 xl6">
		<div class="card grey lighten-5 grey-text text-darken-2">
			<div class="card-content">
				<span class="card-title">FAANG COMP</span>
				<hr />
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">show_chart</i>
					$4,390,399.00
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">group</i>
					4 People
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">close</i>
					November 6, 2019
				</h5>
			</div>
			<div class="card-action">
				<a href="#" class="btn waves-effect waves-light">View Game</a>
			</div>
		</div>
	</div>
	<div class="col s12 m12 xl6">
		<div class="card grey lighten-5 grey-text text-darken-2">
			<div class="card-content">
				<span class="card-title">FAANG COMP</span>
				<hr />
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">show_chart</i>
					$4,390,399.00
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">group</i>
					4 People
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">close</i>
					November 6, 2019
				</h5>
			</div>
			<div class="card-action">
				<a href="#" class="btn waves-effect waves-light">View Game</a>
			</div>
		</div>
	</div>
	<div class="col s12 m12 xl6">
		<div class="card grey lighten-5 grey-text text-darken-2">
			<div class="card-content">
				<span class="card-title">FAANG COMP</span>
				<hr />
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">show_chart</i>
					$4,390,399.00
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">group</i>
					4 People
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">close</i>
					November 6, 2019
				</h5>
			</div>
			<div class="card-action">
				<a href="#" class="btn waves-effect waves-light">View Game</a>
			</div>
		</div>
	</div>
	<div class="fixed-action-btn">
		<a class="btn-floating btn-large teal"> <i
			class="large material-icons">add</i>
		</a>
	</div>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />