# MovieTech
## GRUPO 27 - 6SOAT 

## Autores

- Henrique Rodrigues da Silva RM 353376
- Felipe Reis - RM 353932
- Lucas Marques - RM 353910
- Lucas Domingues - RM 353900

## Linkedin

- https://www.linkedin.com/in/felipe-reis-028a38181/
- https://www.linkedin.com/in/henrique-rodrigues-639873173/
- https://www.linkedin.com/in/lucas-domingues-de-souza-565720a1/
- https://www.linkedin.com/in/lucas-marques-a4848a52/


# Tecnologias utilizadas

- ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
- ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
- ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
- ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
- ![Kubernetes](https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white)

## Base de dados
- ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
  
# Sobre o projeto

MovieTech é um projeto que desenvolve uma aplicação de edição de Vídeos, com ênfase na parte backend. Utilizamos Docker e aplicamos os conceitos de arquitetura hexagonal. Dentro dessa aplicação, documentamos as APIs usando o Swagger. 
Vale destacar que os requisitos fornecidos aos alunos não incluíam a parte de frontend.
O objetivo deste projeto é implementar um sistema que atenda às necessidades dos Usuários. O sistema visa proporcionar autonomia, escalabilidade, resiliência e praticidade em todos os aspectos de sua implementação.
Para desenvolver este software, utilizamos a metodologia DDD (Domain Driven Design). Aplicando esses conceitos, conseguimos nos aprofundar nos requisitos e proporcionar uma experiência agradável e eficiente aos nossos clientes.

## Domain-driven design
![image](https://github.com/grupo27-6soat-fiap/hackaton-video-processor-clean/blob/master/Brainstorming.jpg)

## Linguagem Ubíqua
 ```bash

Usuário
 - Descrição: Pessoa que utiliza o sistema para editar vídeos.
Ações Principais:
 - Criar Projeto: Iniciar um novo projeto de edição de vídeo.
 - Importar Mídia: Adicionar vídeos ao Editor.
 - Fazer Download do Vídeo: Exportar o vídeo editado em um formato final.

Editor
 - Descrição: Sistema responsável por refinar o projeto, aplicar ajustes avançados e garantir a qualidade final.
Ações Principais:
 - Acessar Projeto: Receber e abrir vídeos enviados pelo Usuário.
 - Gerenciar Camadas: Organizar elementos de vídeo com trechos específicos.
 - Compactar Arquivos: Gerar arquivo comprimido com os arquivos resultantes do processamento.
 - Disponibilizar o Projeto: Exportar a versão final com qualidade e formato ideais.

Processamento
 - Descrição: Módulo automatizado que executa tarefas técnicas no sistema.
Ações Principais:
 - Processar Vídeo: Combinar todas as camadas e efeitos em um arquivo final.
 - Gerar Arquivos: Gerar imagens de acordo com o formato definido na regra de negócio
```

## Cobertura de Testes 
![image](https://github.com/grupo27-6soat-fiap/hackaton-video-processor-clean/blob/master/Cobertura%20de%20Teste%20-%20Processor.png)

# Para acessar a Collection da API Postman clique na imagem:
[![Postman](https://img.shields.io/badge/Postman-%23FF6C37.svg?style=for-the-badge&logo=postman&logoColor=white)
](https://github.com/grupo27-6soat-fiap/hackaton-video-processor-clean/blob/master/Hackaton.postman_collection.json)

# Arquitetura Infraestrutura Kubernetes:
![image](https://github.com/grupo27-6soat-fiap/hackaton-video-processor-clean/blob/master/Desenho%20da%20Arquitetura.png)
## 🏗️ Arquitetura do Ambiente AWS

### 📌 Visão Geral
O ambiente foi estruturado na **AWS** utilizando **Amazon EKS (Elastic Kubernetes Service)** para orquestração de containers, garantindo escalabilidade, segurança e alta disponibilidade para os serviços. Além disso, utilizamos diversos serviços AWS para **autenticação, processamento de vídeos, armazenamento e envio de notificações**, garantindo um fluxo eficiente e desacoplado.

---

## 🏛️ Componentes Principais

### 1️⃣ Orquestração com **Amazon EKS**
Nosso cluster **EKS** gerencia os deployments dos serviços dentro do **Kubernetes**, garantindo que os workloads sejam executados de forma escalável e segura. Temos dois principais deployments dentro do cluster:

- **Solicitacao Deployment (Service)** → Responsável pelo recebimento das solicitações dos usuários, autenticação e interação com os demais serviços.
- **Video Deployment (Processor)** → Processa os vídeos enviados, interagindo com os serviços de armazenamento e notificações.

O uso do **EKS** garante que nossa aplicação seja **resiliente**, permitindo autoescalonamento dos pods e garantindo **alta disponibilidade**.

---

### 2️⃣ Segurança e Autenticação - **Amazon Cognito**
A autenticação dos usuários é gerenciada pelo **Amazon Cognito**, um serviço que permite autenticação segura utilizando:
- **Usuário/senha**

O Cognito garante que apenas usuários autenticados possam interagir com a aplicação, oferecendo recursos como **Multi-Factor Authentication (MFA)** e **gerenciamento seguro de tokens JWT**.

---

### 3️⃣ Banco de Dados Relacional - **Amazon RDS (PostgreSQL)**
O serviço **Solicitacao Deployment** acessa um banco de dados **PostgreSQL** hospedado no **Amazon RDS**, garantindo **persistência dos dados com alta disponibilidade**.  
📌 **Motivos para escolher PostgreSQL:**
- **Relacionamentos complexos** entre entidades (ex: Clientes, Pedidos, Produtos)
- **Consultas SQL eficientes e escaláveis**

---

### 4️⃣ Processamento de Vídeos - **Amazon S3 e Amazon SQS**
O fluxo de processamento de vídeos envolve os seguintes componentes:

#### 🔹 **1. Upload e Armazenamento**
- Os vídeos carregados pelos usuários são enviados para o **Amazon S3 (Video Processing Bucket)**, garantindo **armazenamento escalável e seguro**.

#### 🔹 **2. Fila de Processamento - Amazon SQS**
- Após o upload, o **Solicitacao Deployment** envia uma mensagem para a **Video Processing Queue (Amazon SQS)**.
- Isso permite **desacoplar os serviços** e garantir **escalabilidade**, pois os vídeos podem ser processados de forma assíncrona.

#### 🔹 **3. Processamento - Video Deployment (Processor)**
- O **Processor** recebe mensagens da fila **SQS**, baixa os vídeos do **S3**, executa o processamento e gera os arquivos finais (exemplo: extração de frames, compactação, etc.).
- Após o processamento, o **status da solicitação é atualizado** no banco de dados.

---

### 5️⃣ Envio de Notificações - **Amazon SES**
Após o processamento do vídeo, um e-mail de notificação é enviado ao usuário através do **Amazon Simple Email Service (SES)**.
- O **Processor** dispara um evento de conclusão, enviando um e-mail automaticamente para o usuário informando sobre o status do seu vídeo.
- O **Amazon SES** garante alta entregabilidade e segurança na comunicação via e-mail.

---

## 🔄 Fluxo Completo da Aplicação
1️⃣ O usuário acessa a aplicação e realiza login via **Amazon Cognito**.  
2️⃣ Após autenticação, o usuário envia uma solicitação via **Solicitacao Deployment**.  
3️⃣ Os dados são armazenados no **PostgreSQL (RDS)** e os vídeos são enviados para o **Amazon S3**.  
4️⃣ Uma mensagem é enviada para a **Video Processing Queue (SQS)** para iniciar o processamento.  
5️⃣ O **Processor (Video Deployment)** lê a fila, processa os vídeos e salva os resultados.  
6️⃣ Após a conclusão, o **status é atualizado** no banco de dados e um e-mail é enviado via **Amazon SES** notificando o usuário.  

---

## 🛠️ **Motivos para Escolher essa Arquitetura**
✅ **Alta Disponibilidade e Escalabilidade** → Kubernetes (**EKS**) e serviços gerenciados garantem que a aplicação suporte grande volume de usuários e processamento de vídeos.  
✅ **Desacoplamento de Serviços** → Uso de **Amazon SQS** para filas de mensagens permite que o processamento de vídeos seja assíncrono, evitando gargalos.  
✅ **Segurança e Confiabilidade** → Uso de **Amazon Cognito** para autenticação e **IAM Policies** para controle de acesso a recursos da AWS.  
✅ **Eficiência e Custo-Benefício** → Utilização de **S3 para armazenamento escalável** e **SES para notificações com baixo custo**.  

Essa arquitetura garante **eficiência, segurança e escalabilidade**, permitindo que a aplicação cresça de forma sustentável e confiável. 🚀

## Link Youtube:
https://youtu.be/9oLeVEOWEJk

## Implementação
Para implantar o projeto, utilizamos o conceito de containers com o Docker como ferramenta de gerenciamento. Nosso projeto usa tanto Dockerfile quanto Docker-compose. Utilizamos uma imagem do Java com Spring e uma imagem do Postgres para rodar o banco de dados localmente e realizar as operações de CRUD da nossa aplicação.

## Sonar
https://sonarcloud.io/project/overview?id=grupo27-6soat-fiap_grupo27-6soat-fiap-foodtech-fase04-cliente-service

# Como executar o projeto

## Back end
Pré-requisitos: Java 17, JDK 17, Gradle, Postgres.

# Como rodar usando Kubernetes Local:

### Iniciar o localstack através do comando: localstack start

### Após instalar o Docker Desktop, ativar no Docker Desktop a opção do Kubernetes:

### Clicar em Settings (Configuração)
![image](https://github.com/user-attachments/assets/74403f54-8ec9-45f2-913d-ffb8b6c6e634)

### Selecionar opção a esquerda do menu chamada "Kubernetes", clicar em "Enable Kubernetes" e depois em "Apply & Start:
![image](https://github.com/user-attachments/assets/15e37689-fea7-4691-8a90-8354fbac258c)

### Após o Kubernetes incializar, seguir os próximos passos:

### Clonar repositório:
git clone [https://github.com/grupo27-6soat-fiap/hackaton-video-processor-clean.git]

### Entrar na pasta do projeto :
cd .\hackaton-solicitacao-service-clean\
### Abrir o PowerShell ou o terminal do computador
### Ordem de execução dos arquivos Yaml:
1 - Executar os arquivos da pasta foodtech/k8s:
 - 1.1 - kubectl apply -f ./k8s/secret-postgress.yml
 - 1.2 - kubectl apply -f ./k8s/statefulset-postgress.yml
 - 1.3 - kubectl apply -f ./k8s/service-postgress.yml
 - 1.4 - kubectl apply -f ./k8s/service-app.yml
 - 1.5 - kubectl apply -f .k8s/localstack-deployment.yml
 - 1.5 - kubectl apply -f ./k8s/deployment-app.yml
 - 1.6 - kubectl apply -f ./k8s/hpa.yml
   
2 - Alterar a porta da rota no postman quando o Kubernetes estiver rodando
  - Porta: 30002 (Kubernetes)
  - Porta: 8080 (Local)
![image](https://github.com/user-attachments/assets/95f7c9bb-b7bb-4b20-ad6b-60501e4c3905)



# Como Rodar o Projeto Utilizando o Ambiente AWS

Este guia fornece instruções para configurar o ambiente necessário e executar o projeto Foodtech na AWS, utilizando diversas infraestruturas integradas.

## Repositórios Necessários

1. [hackaton-infra-eks](https://github.com/grupo27-6soat-fiap/hackaton-infra-eks)
2. [hackaton-infra-rds](https://github.com/grupo27-6soat-fiap/hackaton-infra-rds)
3. [hackaton-infra-cognito](https://github.com/grupo27-6soat-fiap/hackaton-infra-cognito)
4. [hackaton-solicitacao-service](https://github.com/grupo27-6soat-fiap/hackaton-solicitacao-service.git)

## Ordem de Execução dos Workflows Terraform

A seguir estão os passos para executar os workflows Terraform, que provisionam toda a infraestrutura necessária:

### Passo 1 - hackaton-infra-eks
- Executar o workflow para provisionar o cluster EKS que será utilizado pela aplicação.

### Passo 2 - hackaton-infra-cognito
- Executar o workflow para provisionar o serviço Cognito que gerenciará a autenticação.

### Passo 3 - hackaton-infra-rds
- Executar o workflow para provisionar o banco de dados RDS.

### Passo 4 - foodtech-lambda (criação do API Gateway incluída)

### Passo 5 - hackaton-solicitacao-service


