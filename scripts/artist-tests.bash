#!/bin/bash

ARTIST_API_URL="http://localhost:8080/api/v1/artists"

echo "🎨 GET all artists"
response=$(curl -s -w "\n%{http_code}" "$ARTIST_API_URL")
body=$(echo "$response" | sed '$d')
status=$(echo "$response" | tail -n1)

if [ "$status" -ne 200 ]; then
  echo "❌ Failed: Expected status 200, got $status"
  exit 1
else
  echo "✅ GET all artists passed"
fi

artist_count=$(echo "$body" | jq length)
if [ "$artist_count" -lt 1 ]; then
  echo "❌ Failed: Expected 1+ artists, got $artist_count"
  exit 1
else
  echo "✅ Artist count check passed"
fi

echo "➕ Creating new artist"
create_response=$(curl -s -w "\n%{http_code}" -X POST "$ARTIST_API_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "stageName": "JD"
  }')

create_body=$(echo "$create_response" | sed '$d')
create_status=$(echo "$create_response" | tail -n1)

if [ "$create_status" -ne 201 ] && [ "$create_status" -ne 200 ]; then
  echo "❌ Failed to create artist, status $create_status"
  echo "$create_body"
  exit 1
else
  echo "✅ Artist created successfully"
fi

echo "🎉 GET and POST tests for artists passed!"
