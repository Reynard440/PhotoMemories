insert into local_photo_memories_db.user(user_id, user_email, user_fname, user_lname, user_cell_nr, user_hash_password, user_join_date) values (1, 'reynardengels@gmail.com', 'Reynard', 'Engels', '0723949955', 'King6', '2021-10-30');
insert into local_photo_memories_db.user(user_id, user_email, user_fname, user_lname, user_cell_nr, user_hash_password, user_join_date) values (2, 'rudidreyer6@gmail.com', 'Rudi', 'Dreyer', '0767869466', '445716Rudi', '2021-11-15');

insert into local_photo_memories_db.photo(photo_id, ph_modified_date, ph_capturedby, ph_format, ph_link, ph_location, ph_name, ph_size, ph_upload_date) values (1, '2021-08-05', 'Reynard Engels', 'image/jpeg', 'ReynardEngels.jpeg', 'Vaalpark', 'Reynard', 11543158, '2021-10-30');
insert into local_photo_memories_db.photo(photo_id, ph_modified_date, ph_capturedby, ph_format, ph_link, ph_location, ph_name, ph_size, ph_upload_date) values (2, '2021-07-05', 'Rudi Dreyer', 'image/png', 'RudiDreyer.png', 'Vanderbijlpark', 'Rudi', 1543158, '2021-11-15');

insert into local_photo_memories_db.shared(shared_id, sh_shared_date, sh_has_access, sh_shared_with, photo_id, user_id) values (1, '2021-10-30', true, 1, 1, 1);
insert into local_photo_memories_db.shared(shared_id, sh_shared_date, sh_has_access, sh_shared_with, photo_id, user_id) values (2, '2021-11-15', true, 2, 2, 2);