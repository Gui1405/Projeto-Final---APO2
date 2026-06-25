<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Cliente" %>
<%
    Cliente usuario = (Cliente) session.getAttribute("usuarioLogado");
    if(usuario == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meu Painel - Cinema APO2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            font-family: 'Outfit', sans-serif;
            background-color: #0f172a;
            color: #f8fafc;
        }
        .navbar {
            background: rgba(15, 23, 42, 0.9);
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        .card-custom {
            background: rgba(30, 41, 59, 0.7);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .btn-primary {
            background: linear-gradient(90deg, #0284c7, #4f46e5);
            border: none;
            border-radius: 50px;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" style="color:#38bdf8;" href="dashboard.jsp">CINEMA APO2</a>
            <div class="d-flex">
                <a href="filmes.jsp" class="btn btn-outline-info btn-sm me-2">Ver Filmes</a>
                <a href="../logout.jsp" class="btn btn-outline-danger btn-sm">Sair</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <h2>Bem-vindo, <%= usuario.getNome() %>!</h2>
        <hr class="border-secondary">

        <div class="row">
            <!-- Formulario Perfil -->
            <div class="col-md-4">
                <div class="card-custom">
                    <h5 class="mb-3 text-info">Meus Dados</h5>
                    <div id="perfilAlert" class="alert d-none"></div>
                    <form id="perfilForm">
                        <div class="mb-3">
                            <label class="form-label">Nome</label>
                            <input type="text" class="form-control bg-dark text-light border-secondary" name="nome" value="<%= usuario.getNome() %>" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">E-mail (Fixo)</label>
                            <input type="email" class="form-control bg-dark text-muted border-secondary" value="<%= usuario.getEmail() %>" disabled>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Telefone</label>
                            <input type="text" class="form-control bg-dark text-light border-secondary" name="telefone" value="<%= usuario.getTelefone() != null ? usuario.getTelefone() : "" %>">
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Atualizar Perfil</button>
                    </form>
                </div>
            </div>

            <!-- Historico de Reservas -->
            <div class="col-md-8">
                <div class="card-custom">
                    <h5 class="mb-3 text-info">Meus Ingressos</h5>
                    <div class="table-responsive">
                        <table class="table table-dark table-striped">
                            <thead>
                                <tr>
                                    <th>Data Compra</th>
                                    <th>Filme</th>
                                    <th>Horário</th>
                                    <th>Poltrona</th>
                                    <th>Valor</th>
                                    <th>Status</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody id="listaReservas">
                                <!-- Preenchido via AJAX -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function carregarIngressos() {
            $.ajax({
                url: '../api/privada/reservas',
                type: 'GET',
                dataType: 'json',
                success: function(data){
                    var tbody = $('#listaReservas');
                    tbody.empty();
                    if(data.length === 0){
                        tbody.append('<tr><td colspan="7" class="text-center text-muted">Nenhum ingresso encontrado.</td></tr>');
                        return;
                    }
                    $.each(data, function(i, res){
                        var statusBadge = res.status === 'COMPRADO' ? '<span class="badge bg-success">Comprado</span>' : '<span class="badge bg-secondary">Reembolsado</span>';
                        var acoes = res.status === 'COMPRADO' 
                            ? '<button class="btn btn-sm btn-outline-danger" onclick="solicitarReembolso('+res.id+')">Reembolsar</button>'
                            : '-';
                            
                        tbody.append('<tr>'+
                            '<td>'+ res.data +'</td>'+
                            '<td>'+ res.filme +'</td>'+
                            '<td>'+ new Date(res.horario).toLocaleString('pt-BR') +'</td>'+
                            '<td>'+ res.poltrona +'</td>'+
                            '<td>R$ '+ res.valor.toFixed(2) +'</td>'+
                            '<td>'+ statusBadge +'</td>'+
                            '<td>'+ acoes +'</td>'+
                        '</tr>');
                    });
                }
            });
        }

        function solicitarReembolso(id) {
            if(confirm("Tem certeza que deseja cancelar este ingresso e solicitar reembolso? A poltrona será liberada.")) {
                $.ajax({
                    url: '../api/privada/reservas?id=' + id,
                    type: 'DELETE',
                    dataType: 'json',
                    success: function(resp) {
                        alert(resp.message);
                        carregarIngressos();
                    }
                });
            }
        }

        $(document).ready(function(){
            carregarIngressos();

            // Atualizar Perfil
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
