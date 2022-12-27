#!/bin/bash
#!/bin/sh

if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

case "$1" in
    build)
        mvn clean
        if [ $? -eq 0 ]
        then
            mvn compile
            if [ $? -eq 0 ]
            then
                mvn assembly:single
                if [ $? -eq 0 ]
                then
                    echo "Build completed!"
                else
                    echo "Could not create .jar" >&2
                fi
            else
                echo "Could not compile" >&2
            fi
        else
            echo "Could not clean build folder" >&2
        fi
        ;;
    install)
        sudo cp ./target/gfetch.jar /bin/

        # shellcheck disable=SC2045
        for var in $(ls /home)
        do
        sed -i '/gfetch/d' /home/$var/.bashrc
        echo "alias gfetch=\"java -jar /bin/gfetch.jar\"" >> /home/$var/.bashrc
        done
        echo "Install completed!"
        ;;
    uninstall)
        # shellcheck disable=SC2045
        for var in $(ls /home)
        do
        sed -i '/gfetch/d' /home/$var/.bashrc
        done

        sudo rm /bin/gfetch.jar
        echo "Uninstall completed!"
        ;;
    *)
        printf "Using: %s [option]\n\n" "$0"
        printf "Options:\n"
        printf "\t build\t\t\t Build project to jar. Location is ./target/gfetch.jar.\n"
        printf "\t install\t\t Moves the application to /bin and sets an alias.\n"
        printf "\t uninstall\t\t Removes all references.\n"
        ;;
esac
