package br.com.starlog;

import br.com.starlog.dao.ProdutoDAO;
import br.com.starlog.dao.ProdutoDAOSqlite;
import br.com.starlog.model.Produto;

import java.util.List;

public class Main {

    private static void imprimirTabela(List<Produto> produtos) {
        System.out.printf("%-4s | %-22s | %10s | %7s%n", "ID", "NOME", "PREÇO", "ESTOQUE");
        System.out.println("-----+------------------------+------------+--------");
        for (Produto p : produtos) {
            System.out.printf("%-4d | %-22s | R$ %7.2f | %7d%n",
                    p.getId(), p.getNome(), p.getPreco(), p.getEstoque());
        }
    }

    public static void main(String[] args) {
        // DIP: o Main depende da interface, não do SQLite
        ProdutoDAO dao = new ProdutoDAOSqlite();

        System.out.println("=== [CREATE] Cadastrando 2 produtos ===");
        Produto teclado = new Produto("Teclado Mecânico RGB", 249.90, 10);
        Produto mousepad = new Produto("Mousepad Speed XL", 59.90, 30);
        dao.salvar(teclado);
        dao.salvar(mousepad);
        System.out.println("ID do teclado: " + teclado.getId());
        System.out.println("ID do mousepad: " + mousepad.getId());

        System.out.println("\n=== [READ] Listando todos os produtos ===");
        imprimirTabela(dao.listarTodos());

        System.out.println("\n=== [UPDATE] Teclado: estoque 15 e preço 289.90 ===");
        teclado.setEstoque(15);
        teclado.setPreco(289.90);
        boolean atualizou = dao.atualizar(teclado);
        System.out.println("Atualizou? " + atualizou);
        dao.buscarPorId(teclado.getId()).ifPresent(p -> System.out.println("Registro atual: " + p));

        System.out.println("\n=== [DELETE] Removendo o Mousepad ===");
        boolean removeu = dao.excluir(mousepad.getId());
        System.out.println("Mousepad removido? " + removeu);

        System.out.println("\n=== [DELETE] Tentando excluir ID inexistente (888) ===");
        boolean removeuInexistente = dao.excluir(888L);
        if (removeuInexistente) {
            System.out.println("Produto 888 removido.");
        } else {
            System.out.println("Aviso: nenhum produto com ID 888 foi encontrado (retorno false).");
        }

        System.out.println("\n=== [FINAL STATE] Estado final do banco ===");
        List<Produto> finais = dao.listarTodos();
        imprimirTabela(finais);
        System.out.println("Total de produtos restantes: " + finais.size());
    }
}
