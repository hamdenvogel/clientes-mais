CREATE TABLE meusservicos.usuario (
	id bigserial NOT NULL,
	email varchar(150) NULL,
	senha varchar(120) NULL,
	login varchar(100) NULL,
	email_confirmado bool DEFAULT false NOT NULL,
	email_confirmado_em timestamp NULL,
	CONSTRAINT uk5171l57faosmj8myawaucatdw UNIQUE (email),
	CONSTRAINT uk_pm3f4m4fqv89oeeeac4tbe2f4 UNIQUE (login),
	CONSTRAINT ukpm3f4m4fqv89oeeeac4tbe2f4 UNIQUE (login),
	CONSTRAINT usuario_pkey PRIMARY KEY (id)
);

CREATE TABLE meusservicos.registration_token (
	id bigserial NOT NULL,
	usuario_id int8 NOT NULL,
	token_hash varchar(128) NOT NULL,
	purpose varchar(40) NOT NULL,
	expires_at timestamp NOT NULL,
	used_at timestamp NULL,
	created_at timestamp NOT NULL,
	CONSTRAINT registration_token_pkey PRIMARY KEY (id),
	CONSTRAINT fk_registration_token_usuario FOREIGN KEY (usuario_id) REFERENCES meusservicos.usuario(id)
);

CREATE TABLE meusservicos.perfil (
	id serial4 NOT NULL,
	nome varchar(20) NULL,
	CONSTRAINT perfil_pkey PRIMARY KEY (id)
);

CREATE TABLE meusservicos.usuario_perfil (
	usuario_id int8 NOT NULL,
	perfil_id int4 NOT NULL,
	CONSTRAINT usuario_perfil_pkey PRIMARY KEY (usuario_id, perfil_id),
	CONSTRAINT fk22cgfn0obntlvqyfn33pyk24d FOREIGN KEY (perfil_id) REFERENCES meusservicos.perfil(id),
	CONSTRAINT fknrjqnbylalt4ykxbcef24f57w FOREIGN KEY (usuario_id) REFERENCES meusservicos.usuario(id)
);

Create Table meusservicos.cliente
(
	id bigserial NOT NULL PRIMARY KEY,
	nome character varying(150),
	cpf character varying(11),
	data_cadastro date,
	pix character varying(50),
    cep character varying(12),
    endereco character varying(100),
    complemento character varying(50),
    uf character varying(2),
    cidade character varying(200) 
);

Create Table meusservicos.empresa
(
	id bigserial NOT NULL PRIMARY KEY,
	nome character varying(150),
	cpnj character varying(11),
	data_cadastro date,
	pix character varying(50)
);

Create Table meusservicos.gestor
(
	id bigserial NOT NULL PRIMARY KEY,
	nome character varying(150),
	cpf character varying(11),
	data_cadastro date,
	pix character varying(50),
    cep character varying(12),
    endereco character varying(100),
    complemento character varying(50),
    uf character varying(2),
    cidade character varying(200) 
);

Create Table meusservicos.planejamento
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL,
	data_inicio date,
	data_fim date,
	
	
	
	id_cliente bigint REFERENCES meusservicos.cliente (id), 
	valor numeric(16,2),
	data date,
	data_conclusao date,
	status character varying(1) CHECK (status in ('P', 'E', 'C', 'F')) NOT NULL,  --previsto, em atendimento, cancelado, finalizado
	id_prestador bigint REFERENCES meusservicos.prestador (id),
	tipo character varying(1) CHECK (tipo in ('U','P')) NOT NULL,  --unitário, pacote (de serviços),
	id_natureza bigint REFERENCES meusservicos.natureza (id), --Corretiva, Periódica, Preventiva
	id_atividade bigint REFERENCES meusservicos.atividade (id), --Manutenção, Aquisição
	local_atendimento character varying(150),
	conclusao character varying(150)		
);

Create Table meusservicos.profissao
(
	id bigserial NOT NULL PRIMARY KEY,
	codigo bigint,
	descricao character varying(200)
);

Create Table meusservicos.prestador
(
	id bigserial NOT NULL PRIMARY KEY,
	nome character varying(150),
	cpf character varying(11),
	data_cadastro date,
	pix character varying(50),
	avaliacao bigint,
    id_profissao bigint REFERENCES meusservicos.profissao (id),
	email character varying(200)
);

Create Table meusservicos.atividade
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL
);

Create Table meusservicos.natureza
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL
);

Create Table meusservicos.ServicoPrestado
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL,
	id_cliente bigint REFERENCES meusservicos.cliente (id), 
	valor numeric(16,2),
	data date,
	data_conclusao date,
	status character varying(1) CHECK (status in ('P', 'E', 'C', 'F')) NOT NULL,  --previsto, em atendimento, cancelado, finalizado
	id_prestador bigint REFERENCES meusservicos.prestador (id),
	tipo character varying(1) CHECK (tipo in ('U','P')) NOT NULL,  --unitário, pacote (de serviços),
	id_natureza bigint REFERENCES meusservicos.natureza (id), --Corretiva, Periódica, Preventiva
	id_atividade bigint REFERENCES meusservicos.atividade (id), --Manutenção, Aquisição
	local_atendimento character varying(150),
	conclusao character varying(150)		
);

Create Table meusservicos.chamado
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL,
	id_cliente bigint REFERENCES meusservicos.cliente (id), 
	data date,
	local_acontecimento character varying(150),
	status character varying(1) CHECK (status in ('A', 'E', 'C', 'F')) NOT NULL  --aguardando atendimento, em atendimento, cancelado, finalizado
)

Create Table meusservicos.diagnostico  --status identificado
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL,
	servico_prestado_id bigint REFERENCES meusservicos.ServicoPrestado (id)
);

Create Table meusservicos.solucao --status final
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL,
	servico_prestado_id bigint REFERENCES meusservicos.ServicoPrestado (id),
	valor numeric(16,2),
	desconto numeric(16,2)
);

Create Table meusservicos.equipamento
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL,
	servico_prestado_id bigint REFERENCES meusservicos.ServicoPrestado (id),
	marca character varying(50) NULL,
	modelo character varying(50) NULL,
	ano_fabricacao bigint NULL,
	ano_modelo bigint NULL
);

CREATE TABLE meusservicos.produto (
    id bigserial NOT NULL PRIMARY KEY,
    descricao character varying(100) NOT NULL,
    preco_unitario numeric(20,2),		
	marca character varying(50) NULL,
	modelo character varying(50) NULL,
	ano_fabricacao bigint NULL,
	ano_modelo bigint NULL
);

CREATE TABLE meusservicos.pedido (
    id bigserial NOT NULL PRIMARY KEY,
	servico_prestado_id bigint REFERENCES meusservicos.ServicoPrestado (id),	
    data_pedido timestamp,
    status varchar(20),
    total numeric(20,2)
);

CREATE TABLE meusservicos.item_pedido (
    id bigserial NOT NULL PRIMARY KEY,
    pedido_id integer references meusservicos.pedido (id),
    produto_id integer references meusservicos.produto (id),
    quantidade integer
);

Create Table meusservicos.documento (
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(150) NOT NULL,
	status character varying(1) CHECK (status in ('A','I')) NOT NULL --ATIVO, INATIVO
);

CREATE TABLE meusservicos.imagem
(
	id bigserial NOT NULL PRIMARY KEY,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp without time zone,
    status boolean,
    updated_by character varying(255) COLLATE pg_catalog."default",
    updated_date timestamp without time zone,
    data bytea,
    file_name character varying(255) COLLATE pg_catalog."default",
    file_type character varying(255) COLLATE pg_catalog."default",
    size bigint,
    system_name character varying(255) COLLATE pg_catalog."default",
    uuid character varying(255) COLLATE pg_catalog."default",
	documento_id integer references meusservicos.documento (id) NOT NULL,
	chave_id bigint,
	original_file_name character varying(255) NULL
);

Create Table meusservicos.pacote
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(300) NOT NULL,
	justificativa character varying(100) NOT NULL,
	data date,
	data_previsao date,
	data_conclusao date,
	status character varying(1) CHECK (status in ('I', 'A', 'E', 'C', 'F')) NOT NULL --iniciado, aguardando atendimento, em atendimento, cancelado, finalizado
);

Create Table meusservicos.itemPacote
(
	id bigserial NOT NULL PRIMARY KEY,
	id_pacote bigint REFERENCES meusservicos.pacote (id),
	id_servico_prestado bigint REFERENCES meusservicos.ServicoPrestado (id)	
);	

Create Table meusservicos.parametro
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(300) NOT NULL,	
	data date
);	

Create Table meusservicos.arvore
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(300) NOT NULL,	
	data date,
	id_servico_prestado bigint REFERENCES meusservicos.ServicoPrestado (id),
	valor numeric(16,2)
);

Create Table meusservicos.ramificacao
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(300) NOT NULL,	
	data date,
	id_arvore bigint REFERENCES meusservicos.arvore (id),
	valor numeric(16,2)	
);

Create Table meusservicos.no
(
	id bigserial NOT NULL PRIMARY KEY,
	descricao character varying(300) NOT NULL,	
	data date,
	id_ramificacao bigint REFERENCES meusservicos.ramificacao (id),
	valor numeric(16,2)
);

*************************************************************************************************************
insert into meusservicos.natureza (descricao) values ('Corretiva');
insert into meusservicos.natureza (descricao) values ('Periódica');
insert into meusservicos.natureza (descricao) values ('Preventiva');
select * from meusservicos.natureza;
insert into meusservicos.atividade (descricao) values ('Manutenção');
insert into meusservicos.atividade (descricao) values ('Aquisição');
select * from meusservicos.atividade;


INSERT INTO meusservicos.perfil(nome) VALUES('ROLE_USER');
INSERT INTO meusservicos.perfil(nome) VALUES('ROLE_MODERATOR');
INSERT INTO meusservicos.perfil(nome) VALUES('ROLE_ADMIN');
select * from meusservicos.documento;
insert into meusservicos.documento (descricao, status) values ('P', 'A'); -- P = Prestador
insert into meusservicos.documento (descricao, status) values ('C', 'A'); -- C = Cliente