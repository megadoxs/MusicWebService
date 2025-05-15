#!/bin/bash

USER_API_URL="http://localhost:8080/api/v1/users"

echo "👤 GET all users"
response=$(curl -s -w "\n%{http_code}" "$USER_API_URL")
body=$(echo "$response" | sed '$d')
status=$(echo "$response" | tail -n1)

if [ "$status" -ne 200 ]; then
  echo "❌ Failed: Expected status 200, got $status"
  exit 1
else
  echo "✅ GET all users passed"
fi

user_count=$(echo "$body" | jq length)
if [ "$user_count" -lt 1 ]; then
  echo "❌ Failed: Expected 1+ users, got $user_count"
  exit 1
else
  echo "✅ User count check passed"
fi

echo "➕ Creating new user"
create_response=$(curl -s -w "\n%{http_code}" -X POST "$USER_API_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser123",
    "firstName": "New",
    "lastName": "User",
    "email": "new.user@example.com",
    "dateOfBirth": "2006-02-20",
    "password1": "securepassword",
    "password2": "securepassword"

  }')

create_body=$(echo "$create_response" | sed '$d')
create_status=$(echo "$create_response" | tail -n1)

if [ "$create_status" -ne 201 ] && [ "$create_status" -ne 200 ]; then
  echo "❌ Failed to create user, status $create_status"
  echo "$create_body"
  exit 1
else
  echo "✅ User created successfully"
fi

echo "🎉 GET and POST tests for users passed!"
