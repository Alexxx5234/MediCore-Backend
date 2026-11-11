package com.centromedico.api.servicio;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigoVerificacion(String destinoEmail, String codigo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinoEmail);
            helper.setSubject("Código de Verificación - Centro Médico");

            
            String contenidoHtml = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body {
                            font-family: Arial, Helvetica, sans-serif;
                            background-color: #f0f0f0;
                            margin: 0;
                            padding: 30px 10px;
                        }
                        .card {
                            max-width: 500px;
                            margin: 0 auto;
                            background-color: #ffffff;
                            border-radius: 12px;
                            overflow: hidden;
                            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
                        }
                        .header {
                            background-color: #1e50d8;
                            color: #ffffff;
                            text-align: center;
                            padding: 35px 20px;
                        }
                        .header-title {
                            font-size: 24px;
                            font-weight: bold;
                            margin: 0;
                            display: inline-flex;
                            align-items: center;
                            justify-content: center;
                            gap: 8px;
                        }
                        .header-subtitle {
                            font-size: 13px;
                            margin-top: 8px;
                            color: #e0e8ff;
                            font-weight: normal;
                        }
                        .body {
                            padding: 30px 25px;
                            text-align: center;
                            color: #333333;
                        }
                        .h1-title {
                            font-size: 20px;
                            font-weight: bold;
                            color: #111111;
                            margin-top: 0;
                            margin-bottom: 12px;
                        }
                        .welcome-text {
                            font-size: 14px;
                            color: #555555;
                            margin-bottom: 10px;
                        }
                        .instruction-text {
                            font-size: 14px;
                            color: #555555;
                            margin-bottom: 25px;
                        }
                        .code-box {
                            background-color: #eeeeee;
                            border-radius: 12px;
                            padding: 20px;
                            margin: 0 auto 25px auto;
                            max-width: 85%%;
                        }
                        .code {
                            font-size: 32px;
                            font-weight: bold;
                            color: #1e50d8;
                            letter-spacing: 12px;
                            padding-left: 12px;
                        }
                        .btn-container {
                            margin-top: 25px;
                            margin-bottom: 10px;
                        }
                        .btn {
                            background-color: #1e50d8;
                            color: #ffffff !important;
                            text-decoration: none;
                            padding: 12px 28px;
                            font-size: 13px;
                            font-weight: bold;
                            border-radius: 6px;
                            display: inline-block;
                            letter-spacing: 0.5px;
                        }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <!-- Banner Superior Azul -->
                        <div class="header">
                            <div class="header-title">
                                🏥 Centro Médico
                            </div>
                            <div class="header-subtitle">
                                Sistema de Gestión
                            </div>
                        </div>

                        <!-- Cuerpo del Correo -->
                        <div class="body">
                            <div class="h1-title">Verificación de cuenta</div>
                            
                            <div class="welcome-text">Bienvenido al sistema del centro médico.</div>
                            <div class="instruction-text">Gracias por registrarte. Para activar tu cuenta utiliza el siguiente código:</div>

                            <!-- Caja Gris con Código Azul -->
                            <div class="code-box">
                                <span class="code">%s</span>
                            </div>

                            <div class="instruction-text">Ingresa este código en la aplicación para completar la verificación.</div>

                            <!-- Botón Azul -->
                            <div class="btn-container">
                                <a href="#" class="btn">VERIFICAR CUENTA</a>
                            </div>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(codigo);

            helper.setText(contenidoHtml, true);
            mailSender.send(mensaje);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de verificación: " + e.getMessage(), e);
        }
    }
}