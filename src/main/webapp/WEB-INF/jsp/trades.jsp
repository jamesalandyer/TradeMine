<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Trades" />
<%@include file="/WEB-INF/jsp/common/header.jsp"%>
<div class="row">
	<div class="col s12 m8 grey-text text-darken-3">
		<h3>
			YOUR TRADES FOR <c:out value="${gameName.toUpperCase()}" />
		</h3>
		<hr />
		<br />
	</div>
</div>
<c:choose>
	<c:when test="${trades.size() > 0}">
		<ul class="collection">
			<c:forEach var="trade" items="${trades}">
				<li
					class="collection-item avatar grey lighten-5 grey-text text-darken-2">
					<i class="material-icons circle teal">insert_chart</i> <span
					class="title"><c:out value="${trade.stockSymbol}" /></span>
					<p>
						<c:out value="${trade.shares}" /><c:out value="${trade.shares > 1 ? ' Shares' : ' Share'}" /><br />
						<c:out value="${trade.pricePerShareFormatted} Per Share" /><br />
						<c:out value="${trade.purchase ? 'Purchased ' : 'Sold '}" />Date: <c:out value="${trade.transactionDate}" />
					</p>
					<div class="secondary-content right">
				<h5 class="equal-margin">
					<c:out value="${trade.purchase ? '-' : '+'}" /><c:out value="${trade.totalPriceFormatted}" />
				</h5>
			</div>
				</li>
			</c:forEach>
		</ul>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col s12 grey-text text-darken-3 center-align">
				<h4>You have no trades for this game!</h4>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />