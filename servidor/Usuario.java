package servidor;

import java.net.InetAddress;
import java.util.Objects;

public class Usuario {
    private final String nome;
    private final InetAddress endereco;
    private final int porta;

    public Usuario(String nome, InetAddress endereco, int porta) {
        this.nome = nome;
        this.endereco = endereco;
        this.porta = porta;
    }

    public String getNome() {
        return nome;
    }

    public InetAddress getEndereco() {
        return endereco;
    }

    public int getPorta() {
        return porta;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return porta == usuario.porta && endereco.equals(usuario.endereco) && nome.equals(usuario.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, endereco, porta);
    }
}
