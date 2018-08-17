<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Home" />
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('.modal').modal();
		$('.datepicker').datepicker({
			container : 'body',
			minDate : new Date()
		});
		$('.fixed-action-btn').floatingActionButton()
		$("#createGame").click(function(event) {
			var isValid = ($("#datepicker").val() != "" && $("#name").val() != "") ;
		    if(isValid) {
		    	$("#createForm").submit();
		    }
		});
	});
</script>
<div id="addGame" class="modal modal-fixed-footer">
	<div class="modal-content">
		<div class="row">
			<div class="col s12">
				<h4 class="grey-text text-darken-3">Create A Game</h4>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="col s12">Create a new game to trade against other
				players. Pick your game name and when the game ends to start
				playing! Once you create the game your account will be funded and
				you can invite friends to join!</div>
		</div>
		<br />
		<div class="row">
			<c:url var="formAction" value="/game/create" />
			<form method="POST" action="${formAction}" id="createForm">
				<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" />
				<div class="input-field col s12">
					<input placeholder="Name" id="name" name="gameName" type="text"
						class="validate" maxlength="18"> <label for="name">Game
						Name</label>
				</div>
				<div class="input-field col s12">
					<input id="datepicker" placeholder="Date" name="endDate"
						type="text" class="datepicker validate"> <label
						for="datepicker">Game End Date</label>
				</div>
			</form>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#!" class="modal-close waves-effect waves-teal btn-flat">Cancel</a>
		&nbsp; <a id="createGame"
			class="waves-effect waves-teal btn">Create</a>
	</div>
</div>
<div class="row">
<c:forEach var="game" items="${gamesList}">
	<div class="col s12 m12 xl6">
		<div class="card grey lighten-5 grey-text text-darken-2">
			<div class="card-content">
				<span class="card-title"><c:out value="${game.gameName.toUpperCase()}" /></span>
				<hr />
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">show_chart</i>
					$4,390,399.00
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">group</i>
					<c:out value="${game.playerCount}" /> <c:out value="${(game.playerCount) > 1 ? 'People' : 'Person'}" />
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-2 small white-text left">close</i>
					<c:out value="${game.endDate}" />
				</h5>
			</div>
			<div class="card-action">
				<a href="/capstone/game" class="btn waves-effect waves-light">View
					Game</a>
			</div>
		</div>
	</div>
</c:forEach>
	<div class="fixed-action-btn">
		<a
			class="waves-effect waves-light btn-floating btn-large teal btn modal-trigger"
			href="#addGame"> <i class="large material-icons">add</i>
		</a>
	</div>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />