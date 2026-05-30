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

    <!-- Modal Reserva -->
    <div class="modal fade" id="reservaModal" tabindex="-1" data-bs-theme="dark">
      <div class="modal-dialog">
        <div class="modal-content bg-dark text-light">
          <div class="modal-header border-secondary">
            <h5 class="modal-title text-info">Reservar Ingresso</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <form id="reservaForm">
                <!-- Simulando selecao de sessao. Para o APO2, simplifiquei colocando ID 1 direto -->
                <input type="hidden" name="sessaoId" id="modalSessaoId" value="1">
                <div class="mb-3">
                    <label>Quantidade de Ingressos</label>
                    <input type="number" name="quantidade" class="form-control bg-dark text-light border-secondary" min="1" max="10" value="1" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Confirmar Reserva</button>
            </form>
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
                        // Para simplificar a POC, forca a sessao de id correspondente ao filme (se existir)
                        $('#modalSessaoId').val(filmeId); 
                        $('#reservaModal').modal('show');
                    });
                }
            });

            // Fazer reserva
            $('#reservaForm').submit(function(e){
                e.preventDefault();
                $.ajax({
                    url: '../api/privada/reservas',
                    type: 'POST',
                    data: $(this).serialize(),
                    dataType: 'json',
                    success: function(resp){
                        $('#reservaModal').modal('hide');
                        $('#alertBox').removeClass('d-none alert-danger alert-success');
                        if(resp.status === 'success'){
                            $('#alertBox').addClass('alert-success').text(resp.message);
                        } else {
                            $('#alertBox').addClass('alert-danger').text(resp.message);
                        }
                    }
                });
            });
        });
    </script>
</body>
</html>
