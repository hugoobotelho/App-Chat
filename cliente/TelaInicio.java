/* ***************************************************************
* Autor............: Hugo Botelho Santana
* Matricula........: 202210485
* Inicio...........: 21/11/2024
* Ultima alteracao.: 21/11/2024
* Nome.............: Camada de Transporte/Aplicação - Aplicativo de Instant Messaging
* Funcao...........: Aplicativo de chat para troca de mensagens com o modelo cliente servidor
*************************************************************** */
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TelaInicio {
    private VBox layout = new VBox(30); // Layout da tela inicial

    public TelaInicio(Principal app) {
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Configuração do Título
        Label labelTitulo = new Label("Seja Bem Vindo!");
        labelTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        labelTitulo.setAlignment(Pos.CENTER);

        // Configuração do Campo IP com Placeholder Persistente
        TextField campoIP = criarCampoComPlaceholder("Digite o IP do servidor");

        // Configuração do Campo Usuário com Placeholder Persistente
        TextField campoUsuario = criarCampoComPlaceholder("Digite seu nome de usuario");

        // Configuração do Botão Entrar
        Button botaoEntrar = new Button("Entrar");
        botaoEntrar.setStyle(
            "-fx-background-color: #E5AF18; " +
            "-fx-text-fill: #333333; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 16px; " +
            "-fx-background-radius: 10px; " +
            "-fx-pref-width: 300px; -fx-padding: 10px; -fx-cursor: hand;"
        );
        botaoEntrar.setOnAction(e -> {
            app.setNomeUsuario(campoUsuario.getText());
            TelaMeusGrupos telaMeusGrupos = new TelaMeusGrupos(app);
            app.getRoot().getChildren().setAll(telaMeusGrupos.getLayout());
        });

        VBox inputVBox = new VBox(10, campoIP, campoUsuario);

        // Adicionando elementos ao layout
        layout.getChildren().addAll(labelTitulo, inputVBox, botaoEntrar);
        layout.setAlignment(Pos.CENTER);
        inputVBox.setAlignment(Pos.CENTER);

        // Centraliza o VBox no root
        StackPane root = new StackPane();
        root.getChildren().add(layout);
        root.setAlignment(layout, Pos.CENTER);

        // Remover foco inicial dos inputs
        Platform.runLater(() -> layout.requestFocus());
    }

    /**
     * Cria um TextField com placeholder persistente (não some ao focar).
     *
     * @param placeholder Texto do placeholder.
     * @return TextField configurado.
     */
    private TextField criarCampoComPlaceholder(String placeholder) {
        TextField campo = new TextField();
        campo.setPromptText(placeholder);
        campo.setMaxWidth(300);
        campo.setStyle("-fx-background-color: #E1E0E0; -fx-font-size: 16px; -fx-padding: 10px;");

        // Listener para controlar o comportamento do placeholder
        campo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && campo.getText().isEmpty()) {
                campo.setPromptText(placeholder); // Reaplica o placeholder ao perder o foco
            } else if (newVal && campo.getText().isEmpty()) {
                campo.setPromptText(""); // Limpa o placeholder ao focar
            }
        });

        return campo;
    }
    

    public VBox getLayout() {
        return layout;
    }
}
