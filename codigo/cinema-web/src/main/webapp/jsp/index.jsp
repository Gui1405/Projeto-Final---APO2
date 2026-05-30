<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.cinema.model.Cliente" %>
<%
    // Verifica se existe um utilizador logado para adaptar a barra de navegação
    Cliente usuarioLogado = (Cliente) session.getAttribute("usuarioLogado");
%>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cine-IFSP - Página Inicial</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .hero-banner {
            background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url('https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=1200') no-repeat center center;
            background-size: cover;
            color: white;
            padding: 100px 0;
        }
        .card-filme {
            transition: transform 0.2s;
        }
        .card-filme:hover {
            transform: scale(1.03);
        }
    </style>
</head>
<body class="bg-light">

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold text-primary" href="#">Cine-IFSP</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <ul class="navbar-nav align-items-center">
                    <li class="nav-item">
                        <a class="nav-link active" href="#">Início</a>
                    </li>
                    <% if (usuarioLogado == null) { %>
                        <li class="nav-item ms-2">
                            <a class="btn btn-outline-primary btn-sm px-3" href="login.jsp">Login</a>
                        </li>
                        <li class="nav-item ms-2">
                            <a class="btn btn-primary btn-sm px-3" href="cadastro.jsp">Cadastrar</a>
                        </li>
                    <% } else { %>
                        <li class="nav-item me-3">
                            <span class="navbar-text text-white">Olá, <%= usuarioLogado.getNome() %></span>
                        </li>
                        <li class="nav-item">
                            <% if ("ADMIN".equals(usuarioLogado.getPerfil())) { %>
                                <a class="btn btn-warning btn-sm px-3" href="admin/painel-admin.jsp">Painel Admin</a>
                            <% } else { %>
                                <a class="btn btn-primary btn-sm px-3" href="painel.jsp">Meu Painel</a>
                            <% } %>
                        </li>
                        <li class="nav-item ms-2">
                            <a class="btn btn-outline-danger btn-sm" href="${pageContext.request.contextPath}/logout">Sair</a>
                        </li>
                    <% } %>
                </ul>
            </div>
        </div>
    </nav>

    <header class="hero-banner text-center shadow-sm">
        <div class="container">
            <h1 class="display-4 fw-bold">Bem-vindo ao Cine-IFSP</h1>
            <p class="lead">Confira os grandes sucessos em cartaz e garanta o seu lugar.</p>
            <% if (usuarioLogado == null) { %>
                <a href="login.jsp" class="btn btn-primary btn-lg mt-3">Comprar Ingressos</a>
            <% } else if (!"ADMIN".equals(usuarioLogado.getPerfil())) { %>
                <a href="painel.jsp" class="btn btn-primary btn-lg mt-3">Comprar Ingressos</a>
            <% } %>
        </div>
    </header>

    <main class="container my-5">
        <h2 class="fw-bold text-secondary mb-4 pb-2 border-bottom">Filmes em Exibição</h2>
        
        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4" id="listaFilmesCards">
            </div>
    </main>

    <footer class="bg-dark text-white text-center py-4 mt-5">
        <div class="container">
            <p class="mb-0 small">&copy; 2026 Cine-IFSP. Todos os direitos reservados.</p>
        </div>
    </footer>

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        $(document).ready(function() {
            // Requisição AJAX para o Servlet de Filmes
            $.ajax({
                url: '${pageContext.request.contextPath}/filmes',
                method: 'GET',
                dataType: 'json',
                success: function(filmes) {
                    let cardsHtml = '';
                    
                    if (filmes.length === 0) {
                        cardsHtml = '<div class="col-12"><p class="text-muted text-center">Nenhum filme em cartaz no momento.</p></div>';
                    } else {
                        filmes.forEach(function(filme) {
                            cardsHtml += `
                                <div class="col">
                                    <div class="card h-100 shadow-sm border-0 card-filme">
                                        <div class="card-body d-flex flex-column p-4">
                                            <span class="badge bg-secondary mb-2 align-self-start">${filme.genero}</span>
                                            <h5 class="card-title fw-bold text-dark mb-2">${filme.titulo}</h5>
                                            <p class="card-text text-muted small flex-grow-1">${filme.sinopse}</p>
                                            <div class="mt-3 pt-3 border-top d-flex justify-content-between align-items-center">
                                                <span class="text-secondary small fw-medium">${filme.duracao} min</span>
                                                <span class="badge bg-info text-dark">${filme.classificacao}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            `;
                        });
                    }
                    
                    $('#listaFilmesCards').html(cardsHtml);
                },
                error: function() {
                    $('#listaFilmesCards').html('<div class="col-12"><div class="alert alert-danger">Erro ao carregar a programação.</div></div>');
                }
            });
        });
    </script>
</body>
</html>