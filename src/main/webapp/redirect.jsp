<%-- 
    Document   : redirect
    Created on : 17-giu-2019, 17.18.25
    Author     : agodino
--%>

<%@page import="rc.so.util.Utility"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%response.addHeader("X-Frame-Options", "SAMEORIGIN");%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="_csrf" content="4bfd1575-3ad1-4d21-96c7-4ef2d9f86721"/>
        <meta name="_csrf_header" content="X-CSRF-TOKEN"/>
        <meta http-equiv="Content-Security-Policy" content="default-src * 'unsafe-inline' 'unsafe-eval' data: blob:;">
        <title>YES I Start Up - Toscana</title>
    </head>
    <body>
        <form action="<%=request.getContextPath()%>/<%=request.getParameter("page")%>" method="post" name="form" htmlEscape="true"
              id="form" accept-charset="ISO-8859-1" style="display: none">
            <input type="hidden"
                   name="_csrf"
                   value="4bfd1575-3ad1-4d21-96c7-4ef2d9f86721"/>
            <%Enumeration<String> parameterNames = request.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String paramName = parameterNames.nextElement();
                    String[] paramValues = request.getParameterValues(paramName);
                    if (!paramName.equals("page")) {
                        for (int i = 0; i < paramValues.length; i++) {;%>
            <input name="<%=paramName%>" value="<%=paramValues[i]%>">
            <%}
                    }
                }%>
        </form>
    </body>
    <script src="assets/soop/js/jquery-3.6.1.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            document.getElementById("form").submit();
        });
    </script>
</html>