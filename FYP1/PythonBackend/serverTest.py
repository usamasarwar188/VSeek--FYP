import socket
s = socket.socket()
port = 7766
s.bind(('', port))


# put the socket into listening mode
s.listen(5)
print("socket is listening")

# a forever loop until we interrupt it or
# an error occurs
while True:
    # Establish connection with client.
    c, addr = s.accept()
    print('Got connection from', addr)

    # send a thank you message to the client.

    stri = c.recv(1024)
    print ("Recieved: ",stri)
    # Close the connection with the client
    c.close()