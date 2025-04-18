

INSERT INTO songs (song_id, title, genre, release_date, duration)
VALUES ('100e8400-e29b-41d4-a716-446655440010', 'Shallow', 'POP', '2018-09-27', '00:03:35'),
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
VALUES (1, '550e8400-e29b-41d4-a716-446655440000'), -- Shallow - Lady Gaga
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