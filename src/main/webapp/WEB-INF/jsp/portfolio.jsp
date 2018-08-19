<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Portfolio" />
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('.confirmButton').click(function(event) {
			$('#gameId').val($(event.delegateTarget).data('game'));
			$('#confirmValue').val($(event.delegateTarget).data('confirm'));
			$('#invitesForm').submit();
		});
	});
</script>
<c:url var="formAction" value="/invites/confirm" />
<form method="POST" action="${formAction}" id="invitesForm">
	<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" /> <input
		type="hidden" name="gameId" id="gameId" value="" /> <input type="hidden"
		name="accept" id="confirmValue" value="" />
</form>
<c:choose>
	<c:when test="${playerInvites.size() > 0}">
		<ul class="collection">
			<c:forEach var="invite" items="${playerInvites}">
				<li
					class="collection-item avatar grey lighten-5 grey-text text-darken-2">
					<i class="material-icons circle teal">insert_chart</i> <span
					class="title"><c:out value="${invite.gameName.toUpperCase()}" /></span>
					<p>
						<c:out value="${invite.userName}" />
					</p>
					<div class="secondary-content">
						<a data-confirm="true" data-game="${invite.gameId}"
							class="btn-floating btn-medium waves-effect waves-light teal confirmButton"><i
							class="material-icons medium">add</i></a> &nbsp;&nbsp;<a
							data-confirm="false" data-game="${invite.gameId}"
							class="btn-floating btn-medium waves-effect waves-light teal confirmButton"><i
							class="material-icons">close</i></a>
					</div>
				</li>
			</c:forEach>
		</ul>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col s12 grey-text text-darken-3 center-align">
				<h4>You have no pending invites!</h4>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />