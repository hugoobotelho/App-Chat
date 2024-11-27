import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;

public class TelaChat {
    private VBox layout = new VBox(10); // Layout da tela Chat
    private static HistoricoMensagens historicoMensagens;
    private Principal app; // Instância principal do aplicativo


    public TelaChat(Principal app, String nomeGrupo, HistoricoMensagens historicoMensagens) {
        this.app = app;
        // Remover padding do layout principal
        layout.setStyle("-fx-alignment: top-center;");

        // Criando o Header
        HBox header = new HBox(); // Sem espaçamento entre os elementos
        header.setStyle("-fx-background-color: #333333; -fx-padding: 24px; -fx-alignment: center;");

        // icone de Voltar
        ImageView voltarIcon = new ImageView(new Image("/img/voltarIcon.png"));
        voltarIcon.setFitHeight(40); // Definindo o tamanho do ícone
        voltarIcon.setFitWidth(40);
        Button botaoVoltar = new Button();
        botaoVoltar.setGraphic(voltarIcon);
        botaoVoltar.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        botaoVoltar.setOnAction(e -> {
            TelaMeusGrupos telaMeusGrupos = new TelaMeusGrupos(app);
            app.getRoot().getChildren().setAll(telaMeusGrupos.getLayout());
        });

        // Nome do Grupo (Centralizado e Tamanho fixo)
        Label nomeGrupoTexto = new Label(nomeGrupo);
        nomeGrupoTexto.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #E5AF18;");
        nomeGrupoTexto.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        nomeGrupoTexto.setMaxWidth(400); // Define largura máxima
        nomeGrupoTexto.setWrapText(true); // Permite quebra de texto

        HBox.setHgrow(nomeGrupoTexto, Priority.ALWAYS);

        // icone de Sair do Grupo
        ImageView sairIcon = new ImageView(new Image("/img/sairIcon.png"));
        sairIcon.setFitHeight(40); // Definindo o tamanho do ícone
        sairIcon.setFitWidth(40);
        Button botaoSair = new Button();
        botaoSair.setGraphic(sairIcon);
        botaoSair.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        botaoSair.setOnAction(e -> {
            enviarAPDULeave(nomeGrupo);
            TelaMeusGrupos.removerGrupo(nomeGrupo);
            TelaMeusGrupos telaMeusGrupos = new TelaMeusGrupos(app);
            app.getRoot().getChildren().setAll(telaMeusGrupos.getLayout());
        });

        header.getChildren().addAll(botaoVoltar, nomeGrupoTexto, botaoSair);

        // Criando a Lista de Mensagens
        VBox listaMensagens = new VBox(10);
        listaMensagens.setStyle("-fx-padding: 10px; -fx-background-color: transparent;");
        for (Mensagem mensagem : historicoMensagens.getMensagens()) {
            listaMensagens.getChildren().add(criarComponenteMensagem(mensagem));
        }

        ScrollPane scrollMensagens = new ScrollPane(listaMensagens);
        scrollMensagens.setStyle("-fx-background: #F5F5F5; -fx-border-color: #F5F5F5;");
        scrollMensagens.setFitToWidth(true);
        scrollMensagens.setPrefHeight(600);

        HBox enviarMensagemLayout = new HBox(10);
        enviarMensagemLayout.setStyle("-fx-background-color: #E1E0E0; -fx-background-radius: 10px;");
        enviarMensagemLayout.setPadding(new Insets(10, 20, 10, 20));
        // Adiciona margens externas (espaço entre o HBox e outros elementos)
        VBox.setMargin(enviarMensagemLayout, new Insets(10, 10, 10, 10));
        enviarMensagemLayout.setAlignment(Pos.CENTER_LEFT);

        // icone do campo de mensagem
        ImageView iconeMensagem = new ImageView(new Image("/img/enviarIcon.png"));
        iconeMensagem.setFitHeight(24);
        iconeMensagem.setFitWidth(24);

        // Campo de texto para digitar a mensagem
        TextField campoMensagem = criarCampoComPlaceholder("Mensagem");
        campoMensagem.setMaxWidth(Double.MAX_VALUE);
        campoMensagem.setStyle("-fx-background-color: #E1E0E0; -fx-font-size: 16px; -fx-padding: 8px;");
        HBox.setHgrow(campoMensagem, Priority.ALWAYS);

        campoMensagem.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                String mensagem = campoMensagem.getText();
                if (!mensagem.isEmpty()) {
                    try {
                        // Enviar a mensagem para o servidor UDP
                        // ClienteUDP clienteUDP = new ClienteUDP(app.getIpServidor(), 6789);
                        String mensagemFormatada = "SEND|"+ nomeGrupo + "|" + app.getNomeUsuario() + "|" + mensagem;
                        app.getClienteUDP().enviarMensagem(mensagemFormatada);

                        // Adicionar a nova mensagem à lista
                        Mensagem novaMensagem = new Mensagem("Voce", mensagem, "07:00");
                        historicoMensagens.adicionarMensagem(novaMensagem);
                        listaMensagens.getChildren().add(criarComponenteMensagem(novaMensagem));
                        scrollMensagens.setVvalue(1.0);
                        campoMensagem.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Erro ao enviar mensagem para o servidor: " + e.getMessage());
                    }
                }
            }
        });

        enviarMensagemLayout.getChildren().addAll(iconeMensagem, campoMensagem);
        layout.getChildren().addAll(header, scrollMensagens, enviarMensagemLayout);
    }

    private VBox criarComponenteMensagem(Mensagem mensagem) {
        VBox componenteMensagem = new VBox(5);

        Label remetenteLabel = new Label(mensagem.getRemetente());
        remetenteLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #B4B4B4;");

        Label conteudoMensagem = new Label(mensagem.getConteudo());
        conteudoMensagem.setStyle(
                "-fx-background-color: #333333; " +
                        "-fx-text-fill: #E5AF18; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-max-width: 250px; " +
                        "-fx-wrap-text: true;");

        Label horarioLabel = new Label(mensagem.getHora());
        horarioLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #B4B4B4;");

        if (mensagem.getRemetente().equals("Voce")) {
            componenteMensagem.setStyle("-fx-alignment: top-right;");
            conteudoMensagem.setStyle(
                    "-fx-background-color: #E5AF18; " +
                            "-fx-text-fill: #333333; " +
                            "-fx-font-size: 16px; " +
                            "-fx-padding: 10px; " +
                            "-fx-background-radius: 10px; " +
                            "-fx-max-width: 250px; " +
                            "-fx-wrap-text: true;");
        } else {
            componenteMensagem.setStyle("-fx-alignment: top-left;");
        }

        componenteMensagem.getChildren().addAll(remetenteLabel, conteudoMensagem, horarioLabel);
        return componenteMensagem;
    }

        /**
     * Enviar a APDU de tipo "JOIN" para o servidor TCP.
     * 
     * @param nomeGrupo Nome do grupo a ser adicionado.
     */
    // Exemplo de envio de APDU com tratamento de exceção
    private void enviarAPDULeave(String nomeGrupo) {
        String nomeUsuario = app.getNomeUsuario();

        try {
            // Envio via ClienteTCP - agora utilizando o ClienteTCP configurado
            app.getClienteTCP().enviarAPDULeave(nomeUsuario, nomeGrupo);
        } catch (Exception e) {
            // Exibir mensagem de erro se falhar ao enviar a APDU
            // Label mensagemErro = new Label("Erro ao conectar ao servidor. Tente novamente.");
            // mensagemErro.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            // containerAdicionarGrupo.getChildren().add(mensagemErro);
        }
    }
    

    private TextField criarCampoComPlaceholder(String placeholder) {
        TextField campo = new TextField();
        campo.setPromptText(placeholder);
        campo.setMaxWidth(300);
        campo.setStyle("-fx-background-color: #E1E0E0; -fx-font-size: 16px");

        campo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && campo.getText().isEmpty()) {
                campo.setPromptText(placeholder);
            } else if (newVal && campo.getText().isEmpty()) {
                campo.setPromptText("");
            }
        });

        return campo;
    }

    public VBox getLayout() {
        return layout;
    }
}
