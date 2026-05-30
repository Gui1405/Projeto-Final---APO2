<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro - Cine-IFSP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center" style="height: 100vh;">

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h3 class="text-center mb-4">Criar Conta</h3>
                        
                        <div id="mensagemAlerta" class="alert d-none" role="alert"></div>
                        
                        <form id="formCadastro">
                            <div class="mb-3">
                                <label for="nome" class="form-label">Nome Completo</label>
                                <input type="text" class="form-control" id="nome" name="nome" required>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">E-mail</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                            <div class="mb-3">
                                <label for="telefone" class="form-label">Telefone</label>
                                <input type="text" class="form-control" id="telefone" name="telefone" required>
                            </div>
                            <div class="mb-3">
                                <label for="senha" class="form-label">Senha</label>
                                <input type="password" class="form-control" id="senha" name="senha" required>
                            </div>
                            <button type="submit" id="btnCadastrar" class="btn btn-success w-100 mt-2">Cadastrar</button>
                        </form>
                        
                        <div class="text-center mt-3">
                            <a href="login.jsp" class="text-decoration-none">Já possui conta? Faça o login</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#formCadastro').on('submit', function(e) {
                e.preventDefault();
                
                // Desabilita o botão para evitar múltiplos envios
                $('#btnCadastrar').prop('disabled', true).text('Processando...');
                $('#mensagemAlerta').addClass('d-none').removeClass('alert-success alert-danger');
                
                $.ajax({
                    url: '${pageContext.request.contextPath}/cadastro',
                    method: 'POST',
                    data: $(this).serialize(),
                    dataType: 'json',
                    success: function(resposta) {
                        if(resposta.status === 'sucesso') {
                            $('#mensagemAlerta').addClass('alert-success').removeClass('d-none').text(resposta.mensagem);
                            $('#formCadastro')[0].reset();
                            
                            // Redireciona para o login após 2 segundos
                            setTimeout(function() {
                                window.location.href = 'login.jsp';
                            }, 2000);
                        }
                    },
                    error: function(xhr) {
                        let res = JSON.parse(xhr.responseText);
                        $('#mensagemAlerta').addClass('alert-danger').removeClass('d-none').text(res.mensagem);
                        $('#btnCadastrar').prop('disabled', false).text('Cadastrar');
                    }
                });
            });
        });
    </script>
</body>
</html>