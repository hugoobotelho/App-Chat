import java.util.*;

public class GrupoManager {
    // Estrutura para armazenar grupos e seus membros
    private final Map<String, Set<Usuario>> grupos;

    public GrupoManager() {
        this.grupos = new HashMap<>();
    }

    // Adicionar usuário a um grupo
    public synchronized void adicionarUsuario(String nomeGrupo, Usuario usuario) {
        grupos.computeIfAbsent(nomeGrupo, k -> new HashSet<>()).add(usuario);
    }

    // Remover usuário de um grupo
    public synchronized void removerUsuario(String nomeGrupo, Usuario usuario) {
        if (grupos.containsKey(nomeGrupo)) {
            grupos.get(nomeGrupo).remove(usuario);
            // Remove o grupo se ele estiver vazio
            if (grupos.get(nomeGrupo).isEmpty()) {
                grupos.remove(nomeGrupo);
            }
        }
    }

    // Obter membros de um grupo
    public synchronized Set<Usuario> obterMembros(String nomeGrupo) {
        return grupos.getOrDefault(nomeGrupo, Collections.emptySet());
    }

    // Verificar se um grupo existe
    public synchronized boolean grupoExiste(String nomeGrupo) {
        return grupos.containsKey(nomeGrupo);
    }
}
