INSERT INTO users (user_identifier, first_name, last_name, date_of_birth, email, username, password)
VALUES ('UID123456', 'Alice', 'Johnson', '1995-08-15', 'alice.johnson@example.com', 'alicej95', 'hashedpassword1'),
       ('UID789012', 'Bob', 'Smith', '1990-05-22', 'bob.smith@example.com', 'bobsmith90', 'hashedpassword2'),
       ('UID345678', 'Charlie', 'Brown', '1988-11-30', 'charlie.brown@example.com', 'charlieb88', 'hashedpassword3'),
       ('UID901234', 'Diana', 'Prince', '2000-02-14', 'diana.prince@example.com', 'dianap00', 'hashedpassword4'),
       ('UID567890', 'Ethan', 'Hunt', '1985-06-10', 'ethan.hunt@example.com', 'ethanh85', 'hashedpassword5');




INSERT INTO artists (artist_id, first_name, last_name, stage_name)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Stefani', 'Germanotta', 'Lady Gaga'),
       ('550e8400-e29b-41d4-a716-446655440001', 'Marshall', 'Mathers', 'Eminem'),
       ('550e8400-e29b-41d4-a716-446655440002', 'Aubrey', 'Graham', 'Drake'),
       ('550e8400-e29b-41d4-a716-446655440003', 'Robyn', 'Fenty', 'Rihanna'),
       ('550e8400-e29b-41d4-a716-446655440004', 'Shawn', 'Carter', 'Jay-Z'),
       ('550e8400-e29b-41d4-a716-446655440005', 'Katherine', 'Hudson', 'Katy Perry'),
       ('550e8400-e29b-41d4-a716-446655440006', 'Calvin', 'Broadus', 'Snoop Dogg'),
       ('550e8400-e29b-41d4-a716-446655440007', 'Alicia', 'Cook', 'Alicia Keys'),
       ('550e8400-e29b-41d4-a716-446655440008', 'Eric', 'Bishop', 'Jamie Foxx'),
       ('550e8400-e29b-41d4-a716-446655440009', 'Reginald', 'Dwight', 'Elton John');

INSERT INTO songs (song_id, title, genre, release_date, duration)
VALUES
    ('100e8400-e29b-41d4-a716-446655440010', 'Shallow', 'POP', '2018-09-27', '00:03:35'),
    ('100e8400-e29b-41d4-a716-446655440011', 'Lose Yourself', 'HIP_HOP', '2002-10-28', '00:05:26'),
    ('100e8400-e29b-41d4-a716-446655440012', 'God''s Plan', 'HIP_HOP', '2018-01-19', '00:03:18'),
    ('100e8400-e29b-41d4-a716-446655440013', 'Umbrella', 'R_AND_B', '2007-03-29', '00:04:36'),
    ('100e8400-e29b-41d4-a716-446655440014', 'Empire State of Mind', 'HIP_HOP', '2009-10-20', '00:04:37'),
    ('100e8400-e29b-41d4-a716-446655440015', 'Roar', 'POP', '2013-08-10', '00:03:42'),
    ('100e8400-e29b-41d4-a716-446655440016', 'Drop It Like It''s Hot', 'HIP_HOP', '2004-09-27', '00:04:30'),
    ('100e8400-e29b-41d4-a716-446655440017', 'No One', 'R_AND_B', '2007-09-11', '00:04:13'),
    ('100e8400-e29b-41d4-a716-446655440018', 'Blame It', 'R_AND_B', '2009-01-26', '00:04:49'),
    ('100e8400-e29b-41d4-a716-446655440019', 'Rocket Man', 'ROCK', '1972-04-17', '00:04:41');

INSERT INTO song_artists (song_id, artist_id)
VALUES
    (1, '550e8400-e29b-41d4-a716-446655440000'), -- Shallow - Lady Gaga
    (2, '550e8400-e29b-41d4-a716-446655440001'), -- Lose Yourself - Eminem
    (3, '550e8400-e29b-41d4-a716-446655440002'), -- God's Plan - Drake
    (4, '550e8400-e29b-41d4-a716-446655440003'), -- Umbrella - Rihanna
    (5, '550e8400-e29b-41d4-a716-446655440004'), -- Empire State of Mind - Jay-Z
    (5, '550e8400-e29b-41d4-a716-446655440007'), -- Empire State of Mind - Alicia Keys
    (6, '550e8400-e29b-41d4-a716-446655440005'), -- Roar - Katy Perry
    (7, '550e8400-e29b-41d4-a716-446655440006'), -- Drop It Like It's Hot - Snoop Dogg
    (8, '550e8400-e29b-41d4-a716-446655440007'), -- No One - Alicia Keys
    (9, '550e8400-e29b-41d4-a716-446655440008'), -- Blame It - Jamie Foxx
    (10, '550e8400-e29b-41d4-a716-446655440009'); -- Rocket Man - Elton John

INSERT INTO playlists (playlist_id, "user", name, duration)
VALUES
    ('200e8400-e29b-41d4-a716-446655440020', '300e8400-e29b-41d4-a716-446655440030', 'Workout Hits', '00:08:01'),
    ('200e8400-e29b-41d4-a716-446655440021', '300e8400-e29b-41d4-a716-446655440031', 'Chill Vibes', '00:07:49'),
    ('200e8400-e29b-41d4-a716-446655440022', '300e8400-e29b-41d4-a716-446655440032', 'Hip Hop Bangers', '00:08:04'),
    ('200e8400-e29b-41d4-a716-446655440023', '300e8400-e29b-41d4-a716-446655440033', 'Pop Anthems', '00:08:11'),
    ('200e8400-e29b-41d4-a716-446655440024', '300e8400-e29b-41d4-a716-446655440034', 'RnB Grooves', '00:08:02'),
    ('200e8400-e29b-41d4-a716-446655440025', '300e8400-e29b-41d4-a716-446655440035', 'Classic Rock', '00:04:41'),
    ('200e8400-e29b-41d4-a716-446655440026', '300e8400-e29b-41d4-a716-446655440036', 'Party Mix', '00:09:56'),
    ('200e8400-e29b-41d4-a716-446655440027', '300e8400-e29b-41d4-a716-446655440037', 'Throwback Jams', '00:08:47'),
    ('200e8400-e29b-41d4-a716-446655440028', '300e8400-e29b-41d4-a716-446655440038', 'Focus Mode', '00:08:03'),
    ('200e8400-e29b-41d4-a716-446655440029', '300e8400-e29b-41d4-a716-446655440039', 'Morning Motivation', '00:08:03');

INSERT INTO playlist_songs (playlist_id, song_id)
VALUES
    (1, '100e8400-e29b-41d4-a716-446655440010'), -- Workout Hits - Shallow
    (1, '100e8400-e29b-41d4-a716-446655440011'), -- Workout Hits - Lose Yourself
    (2, '100e8400-e29b-41d4-a716-446655440013'), -- Chill Vibes - Umbrella
    (2, '100e8400-e29b-41d4-a716-446655440017'), -- Chill Vibes - No One
    (3, '100e8400-e29b-41d4-a716-446655440012'), -- Hip Hop Bangers - God's Plan
    (3, '100e8400-e29b-41d4-a716-446655440014'), -- Hip Hop Bangers - Empire State of Mind
    (4, '100e8400-e29b-41d4-a716-446655440015'), -- Pop Anthems - Roar
    (4, '100e8400-e29b-41d4-a716-446655440010'), -- Pop Anthems - Shallow
    (5, '100e8400-e29b-41d4-a716-446655440018'), -- RnB Grooves - Blame It
    (5, '100e8400-e29b-41d4-a716-446655440017'), -- RnB Grooves - No One
    (6, '100e8400-e29b-41d4-a716-446655440019'), -- Classic Rock - Rocket Man
    (7, '100e8400-e29b-41d4-a716-446655440016'), -- Party Mix - Drop It Like It's Hot
    (7, '100e8400-e29b-41d4-a716-446655440011'), -- Party Mix - Lose Yourself
    (8, '100e8400-e29b-41d4-a716-446655440014'), -- Throwback Jams - Empire State of Mind
    (8, '100e8400-e29b-41d4-a716-446655440016'), -- Throwback Jams - Drop It Like It's Hot
    (9, '100e8400-e29b-41d4-a716-446655440017'), -- Focus Mode - No One
    (9, '100e8400-e29b-41d4-a716-446655440019'), -- Focus Mode - Rocket Man
    (10, '100e8400-e29b-41d4-a716-446655440015'), -- Morning Motivation - Roar
    (10, '100e8400-e29b-41d4-a716-446655440010'); -- Morning Motivation - Shallow
