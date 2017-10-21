#!/bin/bash
# Bash Menu Script Example

PS3='Please enter your choice: '
options=("Build" "Run" "Build & Run" "Quit")
select opt in "${options[@]}"
do
    case $opt in
        "Build")
            echo "preparing to build ..."
            mvn package
            ;;
        "Run")
            echo "Please wait. REST API is being started ..."
            java -jar target/revolut-1.0.jar
            ;;
        "Build & Run")
            echo "preparing to build ..."
            mvn package
            echo "Please wait. REST API is being started ..."
            java -jar target/revolut-1.0.jar
            ;;
        "Quit")
            echo "Thank you for using our service! Bye!"
            break
            ;;
        *) echo invalid option;;
    esac
done