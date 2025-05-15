#!/bin/bash

SONG_API_URL="http://localhost:8080/api/v1/songs"
ARTIST_ID="550e8400-e29b-41d4-a716-446655440000"

echo "🎵 GET all songs"
response=$(curl -s -w "\n%{http_code}" "$SONG_API_URL")
body=$(echo "$response" | sed '$d')
status=$(echo "$response" | tail -n1)

if [ "$status" -ne 200 ]; then
  echo "❌ Failed: Expected status 200, got $status"
  exit 1
else
  echo "✅ GET all songs passed"
fi

song_count=$(echo "$body" | jq length)
if [ "$song_count" -le 0 ]; then
  echo "❌ Failed: Expected 1+ songs, got $song_count"
  exit 1
else
  echo "✅ Song count check passed"
fi

echo "➕ Creating new song"
create_response=$(curl -s -w "\n%{http_code}" -X POST "$SONG_API_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Song2",
    "genre": "POP",
    "releaseDate": "2024-01-01",
    "duration": "00:03:30",
    "artists": ["'"$ARTIST_ID"'"]
  }')

create_body=$(echo "$create_response" | sed '$d')
create_status=$(echo "$create_response" | tail -n1)

if [ "$create_status" -ne 201 ] && [ "$create_status" -ne 200 ]; then
  echo "❌ Failed to create song, status $create_status"
  echo "$create_body"
  exit 1
else
  echo "✅ Song created successfully"
fi

echo "🎉 GET and POST tests for songs passed!"
