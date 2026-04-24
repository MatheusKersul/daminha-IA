# Jogo de Damas com IA

Projeto de um jogo de damas em que o usuário compete contra um algoritmo de inteligência artificial. O sistema utiliza a linguagem **Java** para a lógica e a interface gráfica, além de disponibilizar um menu para acompanhar os resultados das jogadas da IA. O núcleo da tomada de decisão é baseado no algoritmo **MinMax**.

---

## Técnicas Relevantes

### Algoritmo MinMax
Avalia jogadas com vários passos à frente para definir a ação inicial mais eficiente a longo prazo. Uma árvore de possibilidades é construída a partir do estado atual do tabuleiro.

### Poda Alpha e Beta
Estratégia acoplada ao MinMax para otimizar a velocidade de processamento. Elimina ramificações que não influenciarão na decisão final, comparando os melhores valores (Alpha e Beta) encontrados para os jogadores maximizador e minimizador.

### Otimização de Memória via Codificação
A classe `Control.java` utiliza uma arquitetura baseada em Hash Maps para codificar as casas do tabuleiro. Ao invés de usar múltiplos bytes para rastrear coordenadas em instâncias complexas, o sistema codifica as jogadas processando um caractere único (`char`), reduzindo o custo de memória durante a geração extensiva de nós.

### Clonagem de Estado Lógico
Cada nó gerado na árvore de decisão processa um clone do tabuleiro original (implementando a interface padrão do Java `Cloneable`) para listar e simular os movimentos na profundidade atual sem corromper o estado principal da partida.

---

## Tecnologias e Bibliotecas

| Tecnologia | Descrição |
|---|---|
| **Java Swing e AWT** | Renderização da camada visual, transmitindo o estado da matriz do tabuleiro para a interface do usuário |
| **Tipografia Nativa** | Família de fontes genérica `Monospaced` do sistema operacional para formatar logs e painéis estatísticos |

---

## Estrutura do Projeto

```
.
├── src/
│   └── main/
└── README.md
```

O diretório `src/main/` concentra as cinco classes responsáveis pela arquitetura e funcionamento do jogo:

| Classe | Responsabilidade |
|---|---|
| `MainInterfaceGrafica.java` | Renderização gráfica |
| `Tabuleiro.java` | Processamento de regras e movimentos |
| `Node.java` | Orquestração da inteligência artificial |
| `Arvore.java` | Orquestração da inteligência artificial |
| `Control.java` | Funções utilitárias de controle de posição |