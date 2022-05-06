![image](https://hive.apache.org/images/hive_logo_medium.jpg)

---

## **Exercícios - Criação de Tabela Raw**

Subir o cluster
	docker-compose up -d

## Hive - Criação de Tabela Raw

1.  Enviar o arquivo local “/input/exercises-data/populacaoLA/populacaoLA.csv” para o diretório no HDFS “/user/aluno/<nome>/data/populacao”

```bash
	docker-bigdata# docker exec -it namenode bash<br>
	ls /input/exercises-data/populacaoLA/<br>
	hdfs dfs -mkdir /user/aluno/manoel/data/populacao<br>
	ls<br>
	hdfs dfs -put /input/exercises-data/populacaoLA/populacaoLA.csv /user/aluno/manoel/data/populacao<br>
	hdfs dfs -ls /user/aluno/manoel/data/populacao<br>
	hdfs dfs -cat /user/aluno/manoel/data/populacao/populacaoLA.csv | head -n 3<br>
	"Ctrl + D" para sair do namenode
```
2. Listar os bancos de dados no Hive<br>

```bash
	docker exec -it hive-server bash<br>
	beeline -u jdbc:hive2://localhost:10000<br>
	show databases;
```

3. Criar o banco de dados <nome><br>

```bash
	create database manoel;
```

4. Criar a Tabela Hive no BD <nome><br>

```bash
	use manoel;<br>

	create table pop(zip_code int,total_population int,median_age float,total_males int,total_females int,total_households int,average_household_size float)
	row format delimited<br>
	fields terminated by ','<br>
	lines terminated by '\n'<br>
	stored as textfile<br>
	tblproperties("skip.header.line.count"="1");<br>
```

5. Visualizar a descrição da tabela pop<br>

```bash
	desc formatted  pop;<br>
	Ctrl + D para sair do beeline<br>
	Ctrl + D para sair do hive_server<br>
	docker-bigdata# docker-compose stop (para fechar o cluster).<br>
```
---

## **Exercícios Inserir Dados na Tabela Raw**<br>

1. Visualizar a descrição da tabela pop do banco de dados <nome><br>

```bash
	docker exec -it hive-server bash<br>
	beeline -u jdbc:hive2://localhost:10000<br>
	show databases;<br>
	use manoel;<br>
	show tables;<br>
	desc formatted pop;<br>
```

2. Selecionar os 10 primeiros registros da tabela pop<br>
```sql
	select * from pop limit 10;<br>
```

3. Carregar o arquivo d o HDFS “/user/aluno/<nome>/data/população populacaoLA.csv ” para a tabela Hive pop

```bash
	Verificando se existe o arquivo populacaoLA.csv
	root@manoel-VirtualBox:/home/manoel/docker-bigdata# docker exec -it namenode bash
	Ctrl + D para sair do namenode

	Entrando no HIVE
	docker exec -it hive-server bash

	Entrando no beeline
	beeline -u jdbc:hive2://localhost:10000
	show databases;
	use manoel;
	show tables;

	Carregndo o arquivo
		load data inpath '/user/aluno/manoel/data/populacao/populacaoLA.csv' overwrite into table pop;
```

4. Selecionar os 10 primeiros registros da tabela pop
```bash
	select * from pop limit 10;
```

5. Contar a quantidade de registros da tabela pop
```bash
	select count(*) from pop;

	Ao sair
		Ctrl + D
		docker-compose stop
```
---

## **Exercícios Criação de Tabela Particionada**<br>

1. Criar a pasta “/user/aluno/<nome>/data/nascimento” no HDFS<br>
```bash
	docker exec -it namenode bash<br>

	Verificando se a pata existe<br>
		hdfs dfs -ls /user/aluno/manoel/data
	Criando a pasta<br>
		hdfs dfs -ls /user/aluno/manoel/data
```

2. Criar e usar o Banco de dados <nome>
```bash
	Já foi criado anteriormente
	Entrando no Hive
		docker exec -it hive-server bash
		beeline -u jdbc:hive2://localhost:10000
		show databases;
		use manoel;
		show tables;
```

3. Criar uma tabela externa no Hive com os parâmetros:<br>

	a)Tabela: nascimento<br>
	b)Campos: nome (String), sexo (String) e frequencia (int)<br>
	c)Partição: ano<br>
	d)Delimitadores:<br>
	- a)Campo ‘,’<br>
	- b)Linha ‘ n’<br>

	e)Salvar<br>
	- a)Tipo do arquivo: texto<br>
	- b)Local: '/user/aluno/<nome>/data/nascimento’<br>
    
	Entrando com o usuario manoel
```bash
	use manoel;
```
Sair e entrar no Hive - para sair (Ctrl + D), tem que estar na pasta: 
```bash
 	root@manoel-VirtualBox:/home/manoel/docker-bigdata# para entrar no Hive.
```

Entrando no Hive
```bash
	root@manoel-VirtualBox:/home/manoel/docker-bigdata# docker exec -it hive-server bash
```

Entrando no beeline
```bash
	beeline -u jdbc:hive2://localhost:10000
```

Usando o usario manoel
```bash
	use manoel;
```

Criando a tabela external nascimento.
```sql
create external table nascimento(nome string, sexo string, frequencia int) partitioned by (ano int) row format delimited fields terminated by ',' lines terminated by '\n' stored as textfile location'/user/aluno/manoel/data/nascimento';
```

Visualizando o conteudo da tabela nascimento
```sql
	select * from nascimento;
```

4. Adicionar partição ano=2015
```bash
	0: jdbc:hive2://localhost:10000> alter table nascimento add partition(ano=2015);
```
Visualizando a pasta ano=2015
	para isto devo sair do Hive e entrar no namenode, usando Ctrl + D para sair e entrando no namenode com o comando: 
```bash
	root@manoel-VirtualBox:/home/manoel/docker-bigdata# docker exec -it namenode bash
```
Para ver a pasta ano=2015 uso o comando: 
```bash
	root@namenode:/# hdfs dfs -ls /user/aluno/manoel/data/nascimento
```

5. Enviar o arquivo local “/input/exercises data/names/yob2015.txt” para o HDFS no diretório /user/aluno/<nome>/data/nascimento/ano=2015

Visualizando o conteudo da pasta names
```bash
	root@namenode:/# ls /input/exercises-data/names
```
Enviando o arquivo yob2015.txt para o diretório: /user/aluno/<nome>/data/nascimento/ano=2015
```bash
	root@namenode:/# hdfs dfs -put /input/exercises-data/names/yob2015.txt /user/aluno/manoel/data/nascimento/ano=2015
```
6. Selecionar os 10 primeiros registros da tabela nascimento no Hive
```bash
	0: jdbc:hive2://localhost:10000> select * from nascimento limit 10;
```
7. Repita o processo do 4 ao 6 para os anos de 2016 e 2017.
```bash
	use manoel;
	alter table nascimento add partition(ano=2016);
	alter table nascimento add partition(ano=2017);
```
	Sair e entrar no namenode usando o comando Ctrl + D para sair e depois entrar no namenode
```bash
	root@manoel-VirtualBox:/home/manoel/docker-bigdata# docker exec -it namenode bash 
```

Listando as pastas ano=2016 e ano=2017
```bash
	root@namenode:/# hdfs dfs -ls /user/aluno/manoel/data/nascimento
```
Agora vamos ver o select na tabela nascimento, para isto deve-se sair do namenode e entrar no Hive e beeline.

Quando entrar no beeline, lembre-se de entrar com o usuario manoel, que é o user que foi criado a tabela nascimento.

Entrando no Hive e beeline.
	Comandos:
```bash
	root@manoel-VirtualBox:/home/manoel/docker-bigdata# docker exec -it hive-server bash
	root@hive_server:/opt# beeline -u jdbc:hive2://localhost:10000

	use manoel;

	select * from nascimento where ano=2015  limit 10;
	select * from nascimento where ano=2016  limit 10;
	select * from nascimento where ano=2017  limit 10;
```

---
## **Exercícios Seleção de Tabelas**<br>

1. Selecionar os 10 primeiros registros da tabela nascimento pelo ano de 2016
```sql
	select * from nascimento where ano=2016  limit 10;
```
2. Contar a quantidade de nomes de crianças nascidas em 2017
```sql
	select count(nome) quantidade_nomes from nascimento where ano=2017;
```
3. Contar a quantidade de crianças nascidas em 2017
```sql
	select sum(frequencia) quantidade_criancas from nascimento where ano=2017;
```
4. Contar a quantidade de crianças nascidas por sexo no ano de 2015
```sql
	select sexo, sum(frequencia) quantidade_criancas from nascimento where ano=2015 group by sexo;
```
5. Mostrar por ordem de ano decrescente a quantidade de crianças nascidas por sexo
```sql
	select ano, sexo, sum(frequencia) quantidade_criancas from nascimento group by ano, sexo order by ano desc;
```
6. Mostrar por ordem de ano decrescente a quantidade de crianças nascidas por sexo com o nome iniciado com ‘A’
```sql
	select ano, sexo, sum(frequencia) quantidade_criancas from nascimento where nome like 'A%' group by ano, sexo order by ano desc;
```
7. Qual nome e quantidade das 5 crianças mais nascidas em 2016
```sql
	select nome, max(frequencia) quantidade_criancas from nascimento where ano=2016 group by nome order by quantidade_criancas desc limit 5;
```
8. Qual nome e quantidade das 5 crianças mais nascidas em 2016 do sexo masculino e feminino
```sql
	select nome, sexo, max(frequencia) quantidade_criancas from nascimento where ano=2016 group by nome, sexo order by quantidade_criancas desc limit 5;
```

--
## 


---