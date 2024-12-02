# Inicio

  Construir um Static Checker que realiza:
  1- Análise Léxica: Identificação e categorização de elementos léxicos no código fonte.
  2- Parte da Análise Sintática: Controle de escopo e verificação básica da ordem dos átomos.


# Requisitos Funcionais: (Entrada e saída)

  Entrada:
    1- O Static Checker deve receber como entrada:
    2- Um texto fonte escrito em ASCII.
    3- Extensão obrigatória: .242.
    4- Caso o nome do arquivo seja passado sem caminho completo, ele deve ser buscado no diretório corrente.
    5- Aceitar nomes completos com caminho para o arquivo.

  Saída:
    => Gerar dois relatórios obrigatórios para cada texto fonte analisado: <=
    
    1- Relatório de Análise Léxica (.LEX):
        Listar símbolos na ordem em que aparecem.
        Informações obrigatórias por símbolo:
        Lexeme (o símbolo textual).
        Código do átomo correspondente.
        Índice na tabela de símbolos (se aplicável).
        Linha de ocorrência no texto.
        Cabeçalho deve conter:
        Código identificador da equipe.
        Nomes, e-mails e telefones dos membros da equipe.
        Título: RELATÓRIO DA ANÁLISE LÉXICA.
        Nome do arquivo fonte analisado.

      2- Relatório da Tabela de Símbolos (.TAB):
        Listar identificadores armazenados na tabela de símbolos.
        Para cada identificador, exibir:
              Número da entrada.
              Código do átomo.
              Lexeme (até 30 caracteres válidos).
              Tamanho antes e depois da truncagem.
              Tipo do símbolo (ex.: INT, STR, etc.).
              Linhas de ocorrência (até as 5 primeiras).



 # Requisitos Funcionais: (Analisador Léxico)

       1- Ler o texto fonte caractere a caractere e formar os átomos.
       2- Usar critério de máximo comprimento possível ao montar átomos.
       3- Ignorar diferenças entre maiúsculas e minúsculas.
       4- Filtrar caracteres inválidos sem reportar erro.
       5- Tratamento especial para espaços em branco e comentários:
       6- Comentários de linha (//) e bloco (/* ... */) devem ser ignorados.
       7- Espaços em branco podem ser delimitadores ou parte do átomo, dependendo do contexto.

  # Requisitos Funcionais: (Analisador Sintático)

       1- Implementar funcionalidades básicas de controle:
       2- Solicitação e abertura do arquivo fonte.
       3- Inicialização das tabelas de símbolos e palavras reservadas.
       4- Emissão dos relatórios .LEX e .TAB.

  # Requisitos Não Funcionais: 

       1-Linguagem de Programação e Ambiente:
       2- Especificar IDE e compilador a serem usados.
       
       -> Formatos de Relatório:
             ->Os arquivos de saída devem ser gerados na mesma pasta do texto fonte analisado.
            ->Nome do arquivo de saída:
            ->Baseado no nome do arquivo fonte, mas com extensões .LEX e .TAB
            
        ->Limites Técnicos:
            Identificadores não podem ter mais de 30 caracteres válidos.

  # Estrutura de Dados

        ==> Tabela de Símbolos Deve conter:
            1- Número da entrada.
            2- Código do átomo.
            3- Lexeme.
            4- Tipo do símbolo.
            5- Tamanho antes e depois da truncagem.
            6- Linhas de ocorrência.

        ==> Tabela de Palavras e Símbolos Reservados
            1- Deve ser carregada antes da análise.
            2- Não deve variar entre diferentes textos fonte.

# Duvidas

- Os tipos de arquivo serão .txt

# Requisitos
[] o código deve pedir o nome do arquivo. Se a resposta for apenas o nome do arquivo, o sistema procurara na pasta do projeto. Se tiver o caminho do arquivo, ele teve buscar nesse caminho.
[] deverão ser gerados obrigatoriamente dois arquivos de saída em separado na mesma pasta onde o texto fonte parâmetro se encontra:
• um arquivo com o relatório da análise léxica e
• um outro arquivo com o relatório da tabela de símbolos

