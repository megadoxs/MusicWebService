#!/bin/bash

API_URL="http://localhost:8080/api/v1/playlists"
USER_ID="57659e81-7c6b-4711-bb9e-bc73e1fa7824"
SONG_ID_1="100e8400-e29b-41d4-a716-446655440010"
SONG_ID_2="100e8400-e29b-41d4-a716-446655440011"

echo "🔍 GET all playlists"
response=$(curl -s -w "\n%{http_code}" "$API_URL")
body=$(echo "$response" | sed '$d')
status=$(echo "$response" | tail -n1)

if [ "$status" -ne 200 ]; then
  echo "❌ Failed: Expected status 200, got $status"
  exit 1
else
  echo "✅ GET all playlists passed"
fi

playlist_count=$(echo "$body" | jq length)
if [ "$playlist_count" -lt 0 ]; then
  echo "❌ Failed: Expected 1+ playlists, got $playlist_count"
  exit 1
else
  echo "✅ Playlist count check passed"
fi

echo "➕ Creating new playlist"
create_response=$(curl -s -w "\n%{http_code}" -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Playlist",
    "user": "'"$USER_ID"'",
    "songs": ["'"$SONG_ID_1"'", "'"$SONG_ID_2"'"]
  }')

create_body=$(echo "$create_response" | sed '$d')
create_status=$(echo "$create_response" | tail -n1)
if [ "$create_status" -eq 400 ]; then
  echo "❌ Failed to create playlist, status $create_status"
  echo "$create_body"
  exit 1
else
  echo "✅ Playlist created successfully"
fi

playlist_id=$(echo "$create_body" | jq -r '.identifier')

echo "📥 GET newly created playlist by ID"
get_response=$(curl -s -w "\n%{http_code}" "$API_URL/$playlist_id")
get_body=$(echo "$get_response" | sed '$d')
get_status=$(echo "$get_response" | tail -n1)

if [ "$get_status" -ne 200 ]; then
  echo "❌ Failed: Could not retrieve playlist with ID $playlist_id"
  exit 1
else
  echo "✅ Successfully retrieved new playlist"
fi

echo "✏️ Updating playlist name and removing one song"
update_response=$(curl -s -w "\n%{http_code}" -X PUT "$API_URL/$playlist_id" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Playlist",
    "user": "'"$USER_ID"'",
    "songs": ["'"$SONG_ID_1"'"]
  }')

update_status=$(echo "$update_response" | tail -n1)

if [ "$update_status" -ne 200 ]; then
  echo "❌ Failed to update playlist"
  exit 1
else
  echo "✅ Playlist updated"
fi

echo "🗑️ Deleting playlist"
delete_status=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$API_URL/$playlist_id")

if [ "$delete_status" -ne 204 ]; then
  echo "❌ Failed to delete playlist"
  exit 1
else
  echo "✅ Playlist deleted"
fi

echo "🧪 Verifying playlist deletion"
verify_status=$(curl -s -o /dev/null -w "%{http_code}" "$API_URL/$playlist_id")

if [ "$verify_status" -ne 404 ]; then
  echo "❌ Playlist still exists after deletion"
  exit 1
else
  echo "✅ Playlist deletion verified"
fi

echo "🎉 All CRUD operations passed successfully."
