<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Cine-IFSP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center" style="height: 100vh;">

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-4">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h3 class="text-center mb-4">Acesso ao Sistema</h3>
                        
                        <div id="mensagemErro" class="alert alert-danger d-none" role="alert"></div>
                        
                        <form id="formLogin">
                            <div class="mb-3">
                                <label for="email" class="form-label">E-mail</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                            <div class="mb-3">
                                <label for="senha" class="form-label">Senha</label>
                                <input type="password" class="form-control" id="senha" name="senha" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">Entrar</button>
                        </form>
                        
                        <div class="text-center mt-3">
                            <a href="cadastro.jsp" class="text-decoration-none">Ainda não tem conta? Cadastre-se</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#formLogin').on('submit', function(e) {
                e.preventDefault(); // Impede o recarregamento da página
                
                $.ajax({
                    url: '${pageContext.request.contextPath}/login',
                    method: 'POST',
                    data: $(this).serialize(), // Pega os dados do form e envia
                    dataType: 'json',
                    success: function(resposta) {
                        if(resposta.status === 'sucesso') {
                            if(resposta.perfil === 'ADMIN') {
                                window.location.href = 'admin/painel-admin.jsp';
                            } else {
                                window.location.href = 'painel.jsp'; // Redireciona o cliente para o novo painel
                            }
                        }
                    },
                    error: function(xhr) {
                        let res = JSON.parse(xhr.responseText);
                        $('#mensagemErro').removeClass('d-none').text(res.mensagem);
                    }
                });
            });
        });
    </script>
</body>
</html>