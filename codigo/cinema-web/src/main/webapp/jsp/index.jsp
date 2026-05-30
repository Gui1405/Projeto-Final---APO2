<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cine-IFSP - Programação</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <div class="card shadow-sm p-4">
            <h1 class="text-primary mb-4">Filmes em Cartaz</h1>
            
            <table class="table table-striped" id="tabelaFilmes">
                <thead>
                    <tr>
                        <th>Título</th>
                        <th>Gênero</th>
                        <th>Duração</th>
                        <th>Classificação</th>
                    </tr>
                </thead>
                <tbody>
                    </tbody>
            </table>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        // Bloco executado automaticamente assim que a página carrega
        $(document).ready(function() {
            $.ajax({
                url: '${pageContext.request.contextPath}/filmes',
                method: 'GET',
                dataType: 'json',
                success: function(dados) {
                    let linhas = '';
                    dados.forEach(function(filme) {
                        linhas += '<tr>' +
                                    '<td>' + filme.titulo + '</td>' +
                                    '<td>' + filme.genero + '</td>' +
                                    '<td>' + filme.duracao + ' min</td>' +
                                    '<td>' + filme.classificacao + '</td>' +
                                  '</tr>';
                    });
                    $('#tabelaFilmes tbody').html(linhas);
                },
                error: function() {
                    alert('Erro ao carregar a lista de filmes do servidor.');
                }
            });
        });
    </script>
</body>
</html>