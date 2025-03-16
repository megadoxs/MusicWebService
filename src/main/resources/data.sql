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

INSERT INTO songs (song_id, title, genre, release_date)
VALUES
    ('100e8400-e29b-41d4-a716-446655440010', 'Shallow', 'POP', '2018-09-27'),
    ('100e8400-e29b-41d4-a716-446655440011', 'Lose Yourself', 'HIP_HOP', '2002-10-28'),
    ('100e8400-e29b-41d4-a716-446655440012', 'God''s Plan', 'HIP_HOP', '2018-01-19'),
    ('100e8400-e29b-41d4-a716-446655440013', 'Umbrella', 'R_AND_B', '2007-03-29'),
    ('100e8400-e29b-41d4-a716-446655440014', 'Empire State of Mind', 'HIP_HOP', '2009-10-20'),
    ('100e8400-e29b-41d4-a716-446655440015', 'Roar', 'POP', '2013-08-10'),
    ('100e8400-e29b-41d4-a716-446655440016', 'Drop It Like It''s Hot', 'HIP_HOP', '2004-09-27'),
    ('100e8400-e29b-41d4-a716-446655440017', 'No One', 'R_AND_B', '2007-09-11'),
    ('100e8400-e29b-41d4-a716-446655440018', 'Blame It', 'R_AND_B', '2009-01-26'),
    ('100e8400-e29b-41d4-a716-446655440019', 'Rocket Man', 'ROCK', '1972-04-17');

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
