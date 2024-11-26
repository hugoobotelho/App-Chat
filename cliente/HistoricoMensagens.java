import java.util.ArrayList;
import java.util.List;

public class HistoricoMensagens {
    private List<Mensagem> mensagens;

    public HistoricoMensagens() {
        mensagens = new ArrayList<>();
    }

    // Adiciona uma nova mensagem ao histórico
    public void adicionarMensagem(Mensagem mensagem) {
        mensagens.add(mensagem);
    }

    // Retorna todas as mensagens do histórico
    public List<Mensagem> getMensagens() {
        return mensagens;
    }
}
