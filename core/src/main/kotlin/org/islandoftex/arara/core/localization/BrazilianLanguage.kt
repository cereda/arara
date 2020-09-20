// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.arara.core.localization

import java.util.Locale
import org.islandoftex.arara.api.localization.Messages

@Suppress("MaxLineLength")
class BrazilianLanguage : Messages(
        providedLocale = Locale.forLanguageTag("pt-BR"),
        translators = listOf("Paulo Roberto Massa Cereda"),
        ERROR_BASENAME_NOT_A_FILE = "O método 'basename' requer um arquivo, não um diretório. Observe que '%s' não parece ser um arquivo. Se você precisa realizar alguma tarefa em um diretório, experimente os métodos disponíveis na API Java.",
        ERROR_CALCULATEHASH_IO_EXCEPTION = "Por alguma razão, eu não pude calcular o hash. Não tenho ideia porque não deu certo. Talvez o arquivo tenha sido movido ou removido antes ou durante a operação de hash. Ou talvez eu não tenha as permissões corretas para leitura do arquivo.",
        ERROR_CHECKBOOLEAN_NOT_VALID_BOOLEAN = "Observe que '%s' não é um valor lógico. Isso é fácil de resolver. Certifique-se de usar uma palavra válida que represente valores lógicos ('yes' e 'no', 'true' e 'false', '1' e '0', e 'on' e 'off').",
        ERROR_CHECKOS_INVALID_OPERATING_SYSTEM = "Não consegui descobrir qual é o seu sistema operacional. O valor '%s' não parece ser uma entrada válida de sistema operacional na minha lista (Eu posso também estar errado, é claro). Por favor, corrija o valor e tente novamente.",
        ERROR_CHECKREGEX_IO_EXCEPTION = "Não consegui ler o conteúdo do arquivo '%s', houve um erro de entrada e saída. Não tenho ideia porque não deu certo. Talvez o arquivo tenha sido movido ou removido antes ou durante a operação de leitura. Ou talvez eu não tenha as permissões corretas para leitura do arquivo.",
        ERROR_CONFIGURATION_GENERIC_ERROR = "Não consegui analisar o arquivo de configuração, alguma coisa de errado aconteceu. Esta parte é complicada, pois envolve aspectos do formato de serialização de dados subjacente. Eu farei o possível para te ajudar da melhor forma que puder.",
        ERROR_CONFIGURATION_LOOPS_INVALID_RANGE = "O valor definido na chave 'loops' no arquivo de configuração, que define o número máximo de iterações, possui um intervalo inválido. Por favor, certifique-se de especificar um valor inteiro positivo.",
        ERROR_DISCOVERFILE_FILE_NOT_FOUND = "Não consegui encontrar o arquivo especificado '%s' %s. Por favor, certifique-se de que o arquivo existe e que possui uma extensão válida.",
        ERROR_EVALUATE_COMPILATION_FAILED = "Por alguma razão, eu não consegui compilar a expressão condicional especificada. Esta parte é complicada, pois envolve aspectos da linguagem de expressão subjacente. Eu farei o possível para te ajudar da melhor forma que puder.",
        ERROR_EVALUATE_NOT_BOOLEAN_VALUE = "A avaliação da expressão condicional deveria retornar um valor lógico como resultado. Isso é fácil de resolver. Apenas certifique-se de que a avaliação da expressão condicional retorne um valor lógico no final.",
        ERROR_EXTRACTOR_IO_ERROR = "Ocorreu um erro de entrada e saída enquanto eu tentava extrair as diretivas. Não tenho ideia porque não deu certo. Talvez o arquivo tenha sido movido ou removido antes ou durante a operação de extração. Ou talvez eu não tenha as permissões corretas para leitura do arquivo.",
        ERROR_FILETYPE_NOT_A_FILE = "O método 'filetype' requer um arquivo, não um diretório. Observe que '%s' não parece ser um arquivo. Se você precisa realizar alguma tarefa em um diretório, experimente os métodos disponíveis na API Java.",
        ERROR_FILETYPE_UNKNOWN_EXTENSION = "Não consegui reconhecer '%s' como uma extensão padrão. Se você quiser definir um novo tipo de arquivo, certifique-se de especificar a extensão e o padrão. Estas são as extensões definidas por padrão: %s",
        ERROR_GETAPPLICATIONPATH_ENCODING_EXCEPTION = "Houve um problema de codificação enquanto eu tentava obter o caminho da aplicação. Não há muito o que eu possa fazer sobre isso.",
        ERROR_INTERPRETER_ARGUMENT_IS_REQUIRED = "Parece que o argumento '%s' está marcado como obrigatório na regra, mas eu não consegui encontrá-lo nos parâmetros da diretiva correspondente. Por favor, certifique-se de adicioná-lo como parâmetro em sua diretiva e tente novamente.",
        ERROR_INTERPRETER_COMMAND_RUNTIME_ERROR = "Não consegui avaliar um dos comandos disponibilizados. Esta parte é complicada, pois envolve aspectos da linguagem de expressão subjacente. Eu farei o possível para te ajudar da melhor forma que puder.",
        ERROR_INTERPRETER_DEFAULT_VALUE_RUNTIME_ERROR = "Não consegui avaliar a expressão associada ao valor padrão de um dos argumentos. Esta parte é complicada, pois envolve aspectos da linguagem de expressão subjacente. Eu farei o possível para te ajudar da melhor forma que puder.",
        ERROR_INTERPRETER_EXIT_RUNTIME_ERROR = "Não consegui avaliar a expressão associada ao código de saída de um dos comandos disponibilizados. Esta parte é complicada, pois envolve aspectos da linguagem de expressão subjacente. Eu farei o possível para te ajudar da melhor forma que puder.",
        ERROR_INTERPRETER_FLAG_RUNTIME_EXCEPTION = "Não consegui avaliar a expressão associada à presença de um dos argumentos. Esta parte é complicada, pois envolve aspectos da linguagem de expressão subjacente. Eu farei o possível para te ajudar da melhor forma que puder.",
        ERROR_INTERPRETER_RULE_NOT_FOUND = "Não consegui encontrar uma regra chamada '%s' nos caminhos de regras especificados. Talvez uma palavra digitada incorretamente? Eu estava procurando por um arquivo chamado '%s.yaml' nos seguintes caminhos, em ordem de prioridade: %s",
        ERROR_INTERPRETER_UNKNOWN_KEYS = "Encontrei as seguintes chaves desconhecidas na diretiva: %s. Isso é fácil de resolver, apenas remova essas chaves do seu mapa.",
        ERROR_INTERPRETER_WRONG_EXIT_CLOSURE_RETURN = "A expressão 'exit' referente ao código de saída deve sempre retornar um valor lógico (mesmo se não há computação propriamente dita). Isso é fácil de resolver: apenas certifique-se de corrigir o tipo de retorno e tente novamente.",
        ERROR_LOAD_COULD_NOT_LOAD_XML = "Não consegui carregar o banco de dados YAML chamado '%s'. Não tenho ideia porque não deu certo. Talvez o arquivo tenha sido movido ou removido antes ou durante a operação de leitura. Ou talvez eu não tenha as permissões corretas para leitura do arquivo. A propósito, certifique-se de que o arquivo YAML esteja correto.",
        ERROR_PARSER_INVALID_PREAMBLE = "Sinto muito, mas o preâmbulo '%s' não foi encontrado. Por favor, certifique-se de que esta chave existe no arquivo de configuração.",
        ERROR_PARSERULE_GENERIC_ERROR = "Não consegui analisar a regra, alguma coisa de errado aconteceu. Esta parte é complicada, pois envolve aspectos do formato de serialização de dados subjacente. Eu farei o possível para te ajudar da melhor forma que puder.",
        ERROR_REPLICATELIST_MISSING_FORMAT_ARGUMENTS_EXCEPTION = "Não consegui replicar a lista por causa de um formato de argumento faltante. Meu palpite é que existem menos (ou mais) parâmetros do que o esperado. Certifique-se de corrigir o número de parâmetros e tente novamente.",
        ERROR_RULE_IDENTIFIER_AND_PATH = "Encontrei um erro na regra '%s' localizada em '%s'.",
        ERROR_RUN_GENERIC_EXCEPTION = "Não consegui executar o comando de sistema disponibilizado, alguma coisa de errado aconteceu. Esta parte é complicada, pois envolve aspectos da linguagem de expressão subjacente. Eu farei o possível para te ajudar da melhor forma que puder.",
        ERROR_RUN_INTERRUPTED_EXCEPTION = "A execução do comando de sistema disponibilizado foi inesperadamente interrompida. Talvez tenha ocorrido uma interrupção externa que forçou o comando a encerrar abruptamente.",
        ERROR_RUN_INVALID_EXIT_VALUE_EXCEPTION = "A execução do comando de sistema retornou um código de saída inválido.",
        ERROR_RUN_IO_EXCEPTION = "A execução do comando de sistema falhou por causa de um erro de entrada e saída. Você tem certeza de que o comando de sistema especificado existe no seu caminho? Pode ser uma boa ideia verificar o caminho do sistema e confirmar se o comando realmente está disponível.",
        ERROR_RUN_TIMEOUT_EXCEPTION = "A execução do comando de sistema atingiu o timeout especificado e foi abortada. Se o tempo foi muito curto, certifique-se de especificar um valor maior.",
        ERROR_SAVE_COULD_NOT_SAVE_XML = "Não consegui salvar o banco de dados YAML chamado '%s'.  Não tenho ideia porque não deu certo. Talvez eu não tenha as permissões corretas para escrever o arquivo YAML no disco.",
        ERROR_SESSION_OBTAIN_UNKNOWN_KEY = "O método 'get' encontrou a chave desconhecida '%s' no escopo de sessão. Não posso obter um valor que não existe na sessão. Por favor, especifique uma chave válida e tente novamente.",
        ERROR_SESSION_REMOVE_UNKNOWN_KEY = "O método 'remove' encontrou a chave desconhecida '%s' no escopo de sessão. Não posso remover um valor que não existe na sessão. Por favor, especifique uma chave válida e tente novamente.",
        ERROR_VALIDATE_EMPTY_FILES_LIST = "Li uma diretiva %s e descobri que a lista 'files' especificada está vazia. Isso é fácil de resolver: certifique-se de que a lista possua, pelo menos, um elemento e tente novamente.",
        ERROR_VALIDATE_FILES_IS_NOT_A_LIST = "Li uma diretiva %s e descobri que 'files' requer uma lista. Por favor, certifique-se de corrigir o tipo para uma lista adequada e tente novamente.",
        ERROR_VALIDATE_INVALID_DIRECTIVE_FORMAT = "Encontrei uma diretiva inválida %s no arquivo especificado. Certifique-se de corrigir a diretiva e tente novamente.",
        ERROR_VALIDATE_NO_DIRECTIVES_FOUND = "Parece que o arquivo especificado não possui diretivas! Certifique-se de incluir, pelo menos, uma diretiva e tente novamente.",
        ERROR_VALIDATE_ORPHAN_LINEBREAK = "Aparentemente, existe uma quebra de linha de uma diretiva órfã na linha %s. Não posso continuar. Por favor, corrija a diretiva e tente novamente.",
        ERROR_VALIDATE_REFERENCE_IS_RESERVED = "Li uma diretiva %s e descobri que a chave 'reference' foi utilizada. Esta chave é reservada, portanto você não pode utilizá-la! Mas não se preocupe, isso é fácil de resolver. Apenas substitua esse nome por outro.",
        ERROR_VALIDATE_YAML_EXCEPTION = "Ocorreu um problema com o mapa YAML especificado em uma diretiva %s. Esta parte é complicada, pois envolve aspectos do formato de serialização de dados subjacente.",
        ERROR_VALIDATEBODY_ARGUMENT_ID_IS_RESERVED = "O identificador de argumento '%s' está reservado, portanto você não pode utilizá-lo. Isso é fácil de resolver. Apenas substitua esse nome por outro.",
        ERROR_VALIDATEBODY_DUPLICATE_ARGUMENT_IDENTIFIERS = "Aparentemente, você possui identificadores de argumentos duplicados em sua regra. Certifique-se de corrigir esse erro e tente novamente.",
        ERROR_VALIDATEBODY_MISSING_KEYS = "Ao definir um escopo de argumento de regra, ao menos a chave 'flag' ou 'default' deve ser utilizada. Por favor, certifique-se de utilizar, pelo menos, uma delas.",
        ERROR_VALIDATEBODY_NULL_ARGUMENT_ID = "Descobri que um dos argumentos não possui identificador associado. Por favor, certifique-se de adicionar um identificador válido ao argumento e tente novamente.",
        ERROR_VALIDATEBODY_NULL_COMMAND = "Encontrei um comando nulo na regra especificada. Isso é fácil de resolver. Certifique-se de adicionar um comando válido na regra.",
        ERROR_VALIDATEHEADER_NULL_NAME = "A regra especificada não possui nome. Isso é fácil de resolver. Certifique-se de adicionar um nome válido e tente novamente.",
        ERROR_VALIDATEHEADER_WRONG_IDENTIFIER = "A regra possui um identificador incorreto. Eu estava esperando '%s', mas encontrei '%s'. Isso é fácil de resolver: apenas substitua o identificador incorreto pelo valor correto.",
        INFO_DISPLAY_EXCEPTION_MORE_DETAILS = "Detalhes adicionais estão disponíveis para esta exceção:",
        INFO_DISPLAY_EXECUTION_TIME = "Total: %s segundos",
        INFO_DISPLAY_FILE_INFORMATION = "Processando '%s' (tamanho: %s, última modificação: %s), por favor, aguarde.",
        INFO_INTERPRETER_DRYRUN_MODE_BOOLEAN_MODE = "Apesar da execução em modo dry-run, é provável que esta entrada já foi processada, dado que o seguinte valor lógico foi retornado: %s",
        INFO_INTERPRETER_DRYRUN_MODE_SYSTEM_COMMAND = "A executar: %s",
        INFO_LABEL_AUTHOR = "Autor:",
        INFO_LABEL_AUTHORS = "Autores:",
        INFO_LABEL_CONDITIONAL = "Expressão condicional:",
        INFO_LABEL_NO_AUTHORS = "Nenhum autor informado",
        INFO_LABEL_ON_DETAILS = "DETALHES",
        INFO_LABEL_ON_ERROR = "ERRO",
        INFO_LABEL_ON_FAILURE = "FRACASSO",
        INFO_LABEL_ON_SUCCESS = "SUCESSO",
        INFO_LABEL_UNNAMED_TASK = "Tarefa sem nome",
        INFO_PARSER_NOTES = "arara é distribuída sob a licença New BSD.",
        LOG_INFO_BEGIN_BUFFER = "INÍCIO DO BUFFER DE SAÍDA",
        LOG_INFO_BOOLEAN_MODE = "Valor lógico: %s",
        LOG_INFO_DIRECTIVES_BLOCK = "DIRETIVAS",
        LOG_INFO_END_BUFFER = "FINAL DO BUFFER DE SAÍDA",
        LOG_INFO_INTERPRET_RULE = "Estou pronto para interpretar a regra '%s'.",
        LOG_INFO_INTERPRET_TASK = "Estou pronto para interpretar a tarefa '%s' da regra '%s'.",
        LOG_INFO_POTENTIAL_PATTERN_FOUND = "Encontrei um padrão em potencial na linha %s: %s",
        LOG_INFO_RULE_LOCATION = "Localização da regra: '%s'",
        LOG_INFO_SYSTEM_COMMAND = "Comando de sistema: %s",
        LOG_INFO_TASK_RESULT = "Resultado da tarefa:",
        LOG_INFO_VALIDATED_DIRECTIVES = "Todas as diretivas foram validadas. Estamos prontos para continuar.",
        LOG_INFO_WELCOME_MESSAGE = "Olá, eu sou o arara %s (revisão %s)!"
)
