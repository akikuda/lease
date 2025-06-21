create table if not exists lease.ai_chat_history
(
    id          bigint auto_increment comment 'ID'
        primary key,
    type        varchar(20)       not null comment '业务类型',
    session_id  varchar(40)       not null comment '会话ID',
    user_id     bigint            not null comment '用户ID',
    content     varchar(500)      null comment '会话内容',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除',
    constraint uk_type_session_user
        unique (type, session_id, user_id)
)
    comment 'AI聊天历史记录表' charset = utf8mb4;

create index idx_user_type
    on lease.ai_chat_history (user_id, type);

create table if not exists lease.apartment_facility
(
    id           bigint auto_increment
        primary key,
    apartment_id bigint            null comment '公寓id',
    facility_id  bigint            null comment '设施id',
    create_time  timestamp         null comment '创建时间',
    update_time  timestamp         null comment '更新时间',
    is_deleted   tinyint default 0 null comment '是否删除'
)
    comment '公寓&配套关联表' row_format = DYNAMIC;

create table if not exists lease.apartment_fee_value
(
    id           bigint auto_increment
        primary key,
    apartment_id bigint            null comment '公寓id',
    fee_value_id bigint            null comment '收费项value_id',
    create_time  timestamp         null comment '创建时间',
    update_time  timestamp         null comment '更新时间',
    is_deleted   tinyint default 0 null comment '是否删除'
)
    comment '公寓&杂费关联表' row_format = DYNAMIC;

create table if not exists lease.apartment_info
(
    id             bigint auto_increment comment '公寓id'
        primary key,
    name           varchar(64)       null comment '公寓名称',
    introduction   varchar(255)      null comment '公寓介绍',
    district_id    bigint            null comment '所处区域id',
    district_name  varchar(16)       null comment '区域名称',
    city_id        bigint            null comment '所处城市id',
    city_name      varchar(16)       null comment '城市名称',
    province_id    bigint            null comment '所处省份id',
    province_name  varchar(16)       null comment '省份名称',
    address_detail varchar(255)      null comment '详细地址',
    latitude       varchar(16)       null comment '经度',
    longitude      varchar(16)       null comment '纬度',
    phone          varchar(11)       null comment '公寓前台电话',
    is_release     tinyint           null comment '是否发布（1:发布，0:未发布）',
    create_time    timestamp         null comment '创建时间',
    update_time    timestamp         null comment '更新时间',
    is_deleted     tinyint default 0 null comment '是否删除'
)
    comment '公寓信息表' row_format = DYNAMIC;

create table if not exists lease.apartment_label
(
    id           bigint auto_increment
        primary key,
    apartment_id bigint            null comment '公寓id',
    label_id     bigint            null comment '标签id',
    create_time  timestamp         null comment '创建时间',
    update_time  timestamp         null comment '更新时间',
    is_deleted   tinyint default 0 null comment '是否删除'
)
    comment '公寓标签关联表' row_format = DYNAMIC;

create table if not exists lease.attr_key
(
    id          bigint auto_increment
        primary key,
    name        varchar(16)       null comment '属性key',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '房间基本属性表' row_format = DYNAMIC;

create table if not exists lease.attr_value
(
    id          bigint auto_increment
        primary key,
    name        varchar(16)       null comment '属性value',
    attr_key_id bigint            null comment '对应的属性key_id',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '修改时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '房间基本属性值表' row_format = DYNAMIC;

create table if not exists lease.browsing_history
(
    id          bigint auto_increment
        primary key,
    user_id     bigint            null comment '用户id',
    room_id     bigint            null comment '浏览房间id',
    browse_time timestamp         null,
    create_time timestamp         null,
    update_time timestamp         null,
    is_deleted  tinyint default 0 null
)
    comment '浏览历史' row_format = DYNAMIC;

create table if not exists lease.city_info
(
    id          int auto_increment comment '城市id'
        primary key,
    name        varchar(16)       null comment '城市名称',
    province_id int               null comment '所属省份id',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    row_format = DYNAMIC;

create table if not exists lease.district_info
(
    id          int auto_increment comment '区域id'
        primary key,
    name        varchar(255)      null comment '区域名称',
    city_id     int               null comment '所属城市id',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    row_format = DYNAMIC;

create table if not exists lease.facility_info
(
    id          bigint auto_increment comment '自增逐渐'
        primary key,
    type        tinyint           null comment '类型（1:公寓图片,2:房间图片）',
    name        varchar(16)       null comment '名称',
    icon        varchar(64)       null,
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '配套信息表' row_format = DYNAMIC;

create table if not exists lease.fee_key
(
    id          bigint auto_increment
        primary key,
    name        varchar(16)       null comment '付款项key',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '杂项费用名称表' row_format = DYNAMIC;

create table if not exists lease.fee_value
(
    id          bigint auto_increment
        primary key,
    name        varchar(255)      null comment '费用value',
    unit        varchar(255)      null comment '收费单位',
    fee_key_id  bigint            null comment '费用所对的fee_key',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '杂项费用值表' row_format = DYNAMIC;

create table if not exists lease.graph_info
(
    id          bigint auto_increment comment '图片id'
        primary key,
    name        varchar(128)      null comment '图片名称',
    item_type   tinyint           null comment '图片所属对象类型（1:apartment,2:room）',
    item_id     bigint            null comment '图片所有对象id',
    url         varchar(255)      null comment '图片地址',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '图片信息表' row_format = DYNAMIC;

create table if not exists lease.group_blog_info
(
    id           bigint unsigned auto_increment comment '博文id'
        primary key,
    apartment_id bigint                                   not null comment '公寓id',
    user_id      bigint unsigned                          not null comment '用户id',
    title        varchar(255) collate utf8mb4_unicode_ci  not null comment '标题',
    content      varchar(2048) collate utf8mb4_unicode_ci not null comment '博文的文字描述',
    liked        int unsigned default '0'                 null comment '点赞数量',
    comments     int unsigned default '0'                 null comment '评论数量',
    create_time  timestamp                                null comment '创建时间',
    update_time  timestamp                                null comment '更新时间',
    is_deleted   tinyint      default 0                   null comment '是否删除'
)
    row_format = DYNAMIC;

create table if not exists lease.group_follow
(
    id             bigint auto_increment comment '主键'
        primary key,
    user_id        bigint unsigned   not null comment '用户id',
    follow_user_id bigint unsigned   not null comment '关联的用户id',
    create_time    timestamp         null comment '创建时间',
    update_time    timestamp         null comment '更新时间',
    is_deleted     tinyint default 0 null comment '是否删除'
)
    row_format = DYNAMIC;

create table if not exists lease.label_info
(
    id          bigint auto_increment
        primary key,
    type        tinyint           null comment '类型（1:公寓标签,2:房间标签）',
    name        varchar(255)      null comment '标签名称',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '标签信息表' row_format = DYNAMIC;

create table if not exists lease.lease_agreement
(
    id                    bigint auto_increment comment '租约id'
        primary key,
    phone                 varchar(11)       null comment '承租人手机号码',
    name                  varchar(50)       null comment '承租人姓名',
    identification_number varchar(18)       null comment '承租人身份证号码',
    apartment_id          bigint            null comment '签约公寓id',
    room_id               bigint unsigned   null comment '签约房间id',
    lease_start_date      date              null comment '租约开始日期',
    lease_end_date        date              null comment '租约结束日期',
    lease_term_id         bigint            null comment '租期id',
    rent                  decimal(16, 2)    null comment '租金（元/月）',
    deposit               decimal(16, 2)    null comment '押金（元）',
    payment_type_id       bigint            null comment '支付类型id
',
    status                tinyint           null comment '租约状态（1:签约待确认，2:已签约，3:已取消，4:已到期，5:退租待确认，6:已退租，7:续约待确认）',
    source_type           tinyint           null comment '租约来源（1:新签，2:续约）',
    additional_info       varchar(255)      null comment '备注信息',
    create_time           timestamp         null comment '创建时间',
    update_time           timestamp         null comment '更新时间',
    is_deleted            tinyint default 0 null comment '是否删除'
)
    comment '租约信息表' row_format = DYNAMIC;

create table if not exists lease.lease_term
(
    id          bigint auto_increment
        primary key,
    month_count int               null comment '租期',
    unit        varchar(16)       null comment '租期单位',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '租期' row_format = DYNAMIC;

create table if not exists lease.message_info
(
    id              varchar(40)                             not null comment '会话id'
        primary key,
    send_user_id    bigint                                  null comment '发送人id',
    receive_user_id bigint                                  null comment '接收人id',
    content         varchar(500) collate utf8mb4_unicode_ci null comment '会话内容',
    is_read         tinyint(1) default 0                    null comment '是否已读,默认false',
    is_ai           tinyint(1) default 0                    null comment '是否为AI对话，默认false',
    create_time     timestamp                               null comment '创建时间 or 留言时间',
    update_time     timestamp                               null comment '更新时间',
    is_deleted      tinyint    default 0                    null comment '是否删除'
)
    comment '会话信息表' charset = utf8mb3;

create index idx_message_info_user_deleted_time
    on lease.message_info (send_user_id, receive_user_id, is_deleted, create_time);

create table if not exists lease.payment_type
(
    id              bigint auto_increment
        primary key,
    name            varchar(16)       null comment '付款方式名称',
    pay_month_count int               null comment '每次支付租期数',
    additional_info varchar(255)      null comment '付费说明',
    create_time     timestamp         null comment '创建时间',
    update_time     timestamp         null comment '更新时间',
    is_deleted      tinyint default 0 null comment '是否删除'
)
    comment '支付方式表' row_format = DYNAMIC;

create table if not exists lease.province_info
(
    id          bigint auto_increment comment '省份id'
        primary key,
    name        varchar(16)       null comment '省份名称',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    row_format = DYNAMIC;

create table if not exists lease.room_attr_value
(
    id            bigint auto_increment
        primary key,
    room_id       bigint            null comment '房间id',
    attr_value_id bigint            null comment '属性值id',
    create_time   timestamp         null comment '创建时间',
    update_time   timestamp         null comment '更新时间',
    is_deleted    tinyint default 0 null comment '是否删除'
)
    comment '房间&基本属性值关联表' row_format = DYNAMIC;

create table if not exists lease.room_facility
(
    id          bigint auto_increment
        primary key,
    room_id     bigint            null comment '房间id',
    facility_id bigint            null comment '房间设施id',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '房间&配套关联表' row_format = DYNAMIC;

create table if not exists lease.room_info
(
    id           bigint auto_increment comment '房间id'
        primary key,
    room_number  varchar(16)       null comment '房间号',
    rent         decimal(16, 2)    null comment '租金（元/月）',
    apartment_id bigint            null comment '所属公寓id',
    is_release   tinyint           null comment '是否发布',
    create_time  timestamp         null comment '创建时间',
    update_time  timestamp         null comment '更新时间',
    is_deleted   tinyint default 0 null comment '是否删除'
)
    comment '房间信息表' row_format = DYNAMIC;

create table if not exists lease.room_label
(
    id          bigint auto_increment
        primary key,
    room_id     bigint            null comment '房间id',
    label_id    bigint            null comment '标签id',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除'
)
    comment '房间&标签关联表' row_format = DYNAMIC;

create table if not exists lease.room_lease_term
(
    id            bigint auto_increment
        primary key,
    room_id       bigint            null comment '房间id',
    lease_term_id bigint            null comment '租期id',
    create_time   timestamp         null comment '创建时间',
    update_time   timestamp         null comment '更新时间',
    is_deleted    tinyint default 0 null comment '是否删除'
)
    comment '房间租期管理表' row_format = DYNAMIC;

create table if not exists lease.room_payment_type
(
    id              bigint auto_increment
        primary key,
    room_id         bigint            null comment '房间id',
    payment_type_id bigint            null comment '支付类型id',
    create_time     timestamp         null comment '创建时间',
    update_time     timestamp         null comment '更新时间',
    is_deleted      tinyint default 0 null comment '是否删除'
)
    comment '房间&支付方式关联表' row_format = DYNAMIC;

create table if not exists lease.system_post
(
    id          bigint auto_increment comment '岗位ID'
        primary key,
    code        varchar(64)                            not null comment '岗位编码',
    name        varchar(50)  default ''                not null comment '岗位名称',
    description varchar(255) default ''                not null comment '描述',
    status      tinyint(1)   default 1                 not null comment '状态（1正常 0停用）',
    create_time timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_time timestamp                              null on update CURRENT_TIMESTAMP,
    is_deleted  tinyint      default 0                 not null comment '删除标记（0:可用 1:已删除）'
)
    comment '岗位信息表' charset = utf8mb3
                         row_format = DYNAMIC;

create table if not exists lease.system_user
(
    id              bigint auto_increment comment '员工id'
        primary key,
    username        varchar(30)       null comment '用户名',
    password        varchar(100)      null comment '密码',
    name            varchar(50)       null comment '姓名',
    type            tinyint           null comment '用户类型',
    phone           varchar(11)       null comment '手机号码',
    avatar_url      varchar(255)      null comment '头像地址',
    additional_info varchar(255)      null comment '备注信息',
    post_id         bigint            null comment '岗位id',
    status          tinyint           null comment '账号状态',
    create_time     timestamp         null comment '创建时间',
    update_time     timestamp         null comment '更新时间',
    is_deleted      tinyint default 0 null comment '是否删除'
)
    comment '员工信息表' row_format = DYNAMIC;

create table if not exists lease.user_info
(
    id          bigint auto_increment comment '用户id'
        primary key,
    phone       varchar(11)       null comment '手机号码（用做登录用户名）',
    password    varchar(50)       null comment '密码',
    avatar_url  varchar(255)      null comment '头像url',
    nickname    varchar(20)       not null comment '昵称',
    status      tinyint default 1 null comment '账号状态',
    create_time timestamp         null comment '创建时间',
    update_time timestamp         null comment '更新时间',
    is_deleted  tinyint default 0 null comment '是否删除',
    constraint unique_nickname
        unique (nickname)
)
    comment '用户信息表' row_format = DYNAMIC;

create table if not exists lease.group_blog_comments
(
    id            bigint unsigned auto_increment comment '评论id'
        primary key,
    user_id       bigint            not null comment '用户id',
    blog_id       bigint unsigned   not null comment '博文id',
    parent_id     bigint unsigned   null comment '父评论id，指向上一级评论，如果当前是一级评论，则值为null',
    reply_user_id bigint            null comment '被回复的用户id,即你回复了谁,如果没有回复谁，则值为null',
    content       varchar(1024)     not null comment '评论内容',
    create_time   timestamp         null comment '创建时间',
    update_time   timestamp         null comment '更新时间',
    is_deleted    tinyint default 0 null comment '是否删除',
    constraint group_blog_comments_ibfk_1
        foreign key (user_id) references lease.user_info (id)
            on delete cascade,
    constraint group_blog_comments_ibfk_2
        foreign key (blog_id) references lease.group_blog_info (id)
            on delete cascade,
    constraint group_blog_comments_ibfk_3
        foreign key (parent_id) references lease.group_blog_comments (id)
            on delete cascade,
    constraint group_blog_comments_ibfk_4
        foreign key (reply_user_id) references lease.user_info (id)
            on delete set null
)
    row_format = DYNAMIC;

create index idx_blog_id
    on lease.group_blog_comments (blog_id);

create index idx_create_time
    on lease.group_blog_comments (create_time);

create index idx_parent_id
    on lease.group_blog_comments (parent_id);

create index idx_user_id
    on lease.group_blog_comments (user_id);

create index reply_user_id
    on lease.group_blog_comments (reply_user_id);

create table if not exists lease.view_appointment
(
    id                 bigint auto_increment comment '预约id'
        primary key,
    user_id            bigint            null comment '用户id',
    name               varchar(16)       null comment '用户姓名',
    phone              varchar(16)       null comment '用户手机号码',
    apartment_id       int               null comment '公寓id',
    appointment_time   timestamp         null comment '预约时间',
    additional_info    varchar(255)      null comment '备注信息',
    appointment_status tinyint           null comment '预约状态（1:待看房，2:已取消，3已看房）',
    create_time        timestamp         null comment '创建时间',
    update_time        timestamp         null comment '更新时间',
    is_deleted         tinyint default 0 null comment '是否删除'
)
    comment '预约看房信息表' row_format = DYNAMIC;

