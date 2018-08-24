var getStockPrice = function(stockSymbol, callback, date) {
	var username = '56841b101e809f8c127c5ec6e16d69f6';
	var password = '3505ed3bb5704616b56b8435623af23c';
	var link = 'https://api.intrinio.com/prices?identifier='
		+ stockSymbol + ((date) ? ('&start_date=' + date + '&end_date=' + date) : '') + '&frequency=daily&page_number=1&page_size=1';
	$.ajax({
		url : link,
		method : 'GET',
		beforeSend: function (xhr) {
		    xhr.setRequestHeader ("Authorization", "Basic " + btoa(username + ":" + password));
		},
		success : function(data) {
			var stockData = data['data'][0];
			if (stockData) {
				var lastRefreshedDate = stockData['date'];
				var price = stockData['open'];
				localStorage.setItem(stockSymbol, `${lastRefreshedDate}:${price}`);
				callback(Number(price).toFixed(2));
			} else {
				callback(null, 'Unable To Process');
			}
		}
	});
}

var findStockPrice = function(stockSymbol, callback, date) {
	var foundStock = localStorage.getItem(stockSymbol);
	var adjustedDate;
	if (date) {
		adjustedDate = date;
		if (adjustedDate.weekday === 0) {
			adjustedDate = adjustedDate.subtract(2, 'days');
		} else if (adjustedDate.weekday === 6) {
			adjustedDate = adjustedDate.subtract(1, 'days');
		}
	}
	if (foundStock) {
		var today = moment().format('YYYY-MM-DD');
		if (!adjustedDate) {
			if (moment().weekday() === 0) {
				adjustedDate = today.subtract(2, 'days');
			} else if (moment().weekday() === 6) {
				adjustedDate = today.subtract(1, 'days');
			}
		}
		if (adjustedDate) {
			adjustedDate = adjustedDate.format('YYYY-MM-DD');
		}
		if ((moment(today).isAfter(foundStock.split(':')[0]) && adjustedDate !== foundStock.split(':')[0]) || (adjustedDate && adjustedDate !== foundStock.split(':')[0])) {
			getStockPrice(stockSymbol, callback, adjustedDate);
		} else {
			callback(foundStock.split(':')[1]);
		}
	} else {
		getStockPrice(stockSymbol, callback, adjustedDate);
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
	return '$' + numberWithCommas((Math.round(Number(str) * 100) / 100).toFixed(2));
};

var addToTotalMoneyString = function(amount, elementToUse) {
	var currentTotal = Number(elementToUse.text().replace('$', '').replace(
			',', ''));
	var newTotal = currentTotal + amount;
	return elementToUse.text(convertToMoneyString(newTotal));
};