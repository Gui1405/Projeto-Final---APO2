<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Filmes em Cartaz - APO2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { font-family: 'Outfit', sans-serif; background-color: #0f172a; color: #f8fafc; }
        .navbar { background: rgba(15, 23, 42, 0.9); border-bottom: 1px solid rgba(255, 255, 255, 0.1); }
        .movie-card {
            background: rgba(30, 41, 59, 0.7);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 15px;
            transition: transform 0.3s;
        }
        .movie-card:hover { transform: translateY(-5px); }
        .btn-primary { background: linear-gradient(90deg, #0284c7, #4f46e5); border: none; border-radius: 50px; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" style="color:#38bdf8;" href="dashboard.jsp">CINEMA APO2</a>
            <a href="dashboard.jsp" class="btn btn-outline-light btn-sm">Voltar ao Painel</a>
        </div>
    </nav>

    <div class="container">
        <h2 class="mb-4">Filmes em Cartaz</h2>
        <div id="alertBox" class="alert d-none"></div>
        <div class="row" id="filmesGrid">
            <!-- Renderizado via AJAX -->
        </div>
    </div>

    <!-- Modal Escolher Sessão -->
    <div class="modal fade" id="sessaoModal" tabindex="-1" data-bs-theme="dark">
      <div class="modal-dialog">
        <div class="modal-content bg-dark text-light">
          <div class="modal-header border-secondary">
            <h5 class="modal-title text-info">Escolher Sessão</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <p class="text-muted">Selecione uma sessão abaixo para escolher sua poltrona:</p>
            <div id="sessoesList" class="list-group">
                <!-- Preenchido via AJAX -->
            </div>
          </div>
        </div>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function(){
            // Carregar filmes
            $.ajax({
                url: '../api/filmes',
                type: 'GET',
                dataType: 'json',
                success: function(data){
                    var grid = $('#filmesGrid');
                    $.each(data, function(i, f){
                        grid.append(
                            '<div class="col-md-4 mb-4">'+
                                '<div class="card movie-card p-3">'+
                                    '<h4 class="text-info">'+ f.nome +'</h4>'+
                                    '<p class="small text-muted">'+ f.genero +' | '+ f.duracao +' min | '+ f.classificacao +'</p>'+
                                    '<p>'+ f.sinopse +'</p>'+
                                    '<button class="btn btn-primary btn-sm mt-2 btn-reservar" data-id="'+ f.id +'">Comprar Ingresso</button>'+
                                '</div>'+
                            '</div>'
                        );
                    });

                    $('.btn-reservar').click(function(){
                        var filmeId = $(this).data('id');
                        $('#sessoesList').html('<p class="text-center">Carregando sessões...</p>');
                        $('#sessaoModal').modal('show');
                        
                        $.ajax({
                            url: '../api/sessoes?filmeId=' + filmeId,
                            type: 'GET',
                            dataType: 'json',
                            success: function(sessoes){
                                $('#sessoesList').empty();
                                if(sessoes.length === 0){
                                    $('#sessoesList').html('<p class="text-center text-warning">Nenhuma sessão disponível para este filme hoje.</p>');
                                    return;
                                }
                                $.each(sessoes, function(i, s){
                                    var btn = $('<button type="button" class="list-group-item list-group-item-action bg-dark text-light border-secondary mb-2 rounded">' +
                                        '<strong>Sala '+ s.numeroSala +'</strong> - ' + s.inicio.replace("T", " ") + 
                                    '</button>');
                                    
                                    btn.click(function() {
                                        window.location.href = "selecionar_poltrona.jsp?sessaoId=" + s.id + "&salaId=" + s.salaId;
                                    });
                                    
                                    $('#sessoesList').append(btn);
                                });
                            },
                            error: function() {
                                $('#sessoesList').html('<p class="text-center text-danger">Erro ao carregar as sessões.</p>');
                            }
                        });
                    });
                }
            });
        });
    </script>
</body>
</html>
