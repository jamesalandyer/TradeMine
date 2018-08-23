<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Game" />
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		var gameEndedDate = '2018-08-20';
		if (${gameEnded && !soldAll}) {
			var playerSellData = $('#sell-data');
			var playerPortfolio = $('.playerPortfolio');
			var totalItems = $('.data-holder').length;
			var returnedItems = 0;
			playerPortfolio.each(function(index, element) {
				var el = $(element);
				el.children('span').each(function(index, element) {
					var stockData = $(element);
					var stockTickerData = stockData.data('stock');
					findStockPrice(stockTickerData, function(price, error) {
						if(!error) {
							console.log(totalItems);
							returnedItems += 1;
							console.log(returnedItems);
							playerSellData.val(playerSellData.val() + stockTickerData + ':' + price + '/');
							if (totalItems === returnedItems) {
								console.log(playerSellData.val());
								$('#endedForm').submit();
							}
						}
					}, gameEndedDate);
				});
			});
		} else if (${!gameEnded}) {
			$('.fixed-action-btn').floatingActionButton();
			$('.modal').modal();
			$('select').formSelect();
			$("#invitePlayer").click(
					function(event) {
						var isValid = $("#playersSelect").val();
						if (isValid) {
							$("#inviteForm").submit();
						}
			});
			var allMoney = $('#all-money');
			$('.playerPortfolio').each(function(index, element) {
				var el = $(element);
				var userId = el.data('id');
				var totalValue = 0;
				el.children('span').each(function(index, element) {
					var stockData = $(element);
					var stockTickerData = stockData.data('stock');
					var sharesData = stockData.data('shares');
					var currentValue;
					findStockPrice(stockTickerData, function(price, error) {
						if(!error) {
							currentValue = price;
						}
					});
					totalValue += currentValue * Number(sharesData);
				});
				addToTotalMoneyString(totalValue, $('#' + userId + '-money'));
				addToTotalMoneyString(totalValue, allMoney);
			});
		}
	});
</script>
<c:if test="${!gameEnded}">
<div id="inviteUser" class="modal modal-fixed-footer">
	<div class="modal-content">
		<div class="row">
			<div class="col s12">
				<h4 class="grey-text text-darken-3">Invite Player</h4>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="col s12">The game is more fun when you invite other
				players. Use the dropdown below to view players you can invite to
				your game!</div>
		</div>
		<br />
		<div class="row">
			<c:url var="formActionSend" value="/invites/send" />
			<form method="POST" action="${formActionSend}" id="inviteForm">
				<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" />
				<input type="hidden" name="gameId" value="${game.gameId}" />
				<div class="input-field col s12">
					<select id="playersSelect" name="inviteeId">
					<c:choose>
					<c:when test="${users.size() > 0}">
						<c:forEach var="user" items="${users}">
							<option value="${user.userId}"><c:out
									value="${user.userName}" /></option>
						</c:forEach>
						</c:when>
						<c:otherwise>
						<option value="" disabled selected>No Players Available</option>
						</c:otherwise>
						</c:choose>
					</select> <label>Player</label>
				</div>
			</form>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#!" class="modal-close waves-effect waves-teal btn-flat">Cancel</a>
		&nbsp; <a id="invitePlayer" class="waves-effect waves-teal btn">Invite</a>
	</div>
</div>
<div class="fixed-action-btn">
	<a
		class="waves-effect waves-light btn-floating btn-large teal btn modal-trigger"
		href="#inviteUser"> <i class="large material-icons">person_add</i>
	</a>
</div>
</c:if>
<div class="row">
	<div class="col s12 m8 grey-text text-darken-3">
		<h3>
			<c:out value="${game.gameName.toUpperCase()}" />
		</h3>
		<hr />
		<h5>
			<i class="material-icons circle grey darken-3 small white-text left">pie_chart_outlined</i>
			<span id="all-money"><c:out value="${total}" /></span>
		</h5>
		<h5>
			<i class="material-icons circle grey darken-3 small white-text left">group</i>
			<c:out value="${players.size()}" />
			<c:out value="${players.size() > 1 ? ' People' : ' Person'}" />
		</h5>
		<h5>
			<i class="material-icons circle grey darken-3 small white-text left">close</i>
			<c:out value="${game.endDate}" />
		</h5>
		<br /> <a href="/capstone/game/${game.gameId}/${currentUser.userName}" class="waves-effect waves-light btn"><i
			class="material-icons left">account_balance</i>Your Portfolio</a>
	</div>
</div>
<br />
<br />
<ul class="collection">
	<c:forEach var="player" items="${players}">
		<li
			class="collection-item avatar grey lighten-5 grey-text text-darken-2 valign-wrapper">
			<i class="material-icons circle teal">insert_chart</i> <span
			class="title"><c:out
					value="${(currentUser.userName == player.userName) ? 'You' : player.userName}" /></span>
			<div class="secondary-content right">
				<h5 id="${player.userId}-money" class="equal-margin">
					<c:out value="${player.moneyLeft}" />
				</h5>
			</div>
			<div class="playerPortfolio" data-id="${player.userId}" hidden>
				<c:forEach var="portfolio" items="${playerPortfolios.get(player.userId)}">
					<span class="data-holder" data-stock="${portfolio.stockSymbol}" data-shares="${portfolio.shares}" hidden></span>
				</c:forEach>
			</div>
		</li>
	</c:forEach>
</ul>
<c:if test="${gameEnded && !soldAll}">
<div hidden>
	<c:url var="formActionEnded" value="/game/${game.gameId}/ended" />
			<form method="POST" action="${formActionEnded}" id="endedForm">
				<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" />
				<input type="hidden" name="gameId" value="${game.gameId}" />
				<input type="hidden" id="sell-data" name="sellData" value="" />
			</form>
			</div>
</c:if>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />