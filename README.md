# Comanda Fácil — versão 0.6

Sistema local de gestão para restaurante, bar e lanchonete, com interface responsiva para computador e celular. O computador Windows 11 hospeda o servidor e os aparelhos acessam pela mesma rede Wi-Fi.

## Novidades da versão 0.6 — segurança e continuidade
- Configuração inicial para criar o primeiro usuário proprietário (administrador).
- Login individual com senha protegida por derivação criptográfica (scrypt) e sessões com validade de 7 dias.
- Perfis de acesso: administrador, gerente, caixa, garçom e cozinha.
- Controle de permissões no servidor (não apenas ocultação de telas).
- Administrador pode cadastrar usuários e ativar/desativar contas.
- Botão para encerrar sessão.
- Backup automático do banco SQLite ao iniciar o servidor e a cada 24 horas.
- Retenção dos 30 backups mais recentes, além de criação e download manual pelo painel de usuários e backup.

## Perfis
- **Administrador:** acesso total, usuários e backups.
- **Gerente:** acesso operacional e painel de gestão, sem administração de usuários e backups.
- **Caixa:** mesas, produtos e operações de caixa/pagamento.
- **Garçom:** consulta de mesas e produtos, abertura/edição de comandas e lançamento de itens.
- **Cozinha:** consulta dos pedidos e atualização do andamento dos itens.

## Instalação no Windows 11
1. Extraia todo o ZIP para uma pasta no computador principal.
2. Instale Node.js LTS em https://nodejs.org/ se ainda não estiver instalado.
3. Execute `INICIAR-WINDOWS.bat`. Na primeira execução, o script instalará as dependências (é necessária internet apenas para essa instalação inicial).
4. No computador, abra `http://localhost:3000`.
5. Na primeira abertura, crie o usuário proprietário. Use uma senha forte com pelo menos 10 caracteres e guarde-a em local seguro.
6. No terminal será exibido o endereço IP da rede local. No celular Android conectado ao mesmo Wi-Fi, abra `http://IP-DO-COMPUTADOR:3000` e entre com o usuário criado ou com a conta individual do funcionário.
7. No Chrome Android, use o menu ⋮ e escolha **Adicionar à tela inicial** ou **Instalar aplicativo**, se disponível.

## Backup
Os backups ficam na pasta `backups` dentro da pasta do sistema. O sistema tenta criar um backup após iniciar e repete diariamente, mantendo até 30 arquivos. O painel permite criar e baixar um backup manualmente. Copie periodicamente os backups para um pendrive ou outro computador: backup armazenado apenas no mesmo computador não protege contra falha ou roubo do equipamento.

## Aplicativo para celular
A interface é um aplicativo web responsivo que pode ser adicionado à tela inicial do Android. Não é um APK nativo da Play Store. Como o acesso local é feito por HTTP usando o IP privado do computador, alguns navegadores limitam a instalação PWA completa e o cache offline. O celular e o computador precisam estar na mesma rede Wi-Fi; o computador servidor deve permanecer ligado. O uso local não depende da internet depois que as dependências são instaladas.

## Recursos mantidos
- Mesas configuráveis e comandas individuais por cliente.
- Cardápio, observações e painel da cozinha.
- Impressão pelo navegador em impressora comum.
- Caixa, pagamentos parciais e formas de pagamento.
- Ingredientes, estoque mínimo, movimentações e fichas técnicas.
- Painel do proprietário, despesas e indicadores.
- Banco SQLite local e atualização em tempo real.

## Avisos importantes
Esta é uma versão de desenvolvimento. Antes do uso comercial, teste todos os fluxos e permissões em um ambiente controlado. O resultado financeiro exibido é vendas recebidas menos despesas lançadas; não representa lucro líquido. Ainda precisam ser concluídos testes de instalação, recuperação de backup, cancelamentos/estornos e integrações fiscais. Não há emissão fiscal integrada. Não exponha a porta do sistema diretamente à internet; use somente a rede local confiável.

## Aplicativo Android — projeto v0.7
A pasta `android-app` contém o projeto Android para gerar um APK instalável. Ao abrir pela primeira vez, informe o endereço de rede local mostrado pelo servidor Windows (ex.: `http://192.168.0.10:3000`). O endereço fica salvo no aparelho. O app usa a interface e os dados do servidor local, mantendo o banco SQLite no computador. Consulte `android-app/LEIA-ME-APK.txt` para gerar o APK no Android Studio.

**Observação:** este pacote contém o projeto-fonte Android pronto para compilação; não contém ainda um APK binário compilado. A compilação exige Android Studio, Android SDK Platform 35 e sincronização do Gradle em um computador Windows ou outro ambiente Android configurado.
