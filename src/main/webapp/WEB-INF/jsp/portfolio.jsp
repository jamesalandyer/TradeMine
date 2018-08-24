<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Portfolio" />
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(
			function() {
				if (${!gameEnded}) {
					var playerPortfolios = $('.portfolio-item');
					if (playerPortfolios.length > 0) {
						var stockPrices = playerPortfolios
								.map(function(index, item) {
									var portfolio = $(item);
									var stockSymbol = portfolio.data('symbol');
									var shares = portfolio.data('shares');
									getPriceForStock(stockSymbol, shares, function(
											formattedPriceStr, totalPrice, error) {
										var replace = $('#' + stockSymbol);
										if (error) {
											replace.text(error);
										} else {
											addToTotalMoneyString(totalPrice,
													$('#portfolioTotal'));
											replace.text(formattedPriceStr);
										}
									});
								});
					}
					$('.fixed-action-btn').floatingActionButton();
					$('.modal').modal({
						dismissible: true,
						onCloseEnd: function() {
							$('#price').val('');
							var optionsSelect = $('#shares-amount');
							optionsSelect.html('<option value="" disabled selected>Not Enough Money</option>');
							optionsSelect.formSelect();
							var ticker = $('#stockTicker');
							ticker.val('');
							ticker.removeAttr('readonly');
							var submit = $('#submit-purchase');
							submit.attr('disabled', 'disabled');
							submit.text('Search');
							$('#priceValue').attr('hidden', 'hidden');
							$('#share-select').attr('hidden', 'hidden');
						}
					});
					$('select').formSelect();
					$('.sell-button').on('click', function(event) {
						var el = $(event.delegateTarget);
						var stockSymbol = el.data('stock');
						var sharesAmount = el.data('shares');
						$('#stockTickerSale').val(stockSymbol);
						findStockPrice(stockSymbol, function(price, error) {
							if (error) {
								$('#price-sell').val(error);
							} else {
								$('#price-sell').val(price);
							}
						});
						var optionsSelect = $('#shares-sell');
						var optionsStr = '';
						for (var i = 1; i <= sharesAmount; i++) {
							var selected = (i == 1);
							optionsStr += '<option value="' + i + '"' + (selected ? ' selected ' : '') + '>' + i + ' Share' + ((i > 1) ? 's' : '') + '</option>';
						}
						optionsSelect.html(optionsStr);
						optionsSelect.formSelect();
						$('#submit-sell').removeAttr('disabled');
					});
					$('#submit-sell').click(function(event) {
						$('#sellForm').submit();
					})
					$('#stockTicker').on('change paste keyup', function(event) {
						if (event.target.value.length > 0) {
							$('#tickerInvalid').attr('hidden', 'hidden');
							$('#submit-purchase').removeAttr('disabled');
						} else {
							$('#submit-purchase').attr('disabled', 'disabled');
						}
					});
					$('#submit-purchase').click(function(event) {
						var submit = $(event.target);
						if (submit.text() === 'Search') {
							var ticker = $('#stockTicker');
							var stockSymbol = ticker.val();
							if (stockSymbol !== '') {
								ticker.attr('readonly', 'readonly');
								submit.attr('disabled', 'disabled');
								submit.text('Purchase');
								findStockPrice(stockSymbol, function(price, error) {
									if (error) {
										ticker.removeAttr('readonly');
										submit.text('Search');
										$('#tickerInvalid').removeAttr('hidden');
									} else {
										var amountLeft = ${gamePlayer.amountLeft};
										var availableOptions = Math.floor(Number(amountLeft) / Number(price));
										if (availableOptions > 0) {
											var priceLabel = $('#price');
											priceLabel.val(Number(price));
											$('#priceValue').removeAttr('hidden');
											var optionsSelect = $('#shares-amount');
											var optionsStr = '';
											for (var i = 1; i <= availableOptions; i++) {
												var selected = (i == 1);
												optionsStr += '<option value="' + i + '"' + (selected ? ' selected ' : '') + '>' + i + ' Share' + ((i > 1) ? 's' : '') + '</option>';
											}
											optionsSelect.html(optionsStr);
											optionsSelect.formSelect();
											submit.removeAttr('disabled');
										}
										$('#share-select').removeAttr('hidden');
									}
								});
							}
						} else {
							$('#purchaseForm').submit();
						}
					});
				}
			});
</script>
<c:if test="${!gameEnded}">
<div id="buyStock" class="modal modal-fixed-footer">
	<div class="modal-content">
		<div class="row">
			<div class="col s12">
				<h4 class="grey-text text-darken-3">Buy A Stock</h4>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="col s12">Add to your portfolio by purchasing a
				stock! Search for a stock and purchase as many as you can afford. Be
				careful on what you choose as the game depends on it!</div>
		</div>
		<br />
		<div class="row">
			<c:url var="formAction" value="purchase" />
			<form method="POST" action="${formAction}" id="purchaseForm">
				<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" />
				<input type="hidden" name="amountLeft" value="${gamePlayer.amountLeft}" />
				<div class="input-field col s12">
					<input id="stockTicker" name="stockTicker" type="text" maxlength="5"
						style="text-transform: uppercase"> <label for="stockTicker">Stock
						Ticker</label>
						<span id="tickerInvalid" hidden>Invalid Ticker</span>
				</div>
				<div class="input-field col s12" id="priceValue" hidden>
				<label>Per Share</label>
						<input id="price" name="price" placeholder="Loading..." type="text" readonly>
				</div>
				<div id="share-select" class="col s12" hidden>
				<label>Choose Amount</label>
					<select id="shares-amount" name="shares" class="browser-default">
						<option value="" disabled selected>Not Enough Money</option>
					</select>
				</div>
			</form>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#!" class="modal-close waves-effect waves-teal btn-flat"
			id="cancel-purchase">Cancel</a> &nbsp; <a id="submit-purchase"
			class="waves-effect waves-teal btn" disabled>Search</a>
	</div>
</div>
<div id="sellStock" class="modal modal-fixed-footer">
	<div class="modal-content">
		<div class="row">
			<div class="col s12">
				<h4 class="grey-text text-darken-3">Sell Your Shares</h4>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="col s12">Sell your shares to increase your cash on hand! Select how many of your shares you want to sell!</div>
		</div>
		<br />
		<div class="row">
			<c:url var="formAction" value="sell" />
			<form method="POST" action="${formAction}" id="sellForm">
				<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}" />
				<input type="hidden" name="amountLeft" value="${gamePlayer.amountLeft}" />
				<div class="input-field col s12">
					<input id="stockTickerSale" name="stockTicker" type="text" maxlength="5"
						style="text-transform: uppercase" placeholder="Ticker" readonly> <label for="stockTicker">Stock
						Ticker</label>
				</div>
				<div class="input-field col s12" id="priceValueSell">
				<label>Per Share</label>
						<input id="price-sell" name="price" placeholder="Per Share" type="text" readonly>
				</div>
				<div id="share-sell-select" class="col s12">
				<label>Choose Amount To Sell</label>
					<select id="shares-sell" name="shares" class="browser-default">
						<option value="" disabled selected>No Shares</option>
					</select>
				</div>
			</form>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#!" class="modal-close waves-effect waves-teal btn-flat"
			id="cancel-sell">Cancel</a> &nbsp; <a id="submit-sell"
			class="waves-effect waves-teal btn" disabled>Sell</a>
	</div>
</div>
<div class="fixed-action-btn">
	<a
		class="waves-effect waves-light btn-floating btn-large teal btn modal-trigger"
		href="#buyStock"> <i class="large material-icons">add</i>
	</a>
</div>
</c:if>
<div class="row">
	<div class="col s12 m8 grey-text text-darken-3">
		<h3>
			<c:out value="${gamePlayer.gameName.toUpperCase()}" />
		</h3>
		<hr />
		<h5>
			<i class="material-icons circle grey darken-3 small white-text left">attach_money</i>
			<c:out value="${gamePlayer.moneyLeft}" />
		</h5>
		<h5>
			<i class="material-icons circle grey darken-3 small white-text left">insert_chart</i>
			<span id="portfolioTotal">$0.00</span>
		</h5>
		<br /> <a href="/capstone/game/${gamePlayer.gameId}/${currentUser.userName}/trades" class="waves-effect waves-light btn"><i
			class="material-icons left">book</i>Your Trades</a>
	</div>
</div>
<br />
<br />
<ul class="collection">
<c:choose>
<c:when test="${playerPortfolios.size() > 0}">
<c:forEach var="playerPortfolio" items="${playerPortfolios}">
		<li
			class="collection-item avatar grey lighten-5 grey-text text-darken-2 portfolio-item"
			data-symbol="${playerPortfolio.stockSymbol}"
			data-shares="${playerPortfolio.shares}"><i
			class="material-icons circle teal">insert_chart</i> <span
			class="title"><c:out value="${playerPortfolio.stockSymbol}" /></span><br />
			<p>
				<c:out value="${playerPortfolio.shares}" />
				<c:out value="${playerPortfolio.shares != 1 ? ' Shares': 'Share'}" />
			</p>
			<div class="secondary-content right">
				<h5 id="${playerPortfolio.stockSymbol}" class="${(gameEnded) ? 'equal-margin' : 'inline-right'}"><c:out value="${(gameEnded) ? '$0.00' : 'Loading...'}" /></h5><c:if test="${!gameEnded}"><a href="#sellStock"
							data-stock="${playerPortfolio.stockSymbol}" data-shares="${playerPortfolio.shares}"
							class="btn-floating btn-small waves-effect waves-light teal bottom-margin modal-trigger sell-button"><i
							class="material-icons">close</i></a></c:if>
			</div></li>
	</c:forEach>
</c:when>
<c:otherwise>
<li
			class="collection-item avatar grey lighten-5 grey-text text-darken-2 portfolio-item"><i
			class="material-icons circle teal">insert_chart</i> <span
			class="title">No Stocks</span><br />
			<p>
				Purchase A Stock Below
			</p><div class="secondary-content right">
				<h5 class="equal-margin">$0.00</h5>
			</div></li>
</c:otherwise>
</c:choose>
</ul>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />