drop table mvcboard if exists;
create table mvcboard (
	idx number default next value for seq_board_num,
	name varchar2(50) not null,
	title varchar2(200) not null,
	content varchar2(2000) not null,
	postdate date default now() not null,
	ofile varchar2(200),
	sfile varchar2(30),
	downcount number(5) default 0 not null,
	pass varchar2(50) not null,
	visitcount number default 0 not null,
	primary key(idx)
);

insert into mvcboard (name, title, content, pass) values ('김유신', '자료실 제목1 입니다.', '내용', '1234');
insert into mvcboard (name, title, content, pass) values ('장보고', '자료실 제목2 입니다.', '내용', '1234');
insert into mvcboard (name, title, content, pass) values ('이순신', '자료실 제목3 입니다.', '내용', '1234');
insert into mvcboard (name, title, content, pass) values ('강감찬', '자료실 제목4 입니다.', '내용', '1234');
insert into mvcboard (name, title, content, pass) values ('대조영', '자료실 제목5 입니다.', '내용', '1234');

drop sequence seq_board_num if exists;
create sequence seq_board_num 
    increment by 1
    start with 1
    minvalue 1
    nomaxvalue
    nocycle
    nocache;