<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Facebook Friend Diff</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <!--link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css"-->

    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style type="text/css">
        li.list-group-item span {
            padding-left: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="page-header">
        <h1>Facebook Friend Diff</h1>
    </div>

    <#include "_alerts.ftl">

<#if authenticated>
    <#include "_friends.ftl">
<#else>
    <h3>Authenticate</h3>
    <a href="https://graph.facebook.com/oauth/authorize?client_id=${applicationId?url}&redirect_uri=${redirectUri?url}&scope=user_friends">Authenticate with Facebook</a>
</#if>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

</body>
</html>