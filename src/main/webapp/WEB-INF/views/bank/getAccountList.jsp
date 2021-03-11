<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#getBalance:hover{background-color: yellow}
</style>
</head>
<body>

${list }
<c:forEach var="bank" items="${list.res_list }">

	<ul>
		<li>은행명: ${bank.bank_name }</li>
		<li>통장이름: ${bank.account_alias }</li>
		<li>이름: ${bank.account_holder_name }</li>
		<li>계좌번호: ${bank.account_num_masked }</li>
		<li><a onclick="getBalance('${bank.fintech_use_num}')">핀테크 이용 번호: ${bank.fintech_use_num }</a></li>
		<li><a onclick="getBalance2('${bank.fintech_use_num}')">핀테크 이용 번호: ${bank.fintech_use_num }</a></li>
	</ul>
	<hr>
	
</c:forEach>

<div id="result"></div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>

	function getBalance(fin){ // html. 데이터만 리턴

		$.ajax({
			url:"getBalance",
			data: {fintech_use_num : fin}, // json으로 값을 받아오려면 이렇게 작성!
			//dataType: "json", // Controller에서 @Responsebody로 json을 넘겨받았기 때문에 받는 값도 json으로! 
			/* dataType:"html", // 메소드 호출 후 html로 값을 받아와야 div 태그에 값 삽입 가능? */
			success:function(response){ console.log(response); $('#result').html(response); }
		});
	}
	
	function getBalance2(fin){ // json. 데이터 리턴 + 태그 삽입

		$.ajax({
			url:"ajaxGetBalance",
			data: {fintech_use_num : fin}, // json으로 값을 받아오려면 이렇게 작성!
			dataType: "json", // Controller에서 @Responsebody로 json을 넘겨받았기 때문에 받는 값도 json으로! 
			success:function(response){
				$('#result').html(response);
				console.log(response);
					var ul = $('#result').append("<ul></ul>");
					var li = ul.append("<li></li>");
					li.append("은행명: " + response.bank_name + "<br>");
					li.append("상품명: " + response.product_name + "<br>");
					li.append("핀테크번호: " + response.fintech_use_num + "<br>");
					li.append("잔액: " + response.balance_amt);
					
					/*$.each(response, function(idx, value){
						li.append(idx + " / " + value[idx]);
					}); */
			}
		});
	}
</script>
</body>
</html>