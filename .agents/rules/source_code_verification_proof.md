---
name: source_code_verification_proof
description: Comprovação por evidência de código-fonte e testes antes de declarar concluído
trigger: always_on
---

# Verificação no Código-Fonte e Comprovação com Evidências

## Regra fundamental
- **SEMPRE** verificar o código após implementar, lendo o código-fonte real e rodando os comandos de build/teste.
- **SEMPRE** comprovar a alteração citando arquivo, caminho e linhas, além do teste/check que prova o comportamento.

## Proibido
- Declarar tarefa concluída baseado em suposição ou relato textual, sem evidência de código e de teste.
- Reportar "funciona" por compilação local sem anotar a saída real do build.