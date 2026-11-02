package com.example.tcc_alistamento.service.integracao;

import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Envia o e-mail para a pessoa

@Service
public class EmailService {
    // O "carteiro" do Spring, cria ele com as configurações do application.properties
    private final JavaMailSender mailSender;
    // A conta que envia (a mesma do spring.mail.username)
    private final String remetente;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String remetente) {
        this.mailSender = mailSender;
        this.remetente = remetente;
    }

    // Envia o código de recuperação de senha
    public void enviarCodigoSenha(String para, String codigo, int minutos) {

        // Monta o e-mail
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom("Alistamento Militar <" + remetente + ">");
        mensagem.setTo(para);
        mensagem.setSubject("Recuperação de senha - Alistamento Militar");
        mensagem.setText(
                "Seu código para redefinir a senha é: " + codigo + "\n\n"
                        + "Ele vale por " + minutos + " minutos e só pode ser usado uma vez.\n"
                        + "Se você não pediu a troca de senha, ignore este e-mail.");
        try {
            // Envia
            mailSender.send(mensagem);
        } catch (MailException e) {
            // Servidor de e-mail fora do ar ou configuração errada: 503
            throw new RegraDeNegocioException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível enviar o e-mail agora. Tente novamente mais tarde.");
        }
    }
}
