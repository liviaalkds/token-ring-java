#!/bin/sh

operacao="${1:-0}"
voltas="${2:-1}"

java Escravo 1 "$operacao" "$voltas" &
java Escravo 2 "$operacao" "$voltas" &
java Escravo 3 "$operacao" "$voltas" &
sleep 1
java Mestre 0 "$operacao" "$voltas"
wait
