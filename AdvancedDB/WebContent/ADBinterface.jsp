<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<link href="my.css" type="text/css" rel="stylesheet" />
<script>
	function demoDisplay() {
		document.getElementById("image1").style.display = "none";
		$(document)
				.ready(
						function() {
							// String='neo4j.json';
							$('#MyButton')
									.click(
											function() {
												//var people = [];

												var data_file = "http://localhost:7654/Result/"
														+ document
																.getElementById('mytext').value
																.toString();

												$
														.ajax({
															type : "GET",
															url : data_file,
															dataType : 'json',
															contentType : "application/json",
															crossDomain : true,
															success : function(
																	data) {
																$(
																		"#userdata tbody")
																		.empty();
																$
																		.each(
																				data,
																				function(
																						i,
																						f) {
																					var tblRow = "<tr>"
																							+ "<td>"
																							+ f.record
																							+ "</td>"
																							+ "<td>"
																							+ f.column
																							+ "</td>"
																							+ "<td>"
																							+ f.table
																							+ "</td>"
																							+ "<td>"
																							+ f.database
																							+ "</td>"
																							+ "</tr>"
																					$(
																							tblRow)
																							.appendTo(
																									"#userdata tbody");

																				});
															},
															error : function(
																	jqXHR,
																	textStatus,
																	errorThrown) {
																alert(textStatus);
															},
														});

											});
						});

	}
</script>
<title>Insert title here</title>
</head>
<body>

	<table align=center cellspacing=0 width=1000px height=700px border=0>
		<tr align=left>
			<td colspan=4><img src="images/logo.jpg" class="img-rounded"
				alt="Cinque Terre" width=250px height=150px></td>


			<td></td>
			<td></td>
		</tr>

		<tr align=center>
			<td></td>
			<td colspan=4>
				<h1>
					<img src="images/title.jpg" class="img-rounded" alt="Cinque Terre"
						width=800px height=150px>
				</h1>
			</td>

			<td></td>
		</tr>
		<tr align=center>
			<td width=70></td>

			<td colspan=2>
				<div id="C1" class="col-xs-4">

					<input class="form-control input-lg" type="text"
						value="Type the key word here..." id="mytext"
						onfocus="if (this.value=='Type the key word here...') this.value='';">
				</div>
				<div id="C2">
					<button type="button" id="mybutton" onclick="demoDisplay()"
						class="btn btn-primary btn-lg">SEARCH</button>
				</div>
			<td></td>

		</tr>
		<tr align=center>
			<td></td>
			<td colspan=4><img id="image1" src="images/G.jpg"
				class="img-rounded" alt="Cinque Terre" width=800px height=500px>
				<div>
					<form id="myform">
						<table id="userdata" border="2" align="center">

							<thead>
								<th>Record</th>
								<th>Column</th>
								<th>Table</th>
								<th>Database</th>
							</thead>
							<tbody>

							</tbody>
						</table>
					</form>
				</div>
				</td>
			<td></td>
		</tr>

	</table>







</body>
</html>