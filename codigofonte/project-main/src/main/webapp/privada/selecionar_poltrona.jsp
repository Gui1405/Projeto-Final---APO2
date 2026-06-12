<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Verificacao basica se esta logado, igual nos outros JSPs privados
    if(session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Selecionar Poltrona - APO2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body { font-family: 'Outfit', sans-serif; background-color: #0f172a; color: #f8fafc; }
        .navbar { background: rgba(15, 23, 42, 0.9); border-bottom: 1px solid rgba(255, 255, 255, 0.1); }
        .screen-container { width: 80%; max-width: 600px; margin: 20px auto; text-align: center; }
        .screen { background: linear-gradient(90deg, #38bdf8, #818cf8); height: 10px; border-radius: 5px; box-shadow: 0 10px 20px rgba(56, 189, 248, 0.5); margin-bottom: 40px; }
        
        .seats-container { display: grid; grid-template-columns: repeat(5, 1fr); gap: 15px; justify-content: center; max-width: 400px; margin: 0 auto; }
        
        .seat { 
            width: 50px; height: 50px; 
            background: #1e293b; 
            border: 2px solid #334155; 
            border-radius: 10px 10px 4px 4px;
            display: flex; align-items: center; justify-content: center;
            cursor: pointer; transition: 0.2s;
            font-size: 0.8rem;
        }
        .seat:hover:not(.occupied) { border-color: #38bdf8; background: #0ea5e9; color: white; }
        .seat.selected { background: #38bdf8; border-color: #0284c7; color: white; box-shadow: 0 0 10px #38bdf8; }
        .seat.occupied { background: #475569; border-color: #334155; color: #94a3b8; cursor: not-allowed; }
        
        .btn-primary { background: linear-gradient(90deg, #0284c7, #4f46e5); border: none; border-radius: 50px; padding: 10px 30px; }
        .legend { display: flex; justify-content: center; gap: 20px; margin-top: 30px; }
        .legend-item { display: flex; align-items: center; gap: 10px; font-size: 0.9rem; }
        .legend-box { width: 20px; height: 20px; border-radius: 4px; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" style="color:#38bdf8;" href="filmes.jsp">Voltar aos Filmes</a>
        </div>
    </nav>

    <div class="container pb-5">
        <h2 class="text-center mb-2">Escolha seus Assentos</h2>
        <p class="text-center text-muted mb-4">Sala <span id="salaNumero"></span></p>

        <div id="alertBox" class="alert d-none max-w-md mx-auto"></div>

        <div class="screen-container">
            <div class="screen"></div>
            <p class="text-muted small">TELA</p>
        </div>

        <div class="seats-container" id="seatsGrid">
            <!-- Renderizado via AJAX -->
        </div>

        <div class="legend">
            <div class="legend-item"><div class="legend-box" style="background: #1e293b; border: 2px solid #334155;"></div> Livre</div>
            <div class="legend-item"><div class="legend-box" style="background: #38bdf8;"></div> Selecionado</div>
            <div class="legend-item"><div class="legend-box" style="background: #475569;"></div> Ocupado</div>
        </div>

        <div class="text-center mt-5">
            <button id="btnComprar" class="btn btn-primary btn-lg" disabled>Confirmar Compra</button>
        </div>
    </div>

    <script>
        $(document).ready(function(){
            const urlParams = new URLSearchParams(window.location.search);
            const sessaoId = urlParams.get('sessaoId');
            const salaId = urlParams.get('salaId');
            
            if(!sessaoId || !salaId) {
                $('#alertBox').removeClass('d-none').addClass('alert-danger').text("Parâmetros inválidos.");
                return;
            }

            let poltronasSelecionadas = [];

            // Carregar Poltronas
            $.ajax({
                url: '../api/poltronas?salaId=' + salaId,
                type: 'GET',
                dataType: 'json',
                success: function(data){
                    const grid = $('#seatsGrid');
                    $.each(data, function(i, p){
                        let statusClass = p.disponivel ? 'available' : 'occupied';
                        grid.append(
                            '<div class="seat '+statusClass+'" data-id="'+p.id+'">' + p.numero + '</div>'
                        );
                    });

                    // Evento de click nos assentos
                    $('.seat.available').click(function(){
                        $(this).toggleClass('selected');
                        const pid = $(this).data('id');
                        
                        if($(this).hasClass('selected')){
                            poltronasSelecionadas.push(pid);
                        } else {
                            poltronasSelecionadas = poltronasSelecionadas.filter(id => id !== pid);
                        }
                        
                        $('#btnComprar').prop('disabled', poltronasSelecionadas.length === 0);
                    });
                }
            });

            // Enviar Compra
            $('#btnComprar').click(function(){
                $(this).prop('disabled', true).text("Processando...");
                
                const payload = {
                    sessaoId: parseFloat(sessaoId),
                    poltronas: poltronasSelecionadas
                };

                $.ajax({
                    url: '../api/privada/ingressos',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(payload),
                    success: function(resp){
                        if(resp.status === 'success'){
                            $('#alertBox').removeClass('d-none alert-danger').addClass('alert-success').text(resp.message + " Redirecionando...");
                            setTimeout(() => { window.location.href = "dashboard.jsp"; }, 2000);
                        } else {
                            $('#alertBox').removeClass('d-none alert-success').addClass('alert-danger').text(resp.message);
                            $('#btnComprar').prop('disabled', false).text("Confirmar Compra");
                        }
                    },
                    error: function(xhr){
                        $('#alertBox').removeClass('d-none alert-success').addClass('alert-danger').text("Erro na requisição. Tente novamente.");
                        $('#btnComprar').prop('disabled', false).text("Confirmar Compra");
                    }
                });
            });
        });
    </script>
</body>
</html>
