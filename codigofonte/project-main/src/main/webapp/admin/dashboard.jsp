<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Cliente" %>
<%
    Cliente usuario = (Cliente) session.getAttribute("usuarioLogado");
    if(usuario == null || !"ADMIN".equals(usuario.getPerfil())) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Painel Admin - Cinema APO2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { font-family: 'Outfit', sans-serif; background-color: #0f172a; color: #f8fafc; }
        .navbar { background: rgba(15, 23, 42, 0.9); border-bottom: 1px solid rgba(255, 255, 255, 0.1); }
        .card-custom { background: rgba(30, 41, 59, 0.7); border: 1px solid rgba(255,255,255,0.1); border-radius: 15px; padding: 20px; }
        .btn-primary { background: linear-gradient(90deg, #10b981, #059669); border: none; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold text-success" href="dashboard.jsp">CINEMA ADMIN</a>
            <div class="d-flex">
                <a href="gerenciar_filmes.jsp" class="btn btn-outline-info btn-sm me-2">Gerenciar Filmes</a>
                <a href="../logout.jsp" class="btn btn-outline-danger btn-sm">Sair</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <h2>Área Administrativa</h2>
        <hr class="border-secondary mb-4">

        <div class="row">
            <div class="col-md-6">
                <div class="card-custom">
                    <h5 class="text-success mb-3">Dados do Administrador</h5>
                    <div id="perfilAlert" class="alert d-none"></div>
                    <form id="perfilForm">
                        <div class="mb-3">
                            <label>Nome</label>
                            <input type="text" name="nome" class="form-control bg-dark text-light border-secondary" value="<%= usuario.getNome() %>" required>
                        </div>
                        <div class="mb-3">
                            <label>Telefone</label>
                            <input type="text" name="telefone" class="form-control bg-dark text-light border-secondary" value="<%= usuario.getTelefone() != null ? usuario.getTelefone() : "" %>">
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Salvar Alterações</button>
                    </form>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="card-custom">
                    <h5 class="text-info mb-3">Configurações Gerais</h5>
                    <p class="text-muted">Acesso restrito a usuários com nível ADMIN.</p>
                    <ul class="list-group list-group-flush bg-transparent">
                        <li class="list-group-item bg-transparent text-light border-secondary d-flex justify-content-between align-items-center">
                            Filmes Cadastrados
                            <a href="gerenciar_filmes.jsp" class="btn btn-sm btn-outline-light">Gerenciar</a>
                        </li>
                        <li class="list-group-item bg-transparent text-light border-secondary d-flex justify-content-between align-items-center">
                            Limpeza de Salas
                            <a href="limpeza.jsp" class="btn btn-sm btn-outline-info">Gerenciar</a>
                        </li>
                        <li class="list-group-item bg-transparent text-light border-secondary d-flex justify-content-between align-items-center">
                            Manutenção de Salas
                            <a href="manutencao.jsp" class="btn btn-sm btn-outline-warning">Gerenciar</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function(){
            $('#perfilForm').submit(function(e){
                e.preventDefault();
                $.ajax({
                    url: '../api/privada/perfil',
                    type: 'POST',
                    data: $(this).serialize(),
                    dataType: 'json',
                    success: function(resp){
                        $('#perfilAlert').removeClass('d-none alert-danger alert-success');
                        if(resp.status === 'success'){
                            $('#perfilAlert').addClass('alert-success').text(resp.message);
                        } else {
                            $('#perfilAlert').addClass('alert-danger').text(resp.message);
                        }
                    }
                });
            });
        });
    </script>
</body>
</html>
