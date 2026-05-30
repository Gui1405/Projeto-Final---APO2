<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.cinema.model.Cliente" %>
<%
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
            <span class="navbar-brand fw-bold mb-0 h1 text-warning">Área Administrativa</span>
            <div class="d-flex align-items-center">
                <span class="text-white me-4">Operador: <%= usuarioLogado.getNome() %></span>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">Sair</a>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="text-secondary m-0">Controle de Salas</h3>
            <button class="btn btn-dark" onclick="abrirModalLogs()">Ver Histórico de Serviços</button>
        </div>
        
        <div id="mensagemAlerta" class="alert d-none" role="alert"></div>

        <div class="row row-cols-1 row-cols-md-3 g-4" id="containerSalas">
            </div>
    </div>
	<div class="modal fade" id="modalLogs" tabindex="-1" aria-labelledby="modalLogsLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header bg-dark text-white">
                    <h5 class="modal-title" id="modalLogsLabel">Histórico de Limpeza e Manutenção</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>Data e Hora</th>
                                <th>Tipo</th>
                                <th>Sala</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody id="tabelaLogs">
                            </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                </div>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
    $(document).ready(function() {
        carregarSalas();
    });

    function carregarSalas() {
        $.ajax({
            url: '${pageContext.request.contextPath}/salas',
            method: 'GET',
            dataType: 'json',
            success: function(salas) {
                let html = '';
                
                if (salas.length === 0) {
                    html = '<div class="col-12"><p class="text-muted text-center">Nenhuma sala cadastrada no banco de dados.</p></div>';
                } else {
                    salas.forEach(function(sala) {
                        let statusClass = sala.disponivel ? 'border-success' : 'border-danger';
                        let statusText = sala.disponivel ? '<span class="badge bg-success">Disponível</span>' : '<span class="badge bg-danger">Manutenção/Limpeza</span>';
                        
                        html += '<div class="col">' +
                                '  <div class="card h-100 shadow-sm ' + statusClass + '">' +
                                '    <div class="card-body text-center">' +
                                '      <h4 class="card-title fw-bold">Sala ' + sala.numeroSala + '</h4>' +
                                '      <p class="text-muted mb-2">Capacidade: ' + sala.capacidade + ' lugares | Tipo: ' + sala.tipoSala + '</p>' +
                                '      <div class="mb-4">' + statusText + '</div>' +
                                '      <div class="d-grid gap-2">';

                        if (sala.disponivel) {
                            html += '        <button class="btn btn-outline-primary btn-sm" onclick="executarAcao(' + sala.id + ', \'INICIAR_LIMPEZA\')">Iniciar Limpeza</button>' +
                                    '        <button class="btn btn-outline-danger btn-sm" onclick="executarAcao(' + sala.id + ', \'INICIAR_MANUTENCAO\')">Iniciar Manutenção</button>';
                        } else {
                            html += '        <button class="btn btn-success btn-sm" onclick="executarAcao(' + sala.id + ', \'FINALIZAR_LIMPEZA\')">Finalizar Limpeza (Liberar)</button>' +
                                    '        <button class="btn btn-warning btn-sm" onclick="executarAcao(' + sala.id + ', \'FINALIZAR_MANUTENCAO\')">Finalizar Manut. (Liberar)</button>';
                        }

                        html += '      </div>' +
                                '    </div>' +
                                '  </div>' +
                                '</div>';
                    });
                }
                
                $('#containerSalas').html(html);
            },
            error: function() {
                mostrarAlerta('Erro ao carregar as salas do servidor.', 'danger');
            }
        });
    }

    function executarAcao(salaId, acao) {
        $.ajax({
            url: '${pageContext.request.contextPath}/salas',
            method: 'POST',
            data: { salaId: salaId, acao: acao },
            dataType: 'json',
            success: function(resposta) {
                if (resposta.status === 'sucesso') {
                    mostrarAlerta('Operação registrada com sucesso!', 'success');
                    carregarSalas();
                }
            },
            error: function() {
                mostrarAlerta('Erro ao registrar a operação. Tente novamente.', 'danger');
            }
        });
    }

    function mostrarAlerta(mensagem, tipo) {
        $('#mensagemAlerta')
            .removeClass('d-none alert-success alert-danger')
            .addClass('alert-' + tipo)
            .text(mensagem);
            
        setTimeout(function() {
            $('#mensagemAlerta').addClass('d-none');
        }, 3000);
    }
    
    function abrirModalLogs() {
        carregarLogs();
        var myModal = new bootstrap.Modal(document.getElementById('modalLogs'));
        myModal.show();
    }

    function carregarLogs() {
        $('#tabelaLogs').html('<tr><td colspan="4" class="text-center">Carregando histórico...</td></tr>');
        
        $.ajax({
            url: '${pageContext.request.contextPath}/logs-servicos',
            method: 'GET',
            dataType: 'json',
            success: function(logs) {
                let html = '';
                if (logs.length === 0) {
                    html = '<tr><td colspan="4" class="text-center text-muted">Nenhum serviço registrado.</td></tr>';
                } else {
                    logs.forEach(function(log) {
                        let badgeClass = (log.status === 'Concluído') ? 'bg-success' : 'bg-warning text-dark';
                        
                        html += '<tr>' +
                                '  <td>' + log.dataHora + '</td>' +
                                '  <td><span class="badge bg-secondary">' + log.tipo + '</span></td>' +
                                '  <td>Sala ' + log.numeroSala + '</td>' +
                                '  <td><span class="badge ' + badgeClass + '">' + log.status + '</span></td>' +
                                '</tr>';
                    });
                }
                $('#tabelaLogs').html(html);
            },
            error: function() {
                $('#tabelaLogs').html('<tr><td colspan="4" class="text-center text-danger">Erro ao buscar histórico.</td></tr>');
            }
        });
    }
    </script>
</body>
</html>