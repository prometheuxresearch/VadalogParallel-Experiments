#!/bin/bash

program="transitiveClosure/transitiveClosure.vada"

# Function to issue the curl command with a given program name
function evaluate_from_repo() {
  local program_name=$1
  curl -X POST localhost:8080/evaluateFromRepo -d "programName=${program_name}"
}

echo "Evaluating $program"
evaluate_from_repo "$program"
