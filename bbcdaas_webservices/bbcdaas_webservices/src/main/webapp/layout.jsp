<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html>
	<head>
		<title><tiles:insertAttribute name="title"/></title>
		<tiles:insertAttribute name="head" />
	</head>
	<body>
		<div>
			<tiles:insertAttribute name="main" />
		</div>
	</body>
</html>