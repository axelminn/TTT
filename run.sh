if [ -p /pipe ]; then
  echo "pipe not found"
  echo "Creating pipe..."
  mkfifo pipe
fi
# Run the program
# javac *.java && java Main $LogLevel server < player2server | java Main $LogLevel > player2server
javac *.java && java Main init verbose < pipe | java Main > pipe
