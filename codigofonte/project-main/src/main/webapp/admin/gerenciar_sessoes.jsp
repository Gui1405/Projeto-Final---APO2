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
    <title>Gerenciar Sessões - Admin APO2</title>
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
            <a href="dashboard.jsp" class="btn btn-outline-light btn-sm">Voltar</a>
        </div>
    </nav>

    <div class="container">
        <h2>Alocação de Sessões</h2>
        <div class="row mt-4">
            <!-- Formulario Cadastro -->
            <div class="col-md-4">
                <div class="card-custom">
                    <h5 class="mb-3 text-success">Nova Sessão</h5>
                    <div id="formAlert" class="alert d-none"></div>
                    <form id="sessaoForm">
                        <div class="mb-3">
                            <label class="text-light">Filme</label>
                            <select name="filme_id" id="selectFilme" class="form-select bg-dark text-light border-secondary" required>
                                <option value="">Carregando filmes...</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="text-light">Sala</label>
                            <select name="sala_id" id="selectSala" class="form-select bg-dark text-light border-secondary" required>
                                <option value="">Carregando salas...</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="text-light">Horário</label>
                            <input type="datetime-local" name="horario" class="form-control bg-dark text-light border-secondary" required>
                        </div>
                        <div class="mb-4">
                            <label class="text-light">Valor do Ingresso (R$)</label>
                            <input type="number" step="0.01" name="valor_ingresso" class="form-control bg-dark text-light border-secondary" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Criar Sessão</button>
                    </form>
                </div>
            </div>

            <!-- Listagem -->
            <div class="col-md-8">
                <div class="card-custom">
                    <h5 class="mb-3 text-info">Sessões Programadas</h5>
                    <div class="table-responsive">
                        <table class="table table-dark table-striped">
                            <thead>
                                <tr>
                                    <th>Filme</th>
                                    <th>Sala</th>
                                    <th>Horário</th>
                                    <th>Valor</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody id="listaSessoes">
                                <!-- Ajax carrega aqui -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Editar Sessao -->
    <div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content text-light" style="background: #1e293b; border: 1px solid rgba(255,255,255,0.1);">
          <div class="modal-header border-secondary">
            <h5 class="modal-title text-info">Editar Sessão</h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form id="editSessaoForm">
              <div class="modal-body">
                  <input type="hidden" name="id" id="editId">
                  <div class="mb-3">
                      <label class="text-light">Filme</label>
                      <select name="filme_id" id="editFilme" class="form-select bg-dark text-light border-secondary" required></select>
                  </div>
                  <div class="mb-3">
                      <label class="text-light">Sala</label>
                      <select name="sala_id" id="editSala" class="form-select bg-dark text-light border-secondary" required></select>
                  </div>
                  <div class="mb-3">
                      <label class="text-light">Horário</label>
                      <input type="datetime-local" name="horario" id="editHorario" class="form-control bg-dark text-light border-secondary" required>
                  </div>
                  <div class="mb-3">
                      <label class="text-light">Valor do Ingresso (R$)</label>
                      <input type="number" step="0.01" name="valor_ingresso" id="editValor" class="form-control bg-dark text-light border-secondary" required>
                  </div>
              </div>
              <div class="modal-footer border-secondary">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="submit" class="btn btn-primary">Salvar Alterações</button>
              </div>
          </form>
        </div>
      </div>
    </div>

    <script>
        function formatarData(dataISO) {
            if(!dataISO) return "";
            var data = new Date(dataISO);
            return data.toLocaleString('pt-BR');
        }

        function carregarDropdowns() {
            $.get('../api/admin/filmes', function(filmes) {
                var options = '<option value="">Selecione...</option>';
                $.each(filmes, function(i, f) {
                    options += '<option value="'+f.id+'">'+f.nome+'</option>';
                });
                $('#selectFilme').html(options);
                $('#editFilme').html(options);
            });

            $.get('../api/admin/salas', function(salas) {
                var options = '<option value="">Selecione...</option>';
                $.each(salas, function(i, s) {
                    if(s.disponivel) {
                        options += '<option value="'+s.id+'">Sala '+s.numero+'</option>';
                    }
                });
                $('#selectSala').html(options);
                $('#editSala').html(options);
            });
        }

        function carregarSessoes() {
            $.ajax({
                url: '../api/admin/sessoes',
                type: 'GET',
                dataType: 'json',
                success: function(data){
                    var tbody = $('#listaSessoes');
                    tbody.empty();
                    $.each(data, function(i, s){
                        tbody.append('<tr>'+
                            '<td>'+ s.filmeTitulo +'</td>'+
                            '<td>'+ s.numeroSala +'</td>'+
                            '<td>'+ formatarData(s.horario) +'</td>'+
                            '<td>R$ '+ s.valor_ingresso.toFixed(2) +'</td>'+
                            '<td>'+
                                '<button class="btn btn-sm btn-outline-info me-1" onclick="editarSessao('+s.id+', '+s.filmeId+', '+s.salaId+', \''+s.horario+'\', '+s.valor_ingresso+')">Editar</button>'+
                                '<button class="btn btn-sm btn-outline-danger" onclick="apagarSessao('+s.id+')">Apagar</button>'+
                            '</td>'+
                        '</tr>');
                    });
                }
            });
        }

        function apagarSessao(id) {
            if(confirm("Tem certeza que deseja cancelar esta sessão? Todos os ingressos vendidos serão perdidos!")) {
                $.ajax({
                    url: '../api/admin/sessoes?id=' + id,
                    type: 'DELETE',
                    dataType: 'json',
                    success: function(resp) {
                        alert(resp.message);
                        carregarSessoes();
                    }
                });
            }
        }

        function editarSessao(id, filmeId, salaId, horario, valor) {
            $('#editId').val(id);
            $('#editFilme').val(filmeId);
            $('#editSala').val(salaId);
            $('#editHorario').val(horario);
            $('#editValor').val(valor);
            $('#editModal').modal('show');
        }

        $(document).ready(function(){
            carregarDropdowns();
            carregarSessoes();

            $('#sessaoForm').submit(function(e){
                e.preventDefault();
                $.ajax({
                    url: '../api/admin/sessoes',
                    type: 'POST',
                    data: $(this).serialize(),
                    dataType: 'json',
                    success: function(resp){
                        $('#formAlert').removeClass('d-none alert-danger alert-success');
                        if(resp.status === 'success'){
                            $('#formAlert').addClass('alert-success').text(resp.message);
                            $('#sessaoForm')[0].reset();
                            carregarSessoes();
                        } else {
                            $('#formAlert').addClass('alert-danger').text(resp.message);
                        }
                    }
                });
            });

            $('#editSessaoForm').submit(function(e){
                e.preventDefault();
                $.ajax({
                    url: '../api/admin/sessoes?' + $(this).serialize(),
                    type: 'PUT',
                    dataType: 'json',
                    success: function(resp){
                        if(resp.status === 'success'){
                            alert(resp.message);
                            $('#editModal').modal('hide');
                            carregarSessoes();
                        } else {
                            alert(resp.message);
                        }
                    }
                });
            });
        });
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
