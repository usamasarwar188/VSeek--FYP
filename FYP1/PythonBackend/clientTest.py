# Import socket module
import socket

# Create a socket object

s = socket.socket()

# Define the port on which you want to connect
port = 7766

# connect to the server on local computer
s.connect(('192.168.137.241', port))

# receive data from the server
stri="Python send this laskjdflaksdf fasldfjsadlfkas dfasldfkjasdfl asdfaksdf asdflaksdf asdflaskdfa sdflaskfdas dflaskdfn sdlfflksd gsldg sdlgk dslg sdfglk sdg lsdkgf sldkfg sdl gk sdfglsdf gsdl"

s.send(stri)

# close the connection
s.close()