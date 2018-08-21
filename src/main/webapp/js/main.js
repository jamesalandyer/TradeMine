var getStockPrice = function(stockSymbol, callback) {
	var link = 'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol='
		+ stockSymbol + '&apikey=6NAAJT5VBIBTUPFE';
	$.ajax({
		url : link,
		method : 'GET',
		success : function(data) {
			if (data['Error Message']) {
				callback(null, 'Unable To Calculate');
			} else if (data['Information']) {
				callback(null, 'Please Try Again');
			} else {
				var lastRefreshedDate = data['Meta Data']['3. Last Refreshed'];
				var price = data['Time Series (Daily)'][lastRefreshedDate]['1. open'];
				localStorage.setItem(stockSymbol, `${lastRefreshedDate}:${price}`);
				callback(price);
			}
		}
	});
}

var findStockPrice = function(stockSymbol, callback) {
	var foundStock = localStorage.getItem(stockSymbol);
	if (foundStock) {
		var today = moment().format('YYYY-MM-DD');
		if(moment(today).isAfter(foundStock.split(':')[0]) && (moment().weekday() !== 0 || moment().weekday() !== 6)) {
			getStockPrice(stockSymbol, callback);
		} else {
			callback(foundStock.split(':')[1]);
		}
	} else {
		getStockPrice(stockSymbol, callback);
	}
};

var getPriceForStock = function(stockSymbol, shares, callback) {
	findStockPrice(stockSymbol, function(price, error) {
		if(!error) {
			var totalPrice = Number(price) * Number(shares);
			callback(convertToMoneyString(totalPrice), totalPrice);
		} else {
			callback(null, null, error);
		}
	});
};

var numberWithCommas = function(x) {
	return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

var convertToMoneyString = function(str) {
	return '$' + numberWithCommas(Math.round(Number(str) * 100) / 100);
};

var addToTotalMoneyString = function(amount, elementToUse) {
	var currentTotal = Number(elementToUse.text().replace('$', '').replace(
			',', ''));
	var newTotal = currentTotal + amount;
	return elementToUse.text(convertToMoneyString(newTotal));
};