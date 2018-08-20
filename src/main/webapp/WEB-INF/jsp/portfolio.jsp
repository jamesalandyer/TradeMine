<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Portfolio" />
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		var playerPortfolios = $('.portfolio-item');
		if (playerPortfolios.length > 0) {
			var stockPrices = playerPortfolios.map(function(index, item) {
				var portfolio = $(item);
				var stockSymbol = portfolio.data('symbol');
				var shares = portfolio.data('shares');
				getPriceForStock(stockSymbol, shares);
			});
		}
		$('.fixed-action-btn').floatingActionButton();
		$('.modal').modal();
		$("#invitePlayer").click(
				function(event) {
					var isValid = $("#playersSelect").val();
					if (isValid) {
						$("#inviteForm").submit();
					}
		});
	});
	
	var getPriceForStock = function(stockSymbol, shares) {
		var link = 'https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=' + stockSymbol + '&interval=1min&apikey=${apiKey}';
		$.ajax({url: link,
		    method: 'GET',
		    success: function (data) {
		    	var replace = $('#' + stockSymbol);
		    	if(data['Error Message']) {
		    		replace.text('Unable To Calculate');
		    	} else if (data['Information']) {
		    		replace.text('Please Try Again');
		    	} else {
		    		var price = data['Time Series (1min)'][data['Meta Data']['3. Last Refreshed']]['4. close'];
		    		var totalPrice = price * shares
		    		addToTotalMoneyString(totalPrice);
		    		replace.text(convertToMoneyString(totalPrice));
		    	}
		    }
		});
	};
	
	var numberWithCommas = function(x) {
		  return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	
	var convertToMoneyString = function(str) {
		return '$' + numberWithCommas(Math.round(Number(str) * 100) / 100);
	};
	
	var addToTotalMoneyString = function(amount) {
		var portfolioTotal = $('#portfolioTotal');
		var currentTotal = Number(portfolioTotal.text().replace('$','').replace(',',''));
		var newTotal = currentTotal + amount;
		return portfolioTotal.text(convertToMoneyString(newTotal));
	};
</script>
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
			<c:url var="formAction" value="/invites/send" />
			<form method="POST" action="${formAction}" id="inviteForm">
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
<div class="row">
	<div class="col s12 m8 grey-text text-darken-3">
		<h2>
			<c:out value="${gamePlayer.gameName.toUpperCase()}" />
		</h2>
		<hr />
		<h5>
			<i class="material-icons circle grey darken-3 small white-text left">attach_money</i>
			<c:out value="${gamePlayer.moneyLeft}" />
		</h5>
		<h5>
			<i class="material-icons circle grey darken-3 small white-text left">insert_chart</i>
			<span id="portfolioTotal">$0.00</span>
		</h5>
	</div>
</div>
<br />
<br />
<ul class="collection">
	<c:forEach var="playerPortfolio" items="${playerPortfolios}">
		<li
			class="collection-item avatar grey lighten-5 grey-text text-darken-2 portfolio-item" data-symbol="${playerPortfolio.stockSymbol}"
				data-shares="${playerPortfolio.shares}">
			<i class="material-icons circle teal">insert_chart</i> <span
			class="title"><c:out
					value="${playerPortfolio.stockSymbol}" /></span><br />
			<p><c:out value="${playerPortfolio.shares}" /><c:out value="${playerPortfolio.shares > 1 ? ' Shares': 'Share'}" /></p>
			<div class="secondary-content right">
				<h5 id="${playerPortfolio.stockSymbol}" class="equal-margin">Loading...</h5>
			</div>
		</li>
	</c:forEach>
</ul>
<div class="fixed-action-btn">
	<a
		class="waves-effect waves-light btn-floating btn-large teal btn modal-trigger"
		href="#inviteUser"> <i class="large material-icons">add</i>
	</a>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />