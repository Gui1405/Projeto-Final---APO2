<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Bem-vindo ao Cinema APO2 - O melhor sistema de reservas de ingressos.">
    <title>Cinema APO2 - Início</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;800&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            font-family: 'Outfit', sans-serif;
            background-color: #0f172a;
            color: #f8fafc;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }
        /* Navbar com Glassmorphism */
        .navbar {
            background: rgba(15, 23, 42, 0.7);
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        .navbar-brand {
            font-weight: 800;
            letter-spacing: 1px;
            color: #38bdf8 !important;
        }
        .nav-link {
            color: #cbd5e1 !important;
            transition: color 0.3s;
        }
        .nav-link:hover {
            color: #38bdf8 !important;
        }
        /* Hero Section */
        .hero {
            position: relative;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(to bottom, rgba(15,23,42,0.3), #0f172a), url('https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=2070&auto=format&fit=crop') center/cover;
            text-align: center;
            animation: fadeIn 1.5s ease-out;
        }
        .hero h1 {
            font-size: 4rem;
            font-weight: 800;
            background: linear-gradient(90deg, #38bdf8, #818cf8);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 20px;
        }
        .hero p {
            font-size: 1.25rem;
            color: #cbd5e1;
            margin-bottom: 40px;
        }
        /* Botoes customizados */
        .btn-custom {
            background: linear-gradient(90deg, #0284c7, #4f46e5);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 50px;
            font-weight: 600;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .btn-custom:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 20px rgba(79, 70, 229, 0.4);
            color: white;
        }
        .btn-outline-custom {
            background: transparent;
            color: #38bdf8;
            border: 2px solid #38bdf8;
            padding: 10px 28px;
            border-radius: 50px;
            font-weight: 600;
            transition: all 0.3s;
        }
        .btn-outline-custom:hover {
            background: #38bdf8;
            color: #0f172a;
        }
        /* Animacoes */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
        <div class="container">
            <a class="navbar-brand" href="index.jsp">CINEMA APO2</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                <ul class="navbar-nav align-items-center">
                    <li class="nav-item"><a class="nav-link" href="#filmes">Filmes em Cartaz</a></li>
                    <li class="nav-item"><a class="nav-link" href="#precos">Preços</a></li>
                    <li class="nav-item ms-lg-3"><a class="btn btn-outline-custom btn-sm px-4" href="login.jsp">Entrar</a></li>
                    <li class="nav-item ms-lg-2"><a class="btn btn-custom btn-sm px-4" href="cadastro.jsp">Cadastre-se</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero">
        <div class="container">
            <h1>A Magia do Cinema na Sua Mão</h1>
            <p>Reserve seus ingressos para os melhores lançamentos com rapidez, conforto e segurança.</p>
            <div>
                <a href="login.jsp" class="btn btn-custom mx-2">Fazer Login</a>
                <a href="cadastro.jsp" class="btn btn-outline-custom mx-2">Criar Conta</a>
            </div>
        </div>
    </section>

    <!-- Filmes Section -->
    <section id="filmes" class="py-5" style="background-color: #0f172a;">
        <div class="container py-5">
            <h2 class="text-center mb-5 fw-bold" style="color: #38bdf8;">Filmes em Cartaz</h2>
            <div class="row" id="filmesGrid">
                <!-- Carregado via AJAX -->
            </div>
        </div>
    </section>

    <!-- Preços Section -->
    <section id="precos" class="py-5" style="background-color: #1e293b;">
        <div class="container py-5">
            <h2 class="text-center mb-5 fw-bold" style="color: #38bdf8;">Tabela de Preços e Ingressos</h2>
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card p-4 rounded-4 shadow-lg" style="background: rgba(15, 23, 42, 0.6) !important; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.1);">
                        <table class="table table-borderless text-light mb-0" style="background: transparent;">
                            <thead>
                                <tr class="border-bottom border-secondary" style="background: transparent;">
                                    <th style="background: transparent; color: #f8fafc;">Tipo de Sessão / Dia</th>
                                    <th class="text-end" style="background: transparent; color: #f8fafc;">Valor Médio</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr style="background: transparent;">
                                    <td style="background: transparent; color: #f8fafc;"><strong>Sessões Standard (2D)</strong> - Segunda a Quinta</td>
                                    <td class="text-end text-success fw-bold" style="background: transparent;">R$ 25,00</td>
                                </tr>
                                <tr style="background: transparent;">
                                    <td style="background: transparent; color: #f8fafc;"><strong>Sessões Standard (2D)</strong> - Sexta, Sábado, Domingo e Feriados</td>
                                    <td class="text-end text-success fw-bold" style="background: transparent;">R$ 30,00</td>
                                </tr>
                                <tr style="background: transparent;">
                                    <td style="background: transparent; color: #f8fafc;"><strong>Sessões VIP / IMAX</strong> - Todos os dias</td>
                                    <td class="text-end text-success fw-bold" style="background: transparent;">R$ 45,00 a R$ 60,00</td>
                                </tr>
                                <tr class="border-top border-secondary" style="background: transparent;">
                                    <td colspan="2" class="text-light small pt-3" style="background: transparent; opacity: 0.85;">
                                        * Meia-entrada disponível para estudantes, idosos e professores mediante apresentação de comprovante na entrada do cinema. Os valores exatos podem variar conforme a sessão escolhida.
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function(){
            $.ajax({
                url: 'api/filmes',
                type: 'GET',
                dataType: 'json',
                success: function(data){
                    var grid = $('#filmesGrid');
                    if(data.length === 0) {
                        grid.html('<p class="text-center text-light fs-5">Nenhum filme com sessão programada para hoje.</p>');
                        return;
                    }
                    $.each(data, function(i, f){
                        grid.append(
                            '<div class="col-md-4 mb-4">'+
                                '<div class="card p-4 h-100 text-light shadow-lg" style="background: rgba(30, 41, 59, 0.7); border: 1px solid rgba(255,255,255,0.1); border-radius: 15px; transition: transform 0.3s;">'+
                                    '<h4 class="text-info fw-bold">'+ f.nome +'</h4>'+
                                    '<p class="small text-muted mb-2">'+ f.genero +' | '+ f.duracao +' min | <span class="badge bg-secondary">'+ f.classificacao +'</span></p>'+
                                    '<p class="flex-grow-1">'+ f.sinopse +'</p>'+
                                    '<a href="login.jsp" class="btn btn-custom btn-sm mt-3 w-100">Comprar Ingresso</a>'+
                                '</div>'+
                            '</div>'
                        );
                    });
                }
            });
        });
    </script>
</body>
</html>
