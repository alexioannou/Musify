<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 2/2/2021
  Time: 9:22 π.μ.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Testing</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
        $(document).ready(function() {
            $("button").click(function() {
                var url = "${createLink(controller:'Musify', action:'test2')}";
                $.ajax({url: url, success: function(result) {
                    $("h1").html("OMG IT KINDA WORKED BUT NOT REALLY<br/>"+result);
                    console.log(result);
                    }});
            });
        });
    </script>
</head>

<body>
    <h1>This is a page for testing</h1>
    <button>
        Test it
    </button>
</body>
</html>