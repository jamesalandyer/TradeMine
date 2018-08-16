<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Game"/>
<%@include file="/WEB-INF/jsp/common/header.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('.fixed-action-btn').floatingActionButton().on('click', function() {
			console.log('here');
		});
	});
</script>

<div class="row">
	<div class="col s12 m8 grey-text text-darken-3">
		<h2>FAANG COMP</h2>
		<hr />
				<h5>
					<i
						class="material-icons circle grey darken-3 small white-text left">pie_chart_outlined</i>
					$12,390,399.00
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-3 small white-text left">group</i>
					4 People
				</h5>
				<h5>
					<i
						class="material-icons circle grey darken-3 small white-text left">close</i>
					November 6, 2019
				</h5>
				<br />
				<a class="waves-effect waves-light btn"><i class="material-icons left">account_balance</i>Your Portfolio</a>
	</div>
</div>
<br />
<br />
<ul class="collection">
    <li class="collection-item avatar grey lighten-5 grey-text text-darken-2 valign-wrapper">
      <i class="material-icons circle teal">insert_chart</i>
      <span class="title">JohnSmith</span>
      <div class="secondary-content right"><h5 class="equal-margin">$4,304,394.00</h5></div>
    </li>
    <li class="collection-item avatar grey lighten-5 grey-text text-darken-2 valign-wrapper">
      <i class="material-icons circle teal">insert_chart</i>
      <span class="title">You</span>
      <div class="secondary-content right"><h5 class="equal-margin">$4,304,394.00</h5></div>
    </li>
    <li class="collection-item avatar grey lighten-5 grey-text text-darken-2 valign-wrapper">
      <i class="material-icons circle teal">insert_chart</i>
      <span class="title">JohnSmith</span>
      <div class="secondary-content right"><h5 class="equal-margin">$4,304,394.00</h5></div>
    </li>
    <li class="collection-item avatar grey lighten-5 grey-text text-darken-2 valign-wrapper">
      <i class="material-icons circle teal">insert_chart</i>
      <span class="title">JohnSmith</span>
      <div class="secondary-content right"><h5 class="equal-margin">$4,304,394.00</h5></div>
    </li>
  </ul>
<div class="fixed-action-btn">
  <a class="btn-floating btn-large teal">
    <i class="large material-icons">person_add</i>
  </a>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp" />