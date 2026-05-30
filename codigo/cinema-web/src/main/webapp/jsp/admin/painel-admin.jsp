<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.cinema.model.Cliente" %>
<%
    // Recupera o objeto do usuário que foi salvo na sessão durante o login
    Cliente usuarioLogado = (Cliente) session.getAttribute("usuarioLogado");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Painel Administrativo - Cine-IFSP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">Área Administrativa</span>
            <span class="text-white">Bem-vindo(a), <%= usuarioLogado.getNome() %></span>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row">
            <div class="col-md-4">
                <div class="card text-white bg-primary mb-3">
                    <div class="card-header">Gerenciar Salas</div>
                    <div class="card-body">
                        <p class="card-text">Iniciar limpeza ou manutenção.</p>
                        <button class="btn btn-light btn-sm">Acessar</button>
                    </div>
                </div>
            </div>
            </div>
    </div>

</body>
</html>