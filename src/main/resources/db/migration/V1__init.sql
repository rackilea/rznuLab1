create TABLE public.t_user (
    id bigint NOT NULL primary key,
    name character varying(255) not NULL,
    surname character varying(255) not NULL,
    email character varying(255) not NULL unique
);

create TABLE public.t_group (
    id bigint NOT NULL primary key,
    name character varying(255) not NULL unique,
    description character varying(255) not NULL
);

create TABLE public.user_group (
    user_id bigint not null,
    group_id bigint not null
);

alter table user_group add CONSTRAINT user_group_pkey PRIMARY KEY(user_id, group_id);

alter table user_group add constraint fk_user_group_group_id foreign key (group_id) references t_group(id);
alter table user_group add constraint fk_user_group_user_id foreign key (user_id) references t_user(id);


create sequence public.user_sequence
    start with 1
    increment by 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

create sequence public.group_sequence
    start with 1
    increment by 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
