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
    <title>Gerenciar Manutenção - Admin APO2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { font-family: 'Outfit', sans-serif; background-color: #0f172a; color: #f8fafc; }
        .navbar { background: rgba(15, 23, 42, 0.9); border-bottom: 1px solid rgba(255, 255, 255, 0.1); }
        .card-custom { background: rgba(30, 41, 59, 0.7); border: 1px solid rgba(255,255,255,0.1); border-radius: 15px; padding: 20px; }
        .btn-iniciar { background: linear-gradient(90deg, #f59e0b, #d97706); border: none; color: white; }
        .btn-finalizar { background: linear-gradient(90deg, #10b981, #059669); border: none; color: white; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold text-warning" href="dashboard.jsp">CINEMA ADMIN - MANUTENÇÃO</a>
            <a href="dashboard.jsp" class="btn btn-outline-light btn-sm">Voltar ao Painel</a>
        </div>
    </nav>

    <div class="container pb-5">
        <h2 class="mb-4 text-center">Status de Manutenção das Salas</h2>
        <div id="alertBox" class="alert d-none max-w-md mx-auto text-center"></div>

        <div class="row" id="salasList">
            <!-- Renderizado via AJAX -->
        </div>
    </div>

    <script>
        $(document).ready(function(){
            function carregarSalas() {
                $.ajax({
                    url: '../api/admin/manutencao',
                    type: 'GET',
                    dataType: 'json',
                    success: function(data){
                        const grid = $('#salasList');
                        grid.empty();
                        $.each(data, function(i, s){
                            let statusTexto = s.statusManutencao || "Desconhecido";
                            let cardBorder = (statusTexto === 'EM ANDAMENTO') ? 'border-danger' : 'border-secondary';
                            let disponivelHtml = s.disponivel ? '<span class="badge bg-success">Livre para Venda</span>' : '<span class="badge bg-danger">Bloqueada</span>';

                            let html = '<div class="col-md-4 mb-4">' +
                                '<div class="card card-custom text-light ' + cardBorder + '">' +
                                    '<h4 class="text-warning">Sala ' + s.numero + '</h4>' +
                                    '<p>Status: <strong>' + statusTexto + '</strong></p>' +
                                    '<p>' + disponivelHtml + '</p>' +
                                    '<div class="d-flex justify-content-between mt-3">';
                            
                            if(statusTexto !== 'EM ANDAMENTO'){
                                html += '<button class="btn btn-iniciar btn-sm w-100" onclick="alterarManutencao('+s.salaId+', \'iniciar\')">Iniciar Manutenção</button>';
                            } else {
                                html += '<button class="btn btn-finalizar btn-sm w-100" onclick="alterarManutencao('+s.salaId+', \'finalizar\')">Finalizar Manutenção</button>';
                            }

                            html += '</div></div></div>';
                            grid.append(html);
                        });
                    }
                });
            }

            window.alterarManutencao = function(salaId, acao) {
                if(acao === 'iniciar' && !confirm('Atenção: Iniciar a manutenção irá bloquear a venda de ingressos para esta sala. Deseja continuar?')) return;
                
                $.ajax({
                    url: '../api/admin/manutencao',
                    type: 'POST',
                    data: { salaId: salaId, acao: acao },
                    success: function(resp){
                        if(resp.status === 'success'){
                            $('#alertBox').removeClass('d-none alert-danger').addClass('alert-success').text(resp.message);
                            carregarSalas();
                        } else {
                            $('#alertBox').removeClass('d-none alert-success').addClass('alert-danger').text(resp.message);
                        }
                    }
                });
            };

            carregarSalas();
        });
    </script>
</body>
</html>
