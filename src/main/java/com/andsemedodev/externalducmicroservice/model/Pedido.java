//package com.andsemedodev.externalducmicroservice.model;
//
//public interface IProduto {
//    String getNome();
//    double getPrecoBase();
//    double calcularPrecoFinal();
//    void aplicarDesconto(double percentual);
//}
//
//public abstract class ProdutoBase implements IProduto {
//    private String nome;
//    private double precoBase;
//    private double desconto;
//
//    public ProdutoBase(String nome, double precoBase) {
//        this.nome = nome;
//        this.precoBase = precoBase;
//        this.desconto = 0;
//    }
//
//    public String getNome() { return nome; }
//
//    public double getPrecoBase() { return precoBase; }
//
//    public double getDesconto() { return desconto; }
//
//    public void aplicarDesconto(double percentual) { this.desconto = percentual; }
//
//    public abstract double calcularPrecoFinal();
//}
//
//public class ProdutoEletronico extends ProdutoBase {
//    public ProdutoEletronico(String nome, double precoBase) {
//        super(nome, precoBase);
//    }
//
//    @Override
//    public double calcularPrecoFinal() {
//        double precoComDesconto = getPrecoBase() * (1 - getDesconto() / 100);
//        return precoComDesconto * 0.9;
//    }
//}
//
//public class ProdutoAlimentar extends ProdutoBase {
//    private LocalDate dataValidade;
//
//    public ProdutoAlimentar(String nome, double precoBase, LocalDate dataValidade) {
//        super(nome, precoBase);
//        this.dataValidade = dataValidade;
//    }
//
//    public LocalDate getDataValidade() { return dataValidade; }
//
//    public boolean isVencido() {
//        return LocalDate.now().isAfter(dataValidade);
//    }
//
//    @Override
//    public double calcularPrecoFinal() {
//        if (isVencido()) {
//            return 0;
//        }
//        return getPrecoBase() * (1 - getDesconto() / 100);
//    }
//}
//
//public class Estoque {
//    private List<Produto> produtos;
//
//    public Estoque() { this.produtos = new ArrayList<>(); }
//
//    public void adicionarProduto(Produto produto) { produtos.add(produto); }
//
//    public void removerProduto(String nome) { produtos.removeIf(p -> p.getNome().equals(nome)); }
//
//    public void atualizarProduto(String nome, double novoPreco) {
//        for (Produto p : produtos) {
//            if (p.getNome().equals(nome)) {
//                ((ProdutoBase) p).aplicarDesconto(0);
//                break;
//            }
//        }
//    }
//
//    public void listarProdutos() {
//        for (Produto p : produtos) {
//            System.out.println(p.getNome() + " - Preço base: " + p.getPrecoBase() +
//                    " - Preço final: " + p.calcularPrecoFinal());
//        }
//    }
//}
//
//
//public class Main {
//    public static void main(String[] args) {
//        Estoque estoque = new Estoque();
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            System.out.println("\nMenu:");
//            System.out.println("1. Adicionar produto");
//            System.out.println("2. Remover produto");
//            System.out.println("3. Atualizar produto");
//            System.out.println("4. Listar produtos");
//            System.out.println("5. Aplicar desconto em produto");
//            System.out.println("6. Sair");
//            System.out.print("Escolha uma opção: ");
//
//            int opcao = scanner.nextInt();
//            scanner.nextLine();
//
//            switch (opcao) {
//                case 1:
//                    System.out.print("Digite o tipo do produto (eletronico/alimentar): ");
//                    String tipo = scanner.nextLine();
//                    System.out.print("Digite o nome do produto: ");
//                    String nome = scanner.nextLine();
//                    System.out.print("Digite o preço base do produto: ");
//                    double precoBase = scanner.nextDouble();
//                    scanner.nextLine();
//
//                    if (tipo.equals("eletronico")) {
//                        estoque.adicionarProduto(new ProdutoEletronico(nome, precoBase));
//                    } else if (tipo.equals("alimentar")) {
//                        System.out.print("Digite a data de validade (ano-mes-dia): ");
//                        String data = scanner.nextLine();
//                        LocalDate dataValidade = LocalDate.parse(data);
//                        estoque.adicionarProduto(new ProdutoAlimentar(nome, precoBase, dataValidade));
//                    } else {
//                        System.out.println("Tipo de produto inválido.");
//                    }
//                    break;
//
//                case 2:
//                    System.out.print("Digite o nome do produto a ser removido: ");
//                    String nomeRemover = scanner.nextLine();
//                    estoque.removerProduto(nomeRemover);
//                    break;
//
//                case 3:
//                    System.out.print("Digite o nome do produto a ser atualizado: ");
//                    String nomeAtualizar = scanner.nextLine();
//                    System.out.print("Digite o novo preço do produto: ");
//                    double novoPreco = scanner.nextDouble();
//                    scanner.nextLine();
//                    estoque.atualizarProduto(nomeAtualizar, novoPreco);
//                    break;
//
//                case 4:
//                    estoque.listarProdutos();
//                    break;
//
//                case 5:
//                    System.out.print("Digite o nome do produto para aplicar desconto: ");
//                    String nomeDesconto = scanner.nextLine();
//                    System.out.print("Digite o percentual de desconto: ");
//                    double percentualDesconto = scanner.nextDouble();
//                    scanner.nextLine();
//
//                    for (Produto p : estoque.produtos) {
//                        if (p.getNome().equals(nomeDesconto)) {
//                            p.aplicarDesconto(percentualDesconto);
//                            break;
//                        }
//                    }
//                    break;
//
//                case 6:
//                    System.out.println("Saindo...");
//                    scanner.close();
//                    return;
//
//                default:
//                    System.out.println("Opção inválida. Tente novamente.");
//                    break;
//            }
//        }
//    }
//}