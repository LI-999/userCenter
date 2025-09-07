-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '用户id'
        primary key,
    username     varchar(512)                           null comment '用户名',
    userAccount  varchar(256)                           null comment '用户账号',
    userPassword varchar(256)                           not null comment '用户密码',
    gender       bigint                                 null comment '性别',
    avatarUrl    varchar(1024)                          null comment '头像',
    phone        varchar(256)                           null comment '电话',
    status       int          default 0                 not null comment '用户状态 0 正常账户',
    email        varchar(256)                           null comment '用户邮箱',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '用户创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '用户更新时间',
    isDelete     tinyint      default 0                 not null comment '标识该用户是否被删除 0 未删除',
    userRole     int          default 0                 not null comment '用户角色 0 - 普通用户  1 - 管理员',
    planetCode   varchar(512) default '0'               not null comment '星球编号'
);

