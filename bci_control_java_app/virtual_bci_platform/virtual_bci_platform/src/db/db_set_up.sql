/*create database if not exists mtech_research;use mtech_research;*/
create table if not exists bci_users(
id tinyint unsigned primary key auto_increment,
surname varchar(20),
initials varchar(5),
image_path varchar(60),
age tinyint unsigned,
gender char(1),
regristration_date datetime
)engine = innodb;

create table if not exists bci_phase(
id tinyint unsigned auto_increment primary key,
phase_name varchar(30)
)engine=innodb;

insert into bci_phase(phase_name) /*different bci phases*/
values('Screening Phase'),
('1D Vertical Control'),
('1D Horizontal Control'),
('2D Control');

/*delete duplicate*/
delete from bci_phase where id > 4;

create table if not exists bci_session(
id smallint  unsigned not null primary key auto_increment,
/*session_nbr smallint unsigned,*/
number_of_trials_per_run tinyint unsigned not null default 40,
number_of_runs_per_session tinyint unsigned not null default 4,

target_workspace_percentage real not null default 4.9,
target_workspace_percentage_w_h1d real not null default 0.05,
target_workspace_percentage_h_h1d real not null default 0.3,
target_workspace_percentage_w_v1d real not null default 0.3,
target_workspace_percentage_h_v1d real not null default 0.05,

cursor_workspace_percentage real not null default 1.3,
cursor_timeout mediumint not null default 15000,/*15 seconds*/
session_date datetime,
user_id tinyint unsigned,
phase_id tinyint unsigned,

/*channel selection*/
        /*32 electrodes*/
fp1_used boolean not null default true,/*channel selector */
fp2_used boolean not null default true,/*channel selector */
af3_used boolean not null default true,/*channel selector */
af4_used boolean not null default true,/*channel selector */
f7_used boolean not null default true,/*channel selector */
f3_used boolean not null default true,/*channel selector */
fz_used boolean not null default true,/*channel selector */
f4_used boolean not null default true,/*channel selector */

f8_used boolean not null default true,/*channel selector */
fc5_used boolean not null default true,/*channel selector */
fc1_used boolean not null default true,/*channel selector */
fc2_used boolean not null default true,/*channel selector */
fc6_used boolean not null default true,/*channel selector */
t7_used boolean not null default true,/*channel selector */
c3_used boolean not null default true,/*channel selector */
cz_used boolean not null default true,/*channel selector */

c4_used boolean not null default true,/*channel selector */
t8_used boolean not null default true,/*channel selector */
cp5_used boolean not null default true,/*channel selector */
cp1_used boolean not null default true,/*channel selector */
cp2_used boolean not null default true,/*channel selector */
cp6_used boolean not null default true,/*channel selector */
p7_used boolean not null default true,/*channel selector */
p3_used boolean not null default true,/*channel selector */

pz_used boolean not null default true,/*channel selector */
p4_used boolean not null default true,/*channel selector */
p8_used boolean not null default true,/*channel selector */
po7_used boolean not null default true,/*channel selector */
po3_used boolean not null default true,/*channel selector */
po4_used boolean not null default true,/*channel selector */
po8_used boolean not null default true,/*channel selector */
oz_used boolean not null default true,/*channel selector */

constraint fk_phase_id foreign key(phase_id) references bci_phase(id),
constraint fk_user_id foreign key(user_id) references bci_users(id)
)engine = innodb;


/*delete from session_settings where id > 4;/*avoid duplicate*/
/*create table if not exists target_geometry(geom_name varchar(20) not null default 'Box' primary key)engine = innodb;insert into target_geometry values('Box'),('Sphere');*/

create table if not exists bci_run(
id tinyint unsigned not null primary key auto_increment,
run_date datetime,
session_id smallint unsigned ,
constraint fk_session_id foreign key(session_id) references bci_session(id)
)engine=innodb;

create table if not exists imagery_type(
id tinyint unsigned auto_increment not null primary key,
description varchar(20)
)engine=innodb;

insert into imagery_type(description)
values('left hand'),
('right hand'),
('left foot'),
('right foot'),
('rest');
/*delete duplicate*/
delete from imagery_type where id > 5;
			
create table if not exists bci_trial(
id int unsigned not null primary key auto_increment,
trial_update_rate smallint unsigned,/*in milliseconds*/
/*trial_attempts smallint unsigned ,/*first trial ,second or gourth,...*/
cursor_x0 real,
cursor_y0 real,
/*cursor_z0 real,*/
cursor_scale real,/*limited to uniform scale in all dimensions*/
target_number tinyint,
/*target_z real,*/
target_scale real,/*limited to uniform scale in all dimensions*/
trial_pass_or_fail boolean,/* if user hitted the target*/
eeg_captured boolean,/*if you were to retrieve due the eeg signals
from sinulink*/
run_id tinyint unsigned,
imagery_type_id tinyint unsigned,
/*target_id varchar(20),*/
constraint fk_run_id foreign key(run_id) references bci_run(id),
constraint fk_imagery_type_id foreign key(imagery_type_id) references imagery_type(id)
/*constraint fk_target_id foreign key(target_id) references target_geometry(geom_name)*/
)engine = innodb;

create table if not exists trial_coordinates(
id bigint unsigned not null primary key auto_increment,
coord_x mediumint,/*trial coordinates x */
coord_y mediumint,/*trial coordinates y : pixel position*/ 
/*coord_z real,/*trial coordinates z */ 
/*coord_attempts smallint unsigned not null,/*first move ,second or third ...*/
trial_id int unsigned,
/*constraint fk_trial_id */
foreign key(trial_id) references bci_trial(id)
)engine = innodb;


create table if not exists eeg_signals(
id bigint unsigned not null primary key auto_increment,
        /*32 electrodes*/
fp1 int unsigned,/*24 bit EEG signals binary 32 bit : */
fp2 int unsigned,/*24 bit EEG signals binary 32 bit : */
af3 int unsigned,/*24 bit EEG signals binary 32 bit : */
af4 int unsigned,/*24 bit EEG signals binary 32 bit : */
f7 int unsigned,/*24 bit EEG signals binary 32 bit : */
f3 int unsigned,/*24 bit EEG signals binary 32 bit : */
fz int unsigned,/*24 bit EEG signals binary 32 bit : */
f4 int unsigned,/*24 bit EEG signals binary 32 bit : */

f8 int unsigned,/*24 bit EEG signals binary 32 bit : */
fc5 int unsigned,/*24 bit EEG signals binary 32 bit : */
fc1 int unsigned,/*24 bit EEG signals binary 32 bit : */
fc2 int unsigned,/*24 bit EEG signals binary 32 bit : */
fc6 int unsigned,/*24 bit EEG signals binary 32 bit : */
t7 int unsigned,/*24 bit EEG signals binary 32 bit : */
c3 int unsigned,/*24 bit EEG signals binary 32 bit : */
cz int unsigned,/*24 bit EEG signals binary 32 bit : */

c4 int unsigned,/*24 bit EEG signals binary 32 bit : */
t8 int unsigned,/*24 bit EEG signals binary 32 bit : */
cp5 int unsigned,/*24 bit EEG signals binary 32 bit : */
cp1 int unsigned,/*24 bit EEG signals binary 32 bit : */
cp2 int unsigned,/*24 bit EEG signals binary 32 bit : */
cp6 int unsigned,/*24 bit EEG signals binary 32 bit : */
p7 int unsigned,/*24 bit EEG signals binary 32 bit : */
p3 int unsigned,/*24 bit EEG signals binary 32 bit : */

pz int unsigned,/*24 bit EEG signals binary 32 bit : */
p4 int unsigned,/*24 bit EEG signals binary 32 bit : */
p8 int unsigned,/*24 bit EEG signals binary 32 bit : */
po7 int unsigned,/*24 bit EEG signals binary 32 bit : */
po3 int unsigned,/*24 bit EEG signals binary 32 bit : */
po4 int unsigned,/*24 bit EEG signals binary 32 bit : */
po8 int unsigned,/*24 bit EEG signals binary 32 bit : */
oz int unsigned,/*24 bit EEG signals binary 32 bit : */
trial_id int unsigned,
/*constraint fk_trial_id */
foreign key(trial_id) references bci_trial(id)
)engine=innodb;
/*insert into sphere_geometry(radius) values(1.0);/*default settings for sphere*/
/*
create table if not exists box_geometry(
id tinyint unsigned not null primary key auto_increment,
x real,/*length symmetrical*/
/*y real/*height*/
/*z real/*depth*/ 
/*)engine=innodb;*/
/*
insert into box_geometry(x,y) values(1.0,1.0);/*default settings*/
