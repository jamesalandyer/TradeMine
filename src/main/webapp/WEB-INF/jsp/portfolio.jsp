<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Portfolio" />
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(
			function() {
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
				$('#stockTicker').on('change paste keyup', function(event) {
					if (event.target.value.length > 0) {
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
							});
						}
					} else {
						$('#purchaseForm').submit();
					}
				});
			});
</script>
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
				</div>
				<div class="input-field col s12" id="priceValue" hidden>
				<label>Per Share</label>
						<input id="price" name="price" placeholder="Per Share" type="text" readonly>
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
			class="collection-item avatar grey lighten-5 grey-text text-darken-2 portfolio-item"
			data-symbol="${playerPortfolio.stockSymbol}"
			data-shares="${playerPortfolio.shares}"><i
			class="material-icons circle teal">insert_chart</i> <span
			class="title"><c:out value="${playerPortfolio.stockSymbol}" /></span><br />
			<p>
				<c:out value="${playerPortfolio.shares}" />
				<c:out value="${playerPortfolio.shares > 1 ? ' Shares': 'Share'}" />
			</p>
			<div class="secondary-content right">
				<h5 id="${playerPortfolio.stockSymbol}" class="equal-margin">Loading...</h5>
			</div></li>
	</c:forEach>
</ul>
<div class="fixed-action-btn">
	<a
		class="waves-effect waves-light btn-floating btn-large teal btn modal-trigger"
		href="#buyStock"> <i class="large material-icons">add</i>
	</a>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />