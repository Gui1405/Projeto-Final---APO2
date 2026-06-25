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
    <title>Gerenciar Salas - Admin APO2</title>
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
        <h2>Gerenciamento de Salas</h2>
        <div class="row mt-4">
            <!-- Formulario Cadastro -->
            <div class="col-md-4">
                <div class="card-custom">
                    <h5 class="mb-3 text-success">Nova Sala</h5>
                    <div id="formAlert" class="alert d-none"></div>
                    <form id="salaForm">
                        <div class="mb-3">
                            <label class="text-light">Número da Sala</label>
                            <input type="number" name="numero" class="form-control bg-dark text-light border-secondary" required>
                        </div>
                        <div class="mb-4">
                            <label class="text-light">Capacidade (Lugares)</label>
                            <input type="number" name="capacidade" class="form-control bg-dark text-light border-secondary" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Cadastrar</button>
                    </form>
                </div>
            </div>

            <!-- Listagem -->
            <div class="col-md-8">
                <div class="card-custom">
                    <h5 class="mb-3 text-info">Salas Cadastradas</h5>
                    <div class="table-responsive">
                        <table class="table table-dark table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Número</th>
                                    <th>Capacidade</th>
                                    <th>Status</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody id="listaSalas">
                                <!-- Ajax carrega aqui -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Editar Sala -->
    <div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content text-light" style="background: #1e293b; border: 1px solid rgba(255,255,255,0.1);">
          <div class="modal-header border-secondary">
            <h5 class="modal-title text-info">Editar Sala</h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form id="editSalaForm">
              <div class="modal-body">
                  <input type="hidden" name="id" id="editId">
                  <div class="mb-3">
                      <label class="text-light">Número da Sala</label>
                      <input type="number" name="numero" id="editNumero" class="form-control bg-dark text-light border-secondary" required>
                  </div>
                  <div class="mb-3">
                      <label class="text-light">Capacidade (Lugares)</label>
                      <input type="number" name="capacidade" id="editCapacidade" class="form-control bg-dark text-light border-secondary" required>
                  </div>
                  <div class="mb-3">
                      <label class="text-light">Status Disponível?</label>
                      <select name="disponivel" id="editDisponivel" class="form-select bg-dark text-light border-secondary" required>
                          <option value="true">Sim (Livre)</option>
                          <option value="false">Não (Bloqueada/Manutenção)</option>
                      </select>
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
        function carregarSalas() {
            $.ajax({
                url: '../api/admin/salas',
                type: 'GET',
                dataType: 'json',
                success: function(data){
                    var tbody = $('#listaSalas');
                    tbody.empty();
                    $.each(data, function(i, s){
                        var status = s.disponivel ? '<span class="badge bg-success">Livre</span>' : '<span class="badge bg-danger">Bloqueada</span>';
                        tbody.append('<tr>'+
                            '<td>'+ s.id +'</td>'+
                            '<td>Sala '+ s.numero +'</td>'+
                            '<td>'+ s.capacidade +' lugares</td>'+
                            '<td>'+ status +'</td>'+
                            '<td>'+
                                '<button class="btn btn-sm btn-outline-info me-1" onclick="editarSala('+s.id+', '+s.numero+', '+s.capacidade+', '+s.disponivel+')">Editar</button>'+
                                '<button class="btn btn-sm btn-outline-danger" onclick="apagarSala('+s.id+')">Apagar</button>'+
                            '</td>'+
                        '</tr>');
                    });
                }
            });
        }

        function apagarSala(id) {
            if(confirm("Tem certeza que deseja apagar esta sala? Sessões e ingressos vinculados serão afetados!")) {
                $.ajax({
                    url: '../api/admin/salas?id=' + id,
                    type: 'DELETE',
                    dataType: 'json',
                    success: function(resp) {
                        alert(resp.message);
                        carregarSalas();
                    }
                });
            }
        }

        function editarSala(id, numero, capacidade, disponivel) {
            $('#editId').val(id);
            $('#editNumero').val(numero);
            $('#editCapacidade').val(capacidade);
            $('#editDisponivel').val(disponivel ? "true" : "false");
            $('#editModal').modal('show');
        }

        $(document).ready(function(){
            carregarSalas();

            $('#salaForm').submit(function(e){
                e.preventDefault();
                $.ajax({
                    url: '../api/admin/salas',
                    type: 'POST',
                    data: $(this).serialize(),
                    dataType: 'json',
                    success: function(resp){
                        $('#formAlert').removeClass('d-none alert-danger alert-success');
                        if(resp.status === 'success'){
                            $('#formAlert').addClass('alert-success').text(resp.message);
                            $('#salaForm')[0].reset();
                            carregarSalas();
                        } else {
                            $('#formAlert').addClass('alert-danger').text(resp.message);
                        }
                    }
                });
            });

            $('#editSalaForm').submit(function(e){
                e.preventDefault();
                $.ajax({
                    url: '../api/admin/salas?' + $(this).serialize(),
                    type: 'PUT',
                    dataType: 'json',
                    success: function(resp){
                        if(resp.status === 'success'){
                            alert(resp.message);
                            $('#editModal').modal('hide');
                            carregarSalas();
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
