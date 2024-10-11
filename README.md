Kaio Rodrigues Silva de Souza
RA:72250446

Matheus Cerqueira de Araujo
RA:72301781

Marcus Vinnícius de Araújo Nascimento
RA:72301373

Weslley Francisco de Oliveira 
RA:72300376

Rosane Barbosa Coelho
RA:72250778


O LINK DO VIDEO DEMOSTRANDO AS FUNCIONALIDADES DO APLICATIVO (https://youtu.be/sJkK1dOJrRc?feature=shared) ( A TAMBÉM UMA PASTA COM PRINTS DA TELA)


FUNCIONALIDADES DO CATÁLOGO DE ENDEREÇOS

1. Tela de Cadastro de Perfil

   Entrada de Dados: A tela inicial exibe um formulário com campos para inserir nome, número de telefone pessoal, número do escritório, email e endereço.
   Validação de Dados: Todos os campos são validados antes do envio para garantir que informações importantes, como email e números de telefone, sejam corretamente inseridas.
   Ação: Ao clicar no botão "SALVAR PERFIL", os dados são armazenados localmente usando o SQLite, permitindo que o perfil seja acessado e visualizado posteriormente.

2. Exibição do Perfil

   Visualização de Dados: Após o cadastro, o usuário pode visualizar os detalhes inseridos (nome, números de telefone, email e endereço).
   Interatividade: Cada número de telefone apresenta ícones de chamadas e SMS, possibilitando ao usuário ligar diretamente ou enviar mensagens com um simples toque. O email é interativo, permitindo a abertura do cliente de email padrão do dispositivo ao clicar.
   Design Responsivo: A tela é projetada para ser limpa e intuitiva, facilitando a navegação entre as informações do perfil e as opções de ação.

3. Edição de Perfil

   Atualização de Dados: O botão "ATUALIZAR" leva o usuário a uma tela com os dados atuais já preenchidos nos campos de edição. O usuário pode modificar qualquer campo, como atualizar o endereço ou número de telefone.
   Validação Visual: Durante a edição, campos com dados inválidos ou incorretos são destacados em vermelho, proporcionando uma validação em tempo real.
   Confirmação de Alteração: Ao clicar em "ATUALIZAR", os dados editados são salvos, e a interface retorna para a visualização atualizada do perfil.

4. Exclusão de Perfil

   Confirmação de Exclusão: O botão "DELETAR" remove o perfil do banco de dados SQLite. Antes da exclusão, um prompt de confirmação é exibido para evitar remoções acidentais.
   Remoção Segura: Após a confirmação, todos os dados são permanentemente apagados, e o usuário é redirecionado para a tela de cadastro, onde pode registrar um novo perfil.

5. Funcionalidades de Comunicação Integradas

   Chamada Telefônica: Com apenas um toque, o usuário pode iniciar uma chamada para o número de telefone registrado no perfil, seja ele pessoal ou do escritório.
   Envio de SMS: O aplicativo também permite o envio de mensagens SMS diretamente a partir da interface, facilitando a comunicação sem a necessidade de abrir manualmente o app de mensagens.
   Envio de Email: Ao clicar no email registrado, o aplicativo abre automaticamente o cliente de email padrão do dispositivo, com o endereço já preenchido, facilitando o envio rápido de emails.

