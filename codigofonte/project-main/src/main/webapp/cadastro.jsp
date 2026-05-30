<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro - Cinema APO2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            font-family: 'Outfit', sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #f8fafc;
            padding: 20px;
        }
        .card-glass {
            background: rgba(30, 41, 59, 0.7);
            backdrop-filter: blur(15px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 20px;
            padding: 40px;
            width: 100%;
            max-width: 500px;
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
        <h3 class="text-center mb-4 fw-bold" style="color: #38bdf8;">Criar Nova Conta</h3>
        
        <div id="alertBox" class="alert d-none" role="alert"></div>

        <form id="registroForm">
            <div class="mb-3">
                <label for="nome" class="form-label">Nome Completo</label>
                <input type="text" class="form-control" id="nome" name="nome" required>
            </div>
            <div class="mb-3 row">
                <div class="col-md-6">
                    <label for="cpf" class="form-label">CPF</label>
                    <input type="text" class="form-control" id="cpf" name="cpf" required>
                </div>
                <div class="col-md-6">
                    <label for="telefone" class="form-label">Telefone</label>
                    <input type="text" class="form-control" id="telefone" name="telefone">
                </div>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">E-mail</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>
            <div class="mb-4">
                <label for="senha" class="form-label">Senha</label>
                <input type="password" class="form-control" id="senha" name="senha" required>
            </div>
            <button type="submit" class="btn btn-primary w-100 mb-3" id="btnSubmit">Cadastrar</button>
            <div class="text-center">
                <a href="login.jsp" class="link-custom">Já tem uma conta? Faça login</a>
            </div>
        </form>
    </div>

    <script>
        $(document).ready(function() {
            $('#registroForm').submit(function(e) {
                e.preventDefault();
                
                var btn = $('#btnSubmit');
                btn.prop('disabled', true).text('Registrando...');
                
                $.ajax({
                    type: "POST",
                    url: "api/registro",
                    data: $(this).serialize(),
                    dataType: "json",
                    success: function(response) {
                        var alertBox = $('#alertBox');
                        alertBox.removeClass('d-none alert-success alert-danger');
                        
                        if (response.status === 'success') {
                            alertBox.addClass('alert-success').text(response.message);
                            $('#registroForm')[0].reset();
                            btn.prop('disabled', false).text('Cadastrar');
                        } else {
                            alertBox.addClass('alert-danger').text(response.message);
                            btn.prop('disabled', false).text('Cadastrar');
                        }
                    },
                    error: function() {
                        $('#alertBox').removeClass('d-none alert-success').addClass('alert-danger').text('Erro de comunicação com o servidor.');
                        btn.prop('disabled', false).text('Cadastrar');
                    }
                });
            });
        });
    </script>
</body>
</html>
