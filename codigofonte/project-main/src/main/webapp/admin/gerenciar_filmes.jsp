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
    <title>Gerenciar Filmes - Admin APO2</title>
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
        <h2>Catálogo de Filmes</h2>
        <div class="row">
            <!-- Formulario Cadastro -->
            <div class="col-md-4">
                <div class="card-custom">
                    <h5 class="mb-3 text-success">Novo Filme</h5>
                    <div id="formAlert" class="alert d-none"></div>
                    <form id="filmeForm">
                        <div class="mb-2">
                            <label class="text-light">Título</label>
                            <input type="text" name="titulo" class="form-control bg-dark text-light border-secondary" required>
                        </div>
                        <div class="mb-2 row">
                            <div class="col-6">
                                <label class="text-light">Duração (min)</label>
                                <input type="number" name="duracao" class="form-control bg-dark text-light border-secondary" required>
                            </div>
                            <div class="col-6">
                                <label class="text-light">Gênero</label>
                                <input type="text" name="genero" class="form-control bg-dark text-light border-secondary" required>
                            </div>
                        </div>
                        <div class="mb-2">
                            <label class="text-light">Classificação</label>
                            <select name="classificacao" class="form-select bg-dark text-light border-secondary" required>
                                <option value="Livre">Livre</option>
                                <option value="10 anos">10 anos</option>
                                <option value="12 anos">12 anos</option>
                                <option value="14 anos">14 anos</option>
                                <option value="16 anos">16 anos</option>
                                <option value="18 anos">18 anos</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="text-light">Sinopse</label>
                            <textarea name="sinopse" class="form-control bg-dark text-light border-secondary" rows="3"></textarea>
                        </div>
                        
                        <hr class="border-secondary">
                        <h6 class="text-info">Alocar Sessão (Opcional)</h6>
                        <div class="mb-2">
                            <label class="text-light">Sala</label>
                            <select name="sala_id" id="salaSelect" class="form-select bg-dark text-light border-secondary">
                                <option value="">Não alocar no momento</option>
                            </select>
                        </div>
                        <div class="mb-2 row">
                            <div class="col-6">
                                <label class="text-light">Horário</label>
                                <input type="datetime-local" name="horario" class="form-control bg-dark text-light border-secondary">
                            </div>
                            <div class="col-6">
                                <label class="text-light">Valor (R$)</label>
                                <input type="number" step="0.01" name="valor_ingresso" class="form-control bg-dark text-light border-secondary">
                            </div>
                        </div>
                        <hr class="border-secondary">
                        <button type="submit" class="btn btn-primary w-100">Cadastrar</button>
                    </form>
                </div>
            </div>

            <!-- Listagem -->
            <div class="col-md-8">
                <div class="card-custom">
                    <h5 class="mb-3 text-info">Filmes Cadastrados</h5>
                    <div class="table-responsive">
                        <table class="table table-dark table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Título</th>
                                    <th>Gênero</th>
                                    <th>Duração</th>
                                    <th>Classificação</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody id="listaFilmes">
                                <!-- Ajax carrega aqui -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function carregarFilmes() {
            $.ajax({
                url: '../api/admin/filmes',
                type: 'GET',
                dataType: 'json',
                success: function(data){
                    var tbody = $('#listaFilmes');
                    tbody.empty();
                    $.each(data, function(i, f){
                        tbody.append('<tr>'+
                            '<td>'+ f.id +'</td>'+
                            '<td>'+ f.nome +'</td>'+
                            '<td>'+ f.genero +'</td>'+
                            '<td>'+ f.duracao +'m</td>'+
                            '<td><span class="badge bg-secondary">'+ f.classificacao +'</span></td>'+
                            '<td>'+
                                '<button class="btn btn-sm btn-outline-info me-1" onclick="editarFilme('+f.id+', \''+f.nome.replace(/'/g, "\\'")+'\', '+f.duracao+', \''+f.genero.replace(/'/g, "\\'")+'\', \''+f.classificacao+'\', \''+(f.sinopse ? f.sinopse.replace(/'/g, "\\'") : '')+'\')">Editar</button>'+
                                '<button class="btn btn-sm btn-outline-danger" onclick="apagarFilme('+f.id+')">Apagar</button>'+
                            '</td>'+
                        '</tr>');
                    });
                }
            });
        }

        function apagarFilme(id) {
            if(confirm("Tem certeza que deseja apagar este filme? Todas as sessões e ingressos vinculados serão deletados!")) {
                $.ajax({
                    url: '../api/admin/filmes?id=' + id,
                    type: 'DELETE',
                    dataType: 'json',
                    success: function(resp) {
                        alert(resp.message);
                        carregarFilmes();
                    }
                });
            }
        }

        function editarFilme(id, titulo, duracao, genero, classificacao, sinopse) {
            $('#editId').val(id);
            $('#editTitulo').val(titulo);
            $('#editDuracao').val(duracao);
            $('#editGenero').val(genero);
            $('#editClassificacao').val(classificacao);
            $('#editSinopse').val(sinopse);
            $('#editModal').modal('show');
        }

        $(document).ready(function(){
            carregarFilmes();

            // Carregar salas
            $.ajax({
                url: '../api/admin/salas',
                type: 'GET',
                dataType: 'json',
                success: function(data){
                    var select = $('#salaSelect');
                    $.each(data, function(i, s){
                        var status = s.disponivel ? "Livre" : "Bloqueada";
                        var attr = s.disponivel ? "" : "disabled";
                        select.append('<option value="'+s.id+'" '+attr+'>Sala '+s.numero+' (Cap: '+s.capacidade+') - '+status+'</option>');
                    });
                }
            });

            $('#filmeForm').submit(function(e){
                e.preventDefault();
                $.ajax({
                    url: '../api/admin/filmes',
                    type: 'POST',
                    data: $(this).serialize(),
                    dataType: 'json',
                    success: function(resp){
                        $('#formAlert').removeClass('d-none alert-danger alert-success');
                        if(resp.status === 'success'){
                            $('#formAlert').addClass('alert-success').text(resp.message);
                            $('#filmeForm')[0].reset();
                            carregarFilmes();
                        } else {
                            $('#formAlert').addClass('alert-danger').text(resp.message);
                        }
                    }
                });
            });

            $('#editFilmeForm').submit(function(e){
                e.preventDefault();
                $.ajax({
                    url: '../api/admin/filmes?' + $(this).serialize(),
                    type: 'PUT',
                    dataType: 'json',
                    success: function(resp){
                        if(resp.status === 'success'){
                            alert(resp.message);
                            $('#editModal').modal('hide');
                            carregarFilmes();
                        } else {
                            alert(resp.message);
                        }
                    }
                });
            });
        });
    </script>

    <!-- Modal Editar Filme -->
    <div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content text-light" style="background: #1e293b; border: 1px solid rgba(255,255,255,0.1);">
          <div class="modal-header border-secondary">
            <h5 class="modal-title text-info">Editar Filme</h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form id="editFilmeForm">
              <div class="modal-body">
                  <input type="hidden" name="id" id="editId">
                  <div class="mb-2">
                      <label class="text-light">Título</label>
                      <input type="text" name="titulo" id="editTitulo" class="form-control bg-dark text-light border-secondary" required>
                  </div>
                  <div class="mb-2 row">
                      <div class="col-6">
                          <label class="text-light">Duração (min)</label>
                          <input type="number" name="duracao" id="editDuracao" class="form-control bg-dark text-light border-secondary" required>
                      </div>
                      <div class="col-6">
                          <label class="text-light">Gênero</label>
                          <input type="text" name="genero" id="editGenero" class="form-control bg-dark text-light border-secondary" required>
                      </div>
                  </div>
                  <div class="mb-2">
                      <label class="text-light">Classificação</label>
                      <select name="classificacao" id="editClassificacao" class="form-select bg-dark text-light border-secondary" required>
                          <option value="Livre">Livre</option>
                          <option value="10 anos">10 anos</option>
                          <option value="12 anos">12 anos</option>
                          <option value="14 anos">14 anos</option>
                          <option value="16 anos">16 anos</option>
                          <option value="18 anos">18 anos</option>
                      </select>
                  </div>
                  <div class="mb-3">
                      <label class="text-light">Sinopse</label>
                      <textarea name="sinopse" id="editSinopse" class="form-control bg-dark text-light border-secondary" rows="3"></textarea>
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
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
