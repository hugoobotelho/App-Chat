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

    public TelaChat(Principal app, String nomeGrupo, HistoricoMensagens historicoMensagens) {
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
        nomeGrupoTexto.setMaxWidth(400); // Definir largura máxima para o título (ajuste conforme necessário)
        nomeGrupoTexto.setWrapText(true); // Permite quebrar o texto se for muito longo

        // Garantir que o nome do grupo ocupe o máximo espaço disponível
        HBox.setHgrow(nomeGrupoTexto, Priority.ALWAYS); // Faz o nome do grupo crescer e ficar centralizado

        // icone de Sair do Grupo
        ImageView sairIcon = new ImageView(new Image("/img/sairIcon.png"));
        sairIcon.setFitHeight(40); // Definindo o tamanho do ícone
        sairIcon.setFitWidth(40);
        Button botaoSair = new Button();
        botaoSair.setGraphic(sairIcon);
        botaoSair.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        botaoSair.setOnAction(e -> {
            // Lógica para sair do grupo
            System.out.println("Saindo do grupo: " + nomeGrupo);

            // Remover o grupo da lista em TelaMeusGrupos
            TelaMeusGrupos.removerGrupo(nomeGrupo);

            // Voltar para a tela de grupos
            TelaMeusGrupos telaMeusGrupos = new TelaMeusGrupos(app);
            app.getRoot().getChildren().setAll(telaMeusGrupos.getLayout());
        });

        // Adicionando os elementos ao header
        header.getChildren().addAll(botaoVoltar, nomeGrupoTexto, botaoSair);
        // Criando a Lista de Mensagens
        VBox listaMensagens = new VBox(10);

        // Carregar as mensagens armazenadas
        for (Mensagem mensagem : historicoMensagens.getMensagens()) {
            listaMensagens.getChildren().add(criarComponenteMensagem(mensagem));
        }

        // Adicionar a lista de mensagens dentro de um ScrollPane
        ScrollPane scrollMensagens = new ScrollPane(listaMensagens);
        scrollMensagens.setStyle("-fx-background: #F5F5F5; -fx-border-color: #F5F5F5;");
        scrollMensagens.setFitToWidth(true); // Ajusta a largura do conteúdo ao ScrollPane
        scrollMensagens.setPrefHeight(600); // Define a altura do ScrollPane

        // Ajustar o estilo do VBox dentro do ScrollPane
        listaMensagens.setStyle("-fx-padding: 10px; -fx-background-color: transparent;");

        // Layout para enviar mensagem
        HBox enviarMensagemLayout = new HBox(10); // Espaçamento de 10px entre ícone e campo de texto
        enviarMensagemLayout.setStyle("-fx-background-color: #E1E0E0; -fx-background-radius: 10px;");
        enviarMensagemLayout.setPadding(new Insets(10, 20, 10, 20)); // Padding interno (top, right, bottom, left)
        enviarMensagemLayout.setAlignment(Pos.CENTER_LEFT); // Centraliza o conteúdo verticalmente

        //icone para o campo de mensagem
        ImageView iconeMensagem = new ImageView(new Image("/img/enviarIcon.png"));
        iconeMensagem.setFitHeight(24); // Tamanho do ícone
        iconeMensagem.setFitWidth(24);

        // Campo de texto para digitar a mensagem
        TextField campoMensagem = criarCampoComPlaceholder("Mensagem");
        campoMensagem.setMaxWidth(Double.MAX_VALUE); // Faz o campo ocupar o máximo de largura disponível
        campoMensagem.setStyle("-fx-background-color: #E1E0E0; -fx-font-size: 16px; -fx-padding: 8px;");

        // Faz com que o campo cresça proporcionalmente
        HBox.setHgrow(campoMensagem, Priority.ALWAYS);

        // Adiciona o evento para envio ao pressionar Enter
        campoMensagem.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                String mensagem = campoMensagem.getText();
                if (!mensagem.isEmpty()) {
                    // Adicionar a nova mensagem ao histórico
                    Mensagem novaMensagem = new Mensagem("Voce", mensagem, "07:00"); // Assumindo que o remetente é
                                                                                     // "Você"
                    historicoMensagens.adicionarMensagem(novaMensagem);

                    // Criar o componente de mensagem e adicionar à tela
                    listaMensagens.getChildren().add(criarComponenteMensagem(novaMensagem));

                    // Scroll automático para a última mensagem
                    scrollMensagens.setVvalue(1.0);

                    // Limpar o campo de mensagem após envio
                    campoMensagem.clear();
                }
            }
        });

        // // Adicionando os elementos ao layout
        // enviarMensagemLayout.getChildren().addAll(iconeMensagem, campoMensagem);

        // // Adicionando margem ao layout principal
        // VBox.setMargin(enviarMensagemLayout, new Insets(10, 10, 10, 10)); // Margem externa (top, right, bottom, left)

        // // Adicionar os elementos ao layout principal
        // layout.getChildren().addAll(header, scrollMensagens, enviarMensagemLayout);

        // Button botaoEnviar = new Button("Enviar");
        // botaoEnviar.setOnAction(e -> {
        // String mensagem = campoMensagem.getText();
        // if (!mensagem.isEmpty()) {
        // // Adicionar a nova mensagem ao histórico
        // Mensagem novaMensagem = new Mensagem("Voce", mensagem, "07:00"); // Assumindo
        // que o remetente é "Você"
        // historicoMensagens.adicionarMensagem(novaMensagem);

        // // Criar o componente de mensagem e adicionar à tela
        // listaMensagens.getChildren().add(criarComponenteMensagem(novaMensagem));

        // // Scroll automático para a última mensagem
        // scrollMensagens.setVvalue(1.0);

        // // Limpar o campo de mensagem após envio
        // campoMensagem.clear();
        // }
        // });

        VBox.setMargin(enviarMensagemLayout, new Insets(10, 10, 10, 10)); // Margem externa (top, right, bottom, left)

        enviarMensagemLayout.getChildren().addAll(iconeMensagem, campoMensagem);

        // Adicionar os elementos ao layout principal
        layout.getChildren().addAll(header, scrollMensagens, enviarMensagemLayout);

    }

    // Cria um componente de mensagem (semelhante a do WhatsApp)
    private VBox criarComponenteMensagem(Mensagem mensagem) {
        VBox componenteMensagem = new VBox(5); // Espaçamento entre os elementos

        // Nome do Remetente
        Label remetenteLabel = new Label(mensagem.getRemetente());
        remetenteLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #B4B4B4;");

        // Conteúdo da mensagem
        Label conteudoMensagem = new Label(mensagem.getConteudo());
        conteudoMensagem.setStyle(
                "-fx-background-color: #333333; " +
                        "-fx-text-fill: #E5AF18; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-max-width: 250px; " +
                        "-fx-wrap-text: true;" +
                        "-fx-margin: 22px;"); // Adiciona margem de 22px

        // Horário da mensagem
        Label horarioLabel = new Label(mensagem.getHora());
        horarioLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #B4B4B4;");

        // Alinhar a mensagem à direita se for do usuário (você)
        if (mensagem.getRemetente().equals("Voce")) {
            componenteMensagem.setStyle("-fx-alignment: top-right;");
            conteudoMensagem.setStyle(
                    "-fx-background-color: #E5AF18; " + // Cor de fundo diferente para o usuário
                            "-fx-text-fill: #333333; " +
                            "-fx-font-size: 16px; " +
                            "-fx-padding: 10px; " +
                            "-fx-background-radius: 10px; " +
                            "-fx-max-width: 250px; " +
                            "-fx-wrap-text: true; " +
                            "-fx-margin: 22px;");
            remetenteLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #B4B4B4;");
            horarioLabel.setStyle("-fx-font-weight: 500; -fx-text-fill: #B4B4B4;");
        } else {
            // Para outras mensagens, elas ficam à esquerda
            componenteMensagem.setStyle("-fx-alignment: top-left;");
        }

        // Adicionando o remetente, conteúdo e horário no VBox
        componenteMensagem.getChildren().addAll(remetenteLabel, conteudoMensagem, horarioLabel);

        return componenteMensagem;
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
        campo.setStyle("-fx-background-color: #E1E0E0; -fx-font-size: 16px");

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
