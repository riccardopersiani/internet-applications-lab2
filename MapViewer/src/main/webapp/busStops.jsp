<%@ page language="java" contentType="text/javascript; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

var busStops = <%= request.getSession().getAttribute("jsonBusStops") %>;

var busLine = <%= request.getSession().getAttribute("jsonBusLine") %>;

var lineStyle = {
    "color": "#ff7800",
    "weight": 5,
    "opacity": 0.65
};
