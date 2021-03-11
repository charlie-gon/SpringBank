<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
<h3>크롤링</h3>
<form action="getBiz" method="post">
<input name="bizno"><button>조회</button>
</form>

<div id="result">
${bizname }<br>
${category }
</div>
</body>
</html>