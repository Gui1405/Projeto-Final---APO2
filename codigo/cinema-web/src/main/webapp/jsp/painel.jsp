<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.cinema.model.Cliente" %>
<%
    // Recupera o objeto do usuário logado na sessão
    Cliente usuarioLogado = (Cliente) session.getAttribute("usuarioLogado");
    
    // Proteção direta na página caso o usuário tente acessar digitando a URL sem logar
    if (usuarioLogado == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Painel do Cliente - Cine-IFSP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="#">Cine-IFSP</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <ul class="navbar-nav align-items-center">
                    <li class="nav-item">
                        <span class="nav-link text-white me-3">Olá, <%= usuarioLogado.getNome() %></span>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">Sair</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row mb-4">
            <div class="col">
                <h2 class="fw-bold text-secondary">Área do Cliente</h2>
                <p class="text-muted">Gerencie seus ingressos e escolha as próximas sessões.</p>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-md-6">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-body p-4 d-flex flex-column">
                        <h4 class="card-title fw-bold text-primary">Comprar Ingresso</h4>
                        <p class="card-text text-muted flex-grow-1">
                            Escolha o seu filme favorito, selecione a sessão disponível e reserve a sua poltrona em tempo real.
                        </p>
                        <a href="comprar-ingresso.jsp" class="btn btn-primary w-100 mt-3">Nova Compra</a>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-body p-4 d-flex flex-column">
                        <h4 class="card-title fw-bold text-success">Meus Ingressos</h4>
                        <p class="card-text text-muted flex-grow-1">
                            Consulte o histórico de compras, verifique os ingressos ativos ou realize o cancelamento de reservas.
                        </p>
                        <a href="meus-ingressos.jsp" class="btn btn-success w-100 text-white mt-3">Visualizar Histórico</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>