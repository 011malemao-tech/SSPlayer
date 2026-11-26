# SSPlayer Android — primeira base nativa

Esta é a primeira base de implementação Android, não um APK pronto.

## Incluído
- Projeto Kotlin para Android Studio.
- Seletor de diretório Android (Storage Access Framework), adequado a pastas internas e USB quando o sistema as disponibiliza.
- Persistência da permissão de leitura da pasta escolhida.
- Varredura recursiva e catalogação em lotes de 200 registros.
- Banco SQLite com índices por nome e caminho.
- Serviço Media3 MediaSessionService declarado para a futura reprodução em segundo plano.

## Abrir
1. Instale Android Studio.
2. Abra a pasta `SSPlayerAndroid` como projeto.
3. Aguarde a sincronização Gradle.
4. Execute em um Android físico para testar o catálogo.

## Próximas etapas
- Conectar a lista paginada ao MediaController e PlaybackService.
- Implementar reprodução direta dos URIs `content://`.
- Implementar cópia opcional do USB para armazenamento interno em streaming com buffer (sem carregar o arquivo inteiro na RAM).
- Criar interface SSPlayer aprovada e equalizador.

A indexação salva metadados, não duplica os arquivos de áudio. A reprodução direta depende de o provedor do USB manter o URI acessível. A cópia será implementada como tarefa separada, com espaço livre verificado antes de iniciar.
