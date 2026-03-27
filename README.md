# 🌍 GPS Report Generator

Uma aplicação Desktop construída em Java para automatizar e padronizar a geração de mapas estáticos em relatórios de campo de telecomunicações.

A aplicação já formata a imagem no padrão paisagem (640x380), ideal para o encaixe perfeito em documentos PDF e relatórios operacionais diários.

## 🚀 Funcionalidades
- **Interface Gráfica (GUI):** Tela simples e intuitiva para inserção de coordenadas.
- **Integração com API:** Consumo direto da API estática do Google Maps.
- **Processamento de Imagem:** Ajuste automático de proporção e inserção de rodapé padronizado.
- **Standalone:** Empacotado como um executável Windows (`.exe`) independente, sem necessidade de instalação do Java ou configurações adicionais na máquina do usuário final.

## 🛠️ Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **JavaFX** (Interface Gráfica)
- **Maven** (Gerenciamento de dependências)
- **jpackage & WiX Toolset v3** (Geração do instalador Windows)
