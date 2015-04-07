<!-- 
Document   : jso
Created on : Feb 23, 2015, 5:29:42 AM
Author     : jiechen
--!>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"> </script>

		<script>

			$(document).ready(function()  { 
				// String='neo4j.json';
				$('#MyButton').click(function(){
					//var people = [];

					var data_file = "http://localhost:7654/Result/"+document.getElementById('mytext').value.toString();

					$.ajax({
						type:"GET", 
						url: data_file, 
						dataType: 'json',
						contentType: "application/json",
						crossDomain: true,
						success: function(data) {
							$("#userdata tbody").empty();
							$.each(data, function(i,f) {
								var tblRow = "<tr>" + "<td>" + f.record + "</td>" +
									"<td>" + f.column + "</td>" + "<td>" + f.table + "</td>" + "<td>" + f.database + "</td>" + "</tr>"
								$(tblRow).appendTo("#userdata tbody");

							}); 
						}, 
						error: function(jqXHR, textStatus, errorThrown) {
							alert(textStatus);
						},
					});

				});
			});

		</script>
	</head>

	<body>

		<div class="wrapper" align="center">
			<div class="profile" align="center">
				<h1 align="center">Key Word Search</h1>
				<form>

					<input type="text" id='mytext' align="center">
					<button type="button"  id="MyButton" align="center" >Search</button>
				</form>
				<form id="myform">


					<table id= "userdata" border="2" align="center">

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
		</div>

	</body>
</html>
