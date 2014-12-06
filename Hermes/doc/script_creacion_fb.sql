-- /data/data/com.modulus.ssc/databases/ssc2.db

CREATE TABLE paradas(
	_id integer primary key  , 
	lat real not null,lng real not null,
	descripcion text ,
	id_recorrido integer not null );


CREATE TABLE lineas(
	_id integer primary key  , 
	numero text not null,
	ramal text,
	id_empresa integer not null );


CREATE TABLE empresas(
	_id integer primary key  , 
	nombre text not null);

CREATE TABLE recorridos(
	_id integer primary key  , 
	id_linea integer not null );

CREATE TABLE recorridos_puntos(
	_id integer primary key  , 
	lat real not null,lng real not null,
	orden integer,id_recorrido integer not null ); 
