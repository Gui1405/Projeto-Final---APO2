package util;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EmailSender {

    private static final String TOKEN = "aadb5e4d113cdd2345a8c5f0f9c5a133";
    private static final String ENDPOINT = "https://sandbox.api.mailtrap.io/api/send/4722708";

    // Utilitário para enviar requisições HTTP POST formatadas em JSON nativamente pelo HttpClient do Java 11+
    private static boolean sendPost(String jsonBody) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            // Montagem da requisição com os cabeçalhos obrigatórios de Autorização (Bearer Token do Mailtrap) e tipo de conteúdo
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Authorization", "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("E-mail enviado com sucesso via API Nativa.");
                return true;
            } else {
                System.out.println("Erro Mailtrap API: HTTP " + response.statusCode() + " - " + response.body());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Erro interno ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método de negócio que formata o Payload JSON com a estrutura exigida pela API do Mailtrap (Boas-vindas/Validação)
    public static boolean enviarEmailValidacao(String destinatario, String nome) {
        String texto = "Olá " + nome + ",\\n\\nSeu cadastro foi realizado com sucesso!\\nPara validar sua conta acesse:\\nhttp://localhost:8080/project-main/validar?email=" + destinatario;
        
        String json = "{"
                + "\"from\": {\"email\": \"no-reply@cinema-apo2.com\", \"name\": \"Cinema APO2\"},"
                + "\"to\": [{\"email\": \"" + destinatario + "\"}],"
                + "\"subject\": \"Validação de Cadastro - Cinema APO2\","
                + "\"text\": \"" + texto + "\","
                + "\"category\": \"Validation\""
                + "}";
                
        return sendPost(json);
    }
    
    public static boolean enviarEmailRecuperacao(String destinatario) {
        String texto = "Olá,\\n\\nSua nova senha temporária é: 123456\\nAcesse o sistema e atualize sua senha.";
        
        String json = "{"
                + "\"from\": {\"email\": \"no-reply@cinema-apo2.com\", \"name\": \"Cinema APO2\"},"
                + "\"to\": [{\"email\": \"" + destinatario + "\"}],"
                + "\"subject\": \"Recuperação de Senha - Cinema APO2\","
                + "\"text\": \"" + texto + "\","
                + "\"category\": \"Recovery\""
                + "}";
                
        return sendPost(json);
    }
}
