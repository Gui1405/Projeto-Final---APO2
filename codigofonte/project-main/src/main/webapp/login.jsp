<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Cinema APO2</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            font-family: 'Outfit', sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #f8fafc;
        }
        .card-glass {
            background: rgba(30, 41, 59, 0.7);
            backdrop-filter: blur(15px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 20px;
            padding: 40px;
            width: 100%;
            max-width: 400px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
            animation: slideUp 0.6s ease-out;
        }
        .form-control {
            background: rgba(15, 23, 42, 0.6);
            border: 1px solid rgba(255,255,255,0.1);
            color: white;
            border-radius: 10px;
        }
        .form-control:focus {
            background: rgba(15, 23, 42, 0.8);
            color: white;
            border-color: #38bdf8;
            box-shadow: 0 0 0 0.25rem rgba(56, 189, 248, 0.25);
        }
        .btn-primary {
            background: linear-gradient(90deg, #0284c7, #4f46e5);
            border: none;
            border-radius: 50px;
            font-weight: 600;
            padding: 10px;
            transition: transform 0.2s;
        }
        .btn-primary:hover {
            transform: scale(1.02);
        }
        .link-custom {
            color: #38bdf8;
            text-decoration: none;
            font-size: 0.9rem;
        }
        .link-custom:hover {
            text-decoration: underline;
        }
        @keyframes slideUp {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>
    <div class="card-glass">
        <h3 class="text-center mb-4 fw-bold" style="color: #38bdf8;">Acesse sua Conta</h3>
        
        <div id="alertBox" class="alert d-none" role="alert"></div>

        <form id="loginForm">
            <div class="mb-3">
                <label for="email" class="form-label">E-mail</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>
            <div class="mb-4">
                <label for="senha" class="form-label">Senha</label>
                <input type="password" class="form-control" id="senha" name="senha" required>
            </div>
            <button type="submit" class="btn btn-primary w-100 mb-3" id="btnSubmit">Entrar</button>
            <div class="d-flex justify-content-between mb-4">
                <a href="recuperar.jsp" class="link-custom">Esqueceu a senha?</a>
                <a href="cadastro.jsp" class="link-custom">Criar conta</a>
            </div>
            <a href="index.jsp" class="btn btn-outline-secondary w-100" style="border-radius: 50px;">Voltar à Home</a>
        </form>
    </div>

    <script>
        $(document).ready(function() {
            $('#loginForm').submit(function(e) {
                e.preventDefault(); // Evita recarregamento da pagina
                
                var btn = $('#btnSubmit');
                btn.prop('disabled', true).text('Aguarde...');
                
                $.ajax({
                    type: "POST",
                    url: "api/login",
                    data: $(this).serialize(),
                    dataType: "json",
                    success: function(response) {
                        var alertBox = $('#alertBox');
                        alertBox.removeClass('d-none alert-success alert-danger');
                        
                        if (response.status === 'success') {
                            alertBox.addClass('alert-success').text(response.message);
                            setTimeout(function(){
                                window.location.href = response.redirect || "privada/dashboard.jsp";
                            }, 1000);
                        } else {
                            alertBox.addClass('alert-danger').text(response.message);
                            btn.prop('disabled', false).text('Entrar');
                        }
                    },
                    error: function() {
                        $('#alertBox').removeClass('d-none alert-success').addClass('alert-danger').text('Erro de comunicação com o servidor.');
                        btn.prop('disabled', false).text('Entrar');
                    }
                });
            });
        });
    </script>
</body>
</html>
