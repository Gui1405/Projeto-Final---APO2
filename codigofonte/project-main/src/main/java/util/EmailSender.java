package util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    // Configurações do Mailtrap (substituir depois com credenciais reais)
    private static final String SMTP_HOST = "sandbox.smtp.mailtrap.io";
    private static final String SMTP_PORT = "2525";
    private static final String SMTP_USER = "INSIRA_SEU_USER_AQUI";
    private static final String SMTP_PASS = "INSIRA_SUA_SENHA_AQUI";

    public static boolean enviarEmailValidacao(String destinatario, String nome) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
          });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@cinema-apo2.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Validação de Cadastro - Cinema APO2");
            message.setText("Olá " + nome + ",\n\nSeu cadastro foi realizado com sucesso!\nPara validar sua conta, clique no link abaixo:\n\nhttp://localhost:8080/project-main/validar?email=" + destinatario);

            Transport.send(message);
            System.out.println("E-mail enviado com sucesso via Mailtrap para " + destinatario);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean enviarEmailRecuperacao(String destinatario) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
          });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@cinema-apo2.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Recuperação de Senha - Cinema APO2");
            message.setText("Olá,\n\nVocê solicitou a recuperação de senha.\nSua nova senha temporária é: 123456\n\nAcesse o sistema e atualize sua senha na Área Privada.");

            Transport.send(message);
            System.out.println("E-mail de recuperação enviado com sucesso via Mailtrap para " + destinatario);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
