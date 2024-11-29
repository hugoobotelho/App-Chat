
/* ***************************************************************
* Autor............: Hugo Botelho Santana
* Matricula........: 202210485
* Inicio...........: 21/11/2024
* Ultima alteracao.: 28/11/2024
* Nome.............: Camada de Transporte/Aplicação - Aplicativo de Instant Messaging
* Funcao...........: Aplicativo de chat para troca de mensagens com o modelo cliente servidor
*************************************************************** */
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaMeusGrupos {
    private VBox layout = new VBox(20); // Layout principal da tela Meus Grupos
    private VBox containerAdicionarGrupo; // Container que será exibido para adicionar um grupo
    private VBox grupoContainer; // Container interno para os grupos
    // private static List<String> grupos = new ArrayList<>(); // Lista dinâmica de
    // grupos
    // private static Map<String, HistoricoMensagens> historicosMensagens = new
    // HashMap<>();
    private static List<String> grupos;
    private static Map<String, HistoricoMensagens> historicosMensagens;

    private static Map<String, TelaChat> telasChat = new HashMap<>();

    private Principal app; // Instância principal do aplicativo

    public TelaMeusGrupos(Principal app) {
        this.app = app;
        this.grupos = app.getGrupos();
        this.historicosMensagens = app.getHistoricosMensagens();

        layout.setStyle("-fx-padding: 20; -fx-alignment: top-left;");

        // Configuração do Título com Estilo Diferente para "Meus" e "Grupos"
        Text textoMeus = new Text("Meus ");
        textoMeus.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #333333;");

        Text textoGrupos = new Text("Grupos");
        textoGrupos.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #E5AF18;");

        HBox tituloHbox = new HBox(textoMeus, textoGrupos);
        tituloHbox.setAlignment(Pos.CENTER_LEFT); // Alinhamento à esquerda
        VBox.setMargin(tituloHbox, new Insets(0, 0, 10, 0)); // Espaçamento abaixo do título

        // Configuração da Lista de Grupos
        grupoContainer = new VBox(10); // Espaçamento entre os grupos
        grupoContainer.setPadding(new Insets(10)); // Margem interna
        grupoContainer.setStyle("-fx-background-color: transparent;");

        // Renderizando os grupos inicialmente
        renderizarGrupos(app);

        // Envolver o container dos grupos em um ScrollPane
        ScrollPane scrollPane = new ScrollPane(grupoContainer);
        scrollPane.setFitToWidth(true); // Ajusta a largura do conteúdo ao ScrollPane
        scrollPane.setMaxHeight(300); // Define a altura do ScrollPane
        scrollPane.setStyle("-fx-background: #F5F5F5; -fx-border-color: #F5F5F5;");

        // Configuração do Botão Adicionar Grupo
        Button botaoAdicionarGrupo = new Button("Adicionar Grupo");
        botaoAdicionarGrupo.setStyle(
                "-fx-background-color: #E5AF18; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-pref-width: 300px; -fx-padding: 10px; -fx-cursor: hand;");
        botaoAdicionarGrupo.setAlignment(Pos.CENTER);

        // Configuração do container para adicionar novo grupo
        containerAdicionarGrupo = new VBox(45);
        containerAdicionarGrupo.setPadding(new Insets(10));
        containerAdicionarGrupo
                .setStyle("-fx-background-color: #333333; -fx-padding: 20px; -fx-background-radius: 10px;");
        containerAdicionarGrupo.setVisible(false); // Inicialmente invisível
        containerAdicionarGrupo.setAlignment(Pos.CENTER);

        TextField inputNomeGrupo = criarCampoComPlaceholder("Digite o nome do grupo");

        Button botaoEntrar = new Button("Adicionar");
        botaoEntrar.setStyle(
                "-fx-background-color: #E5AF18; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-pref-width: 300px; -fx-padding: 10px; -fx-cursor: hand;");
        botaoEntrar.setOnAction(e -> {
            String nomeGrupo = inputNomeGrupo.getText().trim();
            if (!nomeGrupo.isEmpty()) {
                if (app.getGrupos().contains(nomeGrupo)) { // Verifica se o grupo já existe
                    // Exibe um alerta ou mensagem de erro
                    Label mensagemErro = new Label("O grupo ja existe!");
                    mensagemErro.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                    if (!containerAdicionarGrupo.getChildren().contains(mensagemErro)) {
                        containerAdicionarGrupo.getChildren().add(mensagemErro);
                    }
                } else {
                    grupos.add(nomeGrupo); // Adiciona o novo grupo à lista de grupos
                    historicosMensagens.put(nomeGrupo, new HistoricoMensagens()); // Associa um novo histórico ao grupo
                    renderizarGrupos(app); // Atualiza a interface
                    containerAdicionarGrupo.setVisible(false); // Esconde o container novamente
                    inputNomeGrupo.clear(); // Limpa o campo de texto

                    // Enviar a APDU de tipo "JOIN" para o servidor TCP
                    enviarAPDUJoin(nomeGrupo);

                    // Remove a mensagem de erro caso exista
                    containerAdicionarGrupo.getChildren().removeIf(
                            node -> node instanceof Label && "O grupo ja existe!".equals(((Label) node).getText()));
                }
            }
        });

        containerAdicionarGrupo.getChildren().addAll(inputNomeGrupo, botaoEntrar);

        // Ação para mostrar o container de adicionar grupo
        botaoAdicionarGrupo.setOnAction(e -> {
            containerAdicionarGrupo.setVisible(!containerAdicionarGrupo.isVisible());
        });

        VBox containerVbox = new VBox(20, scrollPane, botaoAdicionarGrupo, containerAdicionarGrupo);
        containerVbox.setAlignment(Pos.CENTER);

        // Adicionando elementos ao layout principal
        layout.getChildren().addAll(tituloHbox, containerVbox);
    }

    public VBox getLayout() {
        return layout;
    }

    /**
     * Método para renderizar (atualizar) a lista de grupos na interface.
     * 
     * @param app Instância principal do aplicativo.
     */
    public void renderizarGrupos(Principal app) {
        // Limpar os grupos existentes
        grupoContainer.getChildren().clear();

        // Adicionar os grupos da lista dinâmica
        for (String grupo : grupos) {
            Button grupoBotao = new Button(grupo);
            grupoBotao.setStyle(
                    "-fx-background-color: #D9D9D9; " +
                            "-fx-text-fill: #333333; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 16px; " +
                            "-fx-background-radius: 10px; " +
                            "-fx-pref-width: 500px; " +
                            "-fx-padding: 10px; " +
                            "-fx-cursor: hand; " +
                            "-fx-alignment: center-left;");
            grupoBotao.setOnAction(e -> {
                TelaChat telaChat;

                // Verifica se já existe uma instância de TelaChat para o grupo
                if (telasChat.containsKey(grupo)) {
                    telaChat = telasChat.get(grupo);
                } else {
                    // Cria uma nova instância de TelaChat e armazena no mapa
                    telaChat = new TelaChat(app, grupo, historicosMensagens.get(grupo));
                    telasChat.put(grupo, telaChat);
                }

                // Exibe a tela do chat
                app.getRoot().getChildren().setAll(telaChat.getLayout());
            });

            grupoContainer.getChildren().add(grupoBotao);
        }
    }

    public Map<String, TelaChat> getTelasChat() {
        return telasChat;
    }
    

    /**
     * Método para remover um grupo da lista e atualizar a interface.
     * 
     * @param grupo Nome do grupo a ser removido.
     */
    public static void removerGrupo(String grupo) {
        grupos.remove(grupo); // Remove o grupo da lista
        historicosMensagens.remove(grupo);
        telasChat.remove(grupo); // Remove a instância de TelaChat associada
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

    /**
     * Enviar a APDU de tipo "JOIN" para o servidor TCP.
     * 
     * @param nomeGrupo Nome do grupo a ser adicionado.
     */
    // Exemplo de envio de APDU com tratamento de exceção
    private void enviarAPDUJoin(String nomeGrupo) {
        String nomeUsuario = app.getNomeUsuario();

        try {
            // Envio via ClienteTCP - agora utilizando o ClienteTCP configurado
            app.getClienteTCP().enviarAPDUJoin(nomeUsuario, nomeGrupo);
        } catch (Exception e) {
            // Exibir mensagem de erro se falhar ao enviar a APDU
            Label mensagemErro = new Label("Erro ao conectar ao servidor. Tente novamente.");
            mensagemErro.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            containerAdicionarGrupo.getChildren().add(mensagemErro);
        }
    }

}
