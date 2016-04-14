<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.amazonaws.*" %>
<%@ page import="com.amazonaws.auth.*" %>
<%@ page import="com.amazonaws.services.ec2.*" %>
<%@ page import="com.amazonaws.services.ec2.model.*" %>
<%@ page import="com.amazonaws.services.s3.*" %>
<%@ page import="com.amazonaws.services.s3.model.*" %>
<%@ page import="com.amazonaws.services.dynamodbv2.*" %>
<%@ page import="com.amazonaws.services.dynamodbv2.model.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jQuery, Ajax and Servlet/JSP integration example</title>
 
<script src="http://code.jquery.com/jquery-1.10.2.js"
    type="text/javascript"></script>
<script  type="text/javascript">
$(document).ready(function() {
    $('#userName').blur(function(event) {
            var name = $('#userName').val();
            $.get('GetUserServlet', {
                    userName : name
            }, function(responseText) {
                    $('#ajaxGetUserServletResponse').text(responseText);
            });
    });
});

</script>
</head>
<body>
 
    <form>
        Enter Your Name: <input type="text" id="userName" />
    </form>
    <br>
    <br>
 
    <strong>Ajax Response</strong>:
    <div id="ajaxGetUserServletResponse"></div>
</body>
</html>